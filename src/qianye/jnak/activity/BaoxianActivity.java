package qianye.jnak.activity;

import qianye.jnak.R;
import android.os.Bundle;
import android.os.CountDownTimer;

public class BaoxianActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baoxian);
		initVariables();
		initView();
	}

	private void initVariables() {
	}

	private void initView() {
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
