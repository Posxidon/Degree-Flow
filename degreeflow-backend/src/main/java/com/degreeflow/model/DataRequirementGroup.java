package com.degreeflow.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "requirement_groups")
@Data
public class DataRequirementGroup {

    @Id
    @Column(name = "course_group_id")
    private Integer courseGroupId;

    @Column(name = "transcript_id")
    private String transcriptId;

    @Column(name = "num_required")
    private Integer numRequired;

    @Column(name = "num_completed")
    private Integer numCompleted = 0;

    private String type;

    @ElementCollection
    @CollectionTable(name = "requirement_group_courses", joinColumns = @JoinColumn(name = "course_group_id"))
    @Column(name = "course_code")
    private List<String> courses;

    // No-argument constructor
    public DataRequirementGroup() {}

    // Constructor with parameters
    public DataRequirementGroup(Integer courseGroupId, Integer numRequired, String transcriptId, String type, List<String> courses) {
        this.courseGroupId = courseGroupId;
        this.numRequired = numRequired;
        this.numCompleted = 0;
        this.transcriptId = transcriptId;
        this.type = type;
        this.courses = courses;
    }

    public List<String> getCourses() {
        return courses;
    }
    
    public Integer getNumCompleted() {
        return numCompleted;
    }
    
    public void setNumCompleted(Integer numCompleted) {
        this.numCompleted = numCompleted;
    }
    public Integer getNumRequired() {
        return numRequired;
    }
    

}
