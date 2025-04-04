package com.degreeflow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "json_schedule")
public class Schedule {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Lob
    @Column(name = "json")  // Updated to match the DB column name
    private String scheduleJson;
}
