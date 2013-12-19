package com.newpecunia.bitstamp.service.impl.net;

public interface HttpReaderFactory {

	HttpReader createNewHttpSessionReader();

	HttpReader createNewHttpSimpleReader();

}
