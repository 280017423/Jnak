package qianye.jnak.activity;

import qianye.jnak.R;
import qianye.jnak.common.FCommon;
import qianye.jnak.dao.ArticleDao;
import qianye.jnak.model.Article;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ViewPage extends Activity {
	ProgressDialog progressBar;
	AlertDialog alertDialog;
	private String str_loadUrl;
	private int str_categoryid;
	private int str_articleid;
	private String str_data;
	private String str_title;
private int str_datatype;
	TextView zxtitle;
	ArticleDao dao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_page);
		dao = new ArticleDao(this);
		// 获得传入参数
		Intent intent = getIntent();
		str_loadUrl = intent.getStringExtra("loadUrl");
		str_data = intent.getStringExtra("data");
		str_title = intent.getStringExtra("title");
		str_categoryid = intent.getIntExtra("categoryid", 1);
		str_articleid = intent.getIntExtra("articleid", 0);
		str_datatype=1;
		if(str_data==null ||str_data.equals(""))
		{
			//str_datatype=2;
			Article article=dao.getByArticleId(str_articleid);
			str_title=article.getTitle();
			str_data=FCommon.DecodeHtml(article.getContent());
		}
		//boolean netStatus = FCommon.NetworkStatusOK(this);
		//if (!netStatus) {//如果没有网络,则读本地资源文件
			String sdCard = FCommon.getSDPath() + "/qyjnak/";
			if (sdCard != "") {
				String newChar = "file://" + sdCard;
				String oldChar = "http://jnakdl.idc.1001n.net/";
				str_data = str_data.replace(oldChar, newChar);
			}
		//}	
		Log.d("str_data", str_data);
		zxtitle = (TextView) findViewById(R.id.zxtitle);
		zxtitle.setText(str_title);
		progressBar = ProgressDialog.show(ViewPage.this, null, "正在进入网页，请稍后…");
		alertDialog = new AlertDialog.Builder(this).create();
		WebView webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		// webView.setWebChromeClient(new WebViewClient() );
		// webView.setWebViewClient(client);
		webView.getSettings().setDefaultTextEncodingName("utf-8");

		// tr_loadUrl = getString(R.string.url_default);
		if (str_datatype == 1) {

			webView.loadDataWithBaseURL(null, str_data, "text/html", "utf-8",
					null);
		} else if (str_datatype == 2) {

		} else {
			webView.loadUrl(str_loadUrl);
		}

		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			public void onPageFinished(WebView view, String url) {
				if (progressBar.isShowing()) {
					progressBar.dismiss();
				}
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(ViewPage.this, "网络连接失败 ,请连接网络!",
						Toast.LENGTH_LONG);
				alertDialog.setTitle("ERROR");
				alertDialog.setMessage(description);
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
							}
						});
				alertDialog.show();
			}
		});

	}

	public void closeme(View v) {
		Log.d("closeme;", "点击了关闭");
		this.finish();
	}
}
