package com.javacode.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WalletOperationParameters {

    private UUID walletId;

    private OperationType operationType;

    private double amount;
}
