package com.paymybuddy.paymybuddy.user.service;

import com.paymybuddy.paymybuddy.user.model.UserDTO;
import java.util.List;

public interface UserService {
    List<UserDTO> listAll();
}
