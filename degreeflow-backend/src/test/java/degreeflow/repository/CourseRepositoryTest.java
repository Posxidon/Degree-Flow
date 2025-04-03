package degreeflow.repository;

import com.degreeflow.model.Course;
import com.degreeflow.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.testng.Assert.assertTrue;

@DataJpaTest
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void testSaveAndFindById() {
        Course course = new Course("CS101", "Intro to Computer Science", "Basics of CS");


        Course savedCourse = courseRepository.save(course);

        Optional<Course> retrievedCourse = courseRepository.findById(savedCourse.getId());
        assertTrue(retrievedCourse.isPresent());
    }
}