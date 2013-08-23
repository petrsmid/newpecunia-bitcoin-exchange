package com.newpecunia.unicredit.webdav.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.unicredit.webdav.ForeignPaymentPackage;
import com.newpecunia.unicredit.webdav.Status;
import com.newpecunia.unicredit.webdav.UnicreditWebdavService;

public class UnicreditWebdavServiceImpl implements UnicreditWebdavService {

	private Sardine getSardine() {
		return SardineFactory.begin("username", "password"); //TODO
	}
	
	private List<String> listFolder(String url) throws IOException {
		List<String> files = new ArrayList<>();
		List<DavResource> resources = getSardine().list(url);
		for (DavResource res : resources) {
		     files.add(res.getName());
		}
		return files;		
	}
	
	private String getFile(String url) throws IOException {
		InputStream stream = getSardine().get(url);		
		return IOUtils.toString(stream, NPConfiguration.INSTANCE.getWebdavEncoding());		
	}
	
	private void uploadFile(String url, String file) throws IOException {
		getSardine().put(url, IOUtils.toInputStream(file));
	}
	
	@Override
	public List<String> listForeignUploadFolder() throws IOException {
		return listFolder(NPConfiguration.INSTANCE.getWebdavBaseFolder()+NPConfiguration.INSTANCE.getWebdavForeignUploadFolder());
	}	
	
	@Override	
	public List<String> listStatusFolder() throws IOException {
		return listFolder(NPConfiguration.INSTANCE.getWebdavBaseFolder()+NPConfiguration.INSTANCE.getWebdavStatusFolder());
	}
	
	/**
	 * Creates file from payments and uploads it. 
	 * @return fileName name of the created file
	 * @throws IOException 
	 */
	@Override
	public String uploadForeignPayments(ForeignPaymentPackage foreignPaymentPackage) throws IOException {
		String fileName = "TODO";
		uploadFile(fileName, foreignPaymentPackage.toCVS());
		return fileName;
	}
	
	@Override
	public Status getStatusOfPackage(String fileName) throws IOException {
		String statusFile = getFile("TODO");
		return StatusImpl.parseFile(statusFile);
	}
}
