package com.yingkounews.app.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yingkounews.app.R;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler 
{
	private IWXAPI api;
	private static final String APP_ID = "";
	/**
	 * 
	 * 接受微信返回参数，不能修改包名，类名
	 * 
	 * 
	 * ***/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		api = WXAPIFactory.createWXAPI(this, APP_ID, false);
		api.registerApp(APP_ID);
		api.handleIntent(getIntent(), this);

	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		// 微信发送请求到第三方应用时，会回调到该方法
	}

	@Override
	public void onResp(BaseResp resp) {
		// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
		// TODO Auto-generated method stub
		int result = 0;

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}

		// 作为接受微信的支付结果,不过最终结果以服务器的返回为准notify_url:
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("支付结果");
			builder.setMessage("支付结果" + String.valueOf(resp.errCode));
			builder.show();
		}

		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		//
		// overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		// Intent intent = new Intent(this, Activity_MainActivity.class);
		// startActivity(intent);
		finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);
		api.handleIntent(intent, this);
	}
}
