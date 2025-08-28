package com.paymybuddy.paymybuddy.user.service;

import com.paymybuddy.paymybuddy.user.model.User;
import com.paymybuddy.paymybuddy.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    UserRepository repo;

    @Test
    void loadUserByUsername_ok_returnsSpringUser() {
        var service = new CustomUserDetailsService(repo);
        var user = new User(1, "alice", "alice@mail.com", "hash", Instant.now());
        when(repo.findByEmail("alice@mail.com")).thenReturn(Optional.of(user));

        var details = service.loadUserByUsername("alice@mail.com");

        assertThat(details.getUsername()).isEqualTo("alice@mail.com");
        assertThat(details.getPassword()).isEqualTo("hash");
        assertThat(details.getAuthorities()).extracting("authority").contains("ROLE_USER");
    }

    @Test
    void loadUserByUsername_notFound_throws() {
        var service = new CustomUserDetailsService(repo);
        when(repo.findByEmail("none@mail.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("none@mail.com"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
