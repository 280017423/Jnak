package qianye.jnak.activity;

import java.util.ArrayList;
import java.util.List;

import qianye.jnak.R;
import qianye.jnak.common.FCommon;
import qianye.jnak.util.StringUtil;
import qianye.jnak.widget.LoadingUpView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

/**
 * 基础活动场景
 * 
 * @author zou.sq
 */
public class BaseActivity extends Activity {
	public static List<Activity> mActivityList = new ArrayList<Activity>();
	public static String mPubUserName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.setDebugMode(false);
		MobclickAgent.openActivityDurationTrack(false);

	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("");
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("");
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 默认的toast方法，该方法封装下面的两点特性：<br>
	 * 1、只有当前activity所属应用处于顶层时，才会弹出toast；<br>
	 * 2、默认弹出时间为 Toast.LENGTH_SHORT;
	 * 
	 * @param msg
	 *            弹出的信息内容
	 */
	public void toast(final String msg) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!StringUtil.isNullOrEmpty(msg)) {
					Toast toast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT);
					TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
					// 用来防止某些系统自定义了消息框
					if (tv != null) {
						tv.setGravity(Gravity.CENTER);
					}
					toast.show();
				}
			}
		});
	}

	protected boolean showLoadingUpView(LoadingUpView loadingUpView) {
		if (loadingUpView != null && !loadingUpView.isShowing()) {
			loadingUpView.showPopup();
			return true;
		}
		return false;
	}

	protected boolean dismissLoadingUpView(LoadingUpView loadingUpView) {
		if (loadingUpView != null && loadingUpView.isShowing()) {
			loadingUpView.dismiss();
			return true;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "关于").setIcon(android.R.drawable.ic_menu_edit);
		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "退出").setIcon(android.R.drawable.ic_menu_more);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case Menu.FIRST + 1:// 关于我们
				new AlertDialog.Builder(this)
						.setTitle("关于我们")
						.setMessage(
								getString(R.string.app_name) + "\n当前版本：" + FCommon.getVerName(this) + "("
										+ FCommon.getVerCode(this) + ")")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialoginterface, int i) {

							}
						}).show();
				break;
			case Menu.FIRST + 2:// 退出
				new AlertDialog.Builder(this).setTitle("提示").setMessage("是否退出本程序？")
						.setPositiveButton("退出", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialoginterface, int i) {
								for (Activity activity : mActivityList) {
									activity.finish();
								}
								System.gc();
								System.exit(0);
							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialoginterface, int i) {

							}
						}).show();
				break;
		}

		return false;
	}

}
