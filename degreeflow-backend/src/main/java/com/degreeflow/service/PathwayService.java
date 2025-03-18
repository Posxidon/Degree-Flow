package com.degreeflow.service;

import com.degreeflow.model.Course;
import com.degreeflow.model.CourseGroup;
import com.degreeflow.model.CourseNode;
import com.degreeflow.model.Degree;
import netscape.javascript.JSObject;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.stereotype.Service;


import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class PathwayService {


    public Degree parseDegreePlan(int degreeID){
//        String httpResponse;
//        try {
//            HttpClient client = HttpClient.newHttpClient();
//            String url = "https://api.mcmaster.ca/academic-calendar/v2/courses/class-search?courseCode=115147";
//            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Ocp-Apim-Subscription-Key", "3da32390cf04415e91ed4feac51c9f00").header("secondary-key", "3da32390cf04415e91ed4feac51c9f00").build();
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            httpResponse = response.body();
//        }catch (Exception e){
//            System.out.println("http failed");
//            return null;
//        }
//        System.out.println(httpResponse);
//        JSONObject jsonObject = new JSONObject(httpResponse);
//        System.out.println((String)jsonObject.get("id"));
//        System.out.println(jsonObject.get("courses"));


        if (degreeID == 26811) {
            List<CourseGroup> cscg = new ArrayList<>();
            List<CourseNode> cs1csCourses = new ArrayList<>();

            List<CourseNode> cs1mCourses = new ArrayList<>();
            Course c1 = new Course("1zb3", "1", "");
            Course c2 = new Course("1za3", "1", "");
            CourseNode c1zb3 = new CourseNode(c1,1,new ArrayList<>());
            CourseNode c1za3 = new CourseNode(c2,1,new ArrayList<>());
            cs1mCourses.add(c1za3);
            cs1mCourses.add(c1zb3);

            CourseGroup cs1mathcg = new CourseGroup(cs1mCourses,1);
            CourseGroup cs1mathcs = new CourseGroup(cs1mCourses,2);


            List<CourseGroup> prereq2ac3 = new ArrayList<>();
            prereq2ac3.add(cs1mathcg);
            Course c3 = new Course("2AC3", "1", "");
            CourseNode c2ac3 = new CourseNode(c3,2,prereq2ac3);
            cs1csCourses.add(c2ac3);

            cscg.add(cs1mathcs);
            cscg.add(new CourseGroup(cs1csCourses, 1));

            return new Degree("Computer Science 1", cscg);
        }
        return null;
    }
    public Degree getCoop(){
        List<CourseGroup> coop = new ArrayList<>();
        List<CourseNode> coop1 = new ArrayList<>();
        List<CourseNode> coop2 = new ArrayList<>();
        Course c1 = new Course("coop","coop1","");
        Course c2 = new Course("coop","coop2","");
        coop1.add(new CourseNode(c1,1,null));
        coop2.add(new CourseNode(c2,2,null));
        coop.add(new CourseGroup(coop1,1));
        coop.add(new CourseGroup(coop2,1));
        return new Degree("coops",coop);
    }
    public List<CourseGroup> splitCourseGroup(CourseGroup cg, int courseSplitCnt){
        List<CourseNode> c1 = new ArrayList<>();
        List<CourseNode> c2 = new ArrayList<>();
        for (int i=0;i<cg.CourseCount();i++){
            if (i<courseSplitCnt){
                c1.add(cg.getCourses().get(i));
            }else {
                c1.add(cg.getCourses().get(i));
            }
        }
        List<CourseGroup> cgL = new ArrayList<>();
        cgL.add(new CourseGroup(c1,courseSplitCnt));
        cgL.add(new CourseGroup(c2,cg.getCourses().size()-courseSplitCnt));
        return cgL;
    }
}
