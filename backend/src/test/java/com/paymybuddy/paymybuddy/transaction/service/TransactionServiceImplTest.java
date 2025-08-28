package com.paymybuddy.paymybuddy.transaction.service;

import com.paymybuddy.paymybuddy.connection.repository.UserConnectionRepository;
import com.paymybuddy.paymybuddy.transaction.model.*;
import com.paymybuddy.paymybuddy.transaction.repository.TransactionRepository;
import com.paymybuddy.paymybuddy.user.model.User;
import com.paymybuddy.paymybuddy.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    UserRepository userRepo;
    @Mock
    TransactionRepository txRepo;
    @Mock
    UserConnectionRepository connRepo;

    @InjectMocks
    TransactionServiceImpl service;

    @Test
    void create_ok_returnsDTO_withCommission() {
        var sender = new User(1, "alice", "alice@mail.com", "h", Instant.now());
        var receiver = new User(2, "bob", "bob@mail.com", "h", Instant.now());
        var req = new CreateTransactionRequest(1, "bob@mail.com", new BigDecimal("25.00"), "Resto");

        when(userRepo.findById(1)).thenReturn(Optional.of(sender));
        when(userRepo.findByEmail("bob@mail.com")).thenReturn(Optional.of(receiver));
        when(connRepo.existsByUserAndConnection(sender, receiver)).thenReturn(true);

        when(txRepo.save(any())).thenAnswer(inv -> {
            Transaction t = inv.getArgument(0);
            t.setId(10);
            t.setCreatedAt(Instant.now());
            return t;
        });

        TransactionDTO dto = service.create(req);

        assertThat(dto.id()).isEqualTo(10);
        assertThat(dto.senderId()).isEqualTo(1);
        assertThat(dto.receiverId()).isEqualTo(2);
        assertThat(dto.amount()).isEqualByComparingTo("25.00");
        assertThat(dto.commission()).isEqualByComparingTo(new BigDecimal("25.00").multiply(new BigDecimal("0.005")));
    }

    @Test
    void create_throws_whenSelfTransfer() {
        var sender = new User(1, "alice", "alice@mail.com", "h", Instant.now());
        // friend lookup returns the same instance -> equals true
        when(userRepo.findById(1)).thenReturn(Optional.of(sender));
        when(userRepo.findByEmail("alice@mail.com")).thenReturn(Optional.of(sender));

        var req = new CreateTransactionRequest(1, "alice@mail.com", new BigDecimal("5.00"), "oops");

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(IllegalArgumentException.class);
        verify(txRepo, never()).save(any());
    }

    @Test
    void create_throws_whenNotBuddy() {
        var sender = new User(1, "alice", "alice@mail.com", "h", Instant.now());
        var receiver = new User(2, "bob", "bob@mail.com", "h", Instant.now());
        when(userRepo.findById(1)).thenReturn(Optional.of(sender));
        when(userRepo.findByEmail("bob@mail.com")).thenReturn(Optional.of(receiver));
        when(connRepo.existsByUserAndConnection(sender, receiver)).thenReturn(false);

        var req = new CreateTransactionRequest(1, "bob@mail.com", new BigDecimal("5.00"), "x");

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(IllegalStateException.class);
        verify(txRepo, never()).save(any());
    }

    @Test
    void listForUser_mapsToDTO() {
        var user = new User(1, "alice", "alice@mail.com", "h", Instant.now());
        var other = new User(2, "bob", "bob@mail.com", "h", Instant.now());
        var t1 = new Transaction(100, user, other, "a", new BigDecimal("1.00"), Instant.now());
        var t2 = new Transaction(101, other, user, "b", new BigDecimal("2.00"), Instant.now());

        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(txRepo.findBySenderOrReceiverOrderByCreatedAtDesc(user, user)).thenReturn(List.of(t1, t2));

        var list = service.listForUser(1);
        assertThat(list).hasSize(2);
        assertThat(list.get(0).id()).isEqualTo(100);
        assertThat(list.get(1).amount()).isEqualByComparingTo("2.00");
    }
}
