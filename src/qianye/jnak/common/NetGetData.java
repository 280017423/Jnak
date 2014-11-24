package qianye.jnak.common;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import qianye.jnak.dao.ArticleDao;
import qianye.jnak.model.ArrgEntity;
import qianye.jnak.model.Article;
import qianye.jnak.model.Customer;
import qianye.jnak.widget.LoadingUpView;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NetGetData {
	String key = "NWQ4YjEyMTAtXjc3ZC00M2P0";
	String sn = "SDK-QIANYE-001";
	String serverUrl = "http://jnakdl.idc.1001n.net/tools/jnak_sv.ashx";
	String serverDomain = "http://jnakdl.idc.1001n.net/";

	public String getKey() {
		return key;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public String getServerDomain() {
		return serverDomain;
	}

	ArrgEntity ae = new ArrgEntity();

	public ArrgEntity getData(final Context context, final String action, String bodyStr, String[] par,
			final Activity oldActivity, final Class<?> cls, final LoadingUpView loadingUpView) {
		String sequence = UUID.randomUUID().toString();//
		long timestamp = new Date().getTime();
		// String action = "get_article_list";
		StringBuffer result = new StringBuffer();
		result.append(sn);
		result.append(timestamp);
		result.append(sequence);
		result.append(action);
		if (par != null && par.length > 0) {
			for (int i = 0; i < par.length; i++) {
				result.append(par[i]);
			}
		}

		byte[] md5_singed = EncryptUtil.MD5(result.toString());
		String signed = EncryptUtil.BASE64Encrypt(md5_singed);
		Log.d("bodyStr", bodyStr);
		String passBody = "";
		try {
			byte[] des_passBody = EncryptUtil.DES3Encrypt(key, bodyStr);
			passBody = EncryptUtil.BASE64Encrypt(des_passBody);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		String inputStr = "{\"Sequence\":\"" + sequence + "\",\"Sn\":\"" + sn + "\",\"Signed\":\"" + signed
				+ "\",\"TimeStamp\":\"" + timestamp + "\",\"Action\":\"" + action + "\",\"Body\":\"" + passBody + "\"}";
		System.out.println(inputStr);
		String postStr = "";
		try {
			postStr = URLEncoder.encode(inputStr, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Log.d("postStr", postStr);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("arrg", postStr);

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);
		http.send(HttpMethod.GET, serverUrl, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				Log.d("onStart", "conn...");
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Log.d("onFailure", "onFailure");
				SkipActivity(context, oldActivity, cls, loadingUpView);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String json = arg0.result;
				Log.d("onSuccess", json);

				Gson gson = new Gson();
				ae = gson.fromJson(json, ArrgEntity.class);
				String bStr = ae.getBody();
				if (bStr == null || bStr.equals("")) {
				} else {
					bStr = EncryptUtil.DES3Decrypt(EncryptUtil.BASE64Decrypt(bStr), key);
					Log.d("bStr", bStr);
					Prosess(ae, context, bStr, action, oldActivity, cls, loadingUpView);// 处理返回结果
				}
			}
		});

		return ae;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	// 处理返回结果
	void Prosess(ArrgEntity arrgEntity, Context context, String body, String action, Activity ac1, Class<?> cls,
			LoadingUpView loadingUpView) {
		if (arrgEntity.getMessageCode().equals("100")) {
			JSONObject obj = null;
			try {
				obj = new JSONObject(body);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (action.equals("get_article_list")) {
				if (obj != null) {
					try {
						JSONArray items = obj.getJSONArray("Items");

						for (int i = 0; i < items.length(); i++) {
							int id = items.getJSONObject(i).getInt("id");
							int categoryid = items.getJSONObject(i).getInt("categoryid");
							int channelid = items.getJSONObject(i).getInt("channelid");
							String title = items.getJSONObject(i).getString("title");
							String content = items.getJSONObject(i).getString("content");
							String info = items.getJSONObject(i).getString("info");
							String picurl = items.getJSONObject(i).getString("picurl");
							String videourl = items.getJSONObject(i).getString("videourl");
							String CreateTime = items.getJSONObject(i).getString("CreateTime");

							Log.d("content", "id=" + id + "|||title=" + title);

							Article article = new Article();

							article.setTitle(title);
							article.setZhaiyao(info);
							article.setContent(content);
							article.setCreateon(CreateTime);
							article.setPicurl(picurl);
							article.setVideourl("");
							article.setArticleid(id);
							article.setChannelid(channelid);
							article.setCategoryid(categoryid);
							article.setVideourl(videourl);

							new ArticleDao(context).Add(article);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					} finally {
					}
				}
			} else if (action.equals("get_article_file")) {
				String sdCard = FCommon.getSDPath() + "/qyjnak/";
				Log.d("sdCard", sdCard);
				if (obj != null && sdCard != "") {
					try {
						JSONArray items = obj.getJSONArray("Items");

						for (int i = 0; i < items.length(); i++) {
							String name = items.getJSONObject(i).getString("Name");
							String webPath = items.getJSONObject(i).getString("WebPath");
							String createOn = items.getJSONObject(i).getString("CreateOn");
							long size = items.getJSONObject(i).getLong("Size");

							String target = sdCard + webPath;
							String url = serverDomain + webPath;

							Log.d("fileurl", url);

							HttpUtils http = new HttpUtils();
							http.download(url, target, new RequestCallBack<File>() {

								@Override
								public void onLoading(long total, long current, boolean isUploading) {

								}

								@Override
								public void onFailure(HttpException error, String msg) {
									Log.d("download_onFailure", "下载失败:msg=" + msg);

								}

								@Override
								public void onSuccess(ResponseInfo<File> arg0) {
									Log.d("download_onSuccess", "下载成功:name=" + arg0.result.getName());
								}
							});
						}

					} catch (JSONException e) {
						e.printStackTrace();
					} finally {
					}
				}
			}
		} else {
			Log.d("Message", arrgEntity.getMessage());

			try {
				if (loadingUpView != null) {
					if (loadingUpView.isShowing()) {
						loadingUpView.dismiss();
					}
				}
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		SkipActivity(context, ac1, cls, loadingUpView);
	}

	void SkipActivity(Context context, Activity ac1, Class<?> cls, LoadingUpView loadingUpView) {
		if (loadingUpView != null) {
			if (loadingUpView.isShowing()) {
				loadingUpView.dismiss();
			}

		}

		if (cls != null) {
			Intent txllIntent = new Intent(context, cls);
			context.startActivity(txllIntent);
			ac1.finish();
		}
	}

	public String GetPostCheckUserPostStr(String username, String psw) {

		String sequence = UUID.randomUUID().toString();//
		long timestamp = new Date().getTime();
		String action = "user_checkuser";
		StringBuffer result = new StringBuffer();
		result.append(sn);
		result.append(timestamp);
		result.append(sequence);
		result.append(action);
		result.append(username);

		byte[] md5_singed = EncryptUtil.MD5(result.toString());
		String signed = EncryptUtil.BASE64Encrypt(md5_singed);
		String bodyStr = "{\"UserName\":\"" + username + "\",\"PassWord\":\"" + psw + "\"}";
		Log.d("checkuser_bodyStr", bodyStr);
		String passBody = "";
		try {
			byte[] des_passBody = EncryptUtil.DES3Encrypt(key, bodyStr);
			passBody = EncryptUtil.BASE64Encrypt(des_passBody);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		String inputStr = "{\"Sequence\":\"" + sequence + "\",\"Sn\":\"" + sn + "\",\"Signed\":\"" + signed
				+ "\",\"TimeStamp\":\"" + timestamp + "\",\"Action\":\"" + action + "\",\"Body\":\"" + passBody + "\"}";
		System.out.println(inputStr);
		String postStr = "";
		try {
			postStr = URLEncoder.encode(inputStr, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Log.d("postStr", postStr);
		return postStr;
	}

	public String GetDownCustomerPostStr(String username) {

		String sequence = UUID.randomUUID().toString();//
		long timestamp = new Date().getTime();
		String action = "user_downcustomer";
		StringBuffer result = new StringBuffer();
		result.append(sn);
		result.append(timestamp);
		result.append(sequence);
		result.append(action);
		result.append(username);

		byte[] md5_singed = EncryptUtil.MD5(result.toString());
		String signed = EncryptUtil.BASE64Encrypt(md5_singed);
		String bodyStr = "{\"UserName\":\"" + username + "\"}";
		Log.d("checkuser_bodyStr", bodyStr);
		String passBody = "";
		try {
			byte[] des_passBody = EncryptUtil.DES3Encrypt(key, bodyStr);
			passBody = EncryptUtil.BASE64Encrypt(des_passBody);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		String inputStr = "{\"Sequence\":\"" + sequence + "\",\"Sn\":\"" + sn + "\",\"Signed\":\"" + signed
				+ "\",\"TimeStamp\":\"" + timestamp + "\",\"Action\":\"" + action + "\",\"Body\":\"" + passBody + "\"}";
		System.out.println(inputStr);
		String postStr = "";
		try {
			postStr = URLEncoder.encode(inputStr, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Log.d("postStr", postStr);
		return postStr;
	}

	public String GetUpCustomerPostStr(ArrayList<Customer> lst) {

		String sequence = UUID.randomUUID().toString();//
		long timestamp = new Date().getTime();
		String action = "user_upcustomer";
		StringBuffer result = new StringBuffer();
		result.append(sn);
		result.append(timestamp);
		result.append(sequence);
		result.append(action);

		byte[] md5_singed = EncryptUtil.MD5(result.toString());
		String signed = EncryptUtil.BASE64Encrypt(md5_singed);
		StringBuffer bodyStr = new StringBuffer();
		bodyStr.append("[");
		String head = "";
		for (Customer c : lst) {
			bodyStr.append(head);
			bodyStr.append("{");
			bodyStr.append("\"phone\":\"" + c.getPhone() + "\"");
			bodyStr.append(",\"mobile\":\"" + c.getMobile() + "\"");
			bodyStr.append(",\"email\":\"" + c.getEmail() + "\"");
			bodyStr.append(",\"company\":\"" + c.getCompany() + "\"");
			bodyStr.append(",\"name\":\"" + c.getName() + "\"");
			bodyStr.append(",\"address\":\"" + c.getAddress() + "\"");
			bodyStr.append(",\"sid\":\"" + c.getSid() + "\"");
			bodyStr.append(",\"gid\":\"" + c.getGid() + "\"");
			bodyStr.append(",\"username\":\"" + c.getUsername() + "\"");
			bodyStr.append(",\"post\":\"" + c.getPost() + "\"");
			bodyStr.append(",\"createon\":\"" + c.getCreateon() + "\"");
			bodyStr.append("}");
			head = ",";
		}

		bodyStr.append("]");

		Log.d("upcustomer_bodyStr", bodyStr.toString());
		String passBody = "";
		try {
			byte[] des_passBody = EncryptUtil.DES3Encrypt(key, bodyStr.toString());
			passBody = EncryptUtil.BASE64Encrypt(des_passBody);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		String inputStr = "{\"Sequence\":\"" + sequence + "\",\"Sn\":\"" + sn + "\",\"Signed\":\"" + signed
				+ "\",\"TimeStamp\":\"" + timestamp + "\",\"Action\":\"" + action + "\",\"Body\":\"" + passBody + "\"}";
		System.out.println(inputStr);
		String postStr = "";
		try {
			postStr = URLEncoder.encode(inputStr, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Log.d("postStr", postStr);
		return postStr;
	}
}
