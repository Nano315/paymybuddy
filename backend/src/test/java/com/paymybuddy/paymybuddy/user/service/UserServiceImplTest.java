package com.paymybuddy.paymybuddy.user.service;

import com.paymybuddy.paymybuddy.user.model.UpdateProfileRequest;
import com.paymybuddy.paymybuddy.user.model.User;
import com.paymybuddy.paymybuddy.user.model.UserDTO;
import com.paymybuddy.paymybuddy.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository repo;
    @Mock
    BCryptPasswordEncoder encoder;

    @InjectMocks
    UserServiceImpl service;

    @Test
    void listAll_mapsToDTO() {
        var u1 = new User(1, "alice", "alice@mail.com", "h", Instant.now());
        var u2 = new User(2, "bob", "bob@mail.com", "h", Instant.now());
        when(repo.findAll()).thenReturn(List.of(u1, u2));

        List<UserDTO> list = service.listAll();

        assertThat(list).hasSize(2);
        assertThat(list.get(0).username()).isEqualTo("alice");
        assertThat(list.get(1).email()).isEqualTo("bob@mail.com");
    }

    @Test
    void getByEmail_ok() {
        var u = new User(1, "alice", "alice@mail.com", "h", Instant.now());
        when(repo.findByEmail("alice@mail.com")).thenReturn(Optional.of(u));

        var dto = service.getByEmail("alice@mail.com");

        assertThat(dto.id()).isEqualTo(1);
        assertThat(dto.username()).isEqualTo("alice");
    }

    @Test
    void updateProfile_usernameOnly_noPasswordEncoding() {
        var existing = new User(1, "old", "old@mail.com", "hashOld", Instant.now());
        when(repo.findByEmail("old@mail.com")).thenReturn(Optional.of(existing));
        when(repo.existsByEmail("new@mail.com")).thenReturn(false);
        when(repo.existsByUsername("newname")).thenReturn(false);
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var req = new UpdateProfileRequest("newname", "new@mail.com", null);

        var dto = service.updateProfile("old@mail.com", req);

        assertThat(dto.username()).isEqualTo("newname");
        assertThat(dto.email()).isEqualTo("new@mail.com");
        verify(encoder, never()).encode(anyString());
        verify(repo).save(any(User.class));
        assertThat(existing.getPasswordHash()).isEqualTo("hashOld");
    }

    @Test
    void updateProfile_withPassword_encodes() {
        var existing = new User(1, "old", "old@mail.com", "hashOld", Instant.now());
        when(repo.findByEmail("old@mail.com")).thenReturn(Optional.of(existing));
        // Ne surtout pas stubber existsByEmail ici : l'email ne change pas → condition
        // short-circuit
        when(repo.existsByUsername("newname")).thenReturn(false);
        when(encoder.encode("newpwd")).thenReturn("hashNew");
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var req = new UpdateProfileRequest("newname", "old@mail.com", "newpwd");

        var dto = service.updateProfile("old@mail.com", req);

        assertThat(dto.username()).isEqualTo("newname");
        verify(encoder).encode("newpwd");
        assertThat(existing.getPasswordHash()).isEqualTo("hashNew");
    }

    @Test
    void updateProfile_emailAlreadyUsed_throws() {
        var existing = new User(1, "old", "old@mail.com", "hashOld", Instant.now());
        when(repo.findByEmail("old@mail.com")).thenReturn(Optional.of(existing));
        when(repo.existsByEmail("taken@mail.com")).thenReturn(true);

        var req = new UpdateProfileRequest("newname", "taken@mail.com", null);

        assertThatThrownBy(() -> service.updateProfile("old@mail.com", req))
                .isInstanceOf(IllegalArgumentException.class);
        verify(repo, never()).save(any());
    }

    @Test
    void updateProfile_usernameAlreadyUsed_throws() {
        var existing = new User(1, "old", "old@mail.com", "hashOld", Instant.now());
        when(repo.findByEmail("old@mail.com")).thenReturn(Optional.of(existing));
        // Ne pas stubber existsByEmail ici : l'email ne change pas
        when(repo.existsByUsername("taken")).thenReturn(true);

        var req = new UpdateProfileRequest("taken", "old@mail.com", null);

        assertThatThrownBy(() -> service.updateProfile("old@mail.com", req))
                .isInstanceOf(IllegalArgumentException.class);

        verify(repo, never()).save(any());
    }

}
