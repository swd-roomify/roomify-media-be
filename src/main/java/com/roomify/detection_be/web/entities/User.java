package com.roomify.detection_be.web.entities;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "users")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
  @Id
  @GeneratedValue(generator = "snowflake-id")
  @GenericGenerator(
      name = "snowflake-id",
      strategy = "com.roomify.detection_be.utility.SnowflakeIdGenerator")
  @Column(name = "user_id", unique = true)
  private String userId;

  @Column(name = "username")
  private String username;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "account_non_expired")
  private boolean accountNonExpired;

  @Column(name = "is_enabled")
  private boolean isEnabled;

  @Column(name = "account_non_locked")
  private boolean accountNonLocked;

  @Column(name = "credentials_non_expired")
  private boolean credentialsNonExpired;

  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;

  @Column(name = "provided_id")
  private String providedId;

  @Column(name = "avatar_url")
  private String avatarUrl;

  @Column(name = "created_at", updatable = false)
  private Instant createdAt = Instant.now();
}
