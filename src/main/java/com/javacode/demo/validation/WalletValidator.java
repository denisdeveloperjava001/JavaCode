package com.javacode.demo.validation;

import com.javacode.demo.exception.NegativeAmountException;
import com.javacode.demo.model.WalletOperationParameters;

public class WalletValidator {
    public static void validateOperation(WalletOperationParameters walletOperationParameters) {
        if (walletOperationParameters.getAmount() < 0) {
            throw new NegativeAmountException();
        }
    }
}