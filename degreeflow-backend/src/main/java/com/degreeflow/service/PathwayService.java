package com.degreeflow.service;

import com.degreeflow.model.*;
import netscape.javascript.JSObject;
import org.apache.logging.log4j.Level;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;


import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.cert.X509Certificate;
import java.util.*;

@Service
public class PathwayService {

    public List<List<String>> parsePrereq(String desc){
        List<List<String>> prereqs = new ArrayList<>();
        String regex = "[\\s]";
        String[] processed = desc.split(regex);
        boolean onPrereqs = false;
        boolean onOf = false;
        boolean onAnd = false;
        for (int i=0; i< processed.length; i++){

        }
        return null;
    }

    public JSONObject makeMosaicApiCall(String url){

        String httpResponse;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Ocp-Apim-Subscription-Key", "3da32390cf04415e91ed4feac51c9f00").header("secondary-key", "3da32390cf04415e91ed4feac51c9f00").build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            httpResponse = response.body();
        } catch (Exception e) {
            System.out.println("http failed");
            return null;
        }
        System.out.println(httpResponse);
        return new JSONObject(httpResponse);
    }

    public List<CourseNode> parseCourse(String url){
        JSONObject cJson = makeMosaicApiCall(url);
        List<CourseNode> cns = new ArrayList<>();

        for (int i = 0; i<cJson.getJSONArray("courses").length(); i++) {
            JSONObject course = cJson.getJSONArray("courses").getJSONObject(i);
            if(course.keySet().contains("subjectCode") &&course.keySet().contains("catalogNumber")&&course.keySet().contains("title")&&course.keySet().contains("longDescription")) {
                String catN = course.getString("catalogNumber");
                int year = 0;
                Course c = new Course(course.getString("subjectCode").concat(catN), course.getString("title"), course.getString("longDescription"));
                if (Objects.equals(catN.charAt(0), '1')) {
                    year = 1;
                } else if (Objects.equals(catN.charAt(0), '2')) {
                    year = 2;
                } else if (Objects.equals(catN.charAt(0), '3')) {
                    year = 3;
                } else if (Character.getNumericValue(catN.charAt(0)) >= 4) {
                    year = 4;
                }
                cns.add(new CourseNode(c, year, new ArrayList<>()));
            }
        }
        return cns;
    }

    public List<CourseNode> parseCourseList(String url){
        List<CourseNode> courses = new ArrayList<>();
        JSONArray courseList = makeMosaicApiCall(url).getJSONArray("courseListItems");
        for (int i=0;i<courseList.length();i++){
            String courseUrl = courseList.getJSONObject(i).getString("courseCatalogSearch");
            if (!courseUrl.contains("wildcard-search")){
                List<CourseNode> cns = parseCourse(courseUrl);
                courses.addAll(cns);
            }
        }
        return courses;
    }

    public List<CourseNode> parseItemDetail(JSONArray itemDetails){
        List<CourseNode> courseList = new ArrayList<>();
        for (int i=0;i<itemDetails.length();i++){
            JSONObject itemDetail = itemDetails.getJSONObject(i);
            if (itemDetail.keySet().contains("itemDetailType")){
                if (Objects.equals(itemDetail.getString("itemDetailType").toLowerCase(),"course list")){
                    List<CourseNode> cl = parseCourseList(itemDetail.getString("courseList"));
                    if (itemDetail.keySet().contains("listIncludeMode")){
                        if(itemDetail.getString("listIncludeMode").toLowerCase().contains("intersection")){
                            List<CourseNode> newcl = new ArrayList<>();
                            for (CourseNode cn : courseList){
                                for (CourseNode cn2 : cl){
                                    if (Objects.equals(cn.getName(),cn2.getName())){
                                        newcl.add(cn);
                                        break;
                                    }
                                }
                            }
                            courseList = newcl;
                        }else if (itemDetail.getString("listIncludeMode").toLowerCase().contains("union")){
                            for (CourseNode cn2 : cl){
                                boolean isIn = false;
                                for (CourseNode cn : courseList){
                                    if (Objects.equals(cn.getName(),cn2.getName())){
                                        isIn = true;
                                        break;
                                    }
                                }
                                if (!isIn){
                                    courseList.add(cn2);
                                }
                            }
                        }else if (itemDetail.getString("listIncludeMode").toLowerCase().contains("subtract")){
                            int l = courseList.size();
                            for (int v=0;v<l;v++){
                                for (CourseNode cn2 : cl){
                                    CourseNode cn = courseList.get(v);
                                    if (Objects.equals(cn.getName(),cn2.getName())){
                                        courseList.remove(cn);
                                        v--;
                                        l--;
                                    }
                                }
                            }
                        }
                    }
                    courseList.addAll(cl);
                }
            }
        }
        return courseList;
    }

    public LevelGroup parseReqItems(JSONArray reqItems){
        int reqL = reqItems.length();
        int electiveCnt = 0;
        if (reqL > 0) {
            LevelGroup lg = new LevelGroup(new ArrayList<>());
            for (int i = 0; i<reqL; i++){
                JSONObject reqItem = reqItems.getJSONObject(i);
                boolean isCourseReq = Objects.equals(reqItem.getString("type").toLowerCase(),"course requirement");
                boolean isTCM = Objects.equals(reqItem.getString("shortDescription").toLowerCase(),"transfer credit maximum");
                boolean isL1R = Objects.equals(reqItem.getString("shortDescription").toLowerCase(),"level 1 course restriction");
                if (isCourseReq && !isTCM && ! isL1R) {
                    int amtReq = 0;
                    if (reqItem.keySet().contains("minimumUnits")){
                        amtReq = reqItem.getInt("minimumUnits") / 3;
                    }else if(reqItem.keySet().contains("minimumCourses")){
                        amtReq = reqItem.getInt("minimumCourses");
                    }
                    if (Objects.equals(reqItem.getString("shortDescription").toLowerCase(),"electives")){
                        List<CourseNode> electives = new ArrayList<>();
                        for (int v=0;v<amtReq;v++){
                            Course course = new Course("elective".concat("Year").concat(Integer.toString(electiveCnt)),"elective","elective");
                            electives.add(new CourseNode(course,1,new ArrayList<>()));
                            electiveCnt ++;
                        }
                        List<CourseGroup> es = new ArrayList<>();
                        es.add(new CourseGroup(electives,amtReq));
                        List<Boolean> reqs = new ArrayList<>();
                        reqs.add(true);
                        lg.addReqGrp(new RequirementGroup(es,reqs));
                    }else if (reqItem.keySet().contains("connector")) {
                        if (Objects.equals(reqItem.getString("connector").toLowerCase(), "and")) {
                            lg.getReqGrp().get(lg.getReqGrp().size()-1).addCourseGroup(new CourseGroup(parseItemDetail(reqItem.getJSONArray("itemDetails")),amtReq),true);
                        } else {
                            lg.addReqGrp(new RequirementGroup(new ArrayList<>(),null));
                            lg.getReqGrp().get(lg.getReqGrp().size()-1).addCourseGroup(new CourseGroup(parseItemDetail(reqItem.getJSONArray("itemDetails")),amtReq),false);
                        }
                    } else {
                        lg.addReqGrp(new RequirementGroup(new ArrayList<>(),null));
                        lg.getReqGrp().get(lg.getReqGrp().size()-1).addCourseGroup(new CourseGroup(parseItemDetail(reqItem.getJSONArray("itemDetails")),amtReq),true);
                    }
                }
            }
            return lg;
        }
        return null;
    }

    public LevelGroup changeCourseYear(LevelGroup lg, int year){
        for (RequirementGroup rg : lg.getReqGrp()){
            for (CourseGroup cg : rg.getCourseGroups()){
                for (CourseNode cn : cg.getCourses()){
                    cn.setYear(year);
                }
            }
        }
        return lg;
    }

/*
 * Rounds a given number to a given number of digits.
 * @param subjectCode - the number to be rounded
 * @return a nested list of course groups. The outer list represents the list of requirements at each level. the inner list represents the list of course groups that satisfied each requirement;
 */
    public List<LevelGroup> parseDegree(String subjectCode){
        String baseuUrl = "https://api.mcmaster.ca/academic-calendar/v2/plans/%s/requirement-groups";
        List<LevelGroup> lgs = new ArrayList<>();
        JSONObject degreeResp = makeMosaicApiCall(String.format(baseuUrl,subjectCode));
        JSONObject req = (JSONObject) degreeResp.getJSONArray("requirementGroups").get(0);
        JSONArray reqs = req.getJSONArray("requirements");
        //iterate through the requirement section of requirementGroups
        for (int v = 0; v < reqs.length(); v++){
            JSONObject j = (JSONObject) reqs.get(v);
            //if the keys contain the connector, type and requirementItems keys then traverse it
            if (j.keySet().contains("type") && j.keySet().contains("requirementItems")) {
                //parse each item in requirement items
                if (Objects.equals(j.getString("type").toLowerCase(), "requirement")) {
                    JSONArray reqItems = j.getJSONArray("requirementItems");
                    LevelGroup lg = parseReqItems(reqItems);
                    String shortDesc = j.getString("shortDescription");
                    if(Objects.equals(shortDesc.toLowerCase(),"computer science i")){
                        System.out.println("level changed 1");
                        lg = changeCourseYear(lg,1);
                    }else if(shortDesc.toLowerCase().contains("level") && Character.isDigit(shortDesc.charAt(shortDesc.length()-1)) && shortDesc.length() == 7){
                        System.out.println("level changed");
                        System.out.println(shortDesc.charAt(shortDesc.length()-1));
                        lg = changeCourseYear(lg,Character.getNumericValue(shortDesc.charAt(shortDesc.length()-1)));
                    }
                    lgs.add(lg);
                }
            }
        }
        return lgs;
    }

    public Degree parseDegreePlan(String degreeName){
        makeMosaicApiCall("https://api.mcmaster.ca/academic-calendar/v2/courses/class-search?courseCode=104717&includeEquivalent=false");
        System.out.println("fetched");
        return new Degree("cs1",parseDegree("HCOMPSCICO"));
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
        List<RequirementGroup> c = new ArrayList<>();
        List<Boolean> reqs = new ArrayList<>();
        reqs.add(true);
        reqs.add(true);
        c.add(new RequirementGroup(coop,reqs));
        LevelGroup lg = new LevelGroup(c);
        List<LevelGroup> lgs = new ArrayList<>();
        lgs.add(lg);
        return new Degree("coops",lgs);
    }
}
