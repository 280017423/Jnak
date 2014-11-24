package qianye.jnak.activity;

import java.util.ArrayList;
import java.util.List;

import qianye.jnak.R;
import qianye.jnak.parser.NewsXmlParser;
import qianye.jnak.widget.AutoScrollViewPager;
import qianye.jnak.widget.CircleFlowIndicator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class IndustryActivity extends BaseActivity {
	private NewsXmlParser xmlParser;
	private AutoScrollViewPager mViewPager;
	private RelativeLayout mGalleryLayout;
	private CircleFlowIndicator mCircleFlowIndicator;
	private List<ImageView> imageViews; // 滑动的图片集合

	private int[] imageResId; // 图片ID

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_industry);

		xmlParser = new NewsXmlParser(this);

		imageResId = xmlParser.getSlideImages();

		imageViews = new ArrayList<ImageView>();

		// 初始化图片资源
		for (int i = 0; i < imageResId.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(imageResId[i]);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageViews.add(imageView);
		}

		mCircleFlowIndicator = (CircleFlowIndicator) findViewById(R.id.cfi_indicator);
		mCircleFlowIndicator.setCount(imageViews.size());
		mViewPager = (AutoScrollViewPager) findViewById(R.id.vp);
		mGalleryLayout = (RelativeLayout) findViewById(R.id.rl_gallery_layout);
		mViewPager.setAdapter(new MyAdapter());
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mCircleFlowIndicator.setSeletion(position % imageViews.size());
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
		mViewPager.setAdapter(new MyAdapter());
	}

	@Override
	protected void onResume() {
		mViewPager.startAutoScroll();
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mViewPager.stopAutoScroll();
	}

	/**
	 * 填充ViewPager页面的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageResId.length;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			return imageViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
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