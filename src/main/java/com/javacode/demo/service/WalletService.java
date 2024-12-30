package com.javacode.demo.service;

import com.javacode.demo.exception.WalletNotFoundException;
import com.javacode.demo.exception.NotEnoughFundsException;
import com.javacode.demo.exception.UnknownOperationTypeException;
import com.javacode.demo.model.OperationType;
import com.javacode.demo.model.Wallet;
import com.javacode.demo.model.WalletOperationParameters;
import com.javacode.demo.repository.WalletRepository;
import com.javacode.demo.validation.WalletValidator;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public Wallet createOrUpdate(WalletOperationParameters walletOperationParameters) {
        WalletValidator.validateOperation(walletOperationParameters);

        UUID id = walletOperationParameters.getWalletId();
        OperationType operationType = walletOperationParameters.getOperationType();
        double amount = walletOperationParameters.getAmount();

        Optional<Wallet> walletOptional = walletRepository.findById(id);

        switch (operationType) {
            case DEPOSIT:
                if(walletOptional.isEmpty()) {
                    Wallet wallet = new Wallet(id, amount);
                    walletRepository.save(wallet);
                    return wallet;
                } else {
                    Wallet wallet = walletOptional.get();
                    wallet.setAmount(wallet.getAmount() + amount);
                    walletRepository.save(wallet);
                    return wallet;
                }
            case WITHDRAW:
                if(walletOptional.isEmpty()) {
                    throw new WalletNotFoundException();
                } else {
                    Wallet wallet = walletOptional.get();
                    if(wallet.getAmount() < amount) {
                        throw new NotEnoughFundsException();
                    } else {
                        wallet.setAmount(wallet.getAmount() - amount);
                        walletRepository.save(wallet);
                        return wallet;
                    }
                }
            default:
                throw new UnknownOperationTypeException();
        }
    }

    public double getBalance(UUID id) {
        Optional<Wallet> walletOptional = walletRepository.findById(id);

        if(walletOptional.isEmpty()) {
            throw new WalletNotFoundException();
        } else {
            Wallet wallet = walletOptional.get();
            return wallet.getAmount();
        }
    }
}
