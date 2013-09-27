package ru.paradoxs.bitcoin.client;

/**
 * Copyright 2010 Mats Henricson (mats@henricson.se)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.io.Files;

/**
 * A number of unit tests against a local Bitcoin server.
 * The tests are written so that they fail if the communication
 * fails, i.e. throws an exception. Can't really assert much, since
 * I don't know how much credit, etc, the server has.
 *
 * To make these tests work you need to:
 * <pre>
 *  * Create a wallet, preferrably with not that much Bitcoins in it, say 0.1 at the most, or use testnet
 *  * Create a bitcoin.conf file with RPCUSER and RPCPASSWORD (they can't be the same, as per specification)
 *  * Encrypt the wallet with the WALLET_PASS_PHRASE, for example with the QT GUI, or set it to null below
 *  * Make sure the bitcoind has write permissions to BACKUP_DIRECTORY
 * </pre>
 *
 * @author mats@henricson.se
 */
public class BitcoinTest {

	private static final String BITCOIN_TO_ADDRESS     = "1wGjmUwFLqfL2C15BVUACCDosVRFNbfcR"; //my electrum address
	private static final String TESTNET_FAUCET_ADDRESS = "mq7se9wy2egettFxPbmn99cK8v5AFq55Lx";
    private static final String BITCOIN_AMOUNT_TO_SEND = "0.01";    // You will send two times this amount out of your wallet!!!
    private static final String FEE                    = "-0.0001";
    private static final String RPCUSER                = "rpcuser";
    private static final String RPCPASSWORD            = "rpcpassword";
    private static final String WALLET_PASS_PHRASE     = "password";
    private static final boolean IS_TESTNET            = true; // Set true if you are using testnet
    
    private BitcoinClient bClient; 

    public BitcoinTest(){
    	if (IS_TESTNET){
    		bClient = new BitcoinClient("127.0.0.1", RPCUSER, RPCPASSWORD, 12347);
    	} else {
    		bClient = new BitcoinClient("127.0.0.1", RPCUSER, RPCPASSWORD, 8332);
    	}
    }
    
    @Test
    public void testSetWalletpassphrase() {
        bClient.walletpassphrase(WALLET_PASS_PHRASE, 1);
        bClient.walletlock();

        try {
            String message = "Loan Repayment";
            String messageTo = "Faucet";
            bClient.sendToAddress(BITCOIN_TO_ADDRESS, new BigDecimal(BITCOIN_AMOUNT_TO_SEND), message, messageTo);
            fail("sendToAddress() Should throw exception");
        } catch (Throwable t) {
            // Ignore
        }
    }

    @Test
    public void testGetBalance() {
        BigDecimal balance = bClient.getBalance();

        System.out.println("balance = " + balance);

        assertTrue(balance.compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    public void testGetBalanceWithParameter() {
        String accountName = "anAccountName";
        bClient.getAccountAddress(accountName);        // This creates an account, if it doesn't exist

        BigDecimal balance = bClient.getBalance(accountName);

        System.out.println("balance = " + balance);

        assertTrue(balance.compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    public void testGetBlockCount() {
        int blockCount = bClient.getBlockCount();

        System.out.println("blockCount = " + blockCount);

        assertTrue(blockCount > 0);
    }

    @Test
    public void testGetConnectionCount() {
        int connectionCount = bClient.getConnectionCount();

        System.out.println("connectionCount = " + connectionCount);

        assertTrue(connectionCount >= 0);
    }

    @Test
    public void testGetDifficulty() {
        BigDecimal difficulty = bClient.getDifficulty();

        System.out.println("difficulty = " + difficulty);

        assertTrue(difficulty.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    public void testHelp() {
        String help = bClient.help("getbalance");

        System.out.println("help = " + help);

        assertTrue(help.length() > 0);
    }

    @Test
    public void testGetGenerate() {
        boolean generate = bClient.getGenerate();

        System.out.println("generate = " + generate);
    }

    @Test
    public void testSetGenerate() {
        boolean generate = bClient.getGenerate();

        System.out.println("generate = " + generate);

        bClient.setGenerate(!generate, 1);
        boolean generateNew = bClient.getGenerate();

        System.out.println("generateNew = " + generateNew);

        bClient.setGenerate(generate, 1);
        boolean generateNewNew = bClient.getGenerate();

        System.out.println("generateNewNew = " + generateNewNew);
    }

    @Test
    public void testGetHashesPerSecond() {
        bClient.setGenerate(true, 1);
        long hashesPerSecond = bClient.getHashesPerSecond();
        bClient.setGenerate(false, 1);

        System.out.println("hashesPerSecond = " + hashesPerSecond);

        assertTrue(hashesPerSecond >= 0);
    }

    @Test
    public void testGetServerInfo() {
        ServerInfo serverInfo = bClient.getServerInfo();

        System.out.println("serverInfo = " + serverInfo);

        assertNotNull(serverInfo);
        assertNotNull(serverInfo.getVersion());
        assertTrue(serverInfo.getBalance().compareTo(BigDecimal.ZERO) >= 0);
        
        if (IS_TESTNET) {
        	assertTrue(serverInfo.getBlocks() > 40000);
        	assertTrue(serverInfo.getDifficulty().compareTo(new BigDecimal(.02)) > 0);
        } else {
        	assertTrue(serverInfo.getBlocks() > 90000);
        	assertTrue(serverInfo.getDifficulty().compareTo(new BigDecimal(1000)) > 0);
        }

        String rawServerInfoJSON = bClient.getServerInfoJSON();

        System.out.println("ServerInfo as JSON = " + rawServerInfoJSON);

        assertTrue(rawServerInfoJSON.length() > 0);
    }

    @Test
    public void testListReceivedByAddress() {
        List<AddressInfo> addressInfos = bClient.listReceivedByAddress(1, true);

        System.out.println("addressInfos = " + addressInfos);

        assertTrue(addressInfos != null);
    }

    @Test
    public void testGetAccount() {
        List<AddressInfo> addressInfos = bClient.listReceivedByAddress(1, false);

        for (AddressInfo ai : addressInfos) {
            String address = ai.getAddress();
            String account = bClient.getAccount(address);

            System.out.println("account = " + account);
        }
    }

    @Test
    public void testGetAddressesByAccount() {
        List<AccountInfo> accountInfos = bClient.listReceivedByAccount(1, false);

        for (AccountInfo ai : accountInfos) {
            String account = ai.getAccount();
            List<String> addresses = bClient.getAddressesByAccount(account);

            for (String address : addresses) {
                System.out.println("address = " + address);

                assertTrue(address.length() > 0);
            }
        }
    }

    @Test
    public void testGetReceivedByAddress() {
        List<AddressInfo> addressInfos = bClient.listReceivedByAddress(1, false);

        for (AddressInfo ai : addressInfos) {
            String address = ai.getAddress();
            BigDecimal received = bClient.getReceivedByAddress(address, 1);

            System.out.println("received = " + received);

            assertTrue(received.compareTo(BigDecimal.ZERO) >= 0);
        }
    }

    /**
     * This test might fail if you use non 7-bit ascii characters such as åäö in label/account names
     */
    @Test
    public void testGetReceivedByAccount() {
        List<AccountInfo> accountInfos = bClient.listReceivedByAccount(1, false);

        for (AccountInfo ai : accountInfos) {
            String account = ai.getAccount();
            BigDecimal amount = ai.getAmount();
            long confirmations = ai.getConfirmations();

            assertTrue(amount.compareTo(BigDecimal.ZERO) > 0);
            assertTrue(confirmations > 0);

            BigDecimal received = bClient.getReceivedByAccount(account, 1);

            assertEquals(amount, received);

            System.out.println("received = " + received);
        }
    }

    @Test
    public void testGetAccountAddress() {
        // First we try with null account
        String addressToNullAccount = bClient.getAccountAddress(null);

        System.out.println("addressToNullAccount = " + addressToNullAccount);

        assertNotNull(addressToNullAccount);
        assertTrue(addressToNullAccount.length() > 30);

        // Then we try with empty account
        String addressToEmptyAccount = bClient.getAccountAddress("");

        System.out.println("addressToEmptyAccount = " + addressToEmptyAccount);

        assertNotNull(addressToEmptyAccount);
        assertTrue(addressToEmptyAccount.length() > 30);
    }

    @Test
    public void testSetAccountForAddress() {
        String newAccount = "Brand New Acount";
        String address = bClient.getAccountAddress("Testing testing client");
        bClient.setAccountForAddress(address, newAccount);
        String account = bClient.getAccount(address);

        assertEquals(newAccount, account);

        System.out.println("account = " + account);
    }
    
    @Test
    public void testValidateAddress() {
    	String sendAddress = IS_TESTNET ? TESTNET_FAUCET_ADDRESS : BITCOIN_TO_ADDRESS;
    	
        ValidatedAddressInfo effInfo = bClient.validateAddress(sendAddress);

        assertTrue(effInfo.getIsValid());
        assertFalse(effInfo.getIsMine());
        assertEquals(sendAddress, effInfo.getAddress());

        ValidatedAddressInfo bogusInfo = bClient.validateAddress("BogUsAddr3ss");

        assertFalse(bogusInfo.getIsValid());
    }

    /**
     * This test is somewhat flakey, since it fails on occation.
     */
    @Test
    public void testGetWork() {
        WorkInfo info = bClient.getWork();     // This bombs sometimes, and I don't know why

        assertNotNull(info);
        assertNotNull(info.getMidstate());
        assertNotNull(info.getData());
        assertNotNull(info.getHash1());
        assertNotNull(info.getTarget());

        boolean success = bClient.getWork(info.getData());

        assertFalse(success);     // Not likely in such a short time
    }

    @Test
    public void testSendToAddress() {
    	String sendAddress = IS_TESTNET ? TESTNET_FAUCET_ADDRESS : BITCOIN_TO_ADDRESS;
        String message = "Loan Repayment";
        String messageTo = "Faucet";

        bClient.walletpassphrase(WALLET_PASS_PHRASE, 10);   // Unlock wallet
        String txId = bClient.sendToAddress(sendAddress, new BigDecimal(BITCOIN_AMOUNT_TO_SEND), message, messageTo);
        bClient.walletlock();
        assertNotNull(txId);
        assertFalse(txId.equals("sent"));  // Old (pre 0.3.17) behaviour
        assertTrue(txId.length() > 30);    // A 256 bit hash

        TransactionInfo info = bClient.getTransaction(txId);

        assertNotNull(info);
        assertNull(info.getCategory());    // https://github.com/gavinandresen/bitcoin-git/issues/issue/20
        assertEquals(txId, info.getTxId());
        assertEquals(new BigDecimal("-" + BITCOIN_AMOUNT_TO_SEND), info.getAmount());
        assertEquals(0, info.getFee().compareTo(new BigDecimal(FEE)));
    }

    @Test
    public void testSendFrom() {
    	String sendAddress = IS_TESTNET ? TESTNET_FAUCET_ADDRESS : BITCOIN_TO_ADDRESS;
        String message = "Loan Repayment";
        String toMessage = "Faucet";
        bClient.walletpassphrase(WALLET_PASS_PHRASE, 10);   // Unlock wallet
        String txId = bClient.sendFrom(null, sendAddress, new BigDecimal(BITCOIN_AMOUNT_TO_SEND), 6, message, toMessage);
        bClient.walletlock();
        assertNotNull(txId);
        assertFalse(txId.equals("sent"));  // Old (pre 0.3.17) behaviour
        assertTrue(txId.length() > 30);    // A 256 bit hash

        TransactionInfo info = bClient.getTransaction(txId);

        assertNotNull(info);
        assertNull(info.getCategory());    // https://github.com/gavinandresen/bitcoin-git/issues/issue/20
        assertEquals(txId, info.getTxId());
        assertEquals(new BigDecimal("-" + BITCOIN_AMOUNT_TO_SEND), info.getAmount());
        assertEquals(0, info.getFee().compareTo(new BigDecimal(FEE)));

        String rawJSONInfo = bClient.getTransactionJSON(txId);

        assertTrue(rawJSONInfo.length() > 0);
    }

    @Test
    public void testMove() {
        String stevesAccountName = "Steve's account";
        String message = "Use it wisely, Dude";
        bClient.getAccountAddress(stevesAccountName);   // Creates "Steve's account"

        // Move BITCOIN_AMOUNT_TO_SEND to Steve's account
        boolean success1 = bClient.move(null, stevesAccountName, new BigDecimal(BITCOIN_AMOUNT_TO_SEND), 10, message);
        assertTrue(success1);

        // Move the money back to the default account
        boolean success2 = bClient.move(stevesAccountName, null, new BigDecimal(BITCOIN_AMOUNT_TO_SEND), 10, message);
        assertTrue(success2);

        // Check that the money is 0 again
        BigDecimal stevesBalance = bClient.getBalance(stevesAccountName);
        assertEquals(new BigDecimal("0.0"), stevesBalance);
    }

    @Test
    public void testListTransactions() {
        List<TransactionInfo> txList = bClient.listTransactions(null, 100);     // Default account

        assertNotNull(txList);

        for (TransactionInfo txInfo : txList) {
            // System.out.println("txInfo: " + txInfo);

            String category = txInfo.getCategory();

            assertNotNull(category);

            assertTrue(category.equals("generate") ||
                       category.equals("send") ||
                       category.equals("receive") ||
                       category.equals("move") ||
                       category.equals("immature"));

            // Can't assert on amount, since it can be positive and negative

            if (category.equals("send")) {
                assertTrue(txInfo.getFee().compareTo(BigDecimal.ZERO) <= 0);
                // Can't assert on "message", since it may be null
                // Can't assert on "to", since it may be null
            }

            if (category.equals("generate") ||
                category.equals("send") ||
                category.equals("receive")) {
                assertTrue(txInfo.getConfirmations() >= 0);    // One send may be very recent
                assertNotNull(txInfo.getTxId());
                assertTrue(txInfo.getTxId().length() > 0);
            }

            if (category.equals("move")) {
                // Can't assert on "otheraccount" since it may be null (default account)
            }
        }

        List<TransactionInfo> txList2 = bClient.listTransactions(null);     // Default account

        assertTrue(txList2.size() <= 10);

        List<TransactionInfo> txList3 = bClient.listTransactions();         // Default account

        assertTrue(txList3.size() <= 10);
    }

    @Test
    public void testBackupWallet() {
    	File tmpDir = Files.createTempDir();
        String backupWalletName = tmpDir.getAbsolutePath() + File.separator + "walletbackup" + new Random().nextInt() + ".dat";
        bClient.backupWallet(backupWalletName);

        File file = new File(backupWalletName);

        assertTrue(file.exists());

        boolean success = file.delete();

        assertTrue(success);
    }

    @Test
    @Ignore("If I run this test, then the next run will fail")
    public void testStop() {
        bClient.stop();
    }

    @Test
    public void testRounding() {
        assertEquals(new BigDecimal("0.5"),        BitcoinClient.roundToEightDecimals(new BigDecimal("0.5")));
        assertEquals(new BigDecimal("0.00000006"), BitcoinClient.roundToEightDecimals(new BigDecimal("0.000000055")));
        assertEquals(new BigDecimal("1.00000011"), BitcoinClient.roundToEightDecimals(new BigDecimal("1.000000114")));
        assertEquals(new BigDecimal("1.00000012"), BitcoinClient.roundToEightDecimals(new BigDecimal("1.000000115")));
        assertEquals(new BigDecimal("0.00000001"), BitcoinClient.roundToEightDecimals(new BigDecimal("0.0000000149")));
    }
    
    
    /**
     * I realised that after 100 generated addresses the bitcoind returns 500 (internal error).
     * A workaround is to open the BitcoinQT, create manually one more address.
     * Afterward you can again generate next 100 addresses.
     * Maybe running "./bitcoind -keypool=10000" can fix the issue.
     */
    @Test
    @Ignore("Run this test only initialy when creating accounts.")
    public void generateAccounts() {
        for (int i = 1; i < 10000; i++) {
        	String prefix = "";
        	if (i < 10000) {prefix = "0" + prefix;}
        	if (i < 1000) {prefix = "0" + prefix;}
        	if (i < 100) {prefix = "0" + prefix;}
        	if (i < 10) {prefix = "0" + prefix;}
            String newAccount = "NP"+prefix+i;
            String address = bClient.getAccountAddress(null);
            System.out.println("Adding address "+address);
			bClient.setAccountForAddress(address, newAccount);
			String account = bClient.getAccount(address);
			
			assertEquals(newAccount, account);
			
			System.out.println("as account " + account);
		}
    }    
    
    
}
