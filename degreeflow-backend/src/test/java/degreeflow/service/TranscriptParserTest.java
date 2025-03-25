package degreeflow.service;

// import com.degreeflow.model.TranscriptData;
// import com.degreeflow.repository.TranscriptRepository;
// import com.degreeflow.service.TranscriptParser;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.MockitoAnnotations;
// import org.testng.annotations.BeforeMethod;
// import org.testng.annotations.Test;
//
// import java.util.Arrays;
// import java.util.List;
//
// import static org.testng.Assert.assertEquals;
//
// public class TranscriptParserTest {
//
//     @InjectMocks
//     private TranscriptParser transcriptParser;  // Inject the TranscriptParser service for testing
//
//     @Mock
//     private TranscriptRepository transcriptRepository;  // Mock the TranscriptRepository to simulate database operations
//
//     @BeforeMethod
//     public void setUp() {
//         MockitoAnnotations.openMocks(this);  // Initialize mocks before each test method
//     }
//
//     @Test
//     public void testGetAllCourses() {
//         // Create mock TranscriptData objects to simulate courses
//         TranscriptData transcriptData1 = new TranscriptData();
//         transcriptData1.setId(1L);  // Manually set the id
//         transcriptData1.setCourseCode("CS101");
//         transcriptData1.setCourseName("Intro to Computer Science");
//         transcriptData1.setDescription("Basics of CS");
//
//         TranscriptData transcriptData2 = new TranscriptData();
//         transcriptData2.setId(2L);  // Manually set the id
//         transcriptData2.setCourseCode("MATH101");
//         transcriptData2.setCourseName("Calculus");
//         transcriptData2.setDescription("Intro to Calculus");
//
//         // Create a list of TranscriptData objects to simulate the response
//         List<TranscriptData> cours = Arrays.asList(transcriptData1, transcriptData2);
//
//         // Mock the behavior of the transcriptRepository to return the list of courses
//         Mockito.when(transcriptRepository.findAll()).thenReturn(cours);
//
//         // Call the service method to get all courses and assert the result
//         List<TranscriptData> result = transcriptParser.getAllCourses();
//         assertEquals(result.size(), 2);  // Assert that the result contains 2 courses
//         assertEquals(result.get(0).getCourseCode(), "CS101");  // Assert the first course code is "CS101"
//     }
// }
