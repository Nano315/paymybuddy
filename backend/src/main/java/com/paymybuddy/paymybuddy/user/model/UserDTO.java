package com.paymybuddy.paymybuddy.user.model;

public record UserDTO(Integer id, String username, String email) {
    public static UserDTO from(User u) {
        return new UserDTO(u.getId(), u.getUsername(), u.getEmail());
    }
}
