package com.example.blockchain.Configuration;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Block {
    private int index;
    private long timestamp;
    private Integer empId;
    private Integer jobId;
    private boolean consent;
    private String hash;
    private String previousHash;
    private int nonce;

    public Block(int index, Integer empId, Integer jobId, boolean consent, String previousHash) {
        this.index = index;
        this.timestamp = new Date().getTime();
        this.empId = empId;
        this.jobId = jobId;
        this.consent = consent;
        this.previousHash = previousHash;
        this.nonce = 0;
        this.hash = "";
    }
}