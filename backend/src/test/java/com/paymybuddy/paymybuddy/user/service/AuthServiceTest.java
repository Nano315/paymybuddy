package com.paymybuddy.paymybuddy.user.service;

import com.paymybuddy.paymybuddy.config.security.JwtService;
import com.paymybuddy.paymybuddy.user.model.*;
import com.paymybuddy.paymybuddy.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository repo;
    @Mock
    BCryptPasswordEncoder encoder;
    @Mock
    AuthenticationManager authManager;
    @Mock
    JwtService jwtService;

    @InjectMocks
    AuthService service;

    @Test
    void register_ok_whenEmailAndUsernameFree() {
        // Given
        RegisterRequest req = new RegisterRequest("alice", "alice@mail.com", "topsecret");
        when(repo.existsByEmail("alice@mail.com")).thenReturn(false);
        when(repo.existsByUsername("alice")).thenReturn(false);
        when(encoder.encode("topsecret")).thenReturn("hash123");

        when(repo.save(any())).thenAnswer(inv -> {
            var u = (User) inv.getArgument(0);
            u.setId(1);
            return u;
        });

        // When
        UserDTO dto = service.register(req);

        // Then
        assertThat(dto.id()).isEqualTo(1);
        assertThat(dto.username()).isEqualTo("alice");
        assertThat(dto.email()).isEqualTo("alice@mail.com");
        verify(encoder).encode("topsecret");
        verify(repo).save(any(User.class));
    }

    @Test
    void register_throws_whenEmailAlreadyUsed() {
        RegisterRequest req = new RegisterRequest("alice", "alice@mail.com", "pwd");
        when(repo.existsByEmail("alice@mail.com")).thenReturn(true);

        assertThatThrownBy(() -> service.register(req))
                .isInstanceOf(IllegalArgumentException.class);
        verify(repo, never()).save(any());
    }

    @Test
    void login_ok_generatesToken() {
        // Given
        LoginRequest req = new LoginRequest("alice@mail.com", "topsecret");
        var user = new User(1, "alice", "alice@mail.com", "hash", Instant.now());

        // authenticate(...) retourne un Authentication (on retourne un token
        // “authentifié” simple)
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(req.email(), req.password()));

        when(repo.findByEmail("alice@mail.com")).thenReturn(Optional.of(user));
        when(jwtService.generate("alice@mail.com", 1)).thenReturn("JWT_TOKEN");

        // When
        AuthResponse resp = service.login(req);

        // Then
        assertThat(resp.token()).isEqualTo("JWT_TOKEN");
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generate("alice@mail.com", 1);
    }

}
