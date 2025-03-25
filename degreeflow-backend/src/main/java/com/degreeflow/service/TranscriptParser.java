package com.degreeflow.service;

import com.degreeflow.model.CourseRecord;
import com.degreeflow.model.TranscriptData;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TranscriptParser {

    // Method to parse the uploaded PDF transcript
    public TranscriptData parseTranscript(MultipartFile file) throws IOException {
        // Load the PDF from the MultipartFile
        PDDocument document = PDDocument.load(file.getInputStream());  // Using input stream for MultipartFile
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);  // Extract text from the PDF
        document.close();  // Close the document after extracting text

        // Create a TranscriptData object and populate it with the extracted data
        TranscriptData transcript = new TranscriptData();
        transcript.setProgram(extractProgram(text));  // Extract program from the PDF text
        transcript.setPlan(extractPlan(text));        // Extract plan from the PDF text
        transcript.setGpa(parseGPA(extractGPA(text)));          // Convert GPA string to double
        transcript.setTotalPoints(parseDouble(extractTotalPoints(text)));  // Convert total points string to double
        transcript.setGpaUnits(parseDouble(extractGPAUnits(text))); // Convert GPA units string to double
        transcript.setTermEnrollment(extractTerm(text));  // Extract term enrollment
        transcript.setCourses(extractCourses(text, transcript));  // Extract courses
        transcript.setRecent(validatePrintDate(text));  // Validate print date

        return transcript;  // Return the populated TranscriptData object
    }

    // Helper method to extract program information from the transcript text
    private String extractProgram(String text) {
        return extractTextByKeyword(text, "Program:");
    }

    // Helper method to extract plan information from the transcript text
    private String extractPlan(String text) {
        return extractTextByKeyword(text, "Plan:");
    }

    // Helper method to extract GPA from the transcript text and convert to double
    private String extractGPA(String text) {
        return extractTextByKeyword(text, "GPA:");
    }

    // Helper method to extract total points from the transcript text and convert to double
    private String extractTotalPoints(String text) {
        return extractTextByKeyword(text, "Total Points:");
    }

    // Helper method to extract GPA units from the transcript text and convert to double
    private String extractGPAUnits(String text) {
        return extractTextByKeyword(text, "GPA Units:");
    }

    // Helper method to extract term enrollment information from the transcript text
    private String extractTerm(String text) {
        return extractTextByKeyword(text, "Term Enrollment:");
    }

    // Helper method to extract courses from the transcript text
    private List<CourseRecord> extractCourses(String text, TranscriptData transcript) {
        List<CourseRecord> courses = new ArrayList<>();
        // Parse the courses and create CourseRecord objects (example, you should adapt based on the actual format)
        String coursesText = extractTextByKeyword(text, "Courses:");
        if (!coursesText.isEmpty()) {
            // Assuming the courses are listed in a predictable format, like "Course Code - Course Name"
            String[] courseLines = coursesText.split("\n");
            for (String line : courseLines) {
                if (!line.trim().isEmpty()) {
                    String[] courseDetails = line.split("-");
                    if (courseDetails.length == 2) {
                        CourseRecord course = new CourseRecord();
                        course.setCourseCode(courseDetails[0].trim());  // Set course code
                        course.setTitle(courseDetails[1].trim());  // Set course title (instead of courseName)
                        course.setTranscript(transcript);  // Set the transcript reference
                        courses.add(course);
                    }
                }
            }
        }
        return courses;
    }

    // Helper method to validate the print date on the transcript
    private boolean validatePrintDate(String text) {
        return text.contains("Print Date:");  // Simple example, refine as needed
    }

    // General helper method to extract text by keyword from the transcript
    private String extractTextByKeyword(String text, String keyword) {
        int startIndex = text.indexOf(keyword);
        if (startIndex != -1) {
            int endIndex = text.indexOf("\n", startIndex);  // Find the end of the line
            if (endIndex != -1) {
                return text.substring(startIndex + keyword.length(), endIndex).trim();  // Extract text between keyword and newline
            }
        }
        return "";  // Return empty string if keyword not found
    }

    // Helper method to parse GPA or total points to double
    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;  // Return 0.0 if parsing fails (you can improve this as needed)
        }
    }

    // Helper method to parse GPA specifically (if it needs specific validation)
    private double parseGPA(String gpa) {
        return parseDouble(gpa);
    }
}
