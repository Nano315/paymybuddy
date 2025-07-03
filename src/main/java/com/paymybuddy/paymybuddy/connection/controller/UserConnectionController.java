package com.paymybuddy.paymybuddy.connection.controller;

import com.paymybuddy.paymybuddy.connection.service.UserConnectionService;
import com.paymybuddy.paymybuddy.user.model.UserDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/connections")
public class UserConnectionController {

    private final UserConnectionService service;

    public UserConnectionController(UserConnectionService service) {
        this.service = service;
    }

    /* ---------- GET mes amis ---------- */
    @GetMapping("/{userId}")
    public List<UserDTO> list(@PathVariable Integer userId) {
        return service.listFriends(userId);
    }

    /* ---------- POST ajouter ami ---------- */
    public record AddFriendRequest(
            Integer userId,
            @NotBlank @Email String friendEmail) {
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody AddFriendRequest req) {
        service.addFriend(req.userId(), req.friendEmail());
    }
}
