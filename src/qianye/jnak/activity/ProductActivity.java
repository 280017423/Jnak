package qianye.jnak.activity;


import qianye.jnak.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;


public class ProductActivity extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);

		
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

}
