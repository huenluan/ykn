package com.yingkounews.app.function;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.jar.JarEntry;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageDownLoadByUrl {

	private final static String ALBUM_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/YingKouNews/data/pic/";
	private ImageView mImageView;
	private Button mBtnSave;
	private ProgressDialog mSaveDialog = null;
	private Bitmap mBitmap;
	private String mFileName;
	private String mSaveMessage;
	private Context context;
	private String downloadUrl;

	public ImageDownLoadByUrl(Context context, String url) {
		this.context = context;
		this.downloadUrl = url;

	}

	public void StartDownLoadPic() {
		mSaveDialog = ProgressDialog.show(context, "保存图片", "图片正在保存中，请稍等...",
				true);
		new Thread(connectNet).start();

	}

	/**
	 * Get image from newwork
	 * 
	 * @param path
	 *            The path of image
	 * @return byte[]
	 * @throws Exception
	 */
	public byte[] getImage(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		InputStream inStream = conn.getInputStream();
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return readStream(inStream);
		}
		return null;
	}

	/**
	 * Get image from newwork
	 * 
	 * @param path
	 *            The path of image
	 * @return InputStream
	 * @throws Exception
	 */
	public InputStream getImageStream(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return conn.getInputStream();
		}
		return null;
	}

	/**
	 * Get data from stream
	 * 
	 * @param inStream
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 保存文件
	 * 
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public void saveFile(Bitmap bm, String fileName) throws IOException {
//		File dirFile = new File(ALBUM_PATH);
//		if (!dirFile.exists()) {
//			dirFile.mkdir();
//		}
//		File myCaptureFile = new File(ALBUM_PATH + fileName);
//		BufferedOutputStream bos = new BufferedOutputStream(
//				new FileOutputStream(myCaptureFile));
//		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
//		bos.flush();
//		bos.close();
		saveBitmap(bm,fileName);
	}

	public void saveMyBitmap(Bitmap mBitmap, String bitName) {
		File f = new File(ALBUM_PATH , bitName);
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 保存方法 */
	@SuppressLint("NewApi")
	public void saveBitmap(Bitmap bm, String fileName) {	
		File f = new File(ALBUM_PATH ,fileName);
		
		File directory = new File(ALBUM_PATH);  
		
		if(!directory.exists()){  
			directory.mkdirs();//没有目录先创建目录  
        }  
		
		if (f.exists()) {
			f.delete();
		}
		
		
		
		try {
			if (!f.exists()) {
//			     f.mkdirs();
			     f.createNewFile();
			}
			f.setWritable(true);
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Runnable saveFileRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				Looper.prepare();
				saveFile(mBitmap, mFileName);
				mSaveMessage = "图片保存成功！";
				scanFileAsync(context,ALBUM_PATH+mFileName);
				scanDirAsync(context, ALBUM_PATH);
			} catch (IOException e) {
				mSaveMessage = "图片保存失败！";
				e.printStackTrace();
			}
			messageHandler.sendMessage(messageHandler.obtainMessage());
		}

	};
	
	
	//扫描指定文件
    public void scanFileAsync(Context ctx, String filePath) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        ctx.sendBroadcast(scanIntent);
    }
     
    //扫描指定目录
    public static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";
    public void scanDirAsync(Context ctx, String dir) {
           Intent scanIntent = new Intent(ACTION_MEDIA_SCANNER_SCAN_DIR);
           scanIntent.setData(Uri.fromFile(new File(dir)));
           ctx.sendBroadcast(scanIntent);
    }
	

	private Handler messageHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mSaveDialog.dismiss();
			// Log.d(TAG, mSaveMessage);
			Toast.makeText(context, mSaveMessage, Toast.LENGTH_SHORT).show();
		}
	};

	/*
	 * 连接网络 由于在4.0中不允许在主线程中访问网络，所以需要在子线程中访问
	 */
	private Runnable connectNet = new Runnable() {

		@Override
		public void run() {
			try {

				Looper.prepare();
				String filePath = downloadUrl;
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyyMMddhhmmss");
				String date = format.format(new Date());

				int randomNum = new Random().nextInt(100000);

				mFileName = "" + date + String.valueOf(randomNum)
						+ ".jpg";

				// 以下是取得图片的两种方法
				// ////////////// 方法1：取得的是byte数组, 从byte数组生成bitmap
				// byte[] data = getImage(filePath);
				// if (data != null) {
				// mBitmap = BitmapFactory.decodeByteArray(data, 0,
				// data.length);// bitmap
				// } else {
				// Toast.makeText(context, "Image error!", 1)
				// .show();
				// }
				// //////////////////////////////////////////////////////

				// ******** 方法2：取得的是InputStream，直接从InputStream生成bitmap
				// ***********/
				mBitmap = BitmapFactory.decodeStream(getImageStream(filePath));
				// ********************************************************************/

				// 发送消息，通知handler在主线程中更新UI
				connectHanlder.sendEmptyMessage(0);

			} catch (Exception e) {
				Toast.makeText(context, "无法链接网络！", 1).show();
				e.printStackTrace();
			}

		}

	};

	private Handler connectHanlder = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			// // 更新UI，显示图片
			// if (mBitmap != null) {
			// mImageView.setImageBitmap(mBitmap);// display image
			// }

			new Thread(saveFileRunnable).start();
		}
	};

}
