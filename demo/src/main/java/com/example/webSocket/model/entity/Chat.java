package com.example.webSocket.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "chats")
public class Chat {
    @Id
    private String uuid;

    private String ownerUsername;

    public Chat (String uuid, String ownerUsername) {
        this.uuid = uuid;
        this.ownerUsername = ownerUsername;
    }

    public Chat () {}

    public String getUuid() {
        return uuid;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }
}