package qianye.jnak.common;

import android.content.Context;
import android.webkit.WebView;

public class LoadPage extends Thread {

	String url = null;
    WebView webview_letterList=null;
    Context context=null;
    // �߳̿�ʼ
    	public void doStart(String url,  WebView webview_letterList,Context context) {
    		// �Ѳ���������
    		this.url = url;
    		this.webview_letterList=webview_letterList;
    		this.context=context;
    		this.start(); // �߳̿�ʼ��
    	}
    	
    	@Override
   	public void run() {
   		// TODO Auto-generated method stub
   		super.run();
   		try {
   			
   			FCommon.LoadWebViewUrl(webview_letterList,url);
   		} catch (Exception ex) {
   		

   		} finally {

   		}
   	}
   }
