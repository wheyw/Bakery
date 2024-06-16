package com.web.bakery.controller;

import com.web.bakery.model.*;
import com.web.bakery.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class AccountController {
    private final AccountService accountService;
    private final FavoriteService favoriteService;
    private final HistoryService historyService;
    @Autowired
    public AccountController(AccountService accountService, FavoriteService favoriteService, HistoryService historyService) {
        this.favoriteService = favoriteService;
        this.accountService = accountService;
        this.historyService = historyService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getUserById(@PathVariable UUID id) {
        Optional<AccountDTO> userOptional = accountService.getUserById(id);
        return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @GetMapping("/data")
    public ResponseEntity<AccountDTO> getUserByJWT(@RequestParam String jwt) {
        if(JwtService.validateToken(jwt)) {
            var id = JwtService.getUserIdFromToken(jwt);
            Optional<AccountDTO> userOptional = accountService.getUserById(id);
            return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } else
            return new ResponseEntity<>(HttpStatus.LOCKED);
    }
    @GetMapping("/address")
    public ResponseEntity<String> getAddressByJWT(@RequestParam String jwt) {
        if(JwtService.validateToken(jwt)) {
            var id = JwtService.getUserIdFromToken(jwt);
            String address = accountService.getUserAddress(id);
            return ResponseEntity.status(HttpStatus.OK).body(address);
        } else
            return new ResponseEntity<>(HttpStatus.LOCKED);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateJWT(@RequestParam String jwt)
    {
        if(JwtService.validateToken(jwt))
            return ResponseEntity.status(HttpStatus.OK).build();
        else return ResponseEntity.status(HttpStatus.LOCKED).build();
    }
    //Добавил пагинацию для оптимизации памяти при большом количестве записей в бд [14.04]
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AccountDTO> usersPage = accountService.getAllUsers(pageable);

        if (usersPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(usersPage.getContent());
        }
    }
    @PostMapping("/add")
    public ResponseEntity<AccountDTO> createUser(@Valid @RequestBody AccountDTO request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Обработка ошибок валидации
            return ResponseEntity.badRequest().build();
        }

        AccountDTO createdUser = accountService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    @PostMapping("/auth")
    public ResponseEntity<String> doAuth(@Valid @RequestBody AccountDTO request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Обработка ошибок валидации
            return ResponseEntity.badRequest().build();
        }

        String result = accountService.doAuth(request.getEmail(), request.getPasswordHash());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }
    @PatchMapping("/delete/{id}")
    public ResponseEntity<AccountDTO> deleteUserById(@PathVariable UUID id) {
        if(accountService.deleteUser(id)) {
            return ResponseEntity.status(HttpStatus.OK).body(accountService.getUserById(id).get());
        }
        else return ResponseEntity.notFound().build();
    }
    @PatchMapping("/recover/{id}")
    public ResponseEntity<AccountDTO> recoverUserById(@PathVariable UUID id) {
        if(accountService.recoverUser(id)) {
            return ResponseEntity.status(HttpStatus.OK).body(accountService.getUserById(id).get());
        }
        else return ResponseEntity.notFound().build();
    }
    @PutMapping("/update")
    public ResponseEntity<AccountDTO> updateUser(@RequestParam String jwt, @RequestBody AccountDTO newData)
    {
        if(JwtService.validateToken(jwt)) {
            var id = JwtService.getUserIdFromToken(jwt);
            if (accountService.getUserById(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(accountService.updateUser(id, newData));
            } else return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.LOCKED).build();
    }
    @PatchMapping("/update/password")
    public ResponseEntity<AccountDTO> resetPassword(@RequestParam String jwt, @RequestParam String current, @RequestParam String new_)
    {
        if(JwtService.validateToken(jwt)) {
            var id = JwtService.getUserIdFromToken(jwt);
            if (accountService.getUserById(id).isPresent()) {
                if(PasswordUtils.verifyPassword(current, accountService.getUserHashPass(id))) {
                    new_ = PasswordUtils.encodePassword(new_);
                    accountService.resetPassword(id, new_);
                    return ResponseEntity.status(HttpStatus.OK).build();
                } else ResponseEntity.status(HttpStatus.LOCKED);
            } else return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.LOCKED).build();
    }
    @GetMapping("/count")
    public ResponseEntity<Integer> getUsersCount() {
        Integer count = accountService.getUsersCount();
        return ResponseEntity.ok(count);
    }
    @GetMapping("/id")
    public ResponseEntity<UUID> getIdWithJWT(@RequestParam String jwt)
    {
        if(JwtService.validateToken(jwt))
            return ResponseEntity.ok(JwtService.getUserIdFromToken(jwt));
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    //Избранное
    @GetMapping("/favorite")
    public ResponseEntity<List<FavoriteDTO>> getFavs(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "20") int size,
                                                        @RequestParam String jwt,
                                                        @RequestParam(defaultValue = "") String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FavoriteDTO> favsPage;

        if(JwtService.validateToken(jwt)) {
            var id = JwtService.getUserIdFromToken(jwt);
            favsPage = favoriteService.searchFav(pageable, id, search);
            if (favsPage.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(favsPage.getContent());
            }
        } else return ResponseEntity.status(HttpStatus.LOCKED).build();
    }

    @PostMapping("/favorite/add")
    public ResponseEntity<FavoriteDTO> addToFav(@Valid @RequestBody FavoriteDTO request,
                                                 BindingResult bindingResult,
                                                 @RequestParam String jwt) {
        if (bindingResult.hasErrors()) {
            // Обработка ошибок валидации
            return ResponseEntity.badRequest().build();
        }
        if(JwtService.validateToken(jwt)) {
            request.setUser_id(JwtService.getUserIdFromToken(jwt));
            FavoriteDTO item = favoriteService.addToFav(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        }
        else return ResponseEntity.status(HttpStatus.LOCKED).build();
    }
    @GetMapping("/favorite/count")
    public ResponseEntity<Integer> getFavCount(@RequestParam String jwt,
                                               @RequestParam(defaultValue = "") String search)
    {
        if(JwtService.validateToken(jwt))
        {
            var id = JwtService.getUserIdFromToken(jwt);
            return ResponseEntity.status(HttpStatus.OK).body(favoriteService.getFavCount(id,search));
        }
        return ResponseEntity.status(HttpStatus.LOCKED).build();
    }

    //Покупки
    @GetMapping("/history")
    public ResponseEntity<List<HistoryItemDTO>> getHistory(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "20") int size,
                                                          @RequestParam String jwt,
                                                           @RequestParam String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HistoryItemDTO> historyPage;

        if(JwtService.validateToken(jwt)) {
            var id = JwtService.getUserIdFromToken(jwt);
            historyPage = historyService.getHistory(pageable, id, search);
            if (historyPage.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(historyPage.getContent());
            }
        } else return ResponseEntity.status(HttpStatus.LOCKED).build();
    }

    @PostMapping("/buy")
    public ResponseEntity<HistoryItemDTO> addToHistory(@Valid @RequestBody HistoryItemDTO request,
                                                BindingResult bindingResult,
                                                @RequestParam String jwt) {
        if (bindingResult.hasErrors()) {
            // Обработка ошибок валидации
            return ResponseEntity.badRequest().build();
        }
        if(JwtService.validateToken(jwt)) {
            request.setUser_id(JwtService.getUserIdFromToken(jwt));
            HistoryItemDTO item = historyService.buyItem(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        }
        else return ResponseEntity.status(HttpStatus.LOCKED).build();
    }
    @GetMapping("/history/active")
    public ResponseEntity<List<HistoryItemDTO>> getActive(@RequestParam String jwt)
    {
        if(JwtService.validateToken(jwt))
        {
            return ResponseEntity.ok(historyService.getActive(JwtService.getUserIdFromToken(jwt)));
        }
        else return ResponseEntity.status(HttpStatus.LOCKED).build();
    }
    @GetMapping("/history/count")
    public ResponseEntity<Integer> getHistoryCount(@RequestParam String jwt, @RequestParam String search)
    {
        if(JwtService.validateToken(jwt))
        {
            var id = JwtService.getUserIdFromToken(jwt);
            return ResponseEntity.status(HttpStatus.OK).body(historyService.getHistorySize(id, search));
        }
        return ResponseEntity.status(HttpStatus.LOCKED).build();
    }

    @PatchMapping("/history/confirm")
    public ResponseEntity<HistoryItemDTO> confirmItem(@RequestParam String jwt, @RequestParam Integer item)
    {
        if(JwtService.validateToken(jwt))
        {
            var id = JwtService.getUserIdFromToken(jwt);
            historyService.confirmItem(id, item);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.LOCKED).build();
    }
}