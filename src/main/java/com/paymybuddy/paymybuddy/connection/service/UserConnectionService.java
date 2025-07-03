package com.paymybuddy.paymybuddy.connection.service;

import com.paymybuddy.paymybuddy.user.model.UserDTO;
import java.util.List;

public interface UserConnectionService {
    List<UserDTO> listFriends(Integer userId);

    void addFriend(Integer userId, String friendEmail);
}