package com.roomify.detection_be.web.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    private String roomId;

    @Column(nullable = false)
    private String roomName;

    @Column(unique = true, nullable = false)
    private String roomCode;

    @ManyToOne
    @JoinColumn(name = "host_id", nullable = false)
    private User hostId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
