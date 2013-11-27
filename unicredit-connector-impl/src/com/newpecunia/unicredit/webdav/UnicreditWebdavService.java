package com.newpecunia.unicredit.webdav;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.newpecunia.unicredit.service.ForeignPayment;

public interface UnicreditWebdavService {

	/**
	 * Returns list of IDs of uploaded packages
	 */
	List<String> listForeignUploadedPackages() throws IOException;

	/**
	 * Returns list of IDs of packages with status (in status folder)
	 */
	List<String> listPackagesWithStatus() throws IOException;

	/**
	 * Creates a package with one foreign payment and uploads it 
	 * @return ID of the package
	 * @throws IOException 
	 */
	String uploadForeignPaymentPackage(String reference, ForeignPayment foreignPayment) throws IOException;

	/**
	 * Returns actual status of the uploaded package or null if the status file for the package was not found
	 * @param packageId id of the package
	 */
	Status getStatusOfPackage(String reference) throws IOException;

	/**
	 * Returns references of outgoing payments found in last statement file
	 * @return list of found references
	 * @throws IOException 
	 */
	List<String> findOutgoingNonBitstampPaymentRefsInLastStatement() throws IOException;

	/**
	 * Returns balance found in last statement file
	 * @throws IOException 
	 */
	BigDecimal getLastBalance() throws IOException;

	/**
	 * Returns count of found references indicating that the payment was sent to Bitstamp  (The Bitstamp uses its own reference therefore we do not have reference for each payment. We only know count of outgoing payments.) 
	 * @throws IOException
	 */
	int getOutgoingPaymentsToBitstampCount() throws IOException;



}
