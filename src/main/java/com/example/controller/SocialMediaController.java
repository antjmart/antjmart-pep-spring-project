package com.example.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.*;
import java.util.List;


@RestController
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("register")
    public ResponseEntity<Account> registerUser(@RequestBody Account account) throws DuplicateUsernameException, PasswordTooShortException, BlankUsernameException {
        return ResponseEntity.ok(accountService.registerUser(account));
    }

    @PostMapping("login")
    public ResponseEntity<Account> loginUser(@RequestBody Account account) throws InvalidCredentialsException {
        return ResponseEntity.ok(accountService.loginUser(account));
    }

    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) throws InvalidMessageTextException, UserNotExistsException {
        return ResponseEntity.ok(messageService.createMessage(message));
    }

    @GetMapping("messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("messages/{messageId}")
    public ResponseEntity<Message> getMessage(@PathVariable Integer messageId) {
        return ResponseEntity.ok(messageService.getMessage(messageId));
    }

    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer messageId) {
        return ResponseEntity.ok(messageService.deleteMessage(messageId));
    }


    @ExceptionHandler(DuplicateUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void conflictStatus() {}

    @ExceptionHandler({BlankUsernameException.class,
                       PasswordTooShortException.class,
                       InvalidMessageTextException.class,
                       UserNotExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void clientErrorStatus() {}

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void unauthorizedStatus() {}
}
