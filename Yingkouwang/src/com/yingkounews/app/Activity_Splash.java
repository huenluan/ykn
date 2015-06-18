package com.yingkounews.app;

import java.util.Vector;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
@SuppressWarnings("unused")
public class Activity_Splash extends Activity {


	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	private static final int GO_SHOP = 1002;

	// 延迟2秒
	private static final long SPLASH_DELAY_MILLIS = 2000;


	
	private String award_notice;
	
	/**
	 * Handler:跳转到不同界面
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {			
			case GO_GUIDE:

				goGuide();
				break;
			
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.activity_splash);
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {		
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
	}



	private void goGuide() {
		Intent intent = new Intent(Activity_Splash.this, Activity_Main.class);
		Activity_Splash.this.startActivity(intent);
		Activity_Splash.this.finish();
	}

}
