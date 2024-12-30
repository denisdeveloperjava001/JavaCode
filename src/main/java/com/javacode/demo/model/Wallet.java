package com.javacode.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wallet")
@Entity
public class Wallet {

    @Id
    @Column(name = "id")
    private UUID walletId;

    @Column(name = "amount")
    private double amount;

}
