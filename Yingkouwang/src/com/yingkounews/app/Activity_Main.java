package com.yingkounews.app;

import org.json.JSONException;
import org.json.JSONObject;


import com.yingkounews.app.R;
import com.yingkounews.app.common.MyApplication;
import com.yingkounews.app.common.ResultPacket;
import com.yingkounews.app.function.ImageDownLoadByUrl;
import com.yingkounews.app.net.Request_PushReg;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Message;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;
import android.widget.Toast;

public class Activity_Main extends Activity {

	private WebView view;

	private int count=0;
	MyOrderBroadcastReciver orderReciver;
	
	private MyApplication application;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		CheckIsNetOk();
		init();
		LoadUrl();
		
//		Intent intent = new Intent();
//		intent.setClass(Activity_Main.this, Activity_Share.class);
//		startActivity(intent);
		application =MyApplication.getInstance();
		IntentFilter intentFilter_pushreg = new IntentFilter();
		intentFilter_pushreg.addAction("com.yingkounews.app.pushreg");
		this.registerReceiver(orderReciver, intentFilter_pushreg);
		
		
		
		
	}

	private void init() {
		view = (WebView) findViewById(R.id.webViewMain);
	}

	
	
	
	public class MyOrderBroadcastReciver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			 if (action.equals("com.yingkounews.app.pushreg")) {
				
					PushReg();
				
			}		
		}
	}
	
	
	
	private void PushReg() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ResultPacket resultPacket = new ResultPacket();
				Request_PushReg request = new Request_PushReg(
						Activity_Main.this, String.valueOf("ffd"), "tcs",
						application.get_ClientID());
				resultPacket = request.DealProcess();
			}
		}).start();
	}
	
	
	@SuppressLint("SetJavaScriptEnabled")
	private void LoadUrl() {
		// ����WebView���ԣ��ܹ�ִ��Javascript�ű�
		view.getSettings().setJavaScriptEnabled(true);
		// ���ÿ���֧������
		view.getSettings().setSupportZoom(true);
		// ���ó������Ź���
		view.getSettings().setBuiltInZoomControls(false);
		// ������������
		// webview.getSettings().setUseWideViewPort(true);
		// ����Ӧ��Ļ
		// webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		view.getSettings().setLoadWithOverviewMode(true);

		// webView.loadUrl("http://www.baidu.com");
		view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		// view.setWebChromeClient(new WebChromeClient());
		view.setWebViewClient(new WebViewClient());
		
		
		//��������
		WebSettings s = view.getSettings();    
		s.setBuiltInZoomControls(true);     
		s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);    
		s.setUseWideViewPort(true);    
		s.setLoadWithOverviewMode(true);   
		s.setSavePassword(true);    
		s.setSaveFormData(true);    
		s.setJavaScriptEnabled(true);     
		// enable navigator.geolocation    
		s.setGeolocationEnabled(true);    
		s.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");     
//		// enable Web Storage: localStorage, sessionStorage     
		s.setDomStorageEnabled(true);  
		view.requestFocus(); 
		view.setScrollBarStyle(0);
		
		
		view.addJavascriptInterface(new WebAppInterface(this), "YingKou");
		view.loadUrl(getString(R.string.mainurl) + "?os=android&ver=1");
		
		
		
	}

	// To handle the back button key press
	public boolean onKeyDown(int keyCode, KeyEvent event) {   
    if ((keyCode == KeyEvent.KEYCODE_BACK) && view.canGoBack()) {
    	view.goBack();
        return true;
    }
    
    if (keyCode== KeyEvent.KEYCODE_BACK)
    {
    	if (count==0)
    	{
    		count++;
    		
//    		Intent intent = new Intent();
//    		intent.setClass(Activity_Main.this, Activity_Share.class);
//    		startActivity(intent);
    	}
    }
    
    return super.onKeyDown(keyCode, event);
}

	/**
	 * �Զ����Android�����JavaScript����֮���������
	 * 
	 * @author 1
	 * 
	 */
	class WebAppInterface {
		Context mContext;

		/** Instantiate the interface and set the context */
		WebAppInterface(Context c) {
			mContext = c;
		}

		/** Show a toast from the web page */
		// ���target ���ڵ���API 17������Ҫ��������ע��
		@JavascriptInterface
		public void showToast(String toast) {
			// Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
			Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
		}

		@JavascriptInterface
		public void YingKouShowMsg(String toast) {
			// Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
			Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
		}
		
		
		
		@JavascriptInterface
		public void YingKouOpenShare(String title,String des,String url) {
			Intent intent = new Intent();
			intent.setClass(Activity_Main.this, Activity_Share.class);
			intent.putExtra("title", title);
			intent.putExtra("des", des);
			intent.putExtra("url", url);
			startActivity(intent);
		}
		
		@JavascriptInterface
		public void share(String json) {
			
			try {
				JSONObject object = new JSONObject(json);			
			
			String title = object.getString("title");
			String des= object.getString("des");
			String url= object.getString("url");
			Intent intent = new Intent();
			intent.setClass(Activity_Main.this, Activity_Share.class);
			intent.putExtra("title", title);
			intent.putExtra("des", des);
			intent.putExtra("url", url);
			startActivity(intent);
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		@JavascriptInterface
		public void savetophoto(String url)
		{
			ImageDownLoadByUrl aa = new ImageDownLoadByUrl(Activity_Main.this, url);
			aa.StartDownLoadPic();
		}
		

		@JavascriptInterface
		public String YingKouGetUserGps() {
			// if (latitude > 0 && lontitude > 0) {
			// JSONObject object = new JSONObject();
			// try {
			// object.put("lng", lontitude);
			// object.put("lat", latitude);
			// object.put("addr", addr);
			// } catch (JSONException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// return "";
			// }
			// return object.toString();
			// }
			return "";
		}

		@JavascriptInterface
		public void YingKouOpenWX(String weixinAccount) {
			try {

				Intent intent = new Intent();
				ComponentName cmp = new ComponentName("com.tencent.mm",
						"com.tencent.mm.ui.LauncherUI");
				intent.setAction(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setComponent(cmp);
				startActivity(intent);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	/*
	 * ���ô�ӡ�ӿ�,��ת��ɨһɨ����
	 */
	@JavascriptInterface
	public void nidsGetPrint(String param) {
		// Toast.makeText(Activity_Main.this, param, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		// intent.setClass(Activity_Main.this, Activity_ScanCode.class);
		intent.putExtra("isPrint", true);
		intent.putExtra("param", param);
		startActivity(intent);
	}

	// ͨ�� ID ��ȡ�� App����Ϣ
	private void GetAppInfoByID(final int id) {
		new Thread(new Runnable() {
			@Override
			public void run() {

			}
		}).start();
	}
	
	
	
	//是否联网
	private void CheckIsNetOk() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

		State mobleState = null;
		
		NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
		if (mobileInfo!=null)
		{
			mobleState = mobileInfo.getState();
		}
		else 
		{
			mobleState =State.DISCONNECTED;
		}

		State wifiState = connectivityManager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).getState();

		if (mobleState == State.CONNECTED) {
			
		} else if (wifiState == State.CONNECTED) {
			
		}
		else 
		{
			Toast.makeText(Activity_Main.this, "不好意思你断网了，请先联网再打开应用", Toast.LENGTH_SHORT).show();
			Activity_Main.this.finish();
		}
	}
}
