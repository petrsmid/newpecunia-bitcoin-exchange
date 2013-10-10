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

/**
 * Holds work information, returned from #getWork()
 *
 * @author mats@henricson.se
 * @since 0.3.18
 */
public class WorkInfo {
    private String midstate;    // Precomputed hash state after hashing the first half of the data
    private String data;        // Block data
    private String hash1;       // Formatted hash buffer for second hash
    private String target;      // Little endian hash target

    public String getMidstate() {
        return midstate;
    }

    public void setMidstate(String midstate) {
        this.midstate = midstate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHash1() {
        return hash1;
    }

    public void setHash1(String hash1) {
        this.hash1 = hash1;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "WorkInfo{" +
                "midstate='" + midstate + '\'' +
                ", data='" + data + '\'' +
                ", hash1='" + hash1 + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
