package com.paymybuddy.paymybuddy.connection.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserConnectionKey implements Serializable {
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "connection_id")
    private Integer connectionId;

    public UserConnectionKey() {
    }

    public UserConnectionKey(Integer userId, Integer connectionId) {
        this.userId = userId;
        this.connectionId = connectionId;
    }

    /* getters / setters / equals & hashCode */
    // equals & hashCode basés sur userId + connectionId
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserConnectionKey that = (UserConnectionKey) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(connectionId, that.connectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, connectionId);
    }
}
