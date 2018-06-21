package com.zsxsoft.homework.atm.backend.model;

public enum CardStatus {
    BANKCARDSTATUS_ENABLED(0),
    BANKCARDSTATUS_DISABLED(1);

    private final int value;

    CardStatus(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}
