package com.petrsmid.bitexchange.net;

import java.io.IOException;
import java.util.List;

import org.apache.http.NameValuePair;

public interface HttpReader {

	String get(String url) throws IOException;

	String post(String url, List<NameValuePair> params) throws IOException;

}
