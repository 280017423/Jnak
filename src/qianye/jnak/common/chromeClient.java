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

        //动态在标题栏显示进度条 
		this.activity.setProgress(newProgress*1000); 
        //super.onProgressChanged(view, newProgress); 
    } 
/*
    @Override 
    public void onReceivedTitle(WebView view, String title) { 
        //设置当前activity的标题栏 
    	this.activity.setTitle(title); 
        super.onReceivedTitle(view, title); 
    }*/ 

}


