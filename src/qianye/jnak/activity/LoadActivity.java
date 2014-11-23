package qianye.jnak.activity;

import java.util.Timer;
import java.util.TimerTask;

import qianye.jnak.R;
import qianye.jnak.common.FCommon;
import qianye.jnak.common.NetGetData;
import qianye.jnak.dao.ArticleDao;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * 欢迎场景
 * 
 * @author panxianyi
 * 
 */
public class LoadActivity extends PublicActivity {
	ProgressDialog progressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		progressBar = ProgressDialog.show(this, null, "正努力加载数据，请稍后…");
		getData();
	}

	void getData() {
		boolean netStatus = FCommon.NetworkStatusOK(this);

		if (netStatus) {
			String[] par = new String[] { "0" };
			int maxarticleid = new ArticleDao(this).GetMaxArticleId();
			String action = "get_article_list";
			String bodyStr = "{\"category_id\":0,\"page_size\":100,\"page_index\":1,\"max_article_id\":" + maxarticleid
					+ "}";
			new NetGetData().getData(this, action, bodyStr, par, this, MainActivity.class, progressBar);
		} else {
			Log.d("loadpage", "网络不存在!");
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Intent txllIntent = new Intent(this, MainActivity.class);
			startActivity(txllIntent);
			finish();
		}
	}
}
