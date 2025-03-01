package com.roomify.detection_be.web.entities;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "room_access_history")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RoomAccessHistory {
  @Id
  @GeneratedValue(generator = "snowflake-id")
  @GenericGenerator(
      name = "snowflake-id",
      strategy = "com.roomify.detection_be.utility.SnowflakeIdGenerator")
  private String id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "room_id", nullable = false)
  private Room room;

  @Column(nullable = false)
  private String action;

  @Column(nullable = false, updatable = false)
  private Instant accessAt;

  @PrePersist
  protected void onCreate() {
    this.accessAt = Instant.now();
  }
}
