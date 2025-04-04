package com.degreeflow.service;

import com.degreeflow.model.TranscriptData;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranscriptParser {

    private static final Set<String> ALLOWED_GRADES = new HashSet<>(Arrays.asList(
            "A", "A+", "A-", "B", "B+", "B-", "C", "C+", "C-", "D", "D+", "D-",
            "F", "COM", "MT", "WD", "T", "PASS", "FAIL"
    ));

    public static List<TranscriptData> parseTranscript(String pdfText, String studentId) {
        String normalizedText = normalizePdfText(pdfText);
        UUID transcriptId = UUID.randomUUID();
        List<TranscriptData> allCourseRows = new ArrayList<>();

        Pattern termPattern = Pattern.compile(
                "---\\s*(\\d{4}\\s+[A-Za-z/]+)\\s*---(.*?)(?=---\\s*\\d{4}\\s+[A-Za-z/]+\\s*---|$)",
                Pattern.DOTALL
        );
        Matcher matcher = termPattern.matcher(normalizedText);

        while (matcher.find()) {
            String term = matcher.group(1).trim();


            String termBlock = fixGradeOnSeparateLine(matcher.group(2).trim());
            String program = extractProgramAndPlan(termBlock);
            String coOp = termBlock.toLowerCase().contains("co-op") ? "Yes" : "No";

            double termUnits = 0.0;
            double termGpa = 0.0;
            Matcher totalsMatcher = Pattern.compile(
                    "Term Totals\\s+\\d{1,2}\\.\\d{1,2}/\\d{1,2}\\.\\d{1,2}\\s+(\\d+\\.\\d+)\\s+\\d+\\.\\d+\\s+(\\d+\\.\\d+)"
            ).matcher(termBlock);
            if (totalsMatcher.find()) {
                termUnits = Double.parseDouble(totalsMatcher.group(1));
                termGpa = Double.parseDouble(totalsMatcher.group(2));
            }

            String[] lines = termBlock.split("(?<=\\s)(?=[A-Z]{2,}\\s+\\d[A-Z0-9]{3,4}\\b)");

            for (String rawLine : lines) {
                String line = rawLine.trim();
                if (line.isEmpty()) continue;

                String[] tokens = line.split("\\s+");
                if (tokens.length < 2) {
                    continue;
                }

                // Extract course code
                String courseCode = tokens[0] + " " + tokens[1];

                // Fallback matcher to extract units and grade


                Matcher fallback = Pattern.compile(
                        "(\\d{1,2}\\.\\d{1,2})\\s+(A\\+|A-|A|B\\+|B-|B|C\\+|C-|C|D\\+|D-|D|F|COM|MT|WD|T|PASS|FAIL)(?!\\S)"
                ).matcher(line);

                String extractedUnits = "";
                String extractedGrade = "";
                if (fallback.find()) {
                    extractedUnits = fallback.group(1).trim() + "/" + fallback.group(1).trim(); // assume equal attempted/earned
                    extractedGrade = fallback.group(2).trim().toUpperCase();
                }


                boolean isUnitsValid = extractedUnits.matches("\\d+(\\.\\d+)?/\\d+(\\.\\d+)?");
                boolean isGradeValid = ALLOWED_GRADES.contains(extractedGrade);

                if (!isGradeValid && !isUnitsValid) {
                   continue;
                }

                // Don't skip if grade is valid even if units = 0
                if (safeParseUnits(extractedUnits) == 0.0 && !isGradeValid) {

                    continue;
                }

                // Extract title (excluding units & grade tokens)
                StringBuilder titleBuilder = new StringBuilder();
                for (int j = 2; j < tokens.length; j++) {
                    if (tokens[j].equals(extractedUnits) || tokens[j].equals(extractedGrade)) break;
                    titleBuilder.append(tokens[j]).append(" ");
                }
                String courseTitle = titleBuilder.toString().trim();


                TranscriptData data = new TranscriptData();
                data.setStudentId(studentId);
                data.setTranscriptId(transcriptId.toString());
                data.setTerm(term);
                data.setProgram(program);
                data.setCoOp(coOp);
                data.setUnits(String.format("%.2f", termUnits));
                data.setGpa(String.format("%.2f", termGpa));
                data.setTotalGpa(""); // will fill later
                data.setCourseCode(courseCode);
                data.setCourseTitle(courseTitle);
                data.setGrade(extractedGrade);
                data.setCoursesTaken(null);
                data.setGrades(null);

                allCourseRows.add(data);
            }
        }

        // Compute overall GPA
        double totalGpa = allCourseRows.stream()
                .mapToDouble(r -> safeParseDouble(r.getGpa()))
                .filter(g -> g > 0)
                .average().orElse(0.0);
        String totalGpaStr = String.format("%.2f", totalGpa);
        allCourseRows.forEach(row -> row.setTotalGpa(totalGpaStr));

        return allCourseRows;
    }

    // === Utility ===

    private static double safeParseUnits(String unitsStr) {
        try {
            if (unitsStr.contains("/")) {
                return Double.parseDouble(unitsStr.split("/")[1]);
            }
        } catch (Exception e) {
            return 0.0;
        }
        return 0.0;
    }

    private static String fixGradeOnSeparateLine(String block) {
        return block.replaceAll("(?is)Grade\\s+([A-Za-z+\\-]+)", "$1");
    }

    private static String extractProgramAndPlan(String block) {
        String program = extractSingleLine(block, "Program:\\s*(.+)");
        String plan = extractSingleLine(block, "Plan:\\s*(.+)");
        if ("N/A".equals(program) && "N/A".equals(plan)) return "N/A";
        return (program + " | " + plan).replace("N/A", "").trim();
    }

    private static String extractSingleLine(String text, String regex) {
        Matcher m = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(text);
        return m.find() ? m.group(1).trim() : "N/A";
    }

    private static double safeParseDouble(String s) {
        try { return Double.parseDouble(s); } catch (Exception e) { return 0.0; }
    }

    private static String normalizePdfText(String raw) {
        return raw.replaceAll("\\p{Z}+", " ")
                .replaceAll("[\\r\\n]+", " ")
                .replaceAll("\\s{2,}", " ")
                .trim();
    }
}
