package com.degreeflow.service;

import com.degreeflow.model.JsonSchedule;
import com.degreeflow.repository.DegreeRepository;
import com.degreeflow.repository.ScheduleRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SchedulingService {

    private final DegreeRepository degreeRepository;
    private final SeatAlertService seatAlertService;
    private final TimeTableScraperService timeTableScraperService;

    @Autowired
    public SchedulingService(DegreeRepository degreeRepository, SeatAlertService seatAlertService, TimeTableScraperService timeTableScraperService) {
        this.seatAlertService = seatAlertService;
        this.degreeRepository = degreeRepository;
        this.timeTableScraperService = timeTableScraperService;
    }

    public JSONObject getLatestScheduleJsonByUserId(String userId) {
        JSONObject response = new JSONObject();
        List<JsonSchedule> latest = degreeRepository.findByUserId(userId);
        if(latest.size()>0){
            System.out.println("is present");
            for (JsonSchedule js : latest){
                JSONObject schedule = new JSONObject(js.getJson());
                for (String year : schedule.keySet()){
                    JSONArray courses = schedule.getJSONArray(year);
                    JSONArray fallCourses = new JSONArray();
                    JSONArray winterCourses = new JSONArray();
                    for (int i=0;i<courses.length();i++){
                        JSONObject c = new JSONObject();
                        JSONObject courseObj = courses.getJSONObject(i);
                        System.out.println(courseObj);
                        String courseCode = courseObj.getString("courseCode");
                        String description = courseObj.getString("desc");
                        Integer years = courseObj.getInt("years");
                        c.put("courseCode",courseCode);
                        c.put("desc",description);
                        c.put("years",years);
                        List<String> l = new ArrayList<>();
                        l.add(courseCode);
                        boolean isFall = seatAlertService.courseExistsInTerm(courseCode,"Fall-2024");
                        try {
                            System.out.println("get time table");
                            System.out.println(timeTableScraperService.getOpenSeats("Fall-2024",l));
                        }catch (Exception e){
                            System.out.println("failed to get time");
                        }

                        if (isFall){
                            fallCourses.put(c);
                        }else {
                            winterCourses.put(c);
                        }
                    }
                    JSONObject yearDetail = new JSONObject();
                    yearDetail.put("fall",fallCourses);
                    yearDetail.put("winter",winterCourses);
                    response.put(year,yearDetail);
                }
            }
            return response;
        }else {
            return null;
        }
    }
}
