package degreeflow.repository;

// import com.degreeflow.model.TranscriptData;
// import com.degreeflow.repository.TranscriptRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.testng.annotations.Test;
//
// import java.util.Optional;
//
// import static org.testng.Assert.assertTrue;
//
// @DataJpaTest  // Annotation to test JPA repository functionality
// public class TranscriptDataRepositoryTest {
//
//     @Autowired
//     private TranscriptRepository transcriptRepository;  // Injecting the repository for testing
//
//     @Test
//     public void testSaveAndFindById() {
//         // Create a TranscriptData object and set its properties
//         TranscriptData transcriptData = new TranscriptData();
//         transcriptData.setCourseCode("CS101");
//         transcriptData.setCourseName("Intro to Computer Science");
//         transcriptData.setDescription("Basics of CS");
//
//         // Save the TranscriptData object to the repository
//         TranscriptData savedTranscriptData = transcriptRepository.save(transcriptData);
//
//         // Retrieve the saved TranscriptData by its ID
//         Optional<TranscriptData> retrievedCourse = transcriptRepository.findById(savedTranscriptData.getId());
//
//         // Assert that the retrieved course is present in the repository
//         assertTrue(retrievedCourse.isPresent());
//     }
// }
