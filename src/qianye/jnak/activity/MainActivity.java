package qianye.jnak.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import org.json.JSONException;
import org.json.JSONObject;

import qianye.jnak.R;
import qianye.jnak.adapter.AdverAdapter;
import qianye.jnak.common.EncryptUtil;
import qianye.jnak.common.NetGetData;
import qianye.jnak.dao.UserDao;
import qianye.jnak.model.ArrgEntity;
import qianye.jnak.model.User;
import qianye.jnak.widget.AutoScrollViewPager;
import qianye.jnak.widget.CircleFlowIndicator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends BaseActivity {
	private static final long WAIT_TIME = 2000;
	private AutoScrollViewPager mViewPager;
	private RelativeLayout mGalleryLayout;
	private CircleFlowIndicator mCircleFlowIndicator;
	private AdverAdapter mAdapter;

	private ProgressDialog progressdialog;
	private AlertDialog selfdialog;
	private String usernamestr;
	private String passwordstr;
	private UserDao userDao;
	private long mTouchTime;
	private NetGetData netGetData;

	// 切换当前显示的图片
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:

					break;
				case 2:
					Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_LONG).show();
					break;

				case 3:
					String strResult = (String) msg.obj;

					Gson gson = new Gson();
					ArrgEntity ae1 = gson.fromJson(strResult, ArrgEntity.class);
					String bStr = ae1.getBody();
					if (bStr == null || bStr.equals("")) {
					} else {
						bStr = EncryptUtil.DES3Decrypt(EncryptUtil.BASE64Decrypt(bStr), netGetData.getKey());
						Log.d("bStr", bStr);

						JSONObject obj = null;
						try {
							obj = new JSONObject(bStr);
							String successed = obj.getString("Successed");
							if (successed.equals("1")) {
								byte[] md5_psw = EncryptUtil.MD5(passwordstr);
								String checkPsw = EncryptUtil.BASE64Encrypt(md5_psw);
								User aUser = userDao.get(usernamestr);
								if (aUser.get_id() > 0) {
									aUser.setPassword(checkPsw);
									userDao.update(aUser);
								} else {
									aUser.setUsername(usernamestr);
									aUser.setPassword(checkPsw);
									userDao.Add(aUser);
								}
								Intent spIntent = new Intent(MainActivity.this, CustomerListActivity.class);
								startActivity(spIntent);
								mPubUserName = usernamestr;
								if (progressdialog != null) {
									if (progressdialog.isShowing()) {
										progressdialog.dismiss();
									}

								}
							} else {
								// Log.d("PostResult: ", "登录失败");
								Toast.makeText(MainActivity.this, "用户名称或密码错误", Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					break;
				default:
					break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		UmengUpdateAgent.setDefault();
		UmengUpdateAgent.forceUpdate(this);
		mAdapter = new AdverAdapter(this);
		mCircleFlowIndicator = (CircleFlowIndicator) findViewById(R.id.cfi_indicator);
		mCircleFlowIndicator.setCount(mAdapter.getSize());
		mViewPager = (AutoScrollViewPager) findViewById(R.id.vp);
		mGalleryLayout = (RelativeLayout) findViewById(R.id.rl_gallery_layout);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mCircleFlowIndicator.setSeletion(position % mAdapter.getSize());
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		LayoutParams lp = mGalleryLayout.getLayoutParams();
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int WIDTH = metric.widthPixels;
		lp.width = WIDTH;
		lp.height = WIDTH * 400 / 640;
		mGalleryLayout.setLayoutParams(lp);
		userDao = new UserDao(this);

		netGetData = new NetGetData();
	}

	@Override
	public void onResume() {
		mViewPager.startAutoScroll();
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mViewPager.stopAutoScroll();
	}

	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.tab_img_1:

				break;
			case R.id.tab_img_2:
				Intent i2 = new Intent(this, ProductActivity.class);
				startActivity(i2);
				finish();
				break;
			case R.id.tab_img_3:
				Intent i3 = new Intent(this, WebBrowserActivity.class);
				i3.putExtra("str_loadurl", getString(R.string.url_shop));
				startActivity(i3);
				break;
			case R.id.tab_img_4:
				Intent i4 = new Intent(this, VideoListActivity.class);
				startActivity(i4);
				break;
			default:
				break;
		}
	}

	public void onBtnClick(View v) {

		switch (v.getId()) {
			case R.id.btn_ico_1:
				GotoViewPage(117);
				break;
			case R.id.btn_ico_2:
				GotoViewPage(164);
				break;
			case R.id.btn_ico_3:
				GotoViewPage(168);
				break;
			case R.id.btn_ico_4:
				Intent i4 = new Intent(this, IndustryActivity.class);
				startActivity(i4);
				break;
			case R.id.btn_ico_5:
				if (mPubUserName == null || mPubUserName.equals("")) {
					initLoginView();
				} else {
					Intent spIntent = new Intent(this, CustomerListActivity.class);
					startActivity(spIntent);
				}
				break;
			case R.id.btn_ico_6:
				Toast.makeText(MainActivity.this, "努力开发中...", Toast.LENGTH_LONG).show();
				break;
			case R.id.btn_ico_7:
				Intent i = new Intent(MainActivity.this, FileListActivity.class);
				startActivity(i);
				break;
			case R.id.btn_ico_8:
				Toast.makeText(MainActivity.this, "努力开发中...", Toast.LENGTH_LONG).show();
				break;
			case R.id.btn_ico_9:
				Intent txllIntent = new Intent(this, VideoListActivity.class);
				startActivity(txllIntent);
				break;
			default:
				break;
		}
	}

	public void GotoViewPage(int articleId) {
		if (articleId > 0) {
			Intent i = new Intent(MainActivity.this, ViewPage.class);
			i.putExtra("articleid", articleId);
			startActivity(i);
		}
	}

	public void GotoPageList(int categoryid) {
		Intent i = new Intent(MainActivity.this, NewsListActivity.class);
		i.putExtra("categoryid", categoryid);
		startActivity(i);
	}

	private View loginView;

	public void initLoginView() {
		// 创建view从当前activity获取loginactivity
		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		loginView = inflater.inflate(R.layout.login, null);

		final EditText username = (EditText) loginView.findViewById(R.id.txt_username);
		final EditText password = (EditText) loginView.findViewById(R.id.txt_password);
		// username.setText("admin");
		// password.setText("qianye888"); // 为了测试方便所以在这里初始化弹出框是填上账号密码
		AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
		ad.setView(loginView);
		ad.setTitle("账号登陆");
		selfdialog = ad.create();

		selfdialog.setButton("登陆", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 获取输入框的用户名密码

				usernamestr = username.getText().toString();
				passwordstr = password.getText().toString();
				progressdialog = ProgressDialog.show(MainActivity.this, "请等待...", "正在为您登陆...");
				refreshHandler.sleep(100);
				// dialog.cancel();
			}
		});
		selfdialog.setButton2("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				selfdialog.cancel();
			}
		});
		selfdialog.show();
	}

	private RefreshHandler refreshHandler = new RefreshHandler();

	// 处理器
	@SuppressLint("HandlerLeak")
	class RefreshHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			try {
				String strmsg = "登陆失败";
				if (!usernamestr.equals("") && !passwordstr.equals("")) {
					byte[] md5_psw = EncryptUtil.MD5(passwordstr);
					String checkPsw = EncryptUtil.BASE64Encrypt(md5_psw);
					User user = userDao.get(usernamestr);
					if (user.get_id() > 0) {
						if (passwordstr.equals(checkPsw)) {
							Intent spIntent = new Intent(MainActivity.this, CustomerListActivity.class);
							startActivity(spIntent);
							mPubUserName = usernamestr;
							return;
						} else {
							strmsg = "帐号或密码不正确";
						}
					}
					login(usernamestr, passwordstr);
					return;

				} else {
					strmsg = "账号或密码不能为空";
				}
				Toast.makeText(MainActivity.this, strmsg, Toast.LENGTH_LONG).show();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				progressdialog.dismiss();// 解除进度条
			}
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	}

	public void login(final String username, final String psw) {
		new Thread() {
			@Override
			public void run() {
				String postStr = netGetData.GetPostCheckUserPostStr(username, psw);

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
					httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					/* 取得HTTP response */
					HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
					/* 若状态码为200 ok */
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						/* 取出响应字符串 */
						String strResult = EntityUtils.toString(httpResponse.getEntity());
						Log.d("PostResult", strResult);
						Message msg = handler.obtainMessage();

						msg.what = 3;
						msg.obj = strResult;

						handler.sendMessage(msg);

					} else {
						handler.sendEmptyMessage(2);
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			};
		}.start();
	}

	@Override
	public void onBackPressed() {

		long currentTime = System.currentTimeMillis();
		if ((currentTime - mTouchTime) >= WAIT_TIME) {
			Toast.makeText(this, getString(R.string.once_press_quit), Toast.LENGTH_SHORT).show();
			mTouchTime = currentTime;
			return;
		} else {
			MobclickAgent.onKillProcess(this);
			finish();
		}
		super.onBackPressed();
	}

}
