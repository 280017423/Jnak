package qianye.jnak.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import qianye.jnak.R;
import qianye.jnak.common.FCommon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;

/**
 * 升级模块
 * 
 * @author panxianyi
 * 
 */
public class AppUpdate {
	private Context context = null;
	private String newVerName = null;
	private int newVerCode = 0;
	private ProgressDialog pBar = null;
	private Handler handler = new Handler();
	private int verCode = 0;
	private String verName = null;

	public AppUpdate(Context context) {
		this.context = context;
		verCode = FCommon.getVerCode(context);
		verName = FCommon.getVerName(context);
	}

	public void startCheckUpdate() {
		if (getServerVerCode()) {
			if (newVerCode > verCode) {
				getVersionUpdate();
			}
		}
	}

	// 获得最新版本信息
	private boolean getServerVerCode() {
		try {
			// 取得服务器地址和接口文件名
			String verjson = FCommon.getContent(context
					.getString(R.string.update_verurl));
			JSONArray array = new JSONArray(verjson);
			if (array.length() > 0) {
				JSONObject obj = array.getJSONObject(0);
				try {
					newVerCode = Integer.parseInt(obj.getString("verCode"));
					newVerName = obj.getString("verName");
				} catch (Exception e) {
					newVerCode = -1;
					newVerName = "";
					return false;
				}
			}
		} catch (Exception ex) {
			// Log.e("版本信息", e.getMessage());
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	private void getVersionUpdate() {
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(verName);
		// sb.append(" Code:");
		// sb.append(verCode);
		sb.append(", 发现新版本");
		sb.append(newVerName);
		// sb.append(" Code:");
		// sb.append(newVerCode);
		sb.append(",是否更新?");
		Dialog dialog = new AlertDialog.Builder(context)
				.setTitle("软件更新")
				.setMessage(sb.toString())
				// 设置内容
				.setPositiveButton("更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								pBar = new ProgressDialog(context);
								pBar.setTitle("正在下载");
								pBar.setMessage("请稍候...");
								pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								String downFile = context
										.getString(R.string.update_url)
										+ context
												.getString(R.string.update_app);
								downFile(downFile);
							}
						})
				.setNegativeButton("暂不更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 点击"取消"按钮之后退出程序
								// finish();
							}
						}).create();// 创建
		// 显示对话框
		dialog.show();
	}

	void downFile(final String url) {
		pBar.show();
		new Thread() {
			@Override
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								context.getString(R.string.update_app));
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						// int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							// count += ch;
							if (length > 0) {
							}
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}

					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	void down() {
		handler.post(new Runnable() {
			public void run() {
				pBar.cancel();
				update();
			}
		});
	}

	void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), context
				.getString(R.string.update_app))),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

}
