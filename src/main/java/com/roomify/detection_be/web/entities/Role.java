package com.roomify.detection_be.web.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role")
@Getter
@Setter
public class Role {

  @Id
  @GeneratedValue(generator = "snowflake-id")
  @GenericGenerator(
      name = "snowflake-id",
      strategy = "com.roomify.detection_be.utility.SnowflakeIdGenerator")
  @Column(name = "role_id")
  private String id;

  @Column(name = "role_name")
  private String name;
}
