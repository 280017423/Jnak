package qianye.jnak.activity;

import qianye.jnak.R;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 用户活动场景
 * 
 * 调用HTTP服务
 * 
 * @author panxianyi
 * 
 */
public class WebBrowserActivity extends BaseActivity {

	/** Called when the activity is first created. */

	private WebView webview_letterList;
	private String str_userName;
	private String str_loadUrl;
	ProgressDialog progressBar;
	AlertDialog alertDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webbrowser);
		findViews();
		setListensers();
		initData();
	}

	// 查找控件
	private void findViews() {
		webview_letterList = (WebView) findViewById(R.id.webView1);
	}

	// 初始信息
	private void initData() {

		activityList.add(WebBrowserActivity.this);
		progressBar = ProgressDialog.show(WebBrowserActivity.this, null, "正在努力加载，请稍后…");
		alertDialog = new AlertDialog.Builder(this).create();
		// 获得传入参数
		Intent intent = getIntent();
		str_userName = BaseActivity.pub_userName;
		str_loadUrl = intent.getStringExtra("str_loadurl");

		webview_letterList.getSettings().setJavaScriptEnabled(true);
		webview_letterList.getSettings().setSupportZoom(true);
		webview_letterList.getSettings().setBuiltInZoomControls(true);
		webview_letterList.loadUrl(str_loadUrl);
		webview_letterList.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			public void onPageFinished(WebView view, String url) {
				if (progressBar.isShowing()) {
					progressBar.dismiss();
				}
			}

			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Toast.makeText(WebBrowserActivity.this, "网络连接失败 ,请连接网络!", Toast.LENGTH_LONG);
				// alertDialog.setTitle("ERROR");
				// alertDialog.setMessage(description);
				// alertDialog.setButton("OK", new
				// DialogInterface.OnClickListener(){
				// public void onClick(DialogInterface dialog, int which) {
				// // TODO Auto-generated method stub
				// }
				// });
				// alertDialog.show();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview_letterList.canGoBack()) {
			webview_letterList.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// Listen for button clicks
	private void setListensers() {
		// text_back.setOnClickListener(callBack);
	}

	private TextView.OnClickListener callBack = new TextView.OnClickListener() {
		public void onClick(View v) {
			System.gc();
			// System.exit(0);
			finish();
		}
	};
}
