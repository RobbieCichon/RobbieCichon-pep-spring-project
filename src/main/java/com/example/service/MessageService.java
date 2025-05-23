package com.example.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import java.util.ArrayList;

import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.entity.Message;
import com.example.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        messageRepository = this.messageRepository;
        accountRepository = this.accountRepository;
    }

    public List<Message> getMessageList(){
        return (List<Message>) messageRepository.findAll();
    }

    public Message getMessageById(Integer message_id){
        Optional<Message> optionalMessage = messageRepository.findById(message_id);
        if (optionalMessage.isPresent()){

            Message message = optionalMessage.get();
            return message;
            }
        else{
            Message message = new Message();
            return message;
        }
    }

    public Message addMessage(Message message) throws ResourceNotFoundException{
        if (message.getMessageText().length() >= 255 || message.getMessageText() == ""){
            if (accountRepository.existsById(message.getPostedBy())){
                messageRepository.save(message);
            }
        }
        
        return message;
    }

    public List<Message> getMessagesById(Integer account_id){
        List<Message> messages = getMessageList();
        List<Message> matchMessages = new ArrayList<>();
        for (Message message:messages){
            if (message.getPostedBy().equals(account_id)){
                matchMessages.add(message);
            }
        }
        return matchMessages;
    }

    public Message deleteMessage(Integer message_id){
        Optional<Message> optionalMessage = messageRepository.findById(message_id);
        if(optionalMessage.isPresent()){
            Message message = optionalMessage.get();
            messageRepository.deleteById(message_id);
            return message;
        }
        else{
            Message message = new Message();
            return message;
        }
    }

    public Message patchMessage(Integer message_id, String message_contents)throws ResourceNotFoundException{
        Message message = messageRepository.findById(message_id)
        .orElseThrow(() -> new ResourceNotFoundException(message_id + " was not found. Please try another message ID."));
        message.setMessageText(message_contents);
        return message;
    }

}
