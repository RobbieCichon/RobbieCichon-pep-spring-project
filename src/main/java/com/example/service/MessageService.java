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
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
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
        if (message.getMessageText().length() < 255 && message.getMessageText().length() > 1){
            if (accountRepository.existsById(message.getPostedBy())){
                Message savedMessage = messageRepository.save(message);
                return savedMessage;
            }
        }
        throw new ResourceNotFoundException("Error creating message, either invalid message text or account ID not found");
    }

    public List<Message> getMessagesById(Integer account_id){
        return messageRepository.findByPostedBy(account_id);
    }

    public int deleteMessage(Integer message_id){
        Optional<Message> optionalMessage = messageRepository.findById(message_id);
        if(optionalMessage.isPresent()){
            messageRepository.deleteById(message_id);
            return 1;
        }
        else{
            return 0;
        }
    }

    public Message patchMessage(Integer message_id, String message_contents)throws ResourceNotFoundException{
        Message message = messageRepository.findById(message_id)
        .orElseThrow(() -> new ResourceNotFoundException(message_id + " was not found. Please try another message ID."));
        if (message_contents.length() > 255 || message_contents.length() < 1) throw new ResourceNotFoundException(message_contents + " is not a valid message to post!");
        message.setMessageText(message_contents);
        messageRepository.save(message);

        return messageRepository.getById(message_id);
    }

}
