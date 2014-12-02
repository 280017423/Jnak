package qianye.jnak.activity;

import java.io.File;
import java.util.ArrayList;

import qianye.jnak.R;
import qianye.jnak.adapter.ImageDetailAdapter;
import qianye.jnak.util.ConstantSet;
import qianye.jnak.util.FileUtil;
import qianye.jnak.widget.AutoScrollViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

public class ImageDetailActivity extends BaseActivity {
	private static final int SCROLL_DURATION = 1;
	private static final int DELAY_TIME = 1000;
	private static final String TAG = "MusicDetailActivity";
	private ArrayList<File> mImgsList;
	private int mPosition;
	private AutoScrollViewPager mViewPager;
	private TextView mTvCurrentTotal;
	private TextView mTvMusicName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_detail);
		initVariables();
		initViews();
		setListener();
		// 设置全屏
		// UIUtil.systemUivisibility(this, Build.VERSION.RELEASE);
	}

	@SuppressWarnings("unchecked")
	private void initVariables() {
		Intent intent = getIntent();
		if (null != intent) {
			mImgsList = (ArrayList<File>) intent.getSerializableExtra(ConstantSet.KEY_INTENT_IMGS_LIST);
			mPosition = intent.getIntExtra(ConstantSet.KEY_INTENT_IMG_POSITION, 0);
		} else {
			finish();
		}
	}

	private void initViews() {
		mViewPager = (AutoScrollViewPager) findViewById(R.id.vp_photo_view);
		mTvCurrentTotal = (TextView) findViewById(R.id.tv_current_total_num);
		mTvMusicName = (TextView) findViewById(R.id.tv_current_music_name);
		mViewPager.setAdapter(new ImageDetailAdapter(this, mImgsList));
		mViewPager.setCurrentItem(mPosition, false);
		mTvCurrentTotal.setText((mPosition + 1) + "/" + mImgsList.size());
		mTvMusicName.setText(FileUtil.getFileNameNoEx(mImgsList.get(mPosition).getName()));
		mViewPager.setScrollDurationFactor(SCROLL_DURATION);
	}

	private void setListener() {
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int postion) {
				mPosition = postion;
				mTvCurrentTotal.setText((mPosition + 1) + "/" + mImgsList.size());
				mTvMusicName.setText(FileUtil.getFileNameNoEx(mImgsList.get(postion).getName()));
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

}
