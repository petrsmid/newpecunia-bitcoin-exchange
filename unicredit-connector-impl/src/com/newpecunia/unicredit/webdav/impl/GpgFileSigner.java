package com.newpecunia.unicredit.webdav.impl;

public interface GpgFileSigner {

	byte[] sign(byte[] content);

}
