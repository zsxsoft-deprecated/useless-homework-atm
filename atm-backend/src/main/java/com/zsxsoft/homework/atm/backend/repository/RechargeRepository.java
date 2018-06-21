package com.zsxsoft.homework.atm.backend.repository;

import com.zsxsoft.homework.atm.backend.model.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RechargeRepository extends JpaRepository<Recharge, Long> {
}