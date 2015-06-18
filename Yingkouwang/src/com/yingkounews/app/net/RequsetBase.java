package com.yingkounews.app.net;

import org.json.JSONObject;

import com.yingkounews.app.R;
import com.yingkounews.app.common.HttpUtility;
import com.yingkounews.app.common.MarketUtils;
import com.yingkounews.app.common.ResultPacket;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


@SuppressWarnings("unused")
public abstract class RequsetBase {

	protected Context _contContext;
	private String _responseData;
	public String _url;
	// 第一个地址坐标
	double mLat1 = 0;
	double mLon1 = 0;
	// 第二个地址坐标
	double mLat2 = 0;
	double mLon2 = 0;

	protected JSONObject _requestJson;

	private boolean isDeBug;

	

	public JSONObject getRequestList() {
		// _requestJson=new JSONObject();
		// _requestJson.put("_ver", value);
		return _requestJson;
	}

	protected JSONObject _responseList = new JSONObject();

	public RequsetBase(Context Context) {
		_contContext = Context;
		// config = MyConfig.LoadConfig(_contContext);
		// isDeBug = ConfigEntity.isDuBug;
		// if (true == isDeBug) {
		_url = Context.getResources().getString(R.string.pushurl);
		// } else {
		// _url = Context.getResources().getString(R.string.postUrl1);
		// }

	}

	public String getResponseData() {
		return _responseData;
	}

	public JSONObject DoBeforeSendData() {
		return _requestJson;
	}

	public abstract ResultPacket DoResponseData(String data);

	public ResultPacket DealProcess() {
		try {
			ResultPacket result = new ResultPacket();
			if (!MarketUtils.checkNetWorkStatus(_contContext)) {
				result.setIsError(true);
				result.setResultCode("200");
				result.setDescription("很抱歉，您的网络已经中断，请检查是否连接。");
				return result;
			}
			DoBeforeSendData();
			_requestJson.put("_ver", MarketUtils.GetClientVersion(_contContext));
			HttpUtility conn = new HttpUtility();
			String responseString = conn.openUrl(_contContext, _url, "POST",
					_requestJson);
			if(responseString == null || responseString.equals(""))
			{
				result.setIsError(true);
				result.setResultCode("99");
				result.setDescription("亲，网络不给力啊，连不上，请检查网络");
				return result;
			}
			result = DoResponseData(responseString);

			return result;
		} catch (Exception e) {
			return new ResultPacket(true, "99", e.getMessage());
		}
	}
}
