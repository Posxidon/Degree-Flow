package com.degreeflow.service;

import com.degreeflow.model.Course;
import com.degreeflow.model.Degree;
import com.degreeflow.repository.DegreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DegreeService {
    @Autowired
    private DegreeRepository degreeRepository;

    public List<Degree> getAllDegrees() {
        return degreeRepository.findAll();
    }

    public Degree getDegreeById(Long id) {
        return degreeRepository.findById(id).orElse(null);
    }

    public Degree addDegree(Degree degree) {
        return degreeRepository.save(degree);
    }

    public void deleteDegree(Long id) {
        degreeRepository.deleteById(id);
    }
}
