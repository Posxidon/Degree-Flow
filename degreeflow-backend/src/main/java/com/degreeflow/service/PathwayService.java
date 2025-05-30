package com.degreeflow.service;

import com.degreeflow.model.*;
import com.degreeflow.repository.DegreeRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class PathwayService {
    private final DegreeRepository degreeRepository;

    @Autowired
    public PathwayService(DegreeRepository degreeRepository) {
        this.degreeRepository = degreeRepository;
    }
    /**
     * calls mosaic api
     * @param url - given api url to call
     * @return a json object as response
     */
    public JSONObject makeMosaicApiCall(String url, boolean toPrint){

        String httpResponse;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
                    .header("Ocp-Apim-Subscription-Key", "3da32390cf04415e91ed4feac51c9f00")
                    .header("secondary-key", "3da32390cf04415e91ed4feac51c9f00").build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            httpResponse = response.body();
        } catch (Exception e) {
            System.out.println("http failed");
            return null;
        }
        if (toPrint) {
            System.out.println("json text");
            System.out.println(httpResponse);
            System.out.println(httpResponse.charAt(0));
        }
        return new JSONObject(httpResponse);
    }

    /**
     * parse all the information at a given course
     * @param url - url of the given course
     * @return a list of course(s) at the given url. NOTE the reason why it is a list and not a CourseNode is due to the potential that multiple courses could exist under one course code (multiyear course)
     */
    public List<CourseNode> parseCourse(String url){
        JSONObject cJson = makeMosaicApiCall(url,false);
        if (cJson == null){
            return null;
        }
        List<CourseNode> cns = new ArrayList<>();
        //for every course in the catalogue, if it has information for a subject code, catalogue number, title and a description, parse it
        for (int i = 0; i<cJson.getJSONArray("courses").length(); i++) {
            JSONObject course = cJson.getJSONArray("courses").getJSONObject(i);
            if(course.keySet().contains("subjectCode")
                    &&course.keySet().contains("catalogNumber")
                    &&course.keySet().contains("title")
                    &&course.keySet().contains("longDescription")) {
                String catN = course.getString("catalogNumber");
                //parse year based on catalogue number
                //NOTE this year value will most likely be overwritten in parseDegree via the changeCourseYear function so this is just in case the function somehow misses it
                int year = 0;
                Course c = new Course(course.getString("subjectCode").concat(catN),
                        course.getString("title"),
                        course.getString("longDescription"));
                if (Objects.equals(catN.charAt(0), '1')) {
                    year = 1;
                } else if (Objects.equals(catN.charAt(0), '2')) {
                    year = 2;
                } else if (Objects.equals(catN.charAt(0), '3')) {
                    year = 3;
                } else if (Character.getNumericValue(catN.charAt(0)) >= 4) {
                    year = 4;
                }
                CourseNode cn = new CourseNode(c, year, new ArrayList<>(),new ArrayList<>());
                cn.setUnit((int) course.getDouble("minimumUnits"));
                cns.add(cn);
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
         JSONObject json= makeMosaicApiCall(url,false);
        if (json == null){
            return new ArrayList<>();
        }
        JSONArray courseList = json.getJSONArray("courseListItems");
        for (int i=0;i<courseList.length();i++){
            String courseUrl = courseList.getJSONObject(i).getString("courseCatalogSearch");
            if (!courseUrl.contains("wildcard-search")){
                List<CourseNode> cns = parseCourse(courseUrl);
                if (cns != null) {
                    courses.addAll(cns);
                }
            }
        }
        return courses;
    }

    /**
     * calculates the intersection between two course lists
     * @param cl1 - course list 1
     * @param cl2 - course list to be intersected with course list 1
     * @return - intersected course list between cl1 and cl2
     */
    public List<CourseNode> courseIntersection(List<CourseNode> cl1, List<CourseNode> cl2){
        if (cl1 == null){
            return new ArrayList<>();
        }
        if (cl2 == null){
            return new ArrayList<>();
        }
        if (cl1.size() == 0 || cl2.size() == 0){
            return new ArrayList<>();
        }
        List<CourseNode> newcl = new ArrayList<>();
        for (CourseNode cn : cl1){
            for (CourseNode cn2 : cl2){
                if (Objects.equals(cn.getName(),cn2.getName())){
                    newcl.add(cn);
                    break;
                }
            }
        }
        return newcl;
    }

    /**
     * unions two course lists
     * order of input does not matter
     * @param cl1 - course list 1
     * @param cl2 - course list to be unionised with course list 1
     * @return - unionised list of two course lists
     */
    public List<CourseNode> courseUnion(List<CourseNode> cl1, List<CourseNode> cl2){
        if (cl1 == null){
            return new ArrayList<>();
        }
        if (cl2 == null){
            return new ArrayList<>();
        }
        if (cl1.size() == 0 ){
            return cl2;
        }
        if (cl2.size() == 0 ){
            return cl1;
        }
        for (CourseNode cn2 : cl2){
            boolean isIn = false;
            for (CourseNode cn : cl1){
                if (Objects.equals(cn.getName(),cn2.getName())){
                    isIn = true;
                    break;
                }
            }
            if (!isIn){
                cl1.add(cn2);
            }
        }
        return cl1;
    }

    /**
     * subtract course list 2 from course list 1
     * Order DOES matter
     * @param cl1 - course list 1
     * @param cl2 - course list 2 which will be subtracted from course list 1
     * @return - result of course list 1 - course list 2
     */
    public List<CourseNode> courseSubtract(List<CourseNode> cl1, List<CourseNode> cl2){
        if (cl1 == null){
            return new ArrayList<>();
        }
        if (cl2 == null){
            return new ArrayList<>();
        }
        if (cl1.size() == 0 ){
            return new ArrayList<>();
        }
        if (cl2.size() == 0){
            return cl1;
        }
        int l = cl1.size();
        for (int v=0;v<l;v++){
            for (CourseNode cn2 : cl2){
                CourseNode cn = cl1.get(v);
                if (Objects.equals(cn.getName(),cn2.getName())){
                    cl1.remove(cn);
                    v--;
                    l--;
                }
            }
        }
        return cl1;
    }
    /**
     * parses all the item details of a requirement item and maps the relationship
     * order of input does not matter
     * @param itemDetails - list of item details of a given requirement item
     * @return a list of course nodes that represents the combination of the item details
     */
    public List<CourseNode> parseItemDetail(JSONArray itemDetails){
//        System.out.println(itemDetails);
        List<CourseNode> courseList = new ArrayList<>();
        // iterate through all item details with a item detail type of 'course list'
        for (int i=0;i<itemDetails.length();i++){
            JSONObject itemDetail = itemDetails.getJSONObject(i);
            if (itemDetail.keySet().contains("itemDetailType")){
                if (Objects.equals(itemDetail.getString("itemDetailType").toLowerCase(),"course list")){
                    // grabs all the courses in the item detail and implement the logic stated in the list include mode
                    List<CourseNode> cl = parseCourseList(itemDetail.getString("courseList"));
                    if (itemDetail.keySet().contains("listIncludeMode")){
                        //performs intersection between two course lists
                        if(itemDetail.getString("listIncludeMode").toLowerCase().contains("intersection")){
                            courseList = courseIntersection(courseList,cl);
                        // performs union between two course lists
                        }else if (itemDetail.getString("listIncludeMode").toLowerCase().contains("union")){
                            courseList = courseUnion(courseList, cl);
                        //subtracts the current course list from the previously obtained course list
                        }else if (itemDetail.getString("listIncludeMode").toLowerCase().contains("subtract")){
                            courseList = courseSubtract(courseList,cl);
                        }
                    //if there are no operations then add it all
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
     * @param includeTechElec - option to include list of tech electives instead of generalizing
     * @return a level group which represents a combination of all the requirement items aka represents the requirement
     */
    public LevelGroup parseReqItems(JSONArray reqItems, boolean includeTechElec){
//        System.out.println(reqItems);
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
                    // NOTE TODO this is only an estimate and does NOT accurately depict the amount of courses required
                    // To accurately depict the amount required, you must go through all items in the course list and try to see how much credits each course
                    // in order to accurately obtain the amount required
                    int minUnit = 0;
                    if (reqItem.keySet().contains("minimumUnits")){
                        amtReq = reqItem.getInt("minimumUnits") / 3;
                        minUnit = reqItem.getInt("minimumUnits");
                    }else if(reqItem.keySet().contains("minimumCourses")){
                        amtReq = reqItem.getInt("minimumCourses");
                    }
                    // if it has the word elective in the description, then we do not parse it and instead replace it with a placeholder course
                    // i do this for performance as electives are like all the courses offered in McMaster
                    if (reqItem.getString("shortDescription").toLowerCase().contains("elective") && (!includeTechElec || !(reqItem.getString("shortDescription").toLowerCase().contains("science") || reqItem.getString("shortDescription").toLowerCase().contains("technical")))){
                        List<CourseNode> electives = new ArrayList<>();
                        for (int v=0;v<amtReq;v++){
                            Course course = new Course(reqItem.getString("shortDescription").concat(Integer.toString(electiveCnt)),"elective","elective");
                            electives.add(new CourseNode(course,1,new ArrayList<>(),new ArrayList<>()));
                            electiveCnt ++;
                        }
                        List<CourseGroup> es = new ArrayList<>();
                        CourseGroup cg = new CourseGroup(electives,amtReq);
                        cg.setName(reqItem.getString("shortDescription"));
                        es.add(cg);
                        List<Boolean> reqs = new ArrayList<>();
                        reqs.add(true);
                        lg.addReqGrp(new RequirementGroup(es,reqs));
                    // if the requirement item has an 'and' connector, then the items are not optional and should be added as a required course group
                    // else if it is an 'or', then they are optional and should be added as a non required group
                    }else if (reqItem.keySet().contains("connector")) {
                        List<CourseNode> courseL = parseItemDetail(reqItem.getJSONArray("itemDetails"));
                        if (amtReq > courseL.size()){
                            amtReq = courseL.size();
                        }
                        CourseGroup cg = new CourseGroup(courseL,amtReq);
                        cg.setName(reqItem.getString("shortDescription"));
                        cg.setMinUnit(minUnit);
                        if (Objects.equals(reqItem.getString("connector").toLowerCase(), "and")) {
                            lg.getReqGrp().get(lg.getReqGrp().size()-1).addCourseGroup(cg,true);
                        } else if (Objects.equals(reqItem.getString("connector").toLowerCase(), "or")){
                            lg.getReqGrp().get(lg.getReqGrp().size()-1).addCourseGroup(cg,false);
                        }
                    //if it has no connectors and is not an elective, then we add it as a normal requirement group
                    } else {
                        lg.addReqGrp(new RequirementGroup(new ArrayList<>(),null));
                        List<CourseNode> courseL = parseItemDetail(reqItem.getJSONArray("itemDetails"));
                        if (amtReq > courseL.size()){
                            amtReq = courseL.size();
                        }
                        lg.getReqGrp().get(lg.getReqGrp().size()-1).addCourseGroup(new CourseGroup(courseL,amtReq),true);
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
 * @param includeTechElec - option to include list of tech electives instead of treating it as a regular elective
 * @return a list of level group object representing each year of the degree
 */
    public Degree parseDegree(String degreeName, boolean includeTechElec){
        String baseUrl = "https://api.mcmaster.ca/academic-calendar/v2/plans/%s/requirement-groups";
        List<LevelGroup> lgs = new ArrayList<>();
        JSONObject degreeResp = makeMosaicApiCall(String.format(baseUrl,degreeName),false);
//        System.out.println(degreeResp);
        if (degreeResp == null){
            return null;
        }
        if (!degreeResp.keySet().contains("requirementGroups")){
            return null;
        }
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
                    LevelGroup lg = parseReqItems(reqItems,includeTechElec);
                    String shortDesc = j.getString("shortDescription");
                    // changes all computer science 1 requirement courses to year 1
                    if(Objects.equals(shortDesc.toLowerCase(),"computer science i")){
//                        System.out.println("level changed 1");
                        lg = changeCourseYear(lg,1);
                    //for all requirements with the description following the format 'level x' parse x and change all courses to year x
                    }else if(shortDesc.toLowerCase().contains("level") && Character.isDigit(shortDesc.charAt(shortDesc.length()-1)) && shortDesc.length() == 7){
//                        System.out.println("level changed");
//                        System.out.println(shortDesc.charAt(shortDesc.length()-1));
                        lg = changeCourseYear(lg,Character.getNumericValue(shortDesc.charAt(shortDesc.length()-1)));
                    }
                    lgs.add(lg);
                }
            }
        }

        return new Degree(req.getString("description"),lgs);
    }
    /**
     * Parses a degree and generates a degree object
     * @param degreeName - name of the degree to parse
     * @param includeTechElec - option to include list of tech electives instead of generalizing
     * @return a degree object that represents the requirements of the degree
     */
    public Degree parseDegreePlan(String degreeName,boolean includeTechElec){
        System.out.println("fetched");
        return parseDegree(degreeName, includeTechElec);
    }

    /**
     * calls mosaic api to get list of degree names and plans
     * @return a list of all viable undergrad engineering degree codes and names (in order)
     */
    public List<List<String>> printCodes(){
        JSONObject main = makeMosaicApiCall("https://api.mcmaster.ca/academic-calendar/v2/plans",false);
        if (main == null){
            return null;
        }
        JSONArray plans = main.getJSONArray("plans");
        List<String> codes = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for (int i=0;i<plans.length();i++){
            JSONObject c = plans.getJSONObject(i);
            if (c.keySet().contains("program")) {
                JSONObject program = c.getJSONObject("program");
                JSONObject career = program.getJSONObject("career");
                JSONObject faculty = program.getJSONObject("faculty");
                String desc = c.getString("description");
                if (Objects.equals(career.getString("code").toLowerCase(), "ugrd") && Objects.equals(faculty.getString("shortDescription").toLowerCase(), "eng")) {
                    codes.add(c.getString("code"));
                    names.add(desc);
                }
            }
        }
        List<List<String>> r = new ArrayList<>();
        r.add(codes);
        r.add(names);
        return r;
    }

    /**
     * add json object to db
     * @param json - json string to be added to db
     * @param userId - user id of the matching json / schedule
     * @return - boolean indicating success status
     */
    public boolean addToDB(String json, String userId){
        // get list of all previous records and remove them
        List<JsonSchedule> prevRecords = degreeRepository.findByUserId(userId);
        System.out.println("adding to db");
        System.out.println(json);
        JsonSchedule schedule = new JsonSchedule();
        schedule.setJson(json);
        schedule.setUserId(userId);
        if (prevRecords.size()>0) {
            for (JsonSchedule s : prevRecords) {
                degreeRepository.delete(s);
            }
        }
        degreeRepository.save(schedule);
        return true;
    }
}
