package com.example.blockchain.Services;

import com.example.blockchain.Configuration.Block;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BlockchainService {
    private List<Block> blockchain = new ArrayList<>();
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int DIFFICULTY = 4;

    public BlockchainService() {
        Block genesisBlock = new Block(0, 0, 0, false, "0");
        genesisBlock.setHash(calculateHash(genesisBlock));
        blockchain.add(genesisBlock);
    }

    public Block mineBlock(int empId, int jobId, boolean consent) {
        int nonce = 0;
        Block newBlock = new Block(blockchain.size(), empId, jobId, consent, blockchain.get(blockchain.size() - 1).getHash());
        String hash = calculateHash(newBlock);
        while (!hash.startsWith("0000")) { // Adjust difficulty level as needed
            nonce++;
            newBlock.setNonce(nonce);
            hash = calculateHash(newBlock);
        }
        newBlock.setHash(hash);
        blockchain.add(newBlock);
        return newBlock;
    }

    public String calculateHash(Block block) {
        String data = block.getIndex() +
                Long.toString(block.getTimestamp()) +
                block.getEmpId() +
                block.getJobId() +
                (block.isConsent() ? "true" : "false") +
                block.getPreviousHash() +
                block.getNonce();
        return applySHA256(data);
    }

    private String applySHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Block> getBlockchain() {
        return blockchain;
    }

    public Optional<Block> getBlockByHash(String hash) {
        return blockchain.stream().filter(block -> block.getHash().equals(hash)).findFirst();
    }

    public String addUserConsent(Integer empId, Integer jobId, boolean consent) {
        try {
            Block newBlock = mineBlock(empId, jobId, consent);
            String hash= newBlock.getHash();
            return hash;
        } catch (Exception e) {
            return "Failed to add consent to blockchain";
        }
    }

    public Boolean getUserConsent(String hash) {
            Optional<Block> block = getBlockByHash(hash);
            return block.map(Block::isConsent).orElse(null);
    }
}
