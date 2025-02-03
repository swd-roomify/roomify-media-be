package com.roomify.detection_be.web.entity;

import com.roomify.detection_be.web.entity.Users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;

@Entity
@Table(name = "room_participants")
@NoArgsConstructor
@AllArgsConstructor
public class RoomParticipant {
    @Id
    @GeneratedValue(generator = "snowflake-id")
    @GenericGenerator(name = "snowflake-id", strategy = "com.roomify.detection_be.utility.SnowflakeIdGenerator")
    private String id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, updatable = false)
    private Instant joinTime;
    @PrePersist
    protected void onCreate() {
        this.joinTime = Instant.now();
    }
    private Float lastPositionX;

    private Float lastPositionY;

}
