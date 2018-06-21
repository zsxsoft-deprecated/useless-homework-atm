package com.zsxsoft.homework.atm.backend.model;

public enum TransactionType {
    TRANSACTION_TRANSFER(1),
    TRANSACTION_WITHDRAW(2),
    TRANSACTION_RECHARGE(3),
    TRANSACTION_FREEZE(4),
    TRANSACTION_UNFREEZE(5);

    private final int value;

    TransactionType(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}
