package qianye.jnak.activity;

import java.util.ArrayList;

import qianye.jnak.R;
import qianye.jnak.common.FCommon;
import qianye.jnak.common.NetGetData;
import qianye.jnak.dao.ArticleDao;
import qianye.jnak.dao.ListViewAdapter;
import qianye.jnak.model.Article;
import qianye.jnak.widget.LoadingUpView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class NewsListActivity extends BaseActivity implements OnScrollListener {
	private LoadingUpView mLoadingUpView;
	private TextView loadInfo;
	private ListView listView;
	private LinearLayout loadLayout;
	private ArrayList<Article> items;
	private int currentPage = 1; // 默认在第一页
	private static final int lineSize = 10; // 每次显示数
	private int allRecorders = 0; // 全部记录数
	private int pageSize = 1; // 默认共一页
	private int lastItem;
	private ListViewAdapter baseAdapter;
	ArticleDao dao;
	private int categoryid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_list);
		mLoadingUpView = new LoadingUpView(this);
		dao = new ArticleDao(this);

		listView = (ListView) findViewById(R.id.lv_newslist);
		// 创建一个角标线性布局用来显示"正在加载"
		loadLayout = new LinearLayout(this);
		loadLayout.setGravity(Gravity.CENTER);
		// 定义一个文本显示“正在加载”
		loadInfo = new TextView(this);
		loadInfo.setText("正在加载...");
		loadInfo.setGravity(Gravity.CENTER);
		// 增加组件
		loadLayout.addView(loadInfo, new LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		// 增加到listView底部
		listView.addFooterView(loadLayout);
		listView.setOnScrollListener(this);
		// 获得传入参数
		Intent intent = getIntent();
		categoryid = intent.getIntExtra("categoryid", 0);
		showAllData();

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Article article = (Article) baseAdapter.getItem(arg2);
				Log.d("onItemClick", "item=" + arg2 + "|title=" + article.getTitle());
				Intent i = new Intent(NewsListActivity.this, ViewPage.class);
				i.putExtra("categoryid", article.getCategoryid());
				i.putExtra("data", FCommon.DecodeHtml(article.getContent()));
				i.putExtra("articleid", article.getArticleid());
				i.putExtra("title", article.getTitle());
				i.putExtra("id", article.getArticleid());
				startActivity(i);
			}
		});
	}

	/**
	 * 读取全部数据
	 * 
	 */
	public void showAllData() {
		allRecorders = dao.GetArticleAllCount(categoryid);
		// 计算总页数
		pageSize = (allRecorders + lineSize - 1) / lineSize;
		System.out.println("allRecorders = " + allRecorders);
		System.out.println("pageSize = " + pageSize);
		items = dao.getAllItems(currentPage, lineSize, categoryid);
		// for(int i=0; i
		// System.out.println(items.get(i));
		// }

		baseAdapter = new ListViewAdapter(items, lineSize, this);
		listView.setAdapter(baseAdapter);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount - 1; // 统计是否到最后
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		System.out.println("进入滚动界面了");
		// 是否到最底部并且数据没读完//不再滚动
		if (lastItem == baseAdapter.getCount() && currentPage < pageSize
				&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			currentPage++;
			// 设置显示位置
			listView.setSelection(lastItem);
			// 增加数据
			appendDate();
		}
	}

	/**
	 * 增加数据
	 * */
	private void appendDate() {
		ArrayList<Article> additems = dao.getAllItems(currentPage, lineSize, categoryid);
		baseAdapter.setCount(baseAdapter.getCount() + additems.size());
		// 判断，如果到了最末尾则去掉“正在加载”
		if (allRecorders == baseAdapter.getCount()) {
			listView.removeFooterView(loadLayout);
		}
		items.addAll(additems);
		// 通知记录改变
		baseAdapter.notifyDataSetChanged();
	}

	public void initarticle(View v) {
		boolean netStatus = FCommon.NetworkStatusOK(this);

		if (netStatus) {
			String[] par = new String[] { "0" };
			int maxarticleid = new ArticleDao(this).GetMaxArticleId();
			String action = "get_article_list";
			String bodyStr = "{\"category_id\":0,\"page_size\":100,\"page_index\":1,\"max_article_id\":" + maxarticleid
					+ "}";
			showLoadingUpView(mLoadingUpView);
			new NetGetData().getData(this, action, bodyStr, par, null, null, mLoadingUpView);
		} else {
			Log.d("loadpage", "网络不存在!");
		}

	}
}
