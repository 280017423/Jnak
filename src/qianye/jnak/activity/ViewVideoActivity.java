package qianye.jnak.activity;

import qianye.jnak.R;
import qianye.jnak.common.FCommon;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class ViewVideoActivity extends Activity {
	VideoView videoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置成全屏模式
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 强制为横屏
		setContentView(R.layout.activity_view_video);
		videoView = (VideoView) findViewById(R.id.videoView);
		// videoView.setVideoPath("/sdcard/xyx.3gp");

		String sdCard = FCommon.getSDPath() + "/qyjnak/upload/";
		String videoPath = sdCard + "123.mp4";
		videoView.setVideoURI(Uri.parse(videoPath));
		MediaController mediaController = new MediaController(this);
		videoView.setMediaController(mediaController);
		videoView.start();
		// videoView.requestFocus();
	}

}
