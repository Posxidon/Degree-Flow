package com.degreeflow.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "requirement_groups")
@Data
public class RequirementGroup {

    @Id
    @Column(name = "course_group_id")
    private Integer courseGroupId;

    @Column(name = "transcript_id") 
    private Long transcriptId;

    @Column(name = "num_required")
    private Integer numRequired;

    @Column(name = "num_completed")
    private Integer numCompleted = 0;
    
    private String type;

    @ElementCollection
    @CollectionTable(name = "requirement_group_courses", joinColumns = @JoinColumn(name = "course_group_id"))
    @Column(name = "course_code")
    private List<String> courses;

    public RequirementGroup() {}

    public RequirementGroup(Integer courseGroupId, Integer numRequired, String type, List<String> courses) {
        this.courseGroupId = courseGroupId;
        this.numRequired = numRequired;
        this.numCompleted = 0;
        this.type = type;
        this.courses = courses;
    }
}
