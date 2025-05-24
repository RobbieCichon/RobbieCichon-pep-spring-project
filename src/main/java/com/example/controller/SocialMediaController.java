package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.ResourceNotFoundException;
import com.example.service.AccountService;
import com.example.service.MessageService;

import org.apache.tomcat.util.file.ConfigurationSource.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import javax.naming.AuthenticationException;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> postRegisterAccount(@RequestBody Account account){
        Account newAccount = accountService.registerAccount(account);
        return new ResponseEntity<>(newAccount, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Account> postLoginAccount(@RequestBody Account account) throws AuthenticationException{
        Account accessedAccount = accountService.loginAccount(account);
        return new ResponseEntity<>(accessedAccount, HttpStatus.OK);
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message){
        Message newMessage = messageService.addMessage(message);
        return new ResponseEntity<>(newMessage, HttpStatus.OK);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        List<Message> allMessages = messageService.getMessageList();
        return ResponseEntity.status(HttpStatus.OK).body(allMessages);
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer message_id){
        Optional<Message> optionalMessage = messageService.getMessageById(message_id);
        if(optionalMessage.isPresent()){
            Message foundMessage = optionalMessage.get();
            return new ResponseEntity<>(foundMessage, HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.OK);
    }
 
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

    @PatchMapping("messages/{message_id}")
    public ResponseEntity<Integer> patchMessage(@PathVariable Integer message_id, @RequestBody Message message){
        int result = messageService.patchMessage(message_id, message.getMessageText());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessagesById(@PathVariable Integer account_id){
        List<Message> messagesOfAccount = messageService.getMessagesById(account_id);
        return ResponseEntity.status(HttpStatus.OK).body(messagesOfAccount);
    }

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
