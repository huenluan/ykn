package com.yingkounews.app.common;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/*
 * Application类 百度接口管理
 * 
 * 
 */
@SuppressWarnings("rawtypes")
public class MyApplication extends Application {

	private static MyApplication instance;

	public boolean m_bKeyRight = true;
	
	public static final String strKey = "mypYHVGZYdrpGEvN9nSVmvj5";

	@Override
	public void onCreate() {
		super.onCreate();
		

	
	}

	
	

	

	private List<Activity> activityList = new LinkedList<Activity>();

	/**
	 * 单例模式中获取唯一的ExitApplication实例
	 */

	public static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;

	}

	/**
	 * 添加Activity到容器中
	 * 
	 * @param activity
	 *            传入当前activity
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish

	public void exit() {

		for (Activity activity : activityList) {
			activity.finish();
		}

		System.exit(0);

	}

	private boolean isLogin;

	public void set_isLogin(boolean bool) {
		this.isLogin = bool;
	}

	public boolean get_isLogin() {
		return this.isLogin;
	}

	

	private String auth;

	public void set_auth(String auth) {
		this.auth = auth;
	}

	public String get_auth() {
		return this.auth;
	}

	private String ClientID;

	public void set_ClientID(String clientid) {
		this.ClientID = clientid;
	}

	public String get_ClientID() {
		return this.ClientID;
	}

	private boolean isInMsgActivity;

	public void set_isInMsgActivity(boolean bool) {
		this.isInMsgActivity = bool;
	}

	public boolean get_isInMsgActivity() {
		return this.isInMsgActivity;
	}

	private Vector MsgVec;// 消息数组

	public void set_MsgVec(Vector msgVec) {
		this.MsgVec = msgVec;
	}

	public Vector get_MsgVec() {
		return this.MsgVec;
	}

	
	
	
	public Vector<String> cityinfoVecStrings;
	
	public Vector<String> getcityinfoVecStrings() {
		return cityinfoVecStrings;
	}
	
	private boolean gotoShop;
	public void set_gotoShop(boolean bool)
	{
		this.gotoShop = bool;
	}
	public boolean get_gotoShop()
	{
		return this.gotoShop;
	}

	

}
