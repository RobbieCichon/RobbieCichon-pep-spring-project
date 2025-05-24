package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import com.example.entity.Account;

@Repository
public interface AccountRepository  extends JpaRepository<Account, Integer>{

    @Query
    Optional<Account> findByUsernameAndPassword(String username, String password);

    @Query
    Optional<Account> findByUsername(String username);
}
