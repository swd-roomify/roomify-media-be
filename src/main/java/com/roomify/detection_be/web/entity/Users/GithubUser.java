package com.roomify.detection_be.web.entity.Users;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "GITHUB_USER")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GithubUser {
  @Id private String id;

  @Column(name = "username")
  private String githubUsername;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, unique = true)
  private User user;
}
