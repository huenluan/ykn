package com.yingkounews.app.common;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;

/*
 * 网络请求�?
 */
@SuppressLint("DefaultLocale")
@SuppressWarnings("unused")
public class HttpUtility {
	public final String HTTPMETHOD_POST = "POST";
	public final String HTTPMETHOD_GET = "GET";

	private final static int SET_CONNECTION_TIMEOUT = 30000; // 链接超时的时�?

	private final static int SET_SOCKET_TIMEOUT = 30000;// socket超时的时�?

	private int postCount = 0;

	/**
	 * 访问连接
	 * 
	 * @param context
	 * @param url
	 * @param method
	 * @param params
	 * @return
	 */
	public String openUrl(Context context, String url, String method,
			JSONObject params) {
		String result = "";
		try {
			HttpClient client = getNewHttpClient();
			HttpUriRequest request = null;
			ByteArrayOutputStream bos = null;
			if (method.equals(HTTPMETHOD_POST)) {
				HttpPost post = new HttpPost(url);
				byte[] data = null;
				bos = new ByteArrayOutputStream(1024 * 50);

				post.setHeader("Content-Type",
						"application/x-www-form-urlencoded");
				String postParam = getParamStr(params);
				data = postParam.getBytes("UTF-8");
				bos.write(data);

				data = bos.toByteArray();
				bos.close();
				ByteArrayEntity formEntity = new ByteArrayEntity(data);
				post.setEntity(formEntity);
				request = post;

				HttpResponse response = client.execute(request);
				StatusLine status = response.getStatusLine();
				int statusCode = status.getStatusCode();
				if (statusCode != 200) {
					return "";
				}
				result = read(response);
				return result;
			} else if (method.equals(HTTPMETHOD_GET)) {
				HttpGet get = new HttpGet(url);
				request = get;

				HttpResponse response = client.execute(request);
				StatusLine status = response.getStatusLine();
				int statusCode = status.getStatusCode();
				if (statusCode != 200) {
					return "";
				}
				result = read(response);
				return result;
			}
		} catch (Exception e) {
		}
		return "";
	}

	public static HttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	/**
	 * 读取返回内容
	 * 
	 * @param response
	 * @return
	 * @throws MyException
	 */
	private String read(HttpResponse response) {
		String result = "";
		HttpEntity entity = response.getEntity();
		InputStream inputStream;
		try {
			inputStream = entity.getContent();
			ByteArrayOutputStream content = new ByteArrayOutputStream();

			Header header = response.getFirstHeader("Content-Encoding");
			if (header != null
					&& header.getValue().toLowerCase().indexOf("gzip") > -1) {
				inputStream = new GZIPInputStream(inputStream);
			}

			// Read response into a buffered stream
			int readBytes = 0;
			byte[] sBuffer = new byte[512];
			while ((readBytes = inputStream.read(sBuffer)) != -1) {
				content.write(sBuffer, 0, readBytes);
			}
			// Return result from buffered stream
			result = new String(content.toByteArray());

			result = result.substring(result.indexOf("{"),
					result.lastIndexOf("}") + 1);

			return result;
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * 从json中拼接参数URL
	 * **/
	@SuppressWarnings("rawtypes")
	private static String getParamStr(JSONObject parameters) {
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

}
