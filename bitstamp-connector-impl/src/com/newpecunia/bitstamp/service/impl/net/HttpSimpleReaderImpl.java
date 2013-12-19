package com.newpecunia.bitstamp.service.impl.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpSimpleReaderImpl implements HttpReader {

	private static final Logger logger = LogManager.getLogger(HttpSimpleReaderImpl.class);	
	
	private RequestCountLimitVerifier requestCountLimitVerifier;

	protected HttpClient getHttpClient() {
		return new DefaultHttpClient();
	}
	
	HttpSimpleReaderImpl() {
		this(null);
	}
	
	HttpSimpleReaderImpl(RequestCountLimitVerifier requestCountLimitVerifier) {
		this.requestCountLimitVerifier = requestCountLimitVerifier;
	}
	
	@Override
	public String get(String url) throws IOException {
		return doRequest(url, null, null).getOutput();
	}
	
	@Override
	public HttpReaderOutput getWithMetadata(String url) throws IOException {
		return doRequest(url, null, null);
	}
	
	@Override
	public String get(String url, List<Header> headers) throws IOException {
		return doRequest(url, headers, null).getOutput();
	}
	
	@Override
	public HttpReaderOutput getWithMetadata(String url, List<Header> headers) throws IOException {
		return doRequest(url, headers, null);
	}

	@Override
	public String post(String url, List<NameValuePair> params) throws IOException {
		return doRequest(url, null, params).getOutput();
	}
	
	@Override
	public String post(String url, List<Header> headers, List<NameValuePair> params) throws IOException {
		return doRequest(url, headers, params).getOutput();
	}

	@Override
	public HttpReaderOutput postWithMetadata(String url, List<Header> headers, List<NameValuePair> params) throws IOException {
		return doRequest(url, headers, params);
	}	
	
	private HttpReaderOutput doRequest(String url, List<Header> headers, List<NameValuePair> params) throws IOException, RequestCountLimitExceededException {
		//check requests count limit
		requestCountLimitVerifier.countRequest();		
		
		//disabling caching by adding unused parameter with random value
//		url = addAntiCachingParamToUrl(url);  - uncomment to strongly disable caching
		
		HttpResponse httpResponse;
		if (params == null) { //HTTP GET
			HttpGet httpGet = new HttpGet(url.toString());
			addHeadersToRequest(headers, httpGet);
			httpGet.addHeader("Cache-Control", "no-cache");
			httpResponse = getHttpClient().execute(httpGet); //can throw IOException
		} else { //HTTP POST
			HttpPost httpPost = new HttpPost(url.toString());
			addHeadersToRequest(headers, httpPost);
			httpPost.addHeader("Cache-Control", "no-cache");
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			httpResponse = getHttpClient().execute(httpPost); //can throw IOException
		}
		
		HttpReaderOutput result = new HttpReaderOutput();

		HttpEntity entity = httpResponse.getEntity();
		result.setResultCode(httpResponse.getStatusLine().getStatusCode());
		
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
				result.setOutput(output.toString());
				return result;
			}
		} else {
			return result;
		}			
	}

	/**
	 * Disables caching by adding unused parameter with random value.
	 * This is a dirty solution however works perfectly through all proxies.
	 * @return url with a parameter which value always changes
	 */
	private String addAntiCachingParamToUrl(String url) {
		StringBuilder urlWithFixedCaching = new StringBuilder(url);
		if (url.contains("?")) {
			urlWithFixedCaching.append('&');
		}
		urlWithFixedCaching.append("?antiCaching=");
		urlWithFixedCaching.append(UUID.randomUUID().toString().replaceAll("-", ""));
		return urlWithFixedCaching.toString();
	}

	private void addHeadersToRequest(List<Header> headers, HttpMessage httpMessage) {
		if (headers != null) {
			for (Header header : headers) {
				httpMessage.addHeader(header);
			}
		}
	}
	
}
