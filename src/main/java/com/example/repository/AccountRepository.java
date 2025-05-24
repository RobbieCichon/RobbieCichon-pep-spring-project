package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import com.example.entity.Account;

@Repository
public interface AccountRepository  extends JpaRepository<Account, Integer>{

    @Query("FROM Account WHERE username = :usernameVar AND password = :passwordVar")
    Optional<Account> findByUsernameAndPassword(@Param("usernameVar") String username, @Param("passwordVar") String password);

    @Query("FROM Account WHERE username = :usernameVar")
    Optional<Account> findByUsername(@Param("usernameVar") String username);
}
