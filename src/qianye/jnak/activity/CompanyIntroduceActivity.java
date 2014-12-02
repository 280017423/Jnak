package qianye.jnak.activity;

import qianye.jnak.R;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class CompanyIntroduceActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_introduce);
		initVariables();
		initView();
	}

	private void initVariables() {
	}

	private void initView() {
		ImageView iv = (ImageView) findViewById(R.id.iv_introduce_bg);
		LayoutParams lp = iv.getLayoutParams();
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int WIDTH = metric.widthPixels;
		lp.width = WIDTH;
		lp.height = WIDTH * 279 / 420;
		iv.setLayoutParams(lp);
	}

}
