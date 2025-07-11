package com.paymybuddy.paymybuddy.connection.model;

import com.paymybuddy.paymybuddy.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "user_connections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserConnection {

    @EmbeddedId
    private UserConnectionKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("connectionId")
    @JoinColumn(name = "connection_id")
    private User connection;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant createdAt = Instant.now();

    /* --- constructeur pratique id + 2 users --- */
    public UserConnection(User user, User connection) {
        this.id = new UserConnectionKey(user.getId(), connection.getId());
        this.user = user;
        this.connection = connection;
        this.createdAt = Instant.now();
    }

}