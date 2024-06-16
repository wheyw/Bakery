package com.web.bakery.service;

import com.web.bakery.model.AccountDTO;
import com.web.bakery.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public AccountDTO createUser(AccountDTO user) {
        user.setPasswordHash(PasswordUtils.encodePassword(user.getPasswordHash()));
        return accountRepository.save(user);
    }
    public String doAuth(String email, String password)
    {
        String passwordHash;
        if(!accountRepository.getPasswordHash(email).isEmpty())
            passwordHash = accountRepository.getPasswordHash(email).get();
        else return "Неверный email или пароль!";
        if(PasswordUtils.verifyPassword(password,passwordHash))
            return JwtService.generateToken(accountRepository.getIdByEmail(email).get());
        else return "Неверный email или пароль!";
    }

    public List<AccountDTO> getAllUsers() {
        return accountRepository.findAll();
    }
    public Page<AccountDTO> getAllUsers(Pageable pageable) {
        return accountRepository.getActiveUsers(pageable);
    }

    public Optional<AccountDTO> getUserById(UUID id) {
        return accountRepository.findById(id);
    }

    public void deleteAllUsers() {
        accountRepository.deleteAll();
    }

    public boolean deleteUser(UUID id) {
        try {
            accountRepository.deleteSoftById(id);
        }catch (Exception ex){
            System.out.println("Request error: " + ex.getMessage());
            return false;
        }
        accountRepository.setUpdatedAtById(id,LocalDateTime.now());
        return true;
    }
    public boolean recoverUser(UUID id) {
        try {
            accountRepository.recoverUserById(id);
        }catch (Exception ex){
            System.out.println("Request error: " + ex.getMessage());
            return false;
        }
        accountRepository.setUpdatedAtById(id,LocalDateTime.now());
        return true;
    }
    public AccountDTO updateUser(UUID id,AccountDTO newData)
    {
        try {
            accountRepository.updateUser(id,
                    newData.getName(),
                    newData.getEmail(),
                    newData.getAddress());
            accountRepository.setUpdatedAtById(id,LocalDateTime.now());
        } catch (Exception exception)
        {
            System.out.println("Request error: " + exception.getMessage());
        }
        return newData;
    }
    public void resetPassword(UUID id, String new_)
    {
        try {
            accountRepository.resetPassword(id,new_);
            accountRepository.setUpdatedAtById(id,LocalDateTime.now());
        } catch (Exception exception)
        {
            System.out.println("Request error: " + exception.getMessage());
        }
    }
    public Integer getUsersCount()
    {
        return accountRepository.getUserCount();
    }
    public String getUserHashPass(UUID id) { return accountRepository.getPassHash(id);}
    public String getUserAddress(UUID id) { return accountRepository.getAddress(id);}
}
