package com.example.service;

import java.util.List;

import javax.naming.AuthenticationException;

import java.util.ArrayList;
import com.example.repository.AccountRepository;
import com.example.entity.Account;
import com.example.exception.*;

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
            List<Account> accounts = getAccountList();
            for (Account currentAccounts:accounts){
                if (currentAccounts.getUsername().equals(account.getUsername())){
                    throw new DuplicateUsernameException(account.getUsername() + " already exists, try a different one.");
                }
            }
        }
        else throw new ResourceNotFoundException("Invalid username and/or password specifications, try a different one.");

        accountList.add(account);
        return account;
    }

    public List<Account> getAccountList(){
        return (List<Account>) accountRepository.findAll();
    }

    public Account loginAccount(Account account) throws AuthenticationException{
        for(Account knownAccount:accountList){
            if (knownAccount.getUsername().equals(account.getUsername()) && knownAccount.getPassword().equals(account.getPassword())){
                return account;
            }
        }

        throw new AuthenticationException("Username and password credentials are invalid");
    }

    public Account getAccountById(Integer account_id) throws ResourceNotFoundException{
        return accountRepository.findById(account_id)
        .orElseThrow(() -> new ResourceNotFoundException(account_id + " was not found. Please try another account ID."));
    }
}
