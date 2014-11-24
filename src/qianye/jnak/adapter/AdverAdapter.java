package qianye.jnak.adapter;

import java.util.ArrayList;
import java.util.List;

import qianye.jnak.R;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

/**
 * 
 * Description the class 轮换图片适配器
 * 
 * @version 1.0
 * @author zou.sq
 * 
 */
public class AdverAdapter extends PagerAdapter {

	private Context mContext;
	private List<Integer> mList;

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文
	 */
	public AdverAdapter(Context context) {
		this.mContext = context;
		this.mList = new ArrayList<Integer>();
		mList.add(R.drawable.tb1);
		mList.add(R.drawable.tb2);
		mList.add(R.drawable.tb3);
		mList.add(R.drawable.tb4);
	}

	/**
	 * 
	 * @return 返回默认大小
	 */
	@Override
	public int getCount() {
		if (mList.size() > 1) {
			return Integer.MAX_VALUE;
		} else {
			return mList.size();
		}
	}

	/**
	 * 
	 * @return 返回真实大小
	 */
	public int getSize() {
		if (mList != null) {
			return mList.size();
		} else {
			return 0;
		}
	}

	protected class Item {
		ImageView mIvItem;
	}

	@Override
	public Object instantiateItem(View viewPager, int position) {

		final Item mItem = new Item();
		View itemView = View.inflate(mContext, R.layout.view_marquee_item, null);
		mItem.mIvItem = (ImageView) itemView.findViewById(R.id.iv_item);
		if (mList.size() > 0) {
			final int resId = mList.get(position % mList.size());
			mItem.mIvItem.setImageResource(resId);
		}
		((ViewPager) viewPager).addView(itemView);
		return itemView;
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