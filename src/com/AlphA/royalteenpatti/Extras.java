package com.AlphA.royalteenpatti;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.util.Log;

public class Extras {

	static private File		f;
	private final String	link	= "http://192.168.159.2/xampp/phpstorm/teenpatti/";//"http://192.168.159.64/xampp/phpstorm/teenpatti/";//"http://192.168.159.2/xampp/phpstorm/teenpatti/";//"http://royalteenpatti.in/teenpatti/game/"; 10.0.2.2--192.168.159.2--49.207.232.32
	static String[]			detail;

	void initDetails() {
		BufferedReader br = null;
		detail = new String[3];
		try {
			br = new BufferedReader(new FileReader(f));
			String line = null;
			int i = 0;
			while ((line = br.readLine()) != null) {
				detail[i] = line;
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	Extras(String f) {
		this.f = new File(f, "init.txt");
	}

	Extras() {
	}

	public ArrayList<NameValuePair> Params(String... params) {
		int count = params.length;
		ArrayList<NameValuePair> nv = new ArrayList<NameValuePair>(count);
		for (int i = 0; i < params.length; i++) {
			nv.add(new BasicNameValuePair("i" + i, params[i]));
		}
		return nv;
	}

	HttpResponse Connect(String URL) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(link + URL);
			return httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public HttpResponse Post(String URL, ArrayList<NameValuePair> nv) {
		HttpClient httpclient = null;
		HttpPost httppost = null;
		HttpResponse response = null;
		try {
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(link + URL);
			httppost.setEntity(new UrlEncodedFormEntity(nv));
			response = httpclient.execute(httppost);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	public String Get(HttpResponse response) {
		String line;
		HttpEntity entity = response.getEntity();
		InputStream isr;
		StringBuilder sb = new StringBuilder();
		try {
			isr = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			isr.close();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	boolean fileExist() {
		if (f.exists())
			return true;
		else
			return false;
	}

	boolean deleteFile() {
		if (fileExist()) {
			f.delete();
			return true;
		} else
			return false;
	}

	void writeStringAsFile(String write) {
		try {
			FileWriter out = new FileWriter(f);
			out.write(write);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	String readFileToString() {
		BufferedReader br = null;
		String line;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			br = new BufferedReader(new FileReader(f));
			while ((line = br.readLine()) != null)
				stringBuilder.append(line);
			br.close();
			return stringBuilder.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	String iLineinFile(int i) {
		BufferedReader br = null;
		String s = null;
		try {
			br = new BufferedReader(new FileReader(f));
			for (int j = 0; j <= i; j++)
				s = br.readLine();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}
}
