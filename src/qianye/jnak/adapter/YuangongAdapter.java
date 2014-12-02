package qianye.jnak.adapter;

import java.io.File;
import java.util.List;

import qianye.jnak.R;
import qianye.jnak.listener.ImageLoadListener;
import qianye.jnak.util.AsyncImageLoader;
import qianye.jnak.util.UIUtil;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 文件列表适配器
 * 
 * @author zou.sq
 * @since 2013-03-12 下午04:37:29
 * @version 1.0
 */
public class YuangongAdapter extends BaseAdapter {
	private static final int SPACE_VALUE = 10;
	private static final int NUM_COLUMNS = 4;
	private List<File> mFilesList;
	private Context mContext;
	private int mImgSize;
	private GridView mGridView;
	private AsyncImageLoader mImageLoader;

	/**
	 * 实例化对象
	 * 
	 * @param context
	 *            上下文
	 * @param dataList
	 *            数据列表
	 */
	public YuangongAdapter(Activity context, List<File> dataList, GridView gridView) {
		this.mContext = context;
		this.mFilesList = dataList;
		mGridView = gridView;
		mImageLoader = new AsyncImageLoader();
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		mImgSize = (width - UIUtil.dip2px(mContext, SPACE_VALUE) * (NUM_COLUMNS + 1)) / NUM_COLUMNS;
	}

	@Override
	public int getCount() {
		if (mFilesList != null && !mFilesList.isEmpty()) {
			return mFilesList.size();
		}
		return 0;
	}

	@Override
	public File getItem(int position) {
		if (mFilesList != null) {
			return mFilesList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHode view = new viewHode();
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.view_yuangong_item, null);
			view.mName = (TextView) convertView.findViewById(R.id.tv_folder_name);
			view.mIcon = (ImageView) convertView.findViewById(R.id.iv_folder_img);
			convertView.setTag(view);
		} else {
			view = (viewHode) convertView.getTag();
		}
		File file = mFilesList.get(position);

		LayoutParams layoutParams = view.mIcon.getLayoutParams();
		layoutParams.width = mImgSize;
		layoutParams.height = mImgSize;
		view.mIcon.setLayoutParams(layoutParams);

		view.mIcon.setTag(file.getAbsolutePath());
		if (null != file) {
			view.mIcon.setImageResource(R.drawable.format_picture);
			mImageLoader.loadDrawable(file.getAbsolutePath(), mImgSize, new ImageLoadListener() {

				@Override
				public void imageLoaded(Bitmap bitmap, String imageUrl) {
					ImageView imageViewByTag = (ImageView) mGridView.findViewWithTag(imageUrl);
					if (imageViewByTag != null) {
						imageViewByTag.setImageBitmap(bitmap);
					}
				}
			});
			view.mName.setText(file.getName());
		}
		return convertView;
	}

	class viewHode {
		TextView mName;
		ImageView mIcon;
	}
}