package com.roomify.detection_be.web.entity;

import com.roomify.detection_be.web.entity.Users.User;
import jakarta.persistence.*;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "login_history")
@NoArgsConstructor
@AllArgsConstructor
public class LoginHistory {
    @Id
    @GeneratedValue(generator = "snowflake-id")
    @GenericGenerator(name = "snowflake-id", strategy = "com.roomify.detection_be.utility.SnowflakeIdGenerator")
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, updatable = false)
    private Instant time;

    @PrePersist
    protected void onCreate() {
        this.time = Instant.now();
    }
}
