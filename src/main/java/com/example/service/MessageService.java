package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.entity.Message;
import com.example.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Retrieves a list of Message objects from the Message repository. If non exist yet, the list is empty.
     * @return the generated Message list from the Message repository.
     */
    public List<Message> getMessageList(){
        return (List<Message>) messageRepository.findAll();
    }

    /**
     * Service layer level of retrieving a message using a given ID from the Message repository using a query.
     * @param message_id ID of the message to be searched for and retrieved.
     * @return Optional object of the message to be found for the controller to decide on how to return.
     */
    public Optional<Message> getMessageById(Integer message_id){
        Optional<Message> optionalMessage = messageRepository.findById(message_id);
        return optionalMessage;
    }

    /**
     * Service layer level of persisting a new message to the Message repository. Errors out nonexisting postedBy ID or invalid message contents.
     * @param message The message object to be persisted to the Message repository.
     * @return The newly saved message object retrieved from the Message repository, now with a message ID.
     * @throws ResourceNotFoundException if the postedBy ID does not match an existing Account's ID, or the message contents are too short/long.
     */
    public Message addMessage(Message message) throws ResourceNotFoundException{
        if (message.getMessageText().length() < 255 && message.getMessageText().length() > 1){
            if (accountRepository.existsById(message.getPostedBy())){
                Message savedMessage = messageRepository.save(message);
                return savedMessage;
            }
        }
        throw new ResourceNotFoundException("Error creating message, either invalid message text or account ID not found");
    }

    /**
     * Service layer level of retrieving all messages that were posted by the given account ID. Empty if none are found.
     * @param account_id Given account ID to be queried for in the Message Repository.
     * @return A list of Message objects that matched postedBy with given account ID.
     */
    public List<Message> getMessagesById(Integer account_id){
        return messageRepository.findByPostedBy(account_id);
    }

    /**
     * Service layer level of deleting a message from the Message repository, then returns the number of rows affected.
     * @param message_id ID of the message to be deleted.
     * @return An integer of the number of rows affected by the delete statement (either 1 or 0 since message ID is unique in the table).
     */
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

    /**
     * Service layer level of partially updating an existing message with new message contents. 
     * Errors out on nonexisting message containing the message ID and on invalid message contents (too short / long).
     * @param message_id ID of the message to be found in the Message repository.
     * @param message_contents Text that will replace the existing message contents once found.
     * @return An integer that depicts the number of rows affected. Always 1 unless throws an error when not finding a message.
     * @throws ResourceNotFoundException if no message is found with the given ID or the message contents are invalid (too short / long).
     */
    public Integer patchMessage(Integer message_id, String message_contents)throws ResourceNotFoundException{
        Message message = messageRepository.findById(message_id)
        .orElseThrow(() -> new ResourceNotFoundException(message_id + " was not found. Please try another message ID."));
        if (message_contents.length() > 255 || message_contents.length() < 1) throw new ResourceNotFoundException(message_contents + " is not a valid message to post!");
        message.setMessageText(message_contents);
        messageRepository.save(message);

        return 1;
    }

}
