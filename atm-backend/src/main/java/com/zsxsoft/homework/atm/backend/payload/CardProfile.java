package com.zsxsoft.homework.atm.backend.payload;

import com.zsxsoft.homework.atm.backend.model.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public class CardProfile {
    private Long id;
    private String cardNumber;
    private CardType type;
    private CardStatus status;
    private Long userId;
    private UserProfile user;
    private BigDecimal availableBalance = new BigDecimal(0);
    private BigDecimal frozenBalance = new BigDecimal(0);
    private BigDecimal totalBalance = new BigDecimal(0);

    public CardProfile (Card card) {
        this(card, false, true);
    }

    public CardProfile (Card card, boolean showSensitive, boolean showUserInfo) {
        this(card, showSensitive);
        if (showUserInfo) {
            this.user = new UserProfile(card.getUser(), showSensitive);
        }
    }

    public CardProfile(Card card, boolean showSensitive) {
        this.id = card.getId();
        this.cardNumber = card.getCardNumber();
        this.status = card.getStatus();
        this.type = card.getType();
        this.userId = card.getUser().getId();
        if (showSensitive) {
            this.availableBalance = card.getAvailableBalance();
            this.frozenBalance = card.getFrozenBalance();
            this.totalBalance = card.getTotalBalance();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(BigDecimal frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public CardStatus getStatus() {
        return status;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
