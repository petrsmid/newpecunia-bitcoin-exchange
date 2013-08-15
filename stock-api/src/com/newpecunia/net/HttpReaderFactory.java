package com.newpecunia.net;

public interface HttpReaderFactory {

	HttpReader createNewHttpSessionReader();

	HttpReader createNewHttpSimpleReader();

}
