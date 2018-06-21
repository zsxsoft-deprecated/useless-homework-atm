package com.zsxsoft.homework.atm.backend.repository;

import com.zsxsoft.homework.atm.backend.model.Card;
import com.zsxsoft.homework.atm.backend.model.Transaction;
import com.zsxsoft.homework.atm.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByFromCardOrToCard(Card fromCard, Card toCard, Pageable var1);

    @Query("SELECT t FROM Transaction t where t.fromCard.user.id = :userId OR t.toCard.user.id = :userId")
    Page<Transaction> findByUserId(@Param("userId") Long userId, Pageable var1);


}