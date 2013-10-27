package com.newpecunia.unicredit.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.newpecunia.unicredit.service.ForeignPayment;
import com.newpecunia.unicredit.webdav.Status;
import com.newpecunia.unicredit.webdav.UnicreditWebdavService;

public class UnicreditWebdavServiceMock implements UnicreditWebdavService {

	@Override
	public List<String> listForeignUploadedPackages() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> listPackagesWithStatus() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String uploadForeignPaymentPackage(String reference,
			ForeignPayment foreignPayment) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Status getStatusOfPackage(String reference) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> findOutgoingPaymentRefsInLastStatement()
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getLastBalance() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
