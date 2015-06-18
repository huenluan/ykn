package com.yingkounews.app;


import java.util.Vector;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yingkounews.app.R;
import com.yingkounews.app.sinaweibo.AccessTokenKeeper;
import com.yingkounews.app.sinaweibo.Constants;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.open.utils.Util;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 分享页面
 * @author Administrator
 *
 */
public class Activity_Share extends Activity implements OnClickListener {
	
	
	
	private RelativeLayout layout_sms;
	private RelativeLayout layout_friend;
	private RelativeLayout layout_wx;
	private RelativeLayout layout_sina;
	private RelativeLayout layout_qq;
	
	private TextView tv_code;

	// 微信相关
	private static final String APP_ID = "wx35d5e851908a6d83";
	private IWXAPI api;
	private String share_title = "";
	private String share_content = "";

	// 新浪微博相关
	private WeiboAuth mWeiboAuth;
	public static Oauth2AccessToken accessToken;
	public SsoHandler mSsoHandler;
	/** 微博微博分享接口实例 */
	private IWeiboShareAPI mWeiboShareAPI = null;
	
	//腾讯分享相关
	private String mAppid = "1104643489";//腾讯的APPid
	private int shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
	private Tencent mTencent;
	
	private RelativeLayout layout_invite;
	private TextView tv_invitecount;

	
	
	private String title="";
	 private String des="";
	 private String url="";
	
	
	 private Button btn_exit;
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_share);
		LinearLayout container = (LinearLayout) findViewById(R.id.layout);
		setContentView(container);
	
	
		super.onCreate(savedInstanceState);

		
		title =getIntent().getStringExtra("title");
		des =getIntent().getStringExtra("des");
		url =getIntent().getStringExtra("url");
		
		

		api = WXAPIFactory.createWXAPI(this, APP_ID, true);
		api.registerApp(APP_ID);
		
		// 创建微博分享接口实例
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APP_KEY);

		mTencent = Tencent.createInstance("1102798896", this.getApplicationContext());
		
		initView();
		
		String message =title;
		
		//tv_code.setText(Html.fromHtml(message));
		
		share_title = title;
		share_content = title + " "+des + " "+ url;
		
		btn_exit =(Button)findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Activity_Share.this.finish();
			}
		});
	}

	private void initView()
	{
		layout_sms = (RelativeLayout) findViewById(R.id.layout_sms);
		layout_friend = (RelativeLayout) findViewById(R.id.layout_friend);
		layout_wx = (RelativeLayout) findViewById(R.id.layout_wx);
		layout_sina = (RelativeLayout) findViewById(R.id.layout_sina);
		layout_qq = (RelativeLayout)findViewById(R.id.layout_qq);
		
		//tv_code = (TextView)findViewById(R.id.tv_code);
		
		//layout_invite = (RelativeLayout)findViewById(R.id.layout_invite);
		//tv_invitecount = (TextView)findViewById(R.id.tv_invitecount);

		layout_sms.setOnClickListener(this);
		layout_friend.setOnClickListener(this);
		layout_wx.setOnClickListener(this);
		layout_sina.setOnClickListener(this);
		layout_qq.setOnClickListener(this);
		//layout_invite.setOnClickListener(this);
	}
	
	private void SetProgressBar(boolean bool)
	{
		//super.SetProgressBarShow(bool);
	}
	

	
	
	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			switch (msg.what)
			{
				case 100:
					SetProgressBar(false);
//					
//					Vector<ContactsInfo> vector = (Vector<ContactsInfo>)msg.obj;
//					
//					if(vector != null && vector.size()>0)
//					{
//						layout_invite.setVisibility(View.VISIBLE);
//						tv_invitecount.setText("成功�?���?+vector.size()");
//					}
//					else
//					{
//						layout_invite.setVisibility(View.GONE);
//					}
					
					super.handleMessage(msg);
					break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_sms:
			Uri smsToUri = Uri.parse("smsto:");
			Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
			intent.putExtra(
					"sms_body",
					title +" "+ des +" "+url);
			startActivity(intent);
			break;
		case R.id.layout_friend:
			gotoWx(share_title, share_content, 2);
			break;
		case R.id.layout_wx:
			gotoWx(share_title, share_content, 1);
			break;
		case R.id.layout_sina:
			mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);

			mSsoHandler = new SsoHandler(Activity_Share.this, mWeiboAuth);
			mSsoHandler.authorize(new AuthDialogListener());

			break;
		case R.id.layout_qq:
			
			ShareToQQ();
			
			break;
//		case R.id.layout_invite:
//			
//			Intent intent2 = new Intent();
//			//intent2.setClass(Activity_Share.this, Activity_InviteList.class);
//			startActivity(intent2);
			
			//break;
		default:
			break;
		}
	}

	private void gotoWx(String title, String content, int type) {
		if (isWXAppInstalledAndSupported(api) == true) {
//			// 初始化WXTextObject对象
//
//			WXTextObject textObject = new WXTextObject();
//			textObject.text = content;
//			
//			// 用WXTextObject对象初始化一个WXMediaMessage对象
//
//			WXMediaMessage msg = new WXMediaMessage();
//			msg.mediaObject = textObject;
//			msg.description = textObject.text;
//			
			Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.logo);           
//            msg.setThumbImage(thumb);
//            		 
//			SendMessageToWX.Req req = new SendMessageToWX.Req();
//			req.transaction = String.valueOf(System.currentTimeMillis());
//			req.message = msg;
//
//			if (type == 1) {
//				req.scene = SendMessageToWX.Req.WXSceneSession;// 分享到微信
//			} else if (type == 2) {
//				req.scene = SendMessageToWX.Req.WXSceneTimeline;// 分享到朋友圈
//			}
//			api.sendReq(req);
			
			shareInfo(type,url,title,title,thumb);
			

			finish();
		}
	}
	
	
	/**
	 * 分享信息
	 * 
	 * @param type
	 *            // 分享的类型 (1：分享到微信好友 2：分享到微信朋友圈)
	 * @param url
	 *            // 分享内容的超链接
	 * @param title
	 *            // 分享的标题
	 * @param content
	 *            // 分享的内容文本消息
	 * @param bitmap
	 *            // 分享内容的图片
	 */
	public void shareInfo(int type, String url, String title, String content, Bitmap bitmap) {
	// 初始化webPage连接属性
	WXWebpageObject webpage = new WXWebpageObject();
	webpage.webpageUrl = url;
	  
	// 初始化分享的信息
	WXMediaMessage msg = new WXMediaMessage(webpage);
	msg.title = title;
	msg.description = content;
	msg.setThumbImage(bitmap);
	  
	// 构造Req对象
	SendMessageToWX.Req req = new SendMessageToWX.Req();
	req.transaction = String.valueOf(System.currentTimeMillis());
	req.message = msg;
	req.scene = type == 1 ? SendMessageToWX.Req.WXSceneSession
	: SendMessageToWX.Req.WXSceneTimeline;
	  
	// 发送请求到微信
	api.sendReq(req);
	}
	

	private boolean isWXAppInstalledAndSupported(IWXAPI api) {
		boolean sIsWXAppInstalledAndSupported = api.isWXAppInstalled()
				&& api.isWXAppSupportAPI();
		if (!sIsWXAppInstalledAndSupported) {
			Toast.makeText(Activity_Share.this, "抱歉,微信客户端未安装,不能分享",
					Toast.LENGTH_SHORT).show();
		}

		return sIsWXAppInstalledAndSupported;
	}

	class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onCancel()
		{
			Toast.makeText(Activity_Share.this, "认证取消", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onComplete(Bundle values)
		{
//			token = values.getString("access_token");
//			expires_in = values.getString("expires_in");
//
//			accessToken = new Oauth2AccessToken(token, expires_in);
//			if (accessToken.isSessionValid()) 
//			{
//				Intent it = new Intent(Activity_Share.this,Activity_ShareSinaWeibo.class);
//				it.putExtra(Activity_ShareSinaWeibo.EXTRA_ACCESS_TOKEN,Activity_Share.accessToken.getToken());
//				it.putExtra(Activity_ShareSinaWeibo.EXTRA_EXPIRES_IN,Activity_Share.accessToken.getExpiresTime());
//				startActivity(it);
//			}
			
			accessToken = Oauth2AccessToken.parseAccessToken(values);
			if (accessToken.isSessionValid())
			{
				AccessTokenKeeper.writeAccessToken(Activity_Share.this, accessToken);
				
				mWeiboShareAPI.registerApp();
				sendMessage();
			}
			
		}

		@Override
		public void onWeiboException(WeiboException arg0)
		{
			// TODO Auto-generated method stub
			
		}
	}
	
	private void sendMessage()
	{
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = getTextObj();
        weiboMessage.imageObject = getImageObj();
        
		
		// 2. 初始化从第三方到微博的消息请�?
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识�?��请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        
        // 3. 发�?请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(request);
	}
	
	/**
     * 创建文本消息对象�?
     * 
     * @return 文本消息对象�?
     */
    private TextObject getTextObj()
    {
        TextObject textObject = new TextObject();
        textObject.text = title+" "+url;
        return textObject;
    }
    
    /**
     * 创建图片消息对象�?
     * 
     * @return 图片消息对象�?
     */
    private ImageObject getImageObj() 
    {
        ImageObject imageObject = new ImageObject();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		
		if (null != mTencent) 
		{
			mTencent.onActivityResult(requestCode, resultCode, data);
		}
	}

	

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void ShareToQQ()
	{
		if(mTencent == null)
		{
			mTencent = Tencent.createInstance(mAppid, Activity_Share.this);
		}
		Bundle params = new Bundle();
		
		//这条分享消息被好友点击后的跳转URL�?
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
		//title
		params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
		//图片地址
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://wap.yingkounews.com/images/58_58.png");
		//分享的消息摘要
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, des);
		//标识该消息的来源应用
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "营口新闻网");
		
		params.putInt("req_type", QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		
		mTencent.shareToQQ(this, params , qqShareListener);
	}
	
	IUiListener qqShareListener = new IUiListener()
	{
        @Override
        public void onCancel() 
        {
            
        }
        @Override
        public void onComplete(Object response) 
        {
        }
        @Override
        public void onError(UiError e)
        {
        	Toast.makeText(Activity_Share.this, "onError: " + e.errorMessage, Toast.LENGTH_SHORT).show();
        }
    };
}
