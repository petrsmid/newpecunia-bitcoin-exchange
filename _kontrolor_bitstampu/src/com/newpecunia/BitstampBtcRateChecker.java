package com.newpecunia;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.newpecunia.bitstamp.BitstampConnectorModule;
import com.newpecunia.bitstamp.service.BitstampService;
import com.newpecunia.bitstamp.service.BitstampServiceException;
import com.newpecunia.bitstamp.service.Ticker;
import com.newpecunia.common.CommonModule;
import com.newpecunia.email.EmailSender;

public class BitstampBtcRateChecker {

	private static final BigDecimal LOSS_TRESHOLD_EMAIL = new BigDecimal("0.1"); //10%
	private static final String EMAIL_RECEIVERS = "petr.justin.smid@gmail.com, katalin.smid@gmail.com";
	private static final String ERROR_EMAIL_RECEIVERS = "petr.justin.smid@gmail.com";
	
	private static final long CHECK_PERIOD_IN_MS = 1*60*1000; //1 minute in ms
	private static final long MIN_TIME_BETWEEN_TWO_EMAILS_IN_MS = 5*60*1000; //5 minutes in ms
	private static final String FILE_LOCATION = "/home/pi/BitstampMaximum.txt";
	private static final long TIME_TO_TOLERATE_BITSTAMP_ERROR = 10*60*1000; //10 minutes in ms 
	
	private BitstampService bitstampService;
	private EmailSender emailSender;
	
	private BigDecimal maximum;
	private long lastAccessToBitstamp = 0l;
	private long lastEmailTimestamp = 0l;

	
	BitstampBtcRateChecker(BitstampService bitstampService, EmailSender emailSender) throws IOException {
		this.bitstampService = bitstampService;
		this.emailSender = emailSender;
		
		maximum = loadMaximum();
		
	}
	
	private void performCheck() throws BitstampServiceException, FileNotFoundException, IOException {
		
		log(""+(new Date()).toString()+"  Checking Bitstamp rate", null);
		
		Ticker ticker = bitstampService.getTicker();
		BigDecimal last = ticker.getLast();
		if (maximum.compareTo(last) < 0) {
			maximum = last;
			saveMaximum(maximum);
		}
		
		BigDecimal delta = maximum.subtract(last);
		BigDecimal lossRatio = delta.divide(maximum, 8, RoundingMode.HALF_UP);
		
		log(String.format("Bitcoin rate: %s. Maximum: %s. Loss: %s %%",
				last.toPlainString(), maximum.toPlainString(), lossRatio.multiply(new BigDecimal(100)).toPlainString()), null);
		
		if (lossRatio.compareTo(LOSS_TRESHOLD_EMAIL) >= 0) {
			//FUCK - BTC is falling
			
			String subject = String.format("POZOR! Bitcoin pada %s USD -> %s USD  (%s %%)", 
					maximum.toPlainString(), 
					last.toPlainString(), 
					lossRatio.multiply(new BigDecimal(100)).toPlainString());
			
			String msg = String.format("Toto je automaticky e-mail Petrova systemu na sledovani ceny Bitcoinu na burze Bitstamp. \n" +
					"\n" +
					"POZOR! Bitcoin spadl z maxima %s USD na aktualni ceny %s USD. To je pokles o %s %%\n" +
					"\n" +
					"Okamzite informuj Petra o teto zmene, at zkontroluje, co se deje a pripadne zasahne.\n", 
					maximum.toPlainString(), 
					last.toPlainString(), 
					lossRatio.multiply(new BigDecimal(100)).toPlainString());

			log(subject, null);
			
			long now = System.currentTimeMillis();
			if ((now - lastEmailTimestamp) > MIN_TIME_BETWEEN_TWO_EMAILS_IN_MS) {
				emailSender.sendEmail(EMAIL_RECEIVERS, subject, msg);
				lastEmailTimestamp = now;
			}
		}
		
	}
	
	private void checkPeriodically() {
		while (true) {
			try {
				if (true) {
					throw new IOException("nejaka chyba");
				}
				performCheck();
				lastAccessToBitstamp = System.currentTimeMillis();
				Thread.sleep(CHECK_PERIOD_IN_MS);
			} catch (BitstampServiceException | IOException | InterruptedException e) {
				log("Error ocurred while checking Bitstamp.", e);
				long now = System.currentTimeMillis();
				if ((now - lastAccessToBitstamp)>TIME_TO_TOLERATE_BITSTAMP_ERROR) {
					emailSender.sendEmail(ERROR_EMAIL_RECEIVERS, "Problem s pristupem na Bitstamp", 
							"Nastal problem s pristupem na Bitstamp\n" +
							"Chyba: " + e.getMessage());
					
					lastAccessToBitstamp = now; //prevent sending multiple emails - reset counter
				}
			}
		}
	}
	
	
	private void saveMaximum(BigDecimal max) throws FileNotFoundException, IOException {
		IOUtils.write(max.toPlainString(), new FileOutputStream(FILE_LOCATION));
	}

	private BigDecimal loadMaximum() throws IOException {
		FileInputStream inputStream = new FileInputStream(FILE_LOCATION);
		try {
			String fileContent = IOUtils.toString(inputStream);
			return new BigDecimal(fileContent.trim());
		} finally {
			inputStream.close();
		}
	}

	public static void main(String[] args) throws IOException {
		Injector injector = Guice.createInjector(
				new CommonModule(),
        		new BitstampConnectorModule());
		
		BitstampBtcRateChecker checker = new BitstampBtcRateChecker(
				injector.getInstance(BitstampService.class),
				injector.getInstance(EmailSender.class));
		
		checker.checkPeriodically();
	}
	
	private void log(String msg, Throwable cause) {
		System.out.println(msg);
		if (cause != null) {
			cause.printStackTrace();
		}
	}
	
	
	
}
