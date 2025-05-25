package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.ResourceNotFoundException;
import com.example.service.AccountService;
import com.example.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import javax.naming.AuthenticationException;


@RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /**
     * Creates a new entry into the Account repository. Errors out on invalid username/password parameters or with an already existing username.
     * @param account Account object to be persisted into the Account repository
     * @return a Response Entity containing the status code and persisted account (now with an ID).
     */
    @PostMapping("/register")
    public ResponseEntity<Account> postRegisterAccount(@RequestBody Account account){
        Account newAccount = accountService.registerAccount(account);
        return new ResponseEntity<>(newAccount, HttpStatus.OK);
    }

    /**
     * Logs in to an existing account using given username/password combination. Errors out on invalid (not found) credentials.
     * @param account Account object with username and password to be found in the Account repository
     * @return a Response Entity containing the status code and accessed account.
     */
    @PostMapping("/login")
    public ResponseEntity<Account> postLoginAccount(@RequestBody Account account) throws AuthenticationException{
        Account accessedAccount = accountService.loginAccount(account);
        return new ResponseEntity<>(accessedAccount, HttpStatus.OK);
    }

    /**
     * Creates a new message and persists it to the Message repository. Errors out on invalid message content or invalid postedBy ID (doesn't exist).
     * @param message Message object to be persisted into the Message repositor.
     * @return a Response entity containing the status code and the persisted message (now with an ID).
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message){
        Message newMessage = messageService.addMessage(message);
        return new ResponseEntity<>(newMessage, HttpStatus.OK);
    }

    /**
     * Gets a list of all messages in the Message repository. 
     * @return a Response Entity containing the list of messages and the status code. If none exist, the list is empty. 
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        List<Message> allMessages = messageService.getMessageList();
        return ResponseEntity.status(HttpStatus.OK).body(allMessages);
    }

    /**
     * Gets a particular message using its message ID. Only returns the status if not found.
     * @param message_id ID of the message to be found in the Message repository.
     * @return a Response Entity containing the message object that was to be found and the status code. 
     * If not found, only a Response Entity with a status code.
     */
    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer message_id){
        Optional<Message> optionalMessage = messageService.getMessageById(message_id);
        if(optionalMessage.isPresent()){
            Message foundMessage = optionalMessage.get();
            return new ResponseEntity<>(foundMessage, HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.OK);
    }
 
    /**
     * Deletes a particular message in the Message repository using its message ID. If not found, returns the status code.
     * @param message_id ID of the messge to be deleted.
     * @return a Response Entity containing the status code and the number of rows affected (1 if successful), if it were to be 0 then the response body is empty.
     */
    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer message_id){
        int result = messageService.deleteMessage(message_id);
        if (result == 1){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        else{
        return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    /**
     * Patches an existing message in the Message Repository to change it's message text. Sends out the number of rows affected (1 or 0).
     * @param message_id The ID of the message to find and update the message text.
     * @param message The message text that will replace the existing one of the message in the Message repository.
     * @return a Response Entity containing the number of rows updated (1 or 0) and the status code. 
     */
    @PatchMapping("messages/{message_id}")
    public ResponseEntity<Integer> patchMessage(@PathVariable Integer message_id, @RequestBody Message message){
        int result = messageService.patchMessage(message_id, message.getMessageText());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Gets a list of messages that were all posted by the given account ID. Empty if none found.
     * @param account_id ID of the account to be used as the parameter when retrieving the list of messages from the Message repository.
     * @return a Response Entity containing a list of all messages found that were posted by the account ID. Empty list if none found.
     */
    @GetMapping("accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessagesById(@PathVariable Integer account_id){
        List<Message> messagesOfAccount = messageService.getMessagesById(account_id);
        return ResponseEntity.status(HttpStatus.OK).body(messagesOfAccount);
    }

    /**
     * Exception Handlers to automatically catch given exceptions and return a set status code. 
     * Used for Authentication, Resource Retrieval, and Duplicate Username exceptions that come up during the runtime of this project.
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleUnauthorized(AuthenticationException ex){ return ex.getMessage();}

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNoSuchResource(ResourceNotFoundException ex){ return ex.getMessage();}

    @ExceptionHandler(DuplicateUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicate(DuplicateUsernameException ex){ return ex.getMessage();}
}
