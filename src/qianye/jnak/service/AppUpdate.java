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
 * ����ģ��
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

	// ������°汾��Ϣ
	private boolean getServerVerCode() {
		try {
			// ȡ�÷�������ַ�ͽӿ��ļ���
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
			// Log.e("�汾��Ϣ", e.getMessage());
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	private void getVersionUpdate() {
		StringBuffer sb = new StringBuffer();
		sb.append("��ǰ�汾:");
		sb.append(verName);
		// sb.append(" Code:");
		// sb.append(verCode);
		sb.append(", �����°汾");
		sb.append(newVerName);
		// sb.append(" Code:");
		// sb.append(newVerCode);
		sb.append(",�Ƿ����?");
		Dialog dialog = new AlertDialog.Builder(context)
				.setTitle("�������")
				.setMessage(sb.toString())
				// �������ݮ�
				.setPositiveButton("����",// ����ȷ����ť
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								pBar = new ProgressDialog(context);
								pBar.setTitle("��������");
								pBar.setMessage("���Ժ�...");
								pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								String downFile = context
										.getString(R.string.update_url)
										+ context
												.getString(R.string.update_app);
								downFile(downFile);
							}
						})
				.setNegativeButton("�ݲ�����",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// ���"ȡ��"��ť֮���˳�����
								// finish();
							}
						}).create();// ����
		// ��ʾ�Ի���
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
