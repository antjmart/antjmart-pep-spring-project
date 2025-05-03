package com.example.service;

import org.springframework.stereotype.Service;
import com.example.repository.AccountRepository;
import com.example.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.exception.BlankUsernameException;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.PasswordTooShortException;
import com.example.exception.InvalidCredentialsException;
import java.util.Optional;


@Service
public class AccountService {
    
    private AccountRepository accountRepo;

    @Autowired
    public AccountService(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    public Account registerUser(Account account) throws BlankUsernameException, PasswordTooShortException, DuplicateUsernameException {
        if (account.getUsername().isEmpty())
            throw new BlankUsernameException();
        else if (account.getPassword().length() < 4)
            throw new PasswordTooShortException();
        else if (accountRepo.findByUsername(account.getUsername()).isPresent())
            throw new DuplicateUsernameException();
        else
            return accountRepo.save(account);
    }

    public Account loginUser(Account account) throws InvalidCredentialsException {
        String username = account.getUsername();
        String password = account.getPassword();
        Optional<Account> accountLogin = accountRepo.findByUsernameAndPassword(username, password);

        if (accountLogin.isPresent())
            return accountLogin.get();
        else
            throw new InvalidCredentialsException();
    }
}
