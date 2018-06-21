package com.zsxsoft.homework.atm.backend.model;


public enum UserStatus {
    USERSTATUS_ENABLED(0),
    USERSTATUS_DISABLED(1),
    USERSTATUS_SYSTEM(2);

    private final int value;

    UserStatus(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}
