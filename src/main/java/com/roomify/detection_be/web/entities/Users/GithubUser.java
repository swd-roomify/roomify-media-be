package com.roomify.detection_be.web.entities.Users;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "GITHUB_USER")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GithubUser {
  @Id
  @GeneratedValue(generator = "snowflake-id")
  @GenericGenerator(name = "snowflake-id", strategy = "com.roomify.detection_be.utility.SnowflakeIdGenerator")
  private String id;

  @Column(name = "username")
  private String githubUsername;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, unique = true)
  private User user;
}
