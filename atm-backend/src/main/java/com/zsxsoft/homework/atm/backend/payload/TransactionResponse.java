package com.zsxsoft.homework.atm.backend.payload;

import com.fasterxml.jackson.annotation.*;
import com.zsxsoft.homework.atm.backend.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionResponse {

    private Long id;
    private TransactionType type;
    private Card fromCard;
    private Card toCard;
    private BigDecimal amount;
    private Currency currency;
    private BigDecimal tax = new BigDecimal(0);
    private BigDecimal actualAmount;
    private String remark;

    @CreatedDate
    private Instant createdAt;

    public TransactionResponse(Transaction s) {
        BeanUtils.copyProperties(s, this);
    }

    @JsonGetter("fromCard")
    public CardProfile getFromCardProfile() {
        return new CardProfile(fromCard);
    }

    @JsonGetter("toCard")
    public CardProfile getToCardProfile() {
        return new CardProfile(toCard);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setFromCard(Card fromCard) {
        this.fromCard = fromCard;
    }

    public void setToCard(Card toCard) {
        this.toCard = toCard;
    }
}
