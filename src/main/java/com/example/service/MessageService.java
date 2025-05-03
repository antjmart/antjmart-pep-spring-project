package com.example.service;

import org.springframework.stereotype.Service;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;
import com.example.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.exception.InvalidMessageTextException;
import com.example.exception.UserNotExistsException;
import com.example.exception.MessageNotExistsException;
import java.util.List;
import java.util.Optional;


@Service
public class MessageService {
    
    private MessageRepository messageRepo;
    private AccountRepository accountRepo;

    @Autowired
    public MessageService(MessageRepository messageRepo, AccountRepository accountRepo) {
        this.messageRepo = messageRepo;
        this.accountRepo = accountRepo;
    }

    public Message createMessage(Message message) throws InvalidMessageTextException, UserNotExistsException {
        String text = message.getMessageText();

        if (text.isEmpty() || text.length() > 255)
            throw new InvalidMessageTextException();
        if (accountRepo.findById(message.getPostedBy()).isEmpty())
            throw new UserNotExistsException();
        
        return messageRepo.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepo.findAll();
    }

    public Message getMessage(Integer id) {
        Optional<Message> message = messageRepo.findById(id);
        return message.isPresent() ? message.get() : null;
    }

    public Integer deleteMessage(Integer id) {
        if (messageRepo.findById(id).isEmpty())
            return null;
        
        messageRepo.deleteById(id);
        return 1;
    }

    public Integer updateMessage(Integer id, String newText) throws InvalidMessageTextException, MessageNotExistsException {
        if (newText.isEmpty() || newText.length() > 255)
            throw new InvalidMessageTextException();
        
        Optional<Message> message = messageRepo.findById(id);
        if (message.isEmpty())
            throw new MessageNotExistsException();
        
        message.get().setMessageText(newText);
        messageRepo.save(message.get());
        return 1;
    }

    public List<Message> getUserMessages(Integer accountId) {
        return messageRepo.findByPostedBy(accountId);
    }
}
