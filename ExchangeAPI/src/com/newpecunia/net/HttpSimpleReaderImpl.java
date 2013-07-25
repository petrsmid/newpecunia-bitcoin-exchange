package com.newpecunia.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpSimpleReaderImpl implements HttpReader {

	private HttpClient httpClient = new DefaultHttpClient();

	//package private constructor -> instantiate it always with Factory
	HttpSimpleReaderImpl() {}
	
	@Override
	public String get(String url) throws IOException {
		return doRequest(url, null, null);
	}
	
	@Override
	public String get(String url, List<Header> headers) throws IOException {
		return doRequest(url, headers, null);
	}

	@Override
	public String post(String url, List<NameValuePair> params) throws IOException {
		return doRequest(url, null, params);
	}
	
	@Override
	public String post(String url, List<Header> headers, List<NameValuePair> params) throws IOException {
		return doRequest(url, headers, params);
	}
	
	
	private String doRequest(String url, List<Header> headers, List<NameValuePair> params) throws IOException {
		HttpResponse response;
		
		if (params == null) { //HTTP GET
			HttpGet httpGet = new HttpGet(url);
			addHeadersToRequest(headers, httpGet);
			response = httpClient.execute(httpGet); //can throw IOException
		} else { //HTTP POST
			HttpPost httpPost = new HttpPost(url);
			addHeadersToRequest(headers, httpPost);
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

	private void addHeadersToRequest(List<Header> headers, HttpMessage httpMessage) {
		if (headers != null) {
			for (Header header : headers) {
				httpMessage.addHeader(header);
			}
		}
	}	
	
}