/**
 * Copyright 2010 Aleksey Krivosheev (paradoxs.mail@gmail.com)
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
 * @see <a href="http://www.bitcoin.org/wiki/doku.php?id=api">Bitcoin API</a>
 * @author paradoxs
 */
public class ServerInfo {
    private String version = "";
    private BigDecimal balance = BigDecimal.ZERO;
    private long blocks = 0;
    private int connections = 0;
    private BigDecimal difficulty = BigDecimal.ZERO;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public long getBlocks() {
        return blocks;
    }

    public void setBlocks(long blocks) {
        this.blocks = blocks;
    }

    public int getConnections() {
        return connections;
    }

    public void setConnections(int connections) {
        this.connections = connections;
    }

    /**
     * @deprecated No longer returned from bitcoind, so will always be false
     */
    @Deprecated
    public boolean isIsGenerateCoins() {
        return false;
    }

    /**
     * @deprecated No longer returned from bitcoind, so will always be -1
     */
    @Deprecated
    public int getUsedCPUs() {
        return -1;
    }

    public BigDecimal getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(BigDecimal difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * @deprecated No longer returned from bitcoind, so will always be 0
     */
    @Deprecated
    public long getHashesPerSecond() {
        return 0;
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "version='" + version + '\'' +
                ", balance=" + balance +
                ", blocks=" + blocks +
                ", connections=" + connections +
                ", difficulty=" + difficulty +
                '}';
    }
}
