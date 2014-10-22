package com.andieguoe.locationdemo;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

/**
 * MCC��Mobile Country Code���ƶ����Ҵ��루�й���Ϊ460���� MNC��Mobile Network Code���ƶ�������루�й��ƶ�Ϊ00���й���ͨΪ01���� LAC��Location Area Code��λ�������룻 CID��Cell Identity����վ��ţ��Ǹ�16λ�����ݣ���Χ��0��65535����
 */
public class TelephonyActivity extends Activity {
	private static final String TAG = "TelephonyActivity";
	private JSONObject holder;

	private MyTask mTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.telepony);
		new Thread() {
			@Override
			public void run() {
				try {
					String json = getJsonCellPos();
					Log.i(TAG, "�ύ��JSON��ʽ��" + json);
					String url = "http://www.minigps.net/minigps/map/google/location";
					String result = httpPost(url, json);
					Log.i(TAG, "���صĽ��result = " + result);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e.toString());
					e.printStackTrace();
				}
			}
		}.start();

		// getGSMCellLocationInfo();
		// mTask = new MyTask();
		// mTask.execute();
	}

	private String getJsonCellPos() throws JSONException {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
		if (location == null) {
			return null;
		}
		int cid = location.getCid();
		int lac = location.getLac();
		String netOperator = tm.getNetworkOperator();
		int mcc = Integer.valueOf(netOperator.substring(0, 3));
		int mnc = Integer.valueOf(netOperator.substring(3, 5));
		System.out.println("cid" + cid + ",lac" + lac + ",mcc" + mcc + "" + ",mnc" + mnc);
		JSONObject jsonCellPos = new JSONObject();
		jsonCellPos.put("version", "1.1.0");
		jsonCellPos.put("host", "maps.google.com");

		JSONArray array = new JSONArray();
		JSONObject json1 = new JSONObject();
		json1.put("location_area_code", "" + lac + "");
		json1.put("mobile_country_code", "" + mcc + "");
		json1.put("mobile_network_code", "" + mnc + "");
		json1.put("age", 0);
		json1.put("cell_id", "" + cid + "");
		array.put(json1);
		jsonCellPos.put("cell_towers", array);
		return jsonCellPos.toString();
	}

	public String getJsonCellPos1() {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
		if (location == null) {
			return null;
		}
		int cid = location.getCid();
		int lac = location.getLac();
		String netOperator = tm.getNetworkOperator();
		int mcc = Integer.valueOf(netOperator.substring(0, 3));
		int mnc = Integer.valueOf(netOperator.substring(3, 5));
		System.out.println("cid" + cid + ",lac" + lac + ",mcc" + mcc + "" + ",mnc" + mnc);
		holder = new JSONObject();
		JSONArray array = new JSONArray();
		JSONObject data = new JSONObject();
		try {
			holder.put("version", "1.1.0");
			holder.put("host", "maps.google.com");
			holder.put("address_language", "zh_CN");
			holder.put("request_address", true);
			holder.put("radio_type", "gsm");
			holder.put("carrier", "HTC");
			data.put("cell_id", cid);
			data.put("location_area_code", lac);
			data.put("mobile_countyr_code", mcc);
			data.put("mobile_network_code", mnc);
			array.put(data);
			holder.put("cell_towers", array);
			Log.i(TAG, "JSON��Ϣ��" + holder.toString());
			return holder.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * ��ȡIO������byte[]��ʽ�洢
	 */
	public byte[] read(InputStream inputSream) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		int len = -1;
		byte[] buffer = new byte[1024];
		while ((len = inputSream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inputSream.close();

		return outStream.toByteArray();
	}

	/**
	 * ���õ�����������API���ݻ�վ��Ϣ���һ�վ�ľ�γ��ֵ����ַ��Ϣ
	 */
	public String httpPost(String url, String jsonCellPos) throws IOException {
		byte[] data = jsonCellPos.toString().getBytes();
		URL realUrl = new URL(url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) realUrl.openConnection();
		httpURLConnection.setConnectTimeout(6 * 1000);
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setUseCaches(false);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		httpURLConnection.setRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
		httpURLConnection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
		httpURLConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
		httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
		httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
		httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

		httpURLConnection.setRequestProperty("Host", "www.minigps.net");
		httpURLConnection.setRequestProperty("Referer", "http://www.minigps.net/map.html");
		httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4X-Requested-With:XMLHttpRequest");

		httpURLConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		httpURLConnection.setRequestProperty("Host", "www.minigps.net");

		DataOutputStream outStream = new DataOutputStream(httpURLConnection.getOutputStream());
		outStream.write(data);
		outStream.flush();
		outStream.close();
		int status = httpURLConnection.getResponseCode();
		Log.i(TAG,"status:"+status);
		if (status == HttpURLConnection.HTTP_OK) {
			InputStream inputStream = httpURLConnection.getInputStream();
			return new String(read(inputStream));
		}
		return null;
	}

	
	private class MyTask extends AsyncTask<String, Integer, String> {
		private DefaultHttpClient mClient;
		private String responseString = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mClient = new DefaultHttpClient();
			mClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
			mClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 15000);
		}

		@Override
		protected String doInBackground(String... arg0) {
			try {
				HttpPost httpPost = new HttpPost("http://www.minigps.net/minigps/map/google/location");
				httpPost.setEntity(new StringEntity(holder.toString(), HTTP.UTF_8));
				httpPost.addHeader("Content-Type", "application/json");
				HttpResponse httpResponse = mClient.execute(httpPost);
				int statusCode = httpResponse.getStatusLine().getStatusCode();
				Log.i(TAG, "statusCode:" + statusCode);
				if (statusCode == 200) {
					responseString = EntityUtils.toString(httpResponse.getEntity());
					Log.i(TAG, "���ؽ��:" + responseString);
				} else {
					Log.i(TAG, "����ʧ��");
				}
			} catch (Exception e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}

			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.i(TAG, "result:" + result);
			JSONObject json;
			try {
				json = new JSONObject(result);
				JSONObject mresult = json.getJSONObject("result");
				JSONObject geo = mresult.getJSONObject("geo");
				double lat = geo.getDouble("lat");
				double lng = geo.getDouble("lng");
				Log.i(TAG, "lat" + lat);
				Log.i(TAG, "lng" + lng);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
}
