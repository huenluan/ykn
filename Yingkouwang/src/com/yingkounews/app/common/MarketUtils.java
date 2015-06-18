package com.yingkounews.app.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

import com.yingkounews.app.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

@SuppressLint("DefaultLocale")
public class MarketUtils {
	/*
	 * 判断系统是否挂载gps设备，如果有，就有gps开关选项，如果没有，据移除此选项
	 */
	public static final boolean isHaveGps(Context context) {
		boolean st = context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_LOCATION_GPS);
		return st;
	}

	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 * 
	 * @param context
	 * @return true 表示开启
	 */
	public static final boolean gpsIsOPen(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		// boolean network = locationManager
		// .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		// if (gps || network) {//判断gps和network只要打开一个就认为三开启的
		//
		// return true;
		// }
		if (gps) {// 单独判断gps是否打开

			return true;
		}
		return false;

	}

	/*
	 * 打开gps
	 */
	public static final void openGPS(final Context context) {
		try {
			Intent GPSIntent = new Intent();
			GPSIntent.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
			GPSIntent.setData(Uri.parse("custom:3"));
			try {
				PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
			} catch (CanceledException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static final boolean networkIsOPen(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		// boolean gps = locationManager
		// .isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		// if (gps || network) {//判断gps和network只要打开一个就认为三开启的
		//
		// return true;
		// }
		if (network) {// 单独判断gps是否打开

			return true;
		}
		return false;

	}

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNetWorkStatus(Context context) {
		ConnectivityManager cm = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE));
		if (cm != null) {
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info != null && info.isConnectedOrConnecting()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否为电话号码
	 * 
	 * @return boolean
	 */
	public static boolean checkPhone(String phone) {
		Pattern pattern = Pattern.compile("^[1][3-9]\\d{9}$");
		Matcher matcher = pattern.matcher(phone);

		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param source
	 *            要被压缩的图片
	 * @param width
	 *            压缩的宽度
	 * @param height
	 *            压缩的高度
	 * @return
	 */
	public static Bitmap transImage(Bitmap source, String picName,
			boolean isSave) {

		if (isSave) {
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/tongchengsong/data/photo/";
			String fileName = picName;
			File out = new File(path);
			if (!out.exists()) {
				out.mkdirs();
			}
			out = new File(path, fileName);
			try {
				FileOutputStream outStream = new FileOutputStream(out);
				source.compress(CompressFormat.PNG, 100, outStream);
				outStream.close();
			} catch (Exception e) {
				e.printStackTrace();

				return null;
			}
		}
		return source;
	}

	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		if (bitmap == null)

			return null;

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	/**
	 * 
	 * @param source
	 *            要被压缩的图片
	 * @param width
	 *            压缩的宽度
	 * @param height
	 *            压缩的高度
	 * @return
	 */
	public static Bitmap transImage(Bitmap source, int width, int height,
			String picName, boolean isSave) {
		int bitmapWidth = source.getWidth();
		int bitmapHeight = source.getHeight();
		// 缩放图片的尺寸
		float scaleWidth = (float) width / bitmapWidth;
		float scaleHeight = (float) height / bitmapHeight;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 产生缩放后的Bitmap对象
		Bitmap resizeBitmap = Bitmap.createBitmap(source, 0, 0, bitmapWidth,
				bitmapHeight, matrix, false);
		// 保存图片
		if (isSave) {
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/TCS/data/photo/";
			String fileName = picName;
			File out = new File(path);
			if (!out.exists()) {
				out.mkdirs();
			}
			out = new File(path, fileName);
			try {
				FileOutputStream outStream = new FileOutputStream(out);
				resizeBitmap.compress(CompressFormat.PNG, 100, outStream);
				outStream.close();
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		return resizeBitmap;
	}

	/**
	 * 获取通知栏的高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

	

	

	/**
	 * 获取程序的版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String GetClientVersionName(Context context) {
		String clientVersion = "";
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			clientVersion = String.valueOf(packInfo.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return clientVersion;
	}

	/**
	 * 获取程序的版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String GetClientVersion(Context context) {
		String clientVersion = "";
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			clientVersion = String.valueOf(packInfo.versionCode);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return clientVersion;
	}

	/**
	 * 获取手机imei号
	 * 
	 * @return
	 */
	public static String getTelImei(Context context) {
		TelephonyManager mTm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return mTm.getDeviceId();
	}

	/**
	 * 获取手机mac地址
	 * 
	 * @return
	 */
	public static String GetMacAddress(Context context) {
		String result = "";
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		result = wifiInfo.getMacAddress();
		return result;
	}

	// 比较时间
	public static long dateDiff(String datepart, Calendar beginCal,
			Calendar endCal) {
		long retVal = 0;
		if (datepart.equals("yy"))// 年
		{
			int beginYear = beginCal.get(Calendar.YEAR);
			int endYear = endCal.get(Calendar.YEAR);
			int beginMonth = beginCal.get(Calendar.MONTH) + 1;
			int endMonth = endCal.get(Calendar.MONTH) + 1;
			int beginDay = beginCal.get(Calendar.DATE);
			int endDay = endCal.get(Calendar.DATE);
			retVal = endYear - beginYear;
			if ((endMonth - beginMonth <= 0) && (endDay - beginDay < 0)
					&& retVal > 0) {
				retVal--;
			} else if ((endMonth - beginMonth >= 0) && (endDay - beginDay > 0)
					&& retVal < 0) {
				retVal++;
			}
			return retVal;
		} else if (datepart.equals("mm"))// 月
		{
			int beginYear = beginCal.get(Calendar.YEAR);
			int endYear = endCal.get(Calendar.YEAR);
			int beginMonth = beginCal.get(Calendar.MONTH) + 1;
			int endMonth = endCal.get(Calendar.MONTH) + 1;
			int beginDay = beginCal.get(Calendar.DATE);
			int endDay = endCal.get(Calendar.DATE);
			retVal = endMonth - beginMonth;
			if ((endDay - beginDay < 0) && retVal > 0) {
				retVal--;
			} else if ((endDay - beginDay > 0) && retVal < 0) {
				retVal++;
			}
			return retVal + (endYear - beginYear) * 12;
		}

		long diff = 0;
		Date beginDate = beginCal.getTime();
		Date enDate = endCal.getTime();

		long beginLong = beginDate.getTime();
		long endLong = enDate.getTime();

		if (datepart.equals("dd"))// 日
		{
			diff = (endLong - beginLong) / (1000 * 24 * 60 * 60);
		} else if (datepart.equals("hh"))// 时
		{
			diff = (endLong - beginLong) / (1000 * 60 * 60);
		} else if (datepart.equals("mi"))// 分
		{
			diff = (endLong - beginLong) / (1000 * 60);
		} else if (datepart.equals("ss"))// 秒
		{
			diff = (endLong - beginLong) / (1000);
		}
		return diff;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static float dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (dpValue * scale + 0.5f);
	}

	/**
	 * 格式化价格，去掉小数点后
	 * 
	 * @return
	 */
	// public static String formatPrice(String price) {
	// if (price.indexOf(".00") > 0) {
	// return price.replace(".00", "");
	// } else if (price.indexOf(".0") > 0) {
	// return price.replace(".0", "");
	// }
	// return price;
	// }
	public static String formatPrice(String str) {
		double f = 0f;
		try {
			f = Double.valueOf(str);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return String.format("%.2f", f);
	}

	/**
	 * 从json中拼接参数URL
	 * 
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getParamStr(JSONObject parameters) {
		StringBuilder dataBfr = new StringBuilder();
		Iterator it = parameters.keys();
		for (; it.hasNext();) {
			String key = it.next().toString();
			if (dataBfr.length() != 0) {
				dataBfr.append('&');
			}
			Object value = null;
			try {
				value = parameters.get(key);
				try {
					dataBfr.append(URLEncoder.encode(key.toString(), "UTF-8"));
					dataBfr.append('=').append(
							URLEncoder.encode(value.toString(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			if (value == null) {
				value = "";
			}
		}
		return dataBfr.toString();
	}

	public static void closeInput(Activity context) {// 关闭输入法

		try {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			boolean isOpen = imm.isActive();
			if (isOpen) {
				InputMethodManager inputMethodManager = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (null == context.getCurrentFocus().getWindowToken())
					return;
				inputMethodManager.hideSoftInputFromWindow(context
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public static Bitmap GetBitmapByUrl(Context context, String picUrl,
			String picName) {
		Bitmap bitmap = null;
		try {
			URL url = new URL(picUrl);
			String responseCode = url.openConnection().getHeaderField(0);
			// if (responseCode.indexOf("200") < 0)
			// {
			// throw new Exception("图片文件不存在或路径错误，错误代码：" + responseCode);
			// }
			bitmap = BitmapFactory.decodeStream(url.openStream());

			// 保存图片
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/TCS/guanggao";
			File out = new File(path);
			if (!out.exists()) {
				out.mkdirs();
			}
			// 取得最后一个/的下标
			int index = picName.lastIndexOf("/");
			// 将字符串转为字符数组
			char[] ch = picName.toCharArray();
			// 根据 copyValueOf(char[] data, int offset, int count) 取得最后一个字符串
			String lastString = String.copyValueOf(ch, index + 1, ch.length
					- index - 1);

			out = new File(path, lastString);
			try {
				FileOutputStream outStream = new FileOutputStream(out);
				bitmap.compress(CompressFormat.JPEG, 100, outStream);
				outStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (bitmap == null) {

				Drawable img = context.getResources().getDrawable(
						R.drawable.logo);
				BitmapDrawable bd = (BitmapDrawable) img;

				Bitmap bm = bd.getBitmap();
				return bm;
			}
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();

			Drawable img = context.getResources().getDrawable(R.drawable.logo);
			BitmapDrawable bd = (BitmapDrawable) img;

			Bitmap bm = bd.getBitmap();
			return bm;
		}
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	public static String GetTimeByInt(int time) {
		String HH = String.valueOf(time / 3600);
		String mm = String.valueOf(time % 3600 / 60);
		if (HH.length() == 1) {
			HH = "0" + HH;
		}
		if (mm.length() == 1) {
			mm = "0" + mm;
		}

		return HH + ":" + mm;
	}
}
