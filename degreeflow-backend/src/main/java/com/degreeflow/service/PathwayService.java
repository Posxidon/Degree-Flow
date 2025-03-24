package com.degreeflow.service;

import com.degreeflow.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class PathwayService {
    /**
     * calls mosaic api
     * @param url - given api url to call
     * @return a json object as response
     */
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
    // currently unfunctional prereq parser
    public List<List<List<String>>> parseReq(String desc){
        System.out.println("desc");
        System.out.println(desc);
        List<String> desc_lst = List.of(desc.replaceAll("\n"," ").replaceAll("[^A-Za-z0-9 ]", "").split("\\s+"));
        List<List<String>> preReqList = new ArrayList<>();
        List<String> antiReqList = new ArrayList<>();
        List<String> currentPrereq = new ArrayList<>();
        boolean isPreReq = false;
        boolean isAntiReq = false;
        boolean isOr = false;
        String program = "";
        for (int i=0; i<desc_lst.size(); i++){
            String wrd = desc_lst.get(i);
            if (wrd.toLowerCase().contains("prerequisite")){
                isPreReq = true;
                isAntiReq = false;
            }else if (wrd.toLowerCase().contains("antirequisite")){
                isAntiReq = true;
                isPreReq = false;
                if (currentPrereq.size() > 0){
                    preReqList.add(currentPrereq);
                }
                program = "";
            }else if (isPreReq) {
                if (Objects.equals(wrd.toLowerCase(),"or")){
                    isOr = true;
                    String next_wrd = desc_lst.get(i+1);
                    if(next_wrd.length() == 4 && Character.isDigit(next_wrd.charAt(0))){
                        currentPrereq.add(program.concat(next_wrd));
                    }else if(!(Objects.equals(next_wrd, next_wrd.toUpperCase()) && next_wrd.replaceAll("[0-9]","").length() >= 4)){
                        isOr = false;
                    }
                } else if (Objects.equals(wrd, wrd.toUpperCase()) && wrd.replaceAll("[0-9]","").length() >= 4) {
                    if (!isOr && currentPrereq.size()>0){
                        preReqList.add(new ArrayList<>(currentPrereq));
                        currentPrereq = new ArrayList<>();
                    }
                    program = wrd;
                    int cnt = 1;
                    boolean added = true;
                    while (desc_lst.size() - i > cnt && added) {
                        added = false;
                        String next_wrd = desc_lst.get(i + cnt);
                        if (next_wrd.length() == 4 && Character.isDigit(next_wrd.charAt(0))) {
                            currentPrereq.add(program.concat(next_wrd));
                            added = true;
                        }
                        cnt ++;
                    }
                    i += cnt-1;
                }
            }else if(isAntiReq){
                if (Objects.equals(wrd, wrd.toUpperCase()) && wrd.replaceAll("[0-9]","").length() >= 4) {
                    program = wrd;
                    int cnt = 1;
                    boolean added = true;
                    while (desc_lst.size() - i > cnt && added) {
                        added = false;
                        String next_wrd = desc_lst.get(i + cnt);
                        if (next_wrd.length() == 4 && Character.isDigit(next_wrd.charAt(0))) {
                            antiReqList.add(program.concat(next_wrd));
                            added = true;
                        }
                        cnt ++;
                    }
                    i+= cnt-1;
                }
            }
        }
        List<List<List<String>>> ret = new ArrayList<>();
        List<List<String>> anti = new ArrayList<>();
        anti.add(antiReqList);
        ret.add(preReqList);
        ret.add(anti);
        return ret;
    }

    /**
     * parse all the information at a given course
     * @param url - url of the given course
     * @return a list of course(s) at the given url. NOTE the reason why it is a list and not a CourseNode is due to the potential that multiple courses could exist under one course code (multiyear course)
     */
    public List<CourseNode> parseCourse(String url){
        JSONObject cJson = makeMosaicApiCall(url);
        List<CourseNode> cns = new ArrayList<>();
        //for every course in the catalogue, if it has information for a subject code, catalogue number, title and a description, parse it
        for (int i = 0; i<cJson.getJSONArray("courses").length(); i++) {
            JSONObject course = cJson.getJSONArray("courses").getJSONObject(i);
            if(course.keySet().contains("subjectCode") &&course.keySet().contains("catalogNumber")&&course.keySet().contains("title")&&course.keySet().contains("longDescription")) {
                String catN = course.getString("catalogNumber");
                //parse year based on catalogue number
                //NOTE this year value will most likely be overwritten in parseDegree via the changeCourseYear function so this is just in case the function somehow misses it
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
//                List<List<List<String>>> reqs = parseReq(course.getString("longDescription"));
//                cns.add(new CourseNode(c, year, reqs.get(0),reqs.get(1).get(0)));
                cns.add(new CourseNode(c, year, new ArrayList<>(),new ArrayList<>()));
            }
        }
        return cns;
    }
    /**
     * parses a course list given a url
     * @param url - url of the given course list
     * @return a list of course nodes that represents the course list
     */
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
    /**
     * parses all the item details of a requirement item and maps the relationship
     * @param itemDetails - list of item details of a given requirement item
     * @return a list of course nodes that represents the combination of the item details
     */
    public List<CourseNode> parseItemDetail(JSONArray itemDetails){
        List<CourseNode> courseList = new ArrayList<>();
        // iterate through all item details with a item detail type of 'course list'
        for (int i=0;i<itemDetails.length();i++){
            JSONObject itemDetail = itemDetails.getJSONObject(i);
            if (itemDetail.keySet().contains("itemDetailType")){
                if (Objects.equals(itemDetail.getString("itemDetailType").toLowerCase(),"course list")){
                    // grabs all the courses in the item detail and implement the logic stated in the list include mode
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
                    } else {
                        courseList.addAll(cl);
                    }
                }
            }
        }
        return courseList;
    }
    /**
     * parse all the requirement items in a requirement section
     * @param reqItems - the json array of requirement items
     * @return a level group which represents a combination of all the requirement items aka represents the requirement
     */
    public LevelGroup parseReqItems(JSONArray reqItems){
        int reqL = reqItems.length();
        // for id purposes on front end
        int electiveCnt = 0;
        if (reqL > 0) {
            LevelGroup lg = new LevelGroup(new ArrayList<>());
            //for every requirement item, if it is a course requirement and it is not a requirement on transfer credits or level 1 course restrictions
            for (int i = 0; i<reqL; i++){
                JSONObject reqItem = reqItems.getJSONObject(i);
                boolean isCourseReq = Objects.equals(reqItem.getString("type").toLowerCase(),"course requirement");
                boolean isTCM = Objects.equals(reqItem.getString("shortDescription").toLowerCase(),"transfer credit maximum");
                boolean isL1R = Objects.equals(reqItem.getString("shortDescription").toLowerCase(),"level 1 course restriction");
                if (isCourseReq && !isTCM && ! isL1R) {
                    int amtReq = 0;
                    // calculate amount of courses required
                    // NOTE TODO this is only a temporary placeholder and does NOT accurately depict the amount of courses required
                    // To accurately depict the amount required, you must go through all items in the course list and try to see how much credits each course
                    // in order to accurately obtain the amount required
                    if (reqItem.keySet().contains("minimumUnits")){
                        amtReq = reqItem.getInt("minimumUnits") / 3;
                    }else if(reqItem.keySet().contains("minimumCourses")){
                        amtReq = reqItem.getInt("minimumCourses");
                    }
                    // if it has the word elective in the description, then we do not parse it and instead replace it with a placeholder course
                    // i do this for performance options as electives are like all the courses offered in McMaster
                    if (reqItem.getString("shortDescription").toLowerCase().contains("elective")){
                        List<CourseNode> electives = new ArrayList<>();
                        for (int v=0;v<amtReq;v++){
                            Course course = new Course(reqItem.getString("shortDescription").concat(Integer.toString(electiveCnt).concat("Year")),"elective","elective");
                            electives.add(new CourseNode(course,1,new ArrayList<>(),new ArrayList<>()));
                            electiveCnt ++;
                        }
                        List<CourseGroup> es = new ArrayList<>();
                        es.add(new CourseGroup(electives,amtReq));
                        List<Boolean> reqs = new ArrayList<>();
                        reqs.add(true);
                        lg.addReqGrp(new RequirementGroup(es,reqs));
                    // if the requirement item has an and connector, then the items are not optional and should be added as a required course group
                    // else if it is an or, then they are optional and should be added as a non required group
                    }else if (reqItem.keySet().contains("connector")) {
                        if (Objects.equals(reqItem.getString("connector").toLowerCase(), "and")) {
                            lg.getReqGrp().get(lg.getReqGrp().size()-1).addCourseGroup(new CourseGroup(parseItemDetail(reqItem.getJSONArray("itemDetails")),amtReq),true);
                        } else if (Objects.equals(reqItem.getString("connector").toLowerCase(), "or")){
                            lg.getReqGrp().get(lg.getReqGrp().size()-1).addCourseGroup(new CourseGroup(parseItemDetail(reqItem.getJSONArray("itemDetails")),amtReq),false);
                        }
                    //if it has no connectors and is not an elective, then we add it as a normal requirement group
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
    /**
     * changes all the years information of all courses in a given level group
     * @param lg - chosen courses in a level group to change
     * @param year - chosen year to change all courses to
     * @return the levelgroup with all the courses being changed to the same year
     */
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

/**
 * parse a given degree and returns the requirements for each year
 * @param degreeName - name of degree to parse
 * @return a list of level group object representing each year of the degree
 */
    public List<LevelGroup> parseDegree(String degreeName){
        String baseuUrl = "https://api.mcmaster.ca/academic-calendar/v2/plans/%s/requirement-groups";
        List<LevelGroup> lgs = new ArrayList<>();
        JSONObject degreeResp = makeMosaicApiCall(String.format(baseuUrl,degreeName));
        JSONObject req = (JSONObject) degreeResp.getJSONArray("requirementGroups").get(0);
        JSONArray reqs = req.getJSONArray("requirements");
        //iterate through the requirement section of requirementGroups property
        for (int v = 0; v < reqs.length(); v++){
            JSONObject j = (JSONObject) reqs.get(v);
            //if the keys contain the connector, type and requirementItems keys then traverse it
            if (j.keySet().contains("type") && j.keySet().contains("requirementItems")) {
                //parse each item in requirement items
                if (Objects.equals(j.getString("type").toLowerCase(), "requirement")) {
                    JSONArray reqItems = j.getJSONArray("requirementItems");
                    LevelGroup lg = parseReqItems(reqItems);
                    String shortDesc = j.getString("shortDescription");
                    // changes all computer science 1 requirement courses to year 1
                    if(Objects.equals(shortDesc.toLowerCase(),"computer science i")){
                        System.out.println("level changed 1");
                        lg = changeCourseYear(lg,1);
                    //for all requirements with the description following the format 'level x' parse x and change all courses to year x
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
    /**
     * Parses a degree and generates a degree object
     * @param degreeName - name of the degree to parse
     * @return a degree object that represents the requirements of the degree
     */
    public Degree parseDegreePlan(String degreeName){
        System.out.println("fetched");
        return new Degree("cs1",parseDegree(degreeName));
    }
    //unfunctional get coop
    public Degree getCoop(){
        List<CourseGroup> coop = new ArrayList<>();
        List<CourseNode> coop1 = new ArrayList<>();
        List<CourseNode> coop2 = new ArrayList<>();
        Course c1 = new Course("coop","coop1","");
        Course c2 = new Course("coop","coop2","");
        coop1.add(new CourseNode(c1,1,new ArrayList<>(), new ArrayList<>()));
        coop2.add(new CourseNode(c2,2,new ArrayList<>(), new ArrayList<>()));
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
