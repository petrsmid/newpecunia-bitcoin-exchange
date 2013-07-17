package com.newpecunia.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpReaderImpl implements HttpReader {
	
	@Override
	public String get(String url) throws IOException {
		return doRequest(url, null);
	}
	
	@Override
	public String post(String url, List<NameValuePair> params) throws IOException {
		return doRequest(url, params);
	}
	
	
	private String doRequest(String url, List<NameValuePair> params) throws IOException {
		HttpClient httpClient = new DefaultHttpClient();

		HttpResponse response;
		
		if (params == null) { //HTTP GET
			response = httpClient.execute(new HttpGet(url)); //can throw IOException
		} else { //HTTP POST
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			response = httpClient.execute(httpPost); //can throw IOException
		}
		
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			Header encodingHeader = entity.getContentEncoding();
			try (InputStream in = entity.getContent()){ //automatically closes the stream 
				BufferedReader br;
				if (encodingHeader != null && encodingHeader.getValue() != null) {
					br = new BufferedReader(new InputStreamReader(in, encodingHeader.getValue())); //can throw IOException
				} else {
					br = new BufferedReader(new InputStreamReader(in));
				}
				StringBuilder output = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) { //can throw IOException
					if (output.length() > 0) {
						output.append(System.lineSeparator());
					}
					output.append(line);
				}
				return output.toString();
			}
		} else {
			return null;
		}			
	}
}
