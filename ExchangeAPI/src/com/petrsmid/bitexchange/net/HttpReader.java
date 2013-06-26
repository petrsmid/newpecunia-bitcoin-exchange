package com.petrsmid.bitexchange.net;

import java.io.IOException;

public interface HttpReader {

	String get(String url) throws IOException;

	String post(String url, String request) throws IOException;

}
