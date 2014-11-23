package qianye.jnak.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import qianye.jnak.R;
import qianye.jnak.common.EncryptUtil;
import qianye.jnak.common.NetGetData;
import qianye.jnak.dao.CustomerDAO;
import qianye.jnak.dao.ListViewCustomerAdapter;
import qianye.jnak.model.ArrgEntity;
import qianye.jnak.model.Customer;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public class CustomerListActivity extends baseActivity implements
		OnScrollListener {
	private TextView loadInfo;
	private ListView listView;
	private LinearLayout loadLayout;
	private ArrayList<Customer> items;
	private int currentPage = 1; // 默认在第一页
	private static final int lineSize = 10; // 每次显示数
	private int allRecorders = 0; // 全部记录数
	private int pageSize = 1; // 默认共一页
	private int lastItem;
	private ListViewCustomerAdapter baseAdapter;
	private CustomerDAO dao;
	AutoCompleteTextView act;
	NetGetData netGetData;
	Gson gson = new Gson();
	private ProgressDialog progressdialog;
	String strResult;
	ArrgEntity ae;
	String bStr;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				strResult = (String) msg.obj;
				ae = gson.fromJson(strResult, ArrgEntity.class);
				bStr = ae.getBody();
				if (ae.getMessageCode().equals("100")) {
					bStr = EncryptUtil.DES3Decrypt(
							EncryptUtil.BASE64Decrypt(bStr),
							netGetData.getKey());
					Log.d("bStr", bStr);

					JSONObject obj = null;
					try {
						obj = new JSONObject(bStr);
						JSONArray items = obj.getJSONArray("Items");

						for (int i = 0; i < items.length(); i++) {
							String phone = items.getJSONObject(i).getString(
									"phone");
							String mobile = items.getJSONObject(i).getString(
									"mobile");
							String email = items.getJSONObject(i).getString(
									"email");
							String company = items.getJSONObject(i).getString(
									"company");
							String name = items.getJSONObject(i).getString(
									"name");
							String address = items.getJSONObject(i).getString(
									"address");
							String username = items.getJSONObject(i).getString(
									"username");
							String createon = items.getJSONObject(i).getString(
									"createon");
							String post = items.getJSONObject(i).getString(
									"post");
							int sid = items.getJSONObject(i).getInt("sid");
							int gid = items.getJSONObject(i).getInt("gid");

							Customer cus = new Customer();
							cus.setName(name);
							cus.setCompany(company);
							cus.setPost(post);
							cus.setMobile(mobile);
							cus.setPhone(phone);
							cus.setEmail(email);
							cus.setAddress(address);
							cus.setCreateon(createon);

							cus.setUsername(username);
							cus.setGid(gid);
							cus.setSid(sid);

							dao.Add(cus);

						}
						Toast.makeText(CustomerListActivity.this, "同步客户信息成功",
								Toast.LENGTH_LONG).show();
						showAllData();

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(CustomerListActivity.this, "同步客户信息失败",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(CustomerListActivity.this, ae.getMessage(),
							Toast.LENGTH_LONG).show();
				}
				break;
			case 2:
				Toast.makeText(CustomerListActivity.this, "网络异常",
						Toast.LENGTH_LONG).show();
				break;

			case 3:
				strResult = (String) msg.obj;
				ae = gson.fromJson(strResult, ArrgEntity.class);
				bStr = ae.getBody();
				if (ae.getMessageCode().equals("100")) {
					bStr = EncryptUtil.DES3Decrypt(
							EncryptUtil.BASE64Decrypt(bStr),
							netGetData.getKey());
					Log.d("bStr", bStr);

					JSONObject obj = null;
					try {
						obj = new JSONObject(bStr);
						String successed = obj.getString("Successed");
						if (successed.equals("1")) {
							Toast.makeText(CustomerListActivity.this,
									"上传客户信息成功", Toast.LENGTH_LONG).show();
						} else {
							// Log.d("PostResult: ", "登录失败");
							Toast.makeText(CustomerListActivity.this,
									"上传客户信息失败", Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					Toast.makeText(CustomerListActivity.this, ae.getMessage(),
							Toast.LENGTH_LONG).show();
				}

				break;
			default:
				break;
			}
			if (progressdialog != null) {
				if (progressdialog.isShowing()) {
					progressdialog.dismiss();
				}

			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_customer_list);
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.title_style);

		dao = new CustomerDAO(this);

		listView = (ListView) findViewById(R.id.lv_customerlist);
		// 创建一个角标线性布局用来显示"正在加载"
		loadLayout = new LinearLayout(this);
		loadLayout.setGravity(Gravity.CENTER);
		// 定义一个文本显示“正在加载”
		loadInfo = new TextView(this);
		loadInfo.setText("正在加载...");
		loadInfo.setGravity(Gravity.CENTER);
		// 增加组件
		loadLayout.addView(loadInfo, new LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		// 增加到listView底部
		listView.addFooterView(loadLayout);
		// listView.setOnScrollListener(this);
		showAllData();
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Customer customer = (Customer) baseAdapter.getItem(arg2);
				Log.d("onItemClick",
						"item=" + arg2 + "|title=" + customer.getName());
				Intent i = new Intent(CustomerListActivity.this,
						CustomerManagerActivity.class);
				i.putExtra("datatype", 1);
				i.putExtra("id", customer.get_id());
				startActivity(i);
			}
		});

		registerForContextMenu(listView);// 注册长按菜单

		netGetData = new NetGetData();
	}

	/**
	 * 读取全部数据
	 * 
	 */
	public void showAllData() {
		allRecorders = dao.GetCustomerAllCount(pub_userName);
		// 计算总页数
		pageSize = (allRecorders + lineSize - 1) / lineSize;
		System.out.println("allRecorders = " + allRecorders);
		System.out.println("pageSize = " + pageSize);
		items = dao.getAllItems(pub_userName, currentPage, lineSize);
		baseAdapter = new ListViewCustomerAdapter(items, lineSize, this);
		listView.setAdapter(baseAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.customer_list, menu);
		return true;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount - 1; // 统计是否到最后
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		System.out.println("进入滚动界面了");
		// 是否到最底部并且数据没读完//不再滚动
		if (lastItem == baseAdapter.getCount() && currentPage < pageSize
				&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			currentPage++;
			// 设置显示位置
			listView.setSelection(lastItem);
			// 增加数据
			appendDate();
		}
	}

	/**
	 * 增加数据
	 * */
	private void appendDate() {
		ArrayList<Customer> additems = dao.getAllItems(pub_userName,
				currentPage, lineSize);
		baseAdapter.setCount(baseAdapter.getCount() + additems.size());
		// 判断，如果到了最末尾则去掉“正在加载”
		if (allRecorders == baseAdapter.getCount()) {
			listView.removeFooterView(loadLayout);
		}
		items = (additems);
		// 通知记录改变
		baseAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		long id = info.id;// ID
		int position = info.position;//
		Log.d("list_customerid", String.valueOf(id));
		switch (item.getItemId()) {
		case R.id.action_view:

			break;
		case R.id.action_modify:
			Intent i = new Intent(this, CustomerManagerActivity.class);
			i.putExtra("ID", id);
			startActivity(i);
			finish();
			break;
		case R.id.action_del:
			Customer customer = (Customer) baseAdapter.getItem(position);
			deleteCustomer(customer);

			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	void deleteCustomer(final Customer customer) {
		Builder b = new Builder(this);
		b.setTitle("温馨提示");
		b.setMessage("您确定要删除" + customer.getName() + "吗？");
		b.setNegativeButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dao.delete(customer.get_id());
				showAllData();
				Toast.makeText(CustomerListActivity.this, "删除成功",
						Toast.LENGTH_LONG).show();
			}
		});
		b.setPositiveButton("取消", null);
		b.create().show();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_adds:
			startActivity(new Intent(CustomerListActivity.this,
					CustomerManagerActivity.class));
			finish();
			break;
		case R.id.action_back:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void addcustomer(View v) {
		startActivity(new Intent(CustomerListActivity.this,
				CustomerManagerActivity.class));
		finish();
	}

	public void downcustomer(View v) {
		Builder b = new Builder(this);
		b.setTitle("温馨提示");
		b.setMessage("您确定要下载并同步客户信息吗？");
		b.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				progressdialog = ProgressDialog.show(CustomerListActivity.this,
						"请等待...", "正在为您同步客户信息...");
				Down();
			}
		});
		b.setNegativeButton("取消", null);
		b.create().show();
	}

	public void upcustomer(View v) {
		Builder b = new Builder(this);
		b.setTitle("温馨提示");
		b.setMessage("您确定要上传客户信息吗？");
		b.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				progressdialog = ProgressDialog.show(CustomerListActivity.this,
						"请等待...", "正在为您上传客户信息...");
				Up();
			}
		});
		b.setNegativeButton("取消", null);
		b.create().show();
	}

	public void Up() {
		new Thread() {
			@Override
			public void run() {

				ArrayList<Customer> lst = dao.getAllItems(pub_userName, 1,
						99999);
				String postStr = netGetData.GetUpCustomerPostStr(lst);
				String uriAPI = netGetData.getServerUrl();
				/* 建立HTTP Post联机 */
				HttpPost httpRequest = new HttpPost(uriAPI);
				/*
				 * Post运作传送变量必须用NameValuePair[]数组储存
				 */
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("arrg", postStr));
				try {
					/* 发出HTTP request */
					httpRequest.setEntity(new UrlEncodedFormEntity(params,
							HTTP.UTF_8));
					/* 取得HTTP response */
					HttpResponse httpResponse = new DefaultHttpClient()
							.execute(httpRequest);
					/* 若状态码为200 ok */
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						/* 取出响应字符串 */
						String strResult1 = EntityUtils.toString(httpResponse
								.getEntity());
						Log.d("PostResult", strResult1);
						Message msg = handler.obtainMessage();

						msg.what = 3;
						msg.obj = strResult1;

						handler.sendMessage(msg);

					} else {
						handler.sendEmptyMessage(2);
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			};
		}.start();

	}

	public void Down() {
		new Thread() {
			@Override
			public void run() {

				String postStr = netGetData
						.GetDownCustomerPostStr(pub_userName);
				String uriAPI = netGetData.getServerUrl();
				/* 建立HTTP Post联机 */
				HttpPost httpRequest = new HttpPost(uriAPI);
				/*
				 * Post运作传送变量必须用NameValuePair[]数组储存
				 */
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("arrg", postStr));
				try {
					/* 发出HTTP request */
					httpRequest.setEntity(new UrlEncodedFormEntity(params,
							HTTP.UTF_8));
					/* 取得HTTP response */
					HttpResponse httpResponse = new DefaultHttpClient()
							.execute(httpRequest);
					/* 若状态码为200 ok */
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						/* 取出响应字符串 */
						String strResult1 = EntityUtils.toString(httpResponse
								.getEntity());
						Log.d("PostResult", strResult1);
						Message msg = handler.obtainMessage();

						msg.what = 1;
						msg.obj = strResult1;

						handler.sendMessage(msg);

					} else {
						handler.sendEmptyMessage(2);
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			};
		}.start();

	}

}
