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
package ru.paradoxs.bitcoin.client;

import java.math.BigDecimal;

/**
 * Holds transaction information, returned from both #getTransaction(String)
 * and listTransactions(String, int). In the first case, the category may be
 * null.
 *
 * @author mats@henricson.se
 * @since 0.3.18
 */
public class TransactionInfo {
    private String category;     // Can be null, "generate", "send", "receive", or "move"
    private BigDecimal amount;   // Can be positive or negative
    private BigDecimal fee;      // Only for send, can be 0.0
    private long confirmations;  // only for generate/send/receive
    private String txId;         // only for generate/send/receive
    private long time;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public long getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(long confirmations) {
        this.confirmations = confirmations;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    /**
     * @deprecated No longer sent from bitcoind, function will always return null
     */
    @Deprecated
    public String getOtherAccount() {
        return null;
    }

    /**
     * @deprecated No longer sent from bitcoind, function will always return null
     */
    @Deprecated
    public String getMessage() {
        return null;
    }

    /**
     * @deprecated No longer sent from bitcoind, function will always return null
     */
    @Deprecated
    public String getTo() {
        return null;
    }

    public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
    public String toString() {
        return "TransactionInfo{" +
                "category='" + category + '\'' +
                ", amount=" + amount +
                ", time=" + time +
                ", fee=" + fee +
                ", confirmations=" + confirmations +
                ", txId='" + txId + '\'' +
                '}';
    }
}
