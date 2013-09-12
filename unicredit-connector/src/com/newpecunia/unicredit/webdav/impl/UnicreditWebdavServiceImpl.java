package com.newpecunia.unicredit.webdav.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.google.inject.Inject;
import com.newpecunia.configuration.NPConfiguration;
import com.newpecunia.time.TimeProvider;
import com.newpecunia.unicredit.service.ForeignPayment;
import com.newpecunia.unicredit.webdav.Status;
import com.newpecunia.unicredit.webdav.UnicreditWebdavService;

public class UnicreditWebdavServiceImpl implements UnicreditWebdavService {

	private static final Logger logger = LogManager.getLogger(UnicreditWebdavServiceImpl.class);	

	private UnicreditFileNameResolver fileNameResolver = new UnicreditFileNameResolver();

	private NPConfiguration configuration;

	private TimeProvider timeProvider;
	
	@Inject
	public UnicreditWebdavServiceImpl(TimeProvider timeProvider, NPConfiguration configuration) {
		this.timeProvider = timeProvider;
		this.configuration = configuration;
	}
	
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
		return IOUtils.toString(stream, configuration.getWebdavEncoding());		
	}
	
	private void uploadFile(String url, String file) throws IOException {
		getSardine().put(url, IOUtils.toInputStream(file));
	}
	
	private String getUploadFolderPath() {
		return configuration.getWebdavBaseFolder()+configuration.getWebdavForeignUploadFolder();
	}
	
	private String getStatusFolderPath() {
		return configuration.getWebdavBaseFolder()+configuration.getWebdavStatusFolder();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> listForeignUploadedPackages() throws IOException {
		List<String> uploadedFileNames = listFolder(getUploadFolderPath());
		List<String> ids = new ArrayList<>();
		for (String fileName : uploadedFileNames) {
			String id = fileNameResolver.getIdFromUploadFile(fileName);
			ids.add(id);
		}
		return ids;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	public List<String> listPackagesWithStatus() throws IOException {
		List<String> statusFileNames = listFolder(getStatusFolderPath());
		List<String> ids = new ArrayList<>();
		for (String fileName : statusFileNames) {
			String id = fileNameResolver.getIdFromStatusFile(fileName);
			ids.add(id);
		}
		return ids;		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String uploadForeignPaymentPackage(String reference, ForeignPayment foreignPayment) throws IOException {
		String multicashReference = createMulticashReference(reference);
		MulticashForeignPaymentPackage foreignPaymentPackage = new MulticashForeignPaymentPackage(multicashReference, foreignPayment, timeProvider, configuration); //one payment per package
		
		String fileName = fileNameResolver.getUploadFileNameForId(reference);
		uploadFile(fileName, foreignPaymentPackage.toMultiCashString());
		return fileName;
	}
	
	private String createMulticashReference(String paymentId) {
		String reference = paymentId;
		if (reference.length() > 16) { //reference can be maximally 16 characters long
			reference = reference.substring(reference.length()-16); 
		}
		return reference;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Status getStatusOfPackage(String packageId) throws IOException {
		List<String> statusFilesNames = listFolder(getStatusFolderPath());
		for (String statusFileName : statusFilesNames) {
			String id = fileNameResolver.getIdFromStatusFile(statusFileName);
			if (packageId.equals(id)) {
				String statusFileContent = getFile(statusFileName);
				return StatusImpl.parseFile(statusFileContent);
			}
		}
		logger.info("Status file not found for package with ID "+packageId);
		return null;
	}
}
