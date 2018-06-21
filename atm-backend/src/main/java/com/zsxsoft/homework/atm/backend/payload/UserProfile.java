package com.zsxsoft.homework.atm.backend.payload;

import com.zsxsoft.homework.atm.backend.model.Card;
import com.zsxsoft.homework.atm.backend.model.Role;
import com.zsxsoft.homework.atm.backend.model.User;
import com.zsxsoft.homework.atm.backend.model.UserStatus;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public class UserProfile {
    private Long id;
    private String username;
    private String email;
    private String name;
    private Instant joinedAt;
    private Set<CardProfile> cards;
    private Set<Role> roles;
    private UserStatus status;

    public UserProfile(User user) {
        this(user, false);
    }
    public UserProfile(User user, boolean showSensitive) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.joinedAt = user.getCreatedAt();
        this.roles = user.getRoles();
        this.status = user.getStatus();
        if (showSensitive) {
            this.email = user.getEmail();
            this.cards = user.getCards().stream().map(p -> new CardProfile(p, true)).collect(Collectors.toSet());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Set<CardProfile> getCards() {
        return cards;
    }

    public void setCards(Set<CardProfile> cards) {
        this.cards = cards;
    }
}
