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
 * �û���֤�߳�
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

	// �߳̿�ʼ
	public void doStart(String url, String nameSpace, String methodName,
			String action, Context context, HashMap<String, Object> params) {
		// �Ѳ���������
		this.url = url;
		this.nameSpace = nameSpace;
		this.methodName = methodName;
		this.params = params;
		this.action = action;
		// ����ʹ���ߣ�����ʼ��
		progressDialog = new ProgressDialog(context);
		// progressDialog.setTitle("�����֤");
		// progressDialog.setMessage("�����������Ե�......");
		progressDialog.setIndeterminate(true);
		progressDialog = ProgressDialog.show(context, "�����֤",
				"������ȡ���ݣ����Ե�......", true, true);
		progressDialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
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
		this.start(); // �߳̿�ʼ��
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			// web service����,resultΪ���ؽ��
			String result = CallWebService();

			// ȡ�����ȶԻ���
			progressDialog.dismiss();
			// clswdy.this.setProgressBarIndeterminateVisibility(false);
			// ������Ϣ,��֤ͨ����
			Message message = handle.obtainMessage();
			Bundle b = new Bundle();
			message.what = 1; // ��������Ϣ������
			b.putString("data", result); // ��������Ϣ���͵�����

			message.setData(b);
			handle.sendMessage(message);

		} catch (Exception ex) {
			progressDialog.dismiss();
			// ������Ϣ�����������
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