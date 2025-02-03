package com.roomify.detection_be.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "character")
@NoArgsConstructor
@AllArgsConstructor
public class Character {
    @Id
    @GeneratedValue(generator = "snowflake-id")
    @GenericGenerator(name = "snowflake-id", strategy = "com.roomify.detection_be.utility.SnowflakeIdGenerator")
    @Column(name = "character_id")
    private String id;

    private String name;

    @Column(name = "character_url")
    private String characterUrl;
}
