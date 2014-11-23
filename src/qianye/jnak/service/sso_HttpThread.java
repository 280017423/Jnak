package qianye.jnak.service;

import java.util.HashMap;

import qianye.jnak.service.sso;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 用户认证线程
 * 
 * @author panxianyi
 * 
 */
public class sso_HttpThread extends Thread {

	private Handler handle = null;
	String url = null;
	String nameSpace = null;
	String methodName = null;
	String action = null;
	HashMap<String, Object> params = null;
	ProgressDialog progressDialog = null;

	public sso_HttpThread(Handler hander) {
		handle = hander;
	}

	// 线程开始
	public void doStart(String url, String nameSpace, String methodName,
			String action, Context context, HashMap<String, Object> params) {
		// 把参数传进来
		this.url = url;
		this.nameSpace = nameSpace;
		this.methodName = methodName;
		this.params = params;
		this.action = action;
		// 告诉使用者，请求开始了
		progressDialog = new ProgressDialog(context);
		// progressDialog.setTitle("身份认证");
		// progressDialog.setMessage("正在请求，请稍等......");
		progressDialog.setIndeterminate(true);
		progressDialog = ProgressDialog.show(context, "身份认证",
				"正在拉取数据，请稍等......", true, true);
		progressDialog.setButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i) {
				progressDialog.cancel();

			}
		});
		progressDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
					}
				});
		progressDialog.show();
		this.start(); // 线程开始了
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			// web service请求,result为返回结果
			String result = CallWebService();

			// 取消进度对话框
			progressDialog.dismiss();
			// clswdy.this.setProgressBarIndeterminateVisibility(false);
			// 构造消息,验证通过了
			Message message = handle.obtainMessage();
			Bundle b = new Bundle();
			message.what = 1; // 这里是消息的类型
			b.putString("data", result); // 这里是消息传送的数据

			message.setData(b);
			handle.sendMessage(message);

		} catch (Exception ex) {
			progressDialog.dismiss();
			// 构造消息，程序出错了
			Message message = handle.obtainMessage();
			Bundle b = new Bundle();
			message.what = 2;

			b.putString("error", ex.getMessage());

			message.setData(b);
			handle.sendMessage(message);

		} finally {

		}
	}

	/**

	     * 
	     */
	protected String CallWebService() throws Exception {

		String str_username = null, str_password = null, returnStr = null;

		sso s = new sso(nameSpace, url, methodName, action);
		if (params != null && !params.isEmpty()) {

			str_username = params.get("userID").toString();
			str_password = params.get("password").toString();
		}

		returnStr = s.CheckUser(str_username, str_password);

		return returnStr;

	}
}