package com.yingkounews.app.common;

import java.util.List;
import com.igexin.sdk.PushConsts;
import com.yingkounews.app.Activity_Splash;
import com.yingkounews.app.R;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class PushReceiver extends BroadcastReceiver {

	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {
		case PushConsts.GET_MSG_DATA:
			byte[] payload = bundle.getByteArray("payload");
			ActivityManager am = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> list = am.getRunningTasks(100);
			boolean isAppRunning = false;
			String PackageName = "com.yingkounews.app";
			for (RunningTaskInfo info : list) {
				if (info.topActivity.getPackageName().equals(PackageName)
						|| info.baseActivity.getPackageName().equals(
								PackageName)) {
					isAppRunning = true;
					break;
				}
			}

			if (payload != null) {
				if (isAppRunning) {
					System.out.println("======PushReceiver=======");
					String data = new String(payload);
					Intent intent1 = new Intent();
					intent1.setAction("com.joyee.tongchengsong.msg");
					intent1.putExtra("data", data);
					context.sendBroadcast(intent1);
				} else {
					// 创建一个NotificationManager的引用
					String ns = context.NOTIFICATION_SERVICE;
					NotificationManager mNotificationManager = (NotificationManager) context
							.getSystemService(ns);

					int icon = R.drawable.logo; // 通知图标
					CharSequence tickerText = ""; // 状态栏显示的通知文本提示
					long when = System.currentTimeMillis(); // 通知产生的时间，会在通知信息里显示
					// 用上面的属性初始化 Nofification
					Notification notification = new Notification(icon,
							tickerText, when);
					notification.flags = Notification.FLAG_AUTO_CANCEL;
					//notification.defaults=Notification.DEFAULT_SOUND;//系统默认声音
					notification.sound=Uri.parse("android.resource://" + context.getPackageName() + "/" +R.raw.ring); 

					Context context1 = context.getApplicationContext();
					CharSequence contentTitle = "营口新闻网"; // 通知栏标题
					CharSequence contentText = "你有一条新消息"; // 通知栏内容
					Intent notificationIntent = new Intent(context,
							Activity_Splash.class); // 点击该通知后要跳转的Activity
					PendingIntent contentIntent = PendingIntent.getActivity(
							context, 0, notificationIntent, 0);
					notification.setLatestEventInfo(context1, contentTitle,
							contentText, contentIntent);
					mNotificationManager.notify(1, notification);
				}
			}
			break;
		case PushConsts.GET_CLIENTID:
			// 获取ClientID(CID)
			// 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
			String cid = bundle.getString("clientid");
			MyApplication application = MyApplication.getInstance();
			application.set_ClientID(cid);
			Intent intent2 = new Intent();
			intent2.setAction("com.yingkounews.app.pushreg");
			context.sendBroadcast(intent2);
			break;
		}
	}

}
