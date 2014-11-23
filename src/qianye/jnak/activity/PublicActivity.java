package qianye.jnak.activity;

import java.util.ArrayList;
import java.util.List;

import qianye.jnak.R;
import android.app.Activity;
import android.view.Window;
import android.widget.Toast;

/**
 * 公用活动场景
 * 
 * @author panxianyi
 * 
 */
public class PublicActivity extends Activity {
	public static List<Activity> activityList = new ArrayList<Activity>();

	public void alert(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/*
	 * 自定义标题
	 */
	public void LoadCustomTime(int resLayoutId) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(resLayoutId);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_style);
	}
}
