package qianye.jnak.activity;

import qianye.jnak.R;
import qianye.jnak.common.FCommon;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 基础活动场景
 * 
 * @author panxianyi
 * 
 */
public class baseActivity extends PublicActivity {
	public static String pub_userName;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*
		 * add()方法的四个参数，依次是： 1、组别，如果不分组的话就写Menu.NONE,
		 * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单 3、顺序，那个菜单现在在前面由这个参数的大小决定
		 * 4、文本，菜单的显示文本
		 */
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
								// ActivityManager am =
								// (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
								// am.killBackgroundProcesses(getPackageName());
								for (Activity activity : activityList) {
									activity.finish();
								}
								System.gc();
								System.exit(0);
								// finish();//关闭当前场景
							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialoginterface, int i) {

							}
						}).show();
				break;
		}

		return false;
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// Toast.makeText(this, "选项菜单关闭了", Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Toast.makeText(this,
		// "选项菜单显示之前onPrepareOptionsMenu方法会被调用，你可以用此方法来根据打当时的情况调整菜单",
		// Toast.LENGTH_LONG).show();

		// 如果返回false，此方法就把用户点击menu的动作给消费了，onCreateOptionsMenu方法将不会被调用

		return true;

	}

}
