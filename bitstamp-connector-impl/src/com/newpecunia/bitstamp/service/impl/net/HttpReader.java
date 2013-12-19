package com.newpecunia.bitstamp.service.impl.net;

import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;

public interface HttpReader {

	String get(String url) throws IOException;

	String get(String url, List<Header> headers) throws IOException;
	
	HttpReaderOutput getWithMetadata(String url, List<Header> headers) throws IOException;

	String post(String url, List<NameValuePair> params) throws IOException;

	String post(String url, List<Header> headers, List<NameValuePair> params) throws IOException;

	HttpReaderOutput postWithMetadata(String url, List<Header> headers, List<NameValuePair> params) throws IOException;

	HttpReaderOutput getWithMetadata(String url) throws IOException;
}
