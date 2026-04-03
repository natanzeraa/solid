package com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.models.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {}
