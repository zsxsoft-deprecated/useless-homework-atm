package com.zsxsoft.homework.atm.backend.model;

public enum CardType {
    BANKCARDTYPE_DEBIT(0),
    BANKCARDTYPE_CREDIT(1),
    BANKCARDTYPE_CHARGE(2),
    BANKCARDTYPE_WITHDRAW(3);

    private final int value;

    CardType(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}
