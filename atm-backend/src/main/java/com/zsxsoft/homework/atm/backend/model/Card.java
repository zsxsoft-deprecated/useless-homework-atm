package com.zsxsoft.homework.atm.backend.model;

import com.zsxsoft.homework.atm.backend.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "cards")
public class Card extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String cardNumber = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @DecimalMin("0.00")
    private BigDecimal availableBalance = new BigDecimal(0.00);

    @DecimalMin("0.00")
    private BigDecimal frozenBalance = new BigDecimal(0.00);

    @DecimalMin("0.00")
    private BigDecimal totalBalance = new BigDecimal(0.00);

    @Enumerated(EnumType.ORDINAL)
    private CardType type = CardType.BANKCARDTYPE_DEBIT;

    @Enumerated(EnumType.ORDINAL)
    private CardStatus status = CardStatus.BANKCARDSTATUS_ENABLED;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
        this.totalBalance = this.availableBalance.add(this.frozenBalance);
    }

    public BigDecimal getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(BigDecimal frozenBalance) {
        this.frozenBalance = frozenBalance;
        this.totalBalance = this.availableBalance.add(this.frozenBalance);
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CardStatus getStatus() {
        return status;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }
}
