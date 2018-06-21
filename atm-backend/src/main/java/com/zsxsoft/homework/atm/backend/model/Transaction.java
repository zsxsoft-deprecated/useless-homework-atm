package com.zsxsoft.homework.atm.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zsxsoft.homework.atm.backend.exception.TransactionException;
import com.zsxsoft.homework.atm.backend.model.audit.DateAudit;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
public class Transaction extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(length = 60)
    private TransactionType type;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_card_id", nullable = false)
    private Card fromCard;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_card_id", nullable = false)
    private Card toCard;

    @DecimalMin("0.00")
    private BigDecimal amount;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @DecimalMin("0.00")
    private BigDecimal tax = new BigDecimal(0);

    @DecimalMin("0.00")
    private BigDecimal actualAmount;

    @DecimalMin("0.00")
    private BigDecimal fromCardBeforeAvailableBalance;
    @DecimalMin("0.00")
    private BigDecimal fromCardAfterAvailableBalance;

    @DecimalMin("0.00")
    private BigDecimal toCardBeforeAvailableBalance;
    @DecimalMin("0.00")
    private BigDecimal toCardAfterAvailableBalance;

    @Column
    @Lob
    private String remark;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "withdraw_id", nullable = true)
    private Withdraw withdraw;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "recharge_id", nullable = true)
    private Recharge recharge;

    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonStringType")
    @Column(columnDefinition = "json")
    private TransactionAdditional additional;

    private BigDecimal zero = new BigDecimal("0");


    private void setBalances() {
        if (toCard == null || fromCard == null || actualAmount == null) return;
        fromCardAfterAvailableBalance = fromCardBeforeAvailableBalance.subtract(actualAmount);
        toCardAfterAvailableBalance = toCardBeforeAvailableBalance.add(actualAmount);

        if (fromCardAfterAvailableBalance.compareTo(zero) < 0) {
            throw new TransactionException("Insufficient balance!");
        }
    }

    private void setActualAmount() {
        if (currency == null) return;
        actualAmount = amount.multiply(currency.getExchangeRate()).subtract(tax);
    }

    public void updateCardBalances() {
        setBalances();
        fromCard.setAvailableBalance(fromCardAfterAvailableBalance);
        toCard.setAvailableBalance(toCardAfterAvailableBalance);
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

    public Card getFromCard() {
        return fromCard;
    }

    public void setFromCard(Card fromCard) {
        this.fromCard = fromCard;
        this.fromCardBeforeAvailableBalance = fromCard.getAvailableBalance();
        setBalances();
    }

    public Card getToCard() {
        return toCard;
    }

    public void setToCard(Card toCard) {
        this.toCard = toCard;
        this.toCardBeforeAvailableBalance = toCard.getAvailableBalance();
        setBalances();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        if (amount.compareTo(zero) < 0) {
            throw new TransactionException("Amount must be greater than 0");
        }
        this.amount = amount.setScale(2, BigDecimal.ROUND_DOWN);
        setBalances();
        setActualAmount();
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
        setBalances();
        setActualAmount();
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public BigDecimal getFromCardBeforeAvailableBalance() {
        return fromCardBeforeAvailableBalance;
    }

    public BigDecimal getFromCardAfterAvailableBalance() {
        return fromCardAfterAvailableBalance;
    }

    public BigDecimal getToCardBeforeAvailableBalance() {
        return toCardBeforeAvailableBalance;
    }

    public BigDecimal getToCardAfterAvailableBalance() {
        return toCardAfterAvailableBalance;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public TransactionAdditional getAdditional() {
        return additional;
    }

    public void setAdditional(TransactionAdditional additional) {
        this.additional = additional;
    }

    public Recharge getRecharge() {
        return recharge;
    }

    public void setRecharge(Recharge recharge) {
        this.recharge = recharge;
    }

    public Withdraw getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(Withdraw withdraw) {
        this.withdraw = withdraw;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
        setActualAmount();
    }
}
