package degreeflow.controller;

// import com.degreeflow.controller.PdfParsingController;
// import com.degreeflow.model.TranscriptData;
// import com.degreeflow.service.TranscriptParser;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.test.web.servlet.MockMvc;
// import org.testng.annotations.Test;
//
// import java.util.Arrays;
// import java.util.List;
//
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import static org.hamcrest.Matchers.hasSize;
// import static org.hamcrest.Matchers.is;
//
// @WebMvcTest(PdfParsingController.class)  // Annotation to test the PdfParsingController class
// public class PdfParsingControllerTest {
//
//     @Autowired
//     private MockMvc mockMvc;  // Mock MVC to simulate HTTP requests and verify responses
//
//     @MockBean
//     private TranscriptParser transcriptParser;  // Mock the TranscriptParser service
//
//     @Test
//     public void testGetAllCourses() throws Exception {
//         // Create mock TranscriptData objects to simulate database records
//         TranscriptData transcriptData1 = new TranscriptData("CS101", "Intro to Computer Science", "Basics of CS");
//         transcriptData1.setId(1L);  // Manually setting the id for the course
//
//         TranscriptData transcriptData2 = new TranscriptData("MATH101", "Calculus", "Intro to Calculus");
//         transcriptData2.setId(2L);  // Manually setting the id for the course
//
//         // Create a list of TranscriptData objects to simulate the response
//         List<TranscriptData> cours = Arrays.asList(transcriptData1, transcriptData2);
//
//         // Uncomment to mock the service method (use when the method is implemented)
//         // Mockito.when(transcriptParser.getAllCourses()).thenReturn(cours);
//
//         // Perform a GET request to the "/api/courses" endpoint and check the response
//         mockMvc.perform(get("/api/courses"))
//                 .andExpect(status().isOk())  // Expect HTTP 200 OK status
//                 .andExpect(jsonPath("$", hasSize(2)))  // Expect the response to contain 2 courses
//                 .andExpect(jsonPath("$[0].courseCode", is("CS101")));  // Expect the first course's code to be "CS101"
//     }
//
// }
