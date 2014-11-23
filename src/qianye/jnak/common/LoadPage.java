package qianye.jnak.common;

import android.content.Context;
import android.webkit.WebView;

public class LoadPage extends Thread {

	String url = null;
    WebView webview_letterList=null;
    Context context=null;
    // 线程开始
    	public void doStart(String url,  WebView webview_letterList,Context context) {
    		// 把参数传进来
    		this.url = url;
    		this.webview_letterList=webview_letterList;
    		this.context=context;
    		this.start(); // 线程开始了
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
