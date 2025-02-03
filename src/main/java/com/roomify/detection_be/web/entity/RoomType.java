package com.roomify.detection_be.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "room_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomType {
    @Id
    @GeneratedValue(generator = "snowflake-id")
    @GenericGenerator(name = "snowflake-id", strategy = "com.roomify.detection_be.utility.SnowflakeIdGenerator")
    private String id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Integer maxCapacity;
}
