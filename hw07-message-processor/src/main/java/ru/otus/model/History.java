package ru.otus.model;

import java.time.LocalDateTime;

public class History {
    private final Message message;
    private final LocalDateTime createdAt;

    public History(Message message, LocalDateTime createdAt) throws CloneNotSupportedException {
        this.message = (Message) message.clone();
        this.createdAt = createdAt;
    }

    public Message getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
