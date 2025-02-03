package com.roomify.detection_be.web.entity;

import com.roomify.detection_be.web.entity.Users.User;
import jakarta.persistence.*;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "friendships")
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {
    @Id
    @GeneratedValue(generator = "snowflake-id")
    @GenericGenerator(name = "snowflake-id", strategy = "com.roomify.detection_be.utility.SnowflakeIdGenerator")
    private String id;

    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    @Column(nullable = false)
    private String status;

   @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
