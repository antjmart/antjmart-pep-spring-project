package com.example.service;

import org.springframework.stereotype.Service;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;
import com.example.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.exception.InvalidMessageTextException;
import com.example.exception.UserNotExistsException;
import java.util.List;


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
}
