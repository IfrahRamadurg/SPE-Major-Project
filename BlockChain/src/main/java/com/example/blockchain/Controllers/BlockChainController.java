package com.example.blockchain.Controllers;

import com.example.blockchain.Services.BlockchainService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobPortal/consent")
@AllArgsConstructor
public class BlockChainController {
    @Autowired
    private final BlockchainService blockchainService;

    @PostMapping("/add")
    public String addConsent(@RequestParam Integer empId, @RequestParam Integer jobId, @RequestParam boolean consent) {
        String response=blockchainService.addUserConsent(empId, jobId, consent);
        return response;
    }

    @GetMapping("/get")
    public Boolean getConsent(@RequestParam String hash) {
        return blockchainService.getUserConsent(hash);
    }
}
