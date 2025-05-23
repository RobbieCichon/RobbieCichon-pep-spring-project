package com.example.service;

import java.util.List;

import javax.naming.AuthenticationException;

import java.util.ArrayList;
import com.example.repository.AccountRepository;
import com.example.entity.Account;
import com.example.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    AccountRepository accountRepository;

    private List<Account> accountList = new ArrayList<>();

    @Autowired
    public AccountService(AccountRepository accountRepository){
        accountRepository = this.accountRepository;
    }

    public Account registerAccount(Account account) throws AuthenticationException, ResourceNotFoundException{
        if (account.getUsername().length() > 0 && account.getPassword().length() > 3){
            List<Account> accounts = getAccountList();
            for (Account currentAccounts:accounts){
                if (currentAccounts.getUsername().equals(account.getUsername())){
                    throw new ResourceNotFoundException(account.getUsername() + " already exists, try a different one.");
                }
            }
        }
        else throw new AuthenticationException("Invalid username and/or password specifications, try a different one.");

        accountList.add(account);
        return account;
    }

    public List<Account> getAccountList(){
        return accountList;
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
        for (Account account:accountList){
            if (account.getAccountId().equals(account_id)){
                return account;
            }
        }
        throw new ResourceNotFoundException(account_id + " was not found. Please try another account ID.");
    }
}
