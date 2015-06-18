package com.yingkounews.app.net;

import org.json.JSONObject;

import com.yingkounews.app.common.ResultPacket;

import android.content.Context;
public class Request_PushReg extends RequsetBase
{
	private String _me;
	private String _app;
	private String _cid;

	public Request_PushReg(Context Context,String me,String app,String cid)
	{
		super(Context);
		this._me = me;
		this._app = app;
		this._cid = cid;
		_url = "http://push.id110.cn/client/register";
	}
	
	public JSONObject DoBeforeSendData() 
	{
		_requestJson = new JSONObject();
		try 
		{
			_requestJson.put("me", _me);
			_requestJson.put("os", "1");
			_requestJson.put("app", _app);
			_requestJson.put("cid", _cid);
		} 
		catch (Exception e) 
		{
			
		}
		return _requestJson;
	}

	@Override
	public ResultPacket DoResponseData(String data)
	{
		ResultPacket result = new ResultPacket();
		try 
		{
			JSONObject jsonObject = new JSONObject(data);

            int error = jsonObject.getInt("err");
            if(error == 1)
            {
            	result.setIsError(true);
                result.setResultCode("99");
                result.setDescription(jsonObject.getString("msg"));
                return result;
            }
            
            return result;
		} 
		catch (Exception e) 
		{
			result.setIsError(true);
            result.setResultCode("99");
            result.setDescription("连接服务器失败，请检查网络是否正常");
            return result;
		}
	}

}
