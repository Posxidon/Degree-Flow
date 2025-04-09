package com.degreeflow.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "json_schedule") // Explicitly specify the table name
@Data
public class JsonSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String json;

    @Column(name = "user_id", length = 60000) // Explicitly map to the correct column name
    private String userId;

    public Object getScheduleData() {
        return json;
    }
}

