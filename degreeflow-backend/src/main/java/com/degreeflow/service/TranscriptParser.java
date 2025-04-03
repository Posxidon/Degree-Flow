package com.degreeflow.service;

import com.degreeflow.model.TranscriptData;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranscriptParser {

    /**
     * Whitelist of valid letter-grade tokens or special codes (A, A+, COM, T, WD, MT, etc.).
     * Anything not in here won't appear in the final 'grades' column.
     */
    private static final Set<String> ALLOWED_GRADES = new HashSet<>(Arrays.asList(
            "A", "A+", "A-", "B", "B+", "B-", "C", "C+", "C-", "D", "D+", "D-",
            "F", "COM", "MT", "WD", "T", "PASS", "FAIL"
    ));

    public static List<TranscriptData> parseTranscript(String pdfText, String studentId) {
        // 1) Normalize PDF text (weird whitespace => normal space, newlines => single space, etc.)
        String normalizedText = normalizePdfText(pdfText);

        List<TranscriptData> transcripts = new ArrayList<>();
        UUID transcriptId = UUID.randomUUID(); // If you need a single transcript ID globally

        // 2) Identify each term with a pattern like: --- 2025 Winter ---
        Pattern termPattern = Pattern.compile(
                "---\\s*(\\d{4}\\s+[A-Za-z/]+)\\s*---(.*?)(?=---\\s*\\d{4}\\s+[A-Za-z/]+\\s*---|$)",
                Pattern.DOTALL
        );
        Matcher matcher = termPattern.matcher(normalizedText);

        while (matcher.find()) {
            String termHeader = matcher.group(1).trim();  // e.g. "2025 Winter"
            String termBlock  = matcher.group(2).trim();

            // Merge "3.00/3.00 Grade\nA" => "3.00/3.00 A"
            termBlock = fixGradeOnSeparateLine(termBlock);

            // Build a TranscriptData object for this term
            TranscriptData transcript = new TranscriptData();
            transcript.setStudentId(studentId);
            transcript.setTerm(termHeader);
            transcript.setTranscriptId(transcriptId.toString());

            // Program & Plan lines
            transcript.setProgram(extractProgramAndPlan(termBlock));

            // Courses => skip if 0.00/0.00 units & grade is empty, remove leftover columns
            transcript.setCoursesTaken(extractCoursesNoEmptyNoColumns(termBlock));

            // Grades => only keep tokens that appear in ALLOWED_GRADES
            transcript.setGrades(extractValidGrades(termBlock));

            // Co-op detection
            transcript.setCoOp(termBlock.toLowerCase().contains("co-op") ? "Yes" : "No");

            // Parse "Term Totals" => sets units & GPA
            parseTermTotals(termBlock, transcript);

            transcripts.add(transcript);
        }

        // 3) Compute overall GPA ignoring zero-credit terms
        double sumGpa = 0.0;
        int countTerms = 0;
        for (TranscriptData t : transcripts) {
            double gpaVal   = safeParseDouble(t.getGpa());
            double unitsVal = safeParseDouble(t.getUnits());
            if (gpaVal > 0.0 && unitsVal > 0.0) {
                sumGpa += gpaVal;
                countTerms++;
            }
        }
        double overall = (countTerms > 0) ? sumGpa / countTerms : 0.0;
        String overallStr = String.format("%.2f", overall);
        for (TranscriptData t : transcripts) {
            t.setTotalGpa(overallStr);
        }

        return transcripts;
    }

    /**
     * Unify lines like "3.00/3.00 Grade\nA" => "3.00/3.00 A"
     */
    private static String fixGradeOnSeparateLine(String block) {
        return block.replaceAll("(?is)Grade\\s+([A-Za-z+\\-]+)", "$1");
    }

    /**
     * If "Term Totals 15.0/15.0 15.0 156.0 10.4" => set transcript.gpa & transcript.units
     * else => 0.00
     */
    private static void parseTermTotals(String block, TranscriptData t) {
        Pattern p = Pattern.compile(
                "Term Totals\\s+(\\d{1,2}\\.\\d{1,2}/\\d{1,2}\\.\\d{1,2})\\s+(\\d+\\.\\d+)\\s+(\\d+\\.\\d+)\\s+(\\d+\\.\\d+)",
                Pattern.CASE_INSENSITIVE
        );
        Matcher m = p.matcher(block);
        if (m.find()) {
            t.setUnits(m.group(2));
            t.setGpa(m.group(4));
        } else {
            t.setUnits("0.00");
            t.setGpa("0.00");
        }
    }

    /**
     * Extract "Program" & "Plan" => e.g. "Engineering | Computer Sci Co-op"
     */
    private static String extractProgramAndPlan(String block) {
        String program = extractSingleLine(block, "Program:\\s*(.+)");
        String plan    = extractSingleLine(block, "Plan:\\s*(.+)");
        if ("N/A".equals(program) && "N/A".equals(plan)) {
            return "N/A";
        }
        return (program + " | " + plan).replace("N/A", "").trim();
    }

    /**
     * Finds all "CODE + leftover" pairs. Then:
     *  - parse out units & grade
     *  - if (units=0.00/0.00 && grade="") => skip
     *  - remove leftover column headings
     *  - remove "units + grade" from the final leftover => produce "CODE|Title"
     */
    private static String extractCoursesNoEmptyNoColumns(String block) {
        Pattern pattern = Pattern.compile(
                "([A-Z]{2,}\\s+\\d[A-Z0-9]{3,4})\\s+((?:(?![A-Z]{2,}\\s+\\d[A-Z0-9]{3,4}).)+)",
                Pattern.DOTALL
        );
        Matcher matcher = pattern.matcher(block);

        List<String> courses = new ArrayList<>();
        while (matcher.find()) {
            String code     = matcher.group(1).trim();
            String leftover = matcher.group(2).trim();

            leftover = cleanupTitle(leftover);

            // parse units & grade
            String[] parsed = parseUnitsAndGrade(leftover);
            String units = parsed[0];
            String grade = parsed[1];

            // skip if zero units and no grade
            if (isZeroUnits(units) && grade.isEmpty()) {
                continue;
            }

            // remove leftover columns (like "Term Enrolment Attm./Earned Units", "Totals Attm./Earned Units" etc.)
            leftover = removeLeftoverColumns(leftover);

            // then remove "3.00/3.00 A" from leftover
            String finalTitle = removeUnitsAndGrade(leftover).trim();

            if (!finalTitle.isEmpty()) {
                courses.add(code + "|" + finalTitle);
            } else {
                // might still add if you want code only, but typically we skip if there's no leftover
                courses.add(code);
            }
        }
        return String.join("\n", courses);
    }

    /**
     * Remove transcript column headings such as:
     *   "Term Enrolment Attm./Earned Units"
     *   "Totals Attm./Earned Units GPA Units Total Points GPA Term Totals 15.0 129.0 8.6"
     */
    private static String removeLeftoverColumns(String text) {
        // remove big leftover headings
        // e.g. "Totals Attm./Earned Units GPA Units Total Points GPA Term Totals 15.0 129.0 8.6"
        text = text.replaceAll(
                "(?i)Totals\\s+Attm\\./Earned\\s+Units\\s+GPA\\s+Units\\s+Total\\s+Points\\s+GPA\\s+Term\\s+Totals.*",
                ""
        );
        // remove e.g. "Term Enrolment Attm./Earned Units"
        text = text.replaceAll("(?i)Term\\s+Enrolment\\s+Attm\\./Earned\\s+Units.*", "");

        text = text.replaceAll("\\s{2,}", " ").trim();
        return text;
    }

    /**
     * Removes parentheses, repeated spaces, etc.
     * You can also remove "title" or "grade" words if you want.
     */
    private static String cleanupTitle(String txt) {
        if (txt == null) return "";

        // Remove everything inside parentheses e.g. “(Work Term Details)”
        txt = txt.replaceAll("\\(.*?\\)", "");

        // Remove the literal word "title" (case-insensitive) if you want
        txt = txt.replaceAll("(?i)title", "");

        // Collapse multiple spaces into one
        txt = txt.replaceAll("\\s{2,}", " ");

        return txt.trim();
    }

    /**
     * Attempt to find "digits.digits/digits.digits + optional letter" => [units, grade].
     * E.g. "3.00/3.00 A+" => units="3.00/3.00", grade="A+"
     * or "0.00/0.00" => units="0.00/0.00", grade=""
     */
    private static String[] parseUnitsAndGrade(String leftover) {
        Pattern p = Pattern.compile("(\\d{1,2}\\.\\d{1,2}/\\d{1,2}\\.\\d{1,2})(?:\\s+([-A-Za-z+]+))?");
        Matcher m = p.matcher(leftover);

        String foundUnits = "";
        String foundGrade = "";
        if (m.find()) {
            foundUnits = m.group(1).trim();
            if (m.group(2) != null) {
                foundGrade = m.group(2).trim();
            }
        }
        return new String[]{foundUnits, foundGrade};
    }

    private static boolean isZeroUnits(String units) {
        // e.g. "0.00/0.00", "0/0", "0.0/0.0"
        return units.matches("0*\\.?0*/0*\\.?0*");
    }

    /**
     * remove "3.00/3.00 A" etc. from leftover so final title doesn't contain them
     */
    private static String removeUnitsAndGrade(String text) {
        // e.g. "Intro To Probability 3.00/3.00 A" => "Intro To Probability"
        text = text.replaceAll("\\d{1,2}\\.\\d{1,2}/\\d{1,2}\\.\\d{1,2}\\s+[-A-Za-z+]+", "");
        text = text.replaceAll("\\d{1,2}\\.\\d{1,2}/\\d{1,2}\\.\\d{1,2}", "");
        return text.replaceAll("\\s{2,}", " ").trim();
    }

    /**
     * Extract letter grades from the entire term block,
     * but only keep tokens in ALLOWED_GRADES (to skip "COMPSCI", "MUSIC", etc.).
     */
    private static String extractValidGrades(String block) {
        Pattern pattern = Pattern.compile("\\d{1,2}\\.\\d{1,2}/\\d{1,2}\\.\\d{1,2}\\s+([-A-Za-z+]+)");
        Matcher matcher = pattern.matcher(block);

        List<String> finalGrades = new ArrayList<>();
        while (matcher.find()) {
            String gradeToken = matcher.group(1).trim();
            if (ALLOWED_GRADES.contains(gradeToken.toUpperCase())) {
                finalGrades.add(gradeToken);
            }
        }
        return String.join(", ", finalGrades);
    }

    private static String extractSingleLine(String text, String regex) {
        Matcher m = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(text);
        if (m.find()) {
            return m.group(1).trim();
        }
        return "N/A";
    }

    private static double safeParseDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private static String normalizePdfText(String rawPdfText) {
        // Convert weird whitespace => normal spaces
        String noWeirdSpaces = rawPdfText.replaceAll("\\p{Z}+", " ");
        // Convert line breaks => single space
        String singleLine = noWeirdSpaces.replaceAll("[\\r\\n]+", " ");
        // Collapse multiple spaces => one
        return singleLine.replaceAll("\\s{2,}", " ").trim();
    }
}
