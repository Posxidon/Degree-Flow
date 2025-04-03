package com.degreeflow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "json_schedule")
public class Schedule {

    // Getters and setters
    // Primary key – make sure this column name matches your database schema.
    @Id
    @Column(name = "user_id")
    private String userId;

    // Stores the JSON schedule (as a string)
    @Lob
    @Column(name = "schedule_data")
    private String scheduleData;

}
