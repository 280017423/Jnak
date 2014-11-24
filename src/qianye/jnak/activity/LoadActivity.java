package qianye.jnak.activity;

import qianye.jnak.R;
import qianye.jnak.common.FCommon;
import qianye.jnak.common.NetGetData;
import qianye.jnak.dao.ArticleDao;
import qianye.jnak.widget.LoadingUpView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * 欢迎场景
 * 
 * @author panxianyi
 * 
 */
public class LoadActivity extends BaseActivity {

	private LoadingUpView mLoadingUpView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		mLoadingUpView = new LoadingUpView(this);
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
			showLoadingUpView(mLoadingUpView);
			new NetGetData().getData(this, action, bodyStr, par, this, MainActivity.class, mLoadingUpView);
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
