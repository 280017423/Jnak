package qianye.jnak.service;

import qianye.jnak.common.FCommon;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;

public class loadpage_HttpThread extends Thread {
	private Handler handle = new Handler();
    private ProgressDialog progressDialog = null;
    String url = null;
    WebView webview_letterList=null;
    Context context=null;
    

 // �߳̿�ʼ
 	public void doStart(String url,  WebView webview_letterList,Context context) {
 		// �Ѳ���������
 		this.url = url;
 		this.webview_letterList=webview_letterList;
 		this.context=context;
 		// ����ʹ���ߣ�����ʼ��
 		progressDialog = new ProgressDialog(context);
 		// progressDialog.setTitle("�����֤");
 		// progressDialog.setMessage("�����������Ե�......");
 		progressDialog.setIndeterminate(true);
 		progressDialog = ProgressDialog.show(context, "��ȴ�",
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
			String baseUrl=context.getString(qianye.jnak.R.string.url_pageserver);
			String encoding="UTF-8";
			String mimeType="text/html";
			// web service����,resultΪ���ؽ��
			String result = FCommon.getContent(url);
			//result = URLEncoder.encode(result);
			webview_letterList.getSettings().setDefaultTextEncodingName(encoding) ;
			webview_letterList.loadDataWithBaseURL(baseUrl, result, mimeType, encoding, null);
			//webview_letterList.loadData(result, mimeType, encoding);
			//webview_letterList.loadUrl(url);
			// ȡ�����ȶԻ���
			progressDialog.dismiss();
			// clswdy.this.setProgressBarIndeterminateVisibility(false);
			// ������Ϣ,��֤ͨ����
			Message message = handle.obtainMessage();
			message.what = 1; // ��������Ϣ������
		


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
}
