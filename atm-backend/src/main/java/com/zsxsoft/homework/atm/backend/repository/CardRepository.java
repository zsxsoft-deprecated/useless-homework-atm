package com.zsxsoft.homework.atm.backend.repository;

import com.zsxsoft.homework.atm.backend.model.Card;
import com.zsxsoft.homework.atm.backend.model.CardType;
import com.zsxsoft.homework.atm.backend.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByType(CardType type);

    Optional<Card> findByCardNumber(String cardNumber);
}