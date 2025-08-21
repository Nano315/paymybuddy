package com.paymybuddy.paymybuddy.user.service;

import com.paymybuddy.paymybuddy.user.model.UpdateProfileRequest;
import com.paymybuddy.paymybuddy.user.model.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> listAll();

    UserDTO getByEmail(String email);

    UserDTO updateProfile(String currentEmail, UpdateProfileRequest req);
}
