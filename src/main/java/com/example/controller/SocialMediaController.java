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
        return ResponseEntity.status(HttpStatus.OK).body(accountService.registerUser(account));
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void conflictStatus() {}

    @ExceptionHandler({BlankUsernameException.class, PasswordTooShortException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void clientErrorStatus() {}
}
