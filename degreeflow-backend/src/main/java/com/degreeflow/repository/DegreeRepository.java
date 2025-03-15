package com.degreeflow.repository;

import com.degreeflow.model.Degree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeRepository extends JpaRepository<Degree, Long> {
    // Additional query methods (if needed)
}
