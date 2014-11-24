package qianye.jnak.activity;

import qianye.jnak.R;
import qianye.jnak.adapter.AdverAdapter;
import qianye.jnak.widget.AutoScrollViewPager;
import qianye.jnak.widget.CircleFlowIndicator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

public class IndustryActivity extends BaseActivity {
	private AutoScrollViewPager mViewPager;
	private RelativeLayout mGalleryLayout;
	private CircleFlowIndicator mCircleFlowIndicator;
	private AdverAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_industry);
		mAdapter = new AdverAdapter(this);
		mCircleFlowIndicator = (CircleFlowIndicator) findViewById(R.id.cfi_indicator);
		mCircleFlowIndicator.setCount(mAdapter.getSize());
		mViewPager = (AutoScrollViewPager) findViewById(R.id.vp);
		mGalleryLayout = (RelativeLayout) findViewById(R.id.rl_gallery_layout);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mCircleFlowIndicator.setSeletion(position % mAdapter.getSize());
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		LayoutParams lp = mGalleryLayout.getLayoutParams();
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int WIDTH = metric.widthPixels;
		lp.width = WIDTH;
		lp.height = WIDTH * 400 / 640;
		mGalleryLayout.setLayoutParams(lp);
	}

	@Override
	public void onResume() {
		mViewPager.startAutoScroll();
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mViewPager.stopAutoScroll();
	}

	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.tab_img_1:
				Intent i1 = new Intent(this, MainActivity.class);
				startActivity(i1);
				finish();
				break;
			case R.id.tab_img_2:

				break;
			case R.id.tab_img_3:
				Intent i3 = new Intent(this, WebBrowserActivity.class);
				i3.putExtra("str_loadurl", getString(R.string.url_shop));
				startActivity(i3);
				break;
			case R.id.tab_img_4:
				Intent i4 = new Intent(this, VideoListActivity.class);
				startActivity(i4);
				finish();
				break;
			default:
				break;
		}
	}

	public void onBtnClick(View v) {

		switch (v.getId()) {
			case R.id.btn_ico_1:
				GotoPageList(88);
				break;
			case R.id.btn_ico_2:
				Intent i = new Intent(IndustryActivity.this, TravelListActivity.class);
				startActivity(i);
				break;
			case R.id.btn_ico_3:
				GotoPageList(81);
				break;
			case R.id.btn_ico_4:
				GotoPageList(80);
				break;
			case R.id.btn_ico_5:
				GotoPageList(79);
				break;
			case R.id.btn_ico_6:
				GotoPageList(86);
				break;
		}
	}

	public void GotoPageList(int categoryid) {
		Intent i = new Intent(IndustryActivity.this, NewsListActivity.class);
		i.putExtra("categoryid", categoryid);
		startActivity(i);
	}

}