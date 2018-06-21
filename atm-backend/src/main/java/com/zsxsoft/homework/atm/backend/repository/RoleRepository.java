package com.zsxsoft.homework.atm.backend.repository;

import com.zsxsoft.homework.atm.backend.model.Role;
import com.zsxsoft.homework.atm.backend.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
