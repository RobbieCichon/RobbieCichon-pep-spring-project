package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import javax.naming.AuthenticationException;

import java.util.ArrayList;


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
    public @ResponseBody ResponseEntity<Account> postRegisterAccount(@RequestBody Account account)throws AuthenticationException{
        Account newAccount = accountService.registerAccount(account);
        return new ResponseEntity<>(newAccount, HttpStatus.OK);
    }

    @PostMapping("/login")
    public @ResponseBody ResponseEntity<Account> postLoginAccount(@RequestBody Account account) throws AuthenticationException{
        Account accessedAccount = accountService.loginAccount(account);
        return new ResponseEntity<>(accessedAccount, HttpStatus.OK);
    }

    @PostMapping("/messages")
    public @ResponseBody ResponseEntity<Message> postMessage(@RequestBody Message message){
        Message newMessage = messageService.addMessage(message);
        return new ResponseEntity<>(newMessage, HttpStatus.OK);
    }

    @GetMapping("/messages")
    public @ResponseBody ResponseEntity<List<Message>> getAllMessages(){
        List<Message> allMessages = messageService.getMessageList();
        return new ResponseEntity<>(allMessages, HttpStatus.OK);
    }
 
    @DeleteMapping("/messages/{message_id}")
    public @ResponseBody ResponseEntity<Message> deleteMessage(@RequestParam Integer message_id){
        Message deletedMessage = messageService.deleteMessage(message_id);
        return new ResponseEntity<>(deletedMessage, HttpStatus.OK);
    }

    @PatchMapping("messages/{message_id}")
    public @ResponseBody ResponseEntity<Message> updateMessage(@RequestParam Integer message_id, @RequestBody String message_contents){
        Message updatedMessage = messageService.patchMessage(message_id, message_contents);
        return new ResponseEntity<>(updatedMessage, HttpStatus.OK);
    }

    @GetMapping("accounts/{account_id}/messages")
    public @ResponseBody ResponseEntity<List<Message>> getMessagesById(@RequestParam Integer account_id){
        List<Message> messagesOfAccount = messageService.getMessagesById(account_id);
        return new ResponseEntity<>(messagesOfAccount, HttpStatus.OK);
    }
}
