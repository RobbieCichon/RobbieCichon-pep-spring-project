package com.example.service;

import java.util.List;

import javax.annotation.Resource;

import java.util.ArrayList;

import com.example.repository.MessageRepository;
import com.example.entity.Message;
import com.example.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {
    MessageRepository messageRepository;

    private List<Message> messageList = new ArrayList<>();

    @Autowired
    public MessageService(MessageRepository messageRepository){
        messageRepository = this.messageRepository;
    }

    public List<Message> getMessageList(){
        return messageList;
    }

    public Message getMessageById(Integer message_id){
        for (Message message:messageList){
            if (message.getMessageId().equals(message_id)){
                return message;
            }
        }
        Message message = new Message();
        return message;
    }

    public Message addMessage(Message message) throws ResourceNotFoundException{
        messageList.add(message);
        
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
        List<Message> messages = getMessageList();
        for (Message message:messages){
            if (message.getMessageId().equals(message_id)){
                messageList.remove(message);
                return message;
            }
        }
        Message message = new Message();
        return message;
    }

    public Message patchMessage(Integer message_id, String message_contents)throws ResourceNotFoundException{
        List<Message> messages = getMessageList();
        for (Message message:messages){
            if (message.getMessageId().equals(message_id)){
                message.setMessageText(message_contents);
                return message;
            }
        }
        throw new ResourceNotFoundException(message_id + " was not found. Please try another message ID.");
    }

}
