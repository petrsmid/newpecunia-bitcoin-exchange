package com.petrsmid.bitexchange.net;

import java.io.IOException;

public interface HttpReader {

	public String readUrl(String url) throws IOException;

}
