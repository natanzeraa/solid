package com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.Models.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {}
