package com.zsxsoft.homework.atm.backend.repository;

import com.zsxsoft.homework.atm.backend.model.Currency;
import com.zsxsoft.homework.atm.backend.model.Transaction;
import com.zsxsoft.homework.atm.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Optional<Currency> findByCode(String code);
}