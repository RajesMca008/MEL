package com.morgans_eletranic_ltd.data;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

public class Data {

	public static String uploadData(String url, JSONObject obj) {

		String res = null;
		DefaultHttpClient httpClient = null;
		HttpPost httpPost = null;

		try {
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/json");
			StringEntity entity = new StringEntity(obj.toString());
			httpPost.setEntity(entity);

			res = httpClient.execute(httpPost, new BasicResponseHandler());
		} catch (Exception e) {
			Log.e("e", e + "");
			e.printStackTrace();
		}

		return res;
	}

	public static String downloadData(String url) {
		String res = null;
		DefaultHttpClient httpClient = null;
		HttpGet httpGet = null;

		try {
			httpClient = new DefaultHttpClient();
			httpGet = new HttpGet(url);
			res = httpClient.execute(httpGet, new BasicResponseHandler());
		} catch (Exception e) {
		}

		return res;
	}

	public static String getDataFromServer(String content,
			String requestMethod, String serverUrl, String contentType) {
		String response = "", responseMessage = "";
		Boolean success = false;
		DataOutputStream outputStream = null;

		HttpURLConnection connection = null;
		int responseCode = 0;
		try {
			URL new_url = new URL(serverUrl);
			connection = (HttpURLConnection) new_url.openConnection();

			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(true);
			// Enable PUT method
			connection.setRequestMethod(requestMethod);
			connection.setRequestProperty("Connection", "Keep-Alive");

			connection.setRequestProperty("Content-Type", contentType);
			outputStream = new DataOutputStream(connection.getOutputStream());
			byte[] BytesToBeSent = content.getBytes();
			if (BytesToBeSent != null) {
				outputStream.write(BytesToBeSent, 0, BytesToBeSent.length);
			}
			responseCode = connection.getResponseCode();

			responseMessage = connection.getResponseMessage();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (responseCode == 200 || responseCode == 202) {
			success = true;

		}

		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			inputStreamReader = new InputStreamReader(
					connection.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);

			StringBuilder responseContent = new StringBuilder();

			String temp = null;

			boolean isFirst = true;

			while ((temp = bufferedReader.readLine()) != null) {
				if (!isFirst)
					responseContent.append("\n");
				responseContent.append(temp);
				isFirst = false;
			}

			response = responseContent.toString();

		} catch (Exception ex) {
			success = false;
		} finally {
			try {
				outputStream.flush();
				outputStream.close();
			} catch (Exception e) {
			}
		}

		return response;
	}

}
