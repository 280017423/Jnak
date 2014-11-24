package qianye.jnak.service;

import qianye.jnak.common.FCommon;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;

public class LoadpageHttpThread extends Thread {
	private Handler handle = new Handler();
    private ProgressDialog progressDialog = null;
    String url = null;
    WebView webview_letterList=null;
    Context context=null;
    

 // 线程开始
 	public void doStart(String url,  WebView webview_letterList,Context context) {
 		// 把参数传进来
 		this.url = url;
 		this.webview_letterList=webview_letterList;
 		this.context=context;
 		// 告诉使用者，请求开始了
 		progressDialog = new ProgressDialog(context);
 		// progressDialog.setTitle("身份认证");
 		// progressDialog.setMessage("正在请求，请稍等......");
 		progressDialog.setIndeterminate(true);
 		progressDialog = ProgressDialog.show(context, "请等待",
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
			String baseUrl=context.getString(qianye.jnak.R.string.url_pageserver);
			String encoding="UTF-8";
			String mimeType="text/html";
			// web service请求,result为返回结果
			String result = FCommon.getContent(url);
			//result = URLEncoder.encode(result);
			webview_letterList.getSettings().setDefaultTextEncodingName(encoding) ;
			webview_letterList.loadDataWithBaseURL(baseUrl, result, mimeType, encoding, null);
			//webview_letterList.loadData(result, mimeType, encoding);
			//webview_letterList.loadUrl(url);
			// 取消进度对话框
			progressDialog.dismiss();
			// clswdy.this.setProgressBarIndeterminateVisibility(false);
			// 构造消息,验证通过了
			Message message = handle.obtainMessage();
			message.what = 1; // 这里是消息的类型
		


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
}
