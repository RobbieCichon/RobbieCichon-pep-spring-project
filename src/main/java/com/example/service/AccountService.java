package com.example.service;

import javax.naming.AuthenticationException;

import com.example.repository.AccountRepository;
import com.example.entity.Account;
import com.example.exception.*;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    /**
     * Service Layer level of registering account. Ensures username/password fulfills given parameters and throws appropiate exceptions if not.
     * @param account Account object to be persisted into the Account repository if successful.
     * @return the newly persisted account, now retrieved from the repository with its new ID.
     * @throws DuplicateUsernameException If given username already exists in the Account repository.
     * @throws ResourceNotFoundException If given username is blank or given password is less than 4 characters.
     */
    public Account registerAccount(Account account) throws DuplicateUsernameException, ResourceNotFoundException{
        if (account.getUsername().length() > 0 && account.getPassword().length() > 3){
            Optional<Account> optionalAccount = accountRepository.findByUsername(account.getUsername());
            if (optionalAccount.isPresent()){
                    throw new DuplicateUsernameException(account.getUsername() + " already exists, try a different one.");
            }
        }
        else throw new ResourceNotFoundException("Invalid username and/or password specifications, try a different one.");

        Account savedAccount = accountRepository.save(account);
        return accountRepository.getById(savedAccount.getAccountId());
    }

    /**
     * Service layer level of attempting to log into an account with given username and password. 
     * Checks with Account repository using custom query to avoid looping through all existing accounts. 
     * @param account Account object containing the username/password to be checked.
     * @return Account object if a match is found, otherwise throws exception.
     * @throws AuthenticationException If no matching account is found with the given username/password.
     */
    public Account loginAccount(Account account) throws AuthenticationException{
        Optional<Account> optionalAccount = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
        if (optionalAccount.isPresent()){
            Account foundAccount = optionalAccount.get();
            return foundAccount;
        }
        else throw new AuthenticationException("Username and password credentials are invalid");
    }
}
    
