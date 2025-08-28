package com.paymybuddy.paymybuddy.connection.service;

import com.paymybuddy.paymybuddy.connection.model.UserConnection;
import com.paymybuddy.paymybuddy.connection.repository.UserConnectionRepository;
import com.paymybuddy.paymybuddy.user.model.User;
import com.paymybuddy.paymybuddy.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserConnectionServiceImplTest {

    @Mock
    UserRepository userRepo;
    @Mock
    UserConnectionRepository connRepo;

    @InjectMocks
    UserConnectionServiceImpl service;

    @Test
    void listFriends_returnsMappedDTOs() {
        var alice = new User(1, "alice", "alice@mail.com", "h", Instant.now());
        var bob = new User(2, "bob", "bob@mail.com", "h", Instant.now());
        var carol = new User(3, "carol", "carol@mail.com", "h", Instant.now());

        when(userRepo.findById(1)).thenReturn(Optional.of(alice));
        when(connRepo.findByUser(alice)).thenReturn(List.of(
                new UserConnection(alice, bob),
                new UserConnection(alice, carol)));

        var list = service.listFriends(1);

        assertThat(list).hasSize(2);
        assertThat(list).extracting("username").containsExactlyInAnyOrder("bob", "carol");
        assertThat(list).extracting("email").contains("bob@mail.com");
    }

    @Test
    void addFriend_ok_savesTwoLinks() {
        var alice = new User(1, "alice", "alice@mail.com", "h", Instant.now());
        var bob = new User(2, "bob", "bob@mail.com", "h", Instant.now());

        when(userRepo.findById(1)).thenReturn(Optional.of(alice));
        when(userRepo.findByEmail("bob@mail.com")).thenReturn(Optional.of(bob));
        when(connRepo.existsByUserAndConnection(alice, bob)).thenReturn(false);

        service.addFriend(1, "bob@mail.com");

        verify(connRepo, times(2)).save(any(UserConnection.class));
    }

    @Test
    void addFriend_throws_whenSelf() {
        var alice = new User(1, "alice", "alice@mail.com", "h", Instant.now());
        when(userRepo.findById(1)).thenReturn(Optional.of(alice));
        when(userRepo.findByEmail("alice@mail.com")).thenReturn(Optional.of(alice));

        assertThatThrownBy(() -> service.addFriend(1, "alice@mail.com"))
                .isInstanceOf(IllegalArgumentException.class);
        verify(connRepo, never()).save(any());
    }

    @Test
    void addFriend_throws_whenAlreadyFriends() {
        var alice = new User(1, "alice", "alice@mail.com", "h", Instant.now());
        var bob = new User(2, "bob", "bob@mail.com", "h", Instant.now());

        when(userRepo.findById(1)).thenReturn(Optional.of(alice));
        when(userRepo.findByEmail("bob@mail.com")).thenReturn(Optional.of(bob));
        when(connRepo.existsByUserAndConnection(alice, bob)).thenReturn(true);

        assertThatThrownBy(() -> service.addFriend(1, "bob@mail.com"))
                .isInstanceOf(IllegalStateException.class);
        verify(connRepo, never()).save(any());
    }

    @Test
    void addFriend_throws_whenFriendUnknown() {
        var alice = new User(1, "alice", "alice@mail.com", "h", Instant.now());
        when(userRepo.findById(1)).thenReturn(Optional.of(alice));
        when(userRepo.findByEmail("ghost@mail.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addFriend(1, "ghost@mail.com"))
                .isInstanceOf(IllegalArgumentException.class);
        verify(connRepo, never()).save(any());
    }
}
