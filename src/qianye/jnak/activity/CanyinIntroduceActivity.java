package qianye.jnak.activity;

import qianye.jnak.R;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class CanyinIntroduceActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_canyin_introduce);
		initVariables();
		initView();
	}

	private void initVariables() {
	}

	private void initView() {
		ImageView iv = (ImageView) findViewById(R.id.iv_canyin_bg);
		LayoutParams lp = iv.getLayoutParams();
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int WIDTH = metric.widthPixels;
		lp.width = WIDTH;
		lp.height = WIDTH * 550 / 640;
		iv.setLayoutParams(lp);
		new CountDownTimer(200, 200) {
			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				findViewById(R.id.sv_layout).scrollTo(0, 0);
			}
		}.start();
	}

}
