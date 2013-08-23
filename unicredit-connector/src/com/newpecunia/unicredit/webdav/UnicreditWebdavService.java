package com.newpecunia.unicredit.webdav;

import java.io.IOException;
import java.util.List;

public interface UnicreditWebdavService {

	List<String> listForeignUploadFolder() throws IOException;

	List<String> listStatusFolder() throws IOException;

	String uploadForeignPayments(ForeignPaymentPackage foreignPaymentPackage) throws IOException;

	Status getStatusOfPackage(String fileName) throws IOException;

}
