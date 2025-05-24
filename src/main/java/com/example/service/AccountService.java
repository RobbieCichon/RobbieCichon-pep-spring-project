package com.example.service;

import java.util.List;

import javax.naming.AuthenticationException;

import java.util.ArrayList;
import com.example.repository.AccountRepository;
import com.example.entity.Account;
import com.example.exception.*;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    

    @Autowired
    public AccountService(AccountRepository accountRepository){
        accountRepository = this.accountRepository;
    }

    public Account registerAccount(Account account) throws DuplicateUsernameException, ResourceNotFoundException{
        if (account.getUsername().length() > 0 && account.getPassword().length() > 3){
            Optional<Account> optionalAccount = accountRepository.findByUsername(account.getUsername());
            if (optionalAccount.isPresent()){
                    throw new DuplicateUsernameException(account.getUsername() + " already exists, try a different one.");
            }
        }
        else throw new ResourceNotFoundException("Invalid username and/or password specifications, try a different one.");

        Account savedAccount = accountRepository.save(account);
        return savedAccount;
    }


    public Account loginAccount(Account account) throws AuthenticationException{
        Optional<Account> optionalAccount = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
        if (optionalAccount.isPresent()){
            Account foundAccount = optionalAccount.get();
            return foundAccount;
        }
        else throw new AuthenticationException("Username and password credentials are invalid");
    }
/* 
    public Account getAccountById(Integer account_id) throws ResourceNotFoundException{
        Optional<Account> optionalAccount = accountRepository.findById(account_id);
        if(optionalAccount.isPresent()){
            return optionalAccount.get();
        }
        else{
            Account account = new Account();
            return account;
        }
    }
    */
}
    
