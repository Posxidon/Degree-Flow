package com.degreeflow.service;

import com.degreeflow.model.TranscriptData;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranscriptParser {

    // Returns the latest term block based on occurrence
    public static String getLatestTermBlock(String pdfText) {
        Pattern termHeaderPattern = Pattern.compile("---\\s*(\\d{4}\\s+\\w+)\\s*---", Pattern.CASE_INSENSITIVE);
        Matcher matcher = termHeaderPattern.matcher(pdfText);
        List<Integer> blockIndices = new ArrayList<>();
        while (matcher.find()) {
            blockIndices.add(matcher.start());
        }
        if (blockIndices.isEmpty()) {
            return pdfText;
        }
        int latestIndex = blockIndices.getLast();
        return pdfText.substring(latestIndex);
    }

    // Uses regex patterns to extract fields from the term block
    public static TranscriptData parseTranscript(String pdfText, String studentId) {
        String termBlock = getLatestTermBlock(pdfText);
        TranscriptData transcript = new TranscriptData();
        transcript.setStudentId(studentId);

        transcript.setProgram(extractField(termBlock, "Program:\\s*(.+)"));
        // Use "Plan:" if needed or combine with program:
        // transcript.setProgram(transcript.getProgram() + " | " + extractField(termBlock, "Plan:\\s*(.+)"));

        // For term, try to extract a line following "Term Enrolment" or from the term header
        String termExtract = extractField(termBlock, "Term Enrolment\\s*(.+)");
        if (termExtract.isEmpty()) {
            // Fallback: extract the header from the term block itself.
            Pattern headerPattern = Pattern.compile("---\\s*(\\d{4}\\s+\\w+)\\s*---", Pattern.CASE_INSENSITIVE);
            Matcher headerMatcher = headerPattern.matcher(termBlock);
            if (headerMatcher.find()) {
                termExtract = headerMatcher.group(1).trim();
            }
        }
        transcript.setTerm(termExtract);

        // Extract courses: get the block between "Course" and "Totals"
        transcript.setCoursesTaken(extractCourses(termBlock));

        // Extract GPA from a pattern like: "GPA:\\s*(\\S+)"
        transcript.setGpa(extractField(termBlock, "GPA:\\s*(\\S+)"));
        // Extract Total Points as TotalGpa (adjust pattern as needed)
        transcript.setTotalGpa(extractField(termBlock, "Total Points:\\s*(\\S+)"));
        // Extract GPA Units as Units
        transcript.setUnits(extractField(termBlock, "GPA Units:\\s*(\\S+)"));

        // Determine if Co-op is indicated anywhere in the block
        transcript.setCoOp(termBlock.toLowerCase().contains("co-op") ? "Yes" : "No");

        // For Grades, you might want to parse from coursesTaken block;
        // Here we provide a placeholder since courses/grades extraction can be complex.
        transcript.setGrades(extractField(termBlock, "Grade\\s*(\\S+)"));

        return transcript;
    }

    private static String extractField(String text, String regex) {
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(1).trim();
        }
        return "";
    }

    private static String extractCourses(String text) {
        Pattern p = Pattern.compile("Course\\s*(.+?)Totals", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(1).trim();
        }
        return "";
    }
}
