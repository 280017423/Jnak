package qianye.jnak.common;

import android.app.Activity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class chromeClient extends WebChromeClient {
	private Activity activity;
	
	public chromeClient(Activity activity)
	{
		this.activity=activity;
	}
	@Override 
    public void onProgressChanged(WebView view, int newProgress) { 

        //��̬�ڱ�������ʾ������ 
		this.activity.setProgress(newProgress*1000); 
        //super.onProgressChanged(view, newProgress); 
    } 
/*
    @Override 
    public void onReceivedTitle(WebView view, String title) { 
        //���õ�ǰactivity�ı����� 
    	this.activity.setTitle(title); 
        super.onReceivedTitle(view, title); 
    }*/ 

}


