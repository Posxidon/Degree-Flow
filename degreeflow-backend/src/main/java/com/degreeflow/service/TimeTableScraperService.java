package com.degreeflow.service;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

@Service
public class TimeTableScraperService {

    private static final Logger logger = Logger.getLogger(TimeTableScraperService.class.getName());

    private static final String BASE_URL = "https://mytimetable.mcmaster.ca/";
    private static final String LOGIN_URL = BASE_URL + "login.jsp";
    private static final String API_ENDPOINT = BASE_URL + "api/class-data";

    // McMaster wildcard endpoint
    private static final String WILDCARD_API_URL = "https://api.mcmaster.ca/academic-calendar/v2/courses/wildcard-search";

    // Subscription keys
// Subscription keys (hardcoded – move to secrets/env vars for production)
    private static final String PRIMARY_KEY = "3da32390cf04415e91ed4feac51c9f00";
    private static final String SECONDARY_KEY = "f86eee675259432cb3e367128453e9b6";

    // Caches the most recently fetched session ID
    // "volatile" so multiple threads see up-to-date value
    private volatile String cachedSessionId = null;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .followRedirects(true)
            .cookieJar(new InMemoryCookieJar())
            .build();

    /**
     * Fetches a fresh JSESSIONID from the Timetable system by simulating "Use as Guest".
     */
    private String fetchNewSessionId() throws IOException {
        ZonedDateTime systemTime = ZonedDateTime.now(ZoneId.of("America/Toronto"));
        String formattedTime = systemTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));
        logger.info("Adjusted System Time: " + formattedTime);

        Request loginPageRequest = new Request.Builder()
                .url(LOGIN_URL)
                .header("User-Agent", "Mozilla/5.0")
                .build();

        try (Response response = client.newCall(loginPageRequest).execute()) {
            if (!response.isSuccessful()) {
                logger.severe("Failed to load login page. HTTP Code: " + response.code());
                return null;
            }

            String newSessionId = null;
            for (Cookie cookie : client.cookieJar().loadForRequest(HttpUrl.get(BASE_URL))) {
                if ("JSESSIONID".equals(cookie.name())) {
                    newSessionId = cookie.value();
                    break;
                }
            }

            if (newSessionId == null) {
                logger.severe("JSESSIONID not found in cookies!");
                return null;
            }

            logger.info("New JSESSIONID retrieved: " + newSessionId);
            return newSessionId;
        }
    }

    /**
     * Convert a human-readable term (e.g. "Fall-2024") to the numeric code used by Timetable.
     */
    String formatTerm(String term) {
        switch (term) {
            case "Fall-2024":
                return "3202430";
            case "Winter-2025":
                return "3202510";
            case "Spring/Summer-2025":
                return "3202520";
            default:
                logger.severe("Error: Unsupported term format - " + term);
                return null;
        }
    }

    /**
     * Public method to fetch seat availability for a given term & list of courses.
     */
    public Map<String, Object> getOpenSeats(String term, List<String> courses) throws IOException {
        logger.info("Fetching seat availability for term: " + term + ", courses: " + courses);

        String resolvedTerm = formatTerm(term);
        if (resolvedTerm == null) {
            logger.severe("Invalid or unsupported term format: " + term);
            return Collections.emptyMap();
        }

        // Attempt the seat fetch once; if fails, try again after refreshing session
        Map<String, Object> seatData = doGetOpenSeats(resolvedTerm, courses, false);
        if (seatData.isEmpty()) {
            // Possibly we had an invalid session => retry with forceRefresh=true
            logger.warning("Seat data empty or failed; retrying once with forced session refresh...");
            seatData = doGetOpenSeats(resolvedTerm, courses, true);
        }

        return seatData;
    }

    /**
     * Helper that tries to fetch seat data, optionally forcing a fresh session ID.
     */
    private Map<String, Object> doGetOpenSeats(String resolvedTerm, List<String> courses, boolean forceRefresh) throws IOException {
        // If we must force a new session or we have none, fetch it
        if (forceRefresh || cachedSessionId == null) {
            cachedSessionId = fetchNewSessionId();
        }

        if (cachedSessionId == null) {
            logger.severe("Cannot fetch seat data without valid session.");
            return Collections.emptyMap();
        }

        // Build request
        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_ENDPOINT).newBuilder()
                .addQueryParameter("term", resolvedTerm);

        long nowSecs = System.currentTimeMillis() / 1000;
        long t = (nowSecs / 60) % 1000;
        long e = (t % 3) + (t % 39) + (t % 42);

        urlBuilder.addQueryParameter("t", String.valueOf(t));
        urlBuilder.addQueryParameter("e", String.valueOf(e));

        for (int i = 0; i < courses.size(); i++) {
            urlBuilder.addQueryParameter("course_" + i + "_0", courses.get(i));
        }

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .header("User-Agent", "Mozilla/5.0")
                .header("Cookie", "JSESSIONID=" + cachedSessionId)
                .build();

        logger.info("Sending seat-data request with JSESSIONID=" + cachedSessionId);

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                // Log & return empty
                logger.severe("Error fetching seat data: HTTP " + response.code());
                return Collections.emptyMap();
            }

            String responseBody = response.body().string();
            logger.info("Successfully fetched seat data!");
            return parseSeatData(responseBody);
        }
    }

    /**
     * The wildcard search method for "COMPSCI 3***", etc.
     */
    public List<Map<String, Object>> searchCoursesByWildcard(String subjectCode, String pattern) throws IOException {
        HttpUrl url = HttpUrl.parse(WILDCARD_API_URL)
                .newBuilder()
                .addQueryParameter("subjectCode", subjectCode)
                .addQueryParameter("catalogNumberPattern", pattern)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0")
                .header("Ocp-Apim-Subscription-Key", PRIMARY_KEY)
                .header("secondary-key", SECONDARY_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.severe("Error fetching wildcard courses: HTTP " + response.code());
                return Collections.emptyList();
            }

            String responseBody = response.body().string();
            logger.info("Wildcard search response for " + subjectCode + " " + pattern + ": " + responseBody);

            JSONObject root = new JSONObject(responseBody);
            if (!root.has("courses")) {
                return Collections.emptyList();
            }

            JSONArray courseArray = root.getJSONArray("courses");
            List<Map<String, Object>> results = new ArrayList<>();

            for (int i = 0; i < courseArray.length(); i++) {
                JSONObject courseJson = courseArray.getJSONObject(i);
                Map<String, Object> courseMap = new HashMap<>();

                courseMap.put("id", courseJson.optString("id"));
                courseMap.put("code", courseJson.optString("code"));
                courseMap.put("subjectCode", courseJson.optString("subjectCode"));
                courseMap.put("catalogNumber", courseJson.optString("catalogNumber"));
                courseMap.put("title", courseJson.optString("title"));
                courseMap.put("description", courseJson.optString("description"));
                courseMap.put("longDescription", courseJson.optString("longDescription"));

                results.add(courseMap);
            }
            return results;
        }
    }

    /**
     * Parse the seat-data XML from McMaster Timetable into a Map of course -> block details.
     */
    private Map<String, Object> parseSeatData(String xmlBody) {
        Map<String, Object> results = new HashMap<>();
        try {
            Document doc = Jsoup.parse(xmlBody, "", org.jsoup.parser.Parser.xmlParser());
            logger.info("Checking for available courses in XML...");

            Elements courses = doc.select("course");
            for (org.jsoup.nodes.Element course : courses) {
                String courseCode = course.attr("code") + " " + course.attr("number");
                logger.info("Found course: " + courseCode);

                Map<String, List<Map<String, Object>>> blockTypesMap = new HashMap<>();
                List<String> blockTypes = Arrays.asList("LEC", "TUT", "LAB", "SEM", "PRJ", "PRA", "FLD", "THE");
                blockTypes.forEach(bt -> blockTypesMap.put(bt, new ArrayList<>()));

                Elements blocks = course.select("block");
                for (org.jsoup.nodes.Element block : blocks) {
                    String type = block.attr("type");
                    String section = block.attr("secNo");
                    String key = block.attr("key");
                    int openSeats = Integer.parseInt(block.attr("os"));

                    if (blockTypes.contains(type)) {
                        Map<String, Object> blockData = new HashMap<>();
                        blockData.put("section", section);
                        blockData.put("key", key);
                        blockData.put("open_seats", openSeats);
                        blockTypesMap.get(type).add(blockData);
                    }
                }
                results.put(courseCode, blockTypesMap);
            }
        } catch (Exception e) {
            logger.severe("Error parsing XML response: " + e.getMessage());
        }

        logger.info("Final Parsed seat data: " + results);
        return results;
    }

    /**
     * Basic in-memory cookie store for requests.
     */
    static class InMemoryCookieJar implements CookieJar {
        private final Map<String, List<Cookie>> cookieStore = new HashMap<>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookieStore.put(url.host(), cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            return cookieStore.getOrDefault(url.host(), Collections.emptyList());
        }
    }
}
