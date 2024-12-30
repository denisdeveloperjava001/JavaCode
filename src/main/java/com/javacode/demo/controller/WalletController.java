package com.javacode.demo.controller;

import com.javacode.demo.model.OperationType;
import com.javacode.demo.model.Wallet;
import com.javacode.demo.model.WalletOperationParameters;
import com.javacode.demo.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/api/v1/wallet")
    public Wallet createOrUpdate(@RequestBody WalletOperationParameters walletOperationParameters) {
        return walletService.createOrUpdate(walletOperationParameters);
    }

    @GetMapping("/api/v1/wallets/{id}")
    public double getBalance(@PathVariable UUID id){
        return walletService.getBalance(id);
    }

}
