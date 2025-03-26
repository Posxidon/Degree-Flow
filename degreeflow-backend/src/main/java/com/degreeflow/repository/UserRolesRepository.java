package com.degreeflow.repository;

import com.degreeflow.model.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {
    // You can add custom queries here if needed
}
