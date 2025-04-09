package com.degreeflow.service;
import com.degreeflow.model.*;
import com.degreeflow.repository.DegreeRepository;
import org.checkerframework.checker.units.qual.C;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class PathwayServiceTest {
    @InjectMocks
    private PathwayService pathwayService;

    @Mock
    private DegreeRepository degreeRepository;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testMakeMosaicApiCall(){
        // testing for valid links
        JSONObject response = pathwayService.makeMosaicApiCall("https://api.mcmaster.ca/academic-calendar/v2/plans",false);
        assertNotNull(response,null);

        // testing for invalid links
        response = pathwayService.makeMosaicApiCall("https://",false);
        assertNull(response,null);
    }

    @Test
    public void testParseCourse(){
        // testing valid links
        List<CourseNode> cns;
        cns = pathwayService.parseCourse("https://api.mcmaster.ca/academic-calendar/v2/courses/class-search?courseCode=104314&includeEquivalent=false");
        assertNotNull(cns);

        // testing invalid links
        cns = pathwayService.parseCourse("https://");
        assertNull(cns);
    }

    @Test
    public void testParseCourseList(){
        //testing valid links
        List<CourseNode> cns;
        cns = pathwayService.parseCourseList("https://api.mcmaster.ca/academic-calendar/v2/course-lists/000012963");
        assertNotEquals(cns.size(),0);

        //testing invalid links
        cns = pathwayService.parseCourseList("https://");
        assertEquals(cns.size(),0);
    }

    @Test
    public void testCourseIntersection(){
        List<CourseNode> cl;

        //testing the empty case
        List<CourseNode> cl1 = new ArrayList<>();
        List<CourseNode> cl2 = new ArrayList<>();
        cl = pathwayService.courseIntersection(cl1,cl2);
        assertEquals(cl.size(),0);

        //testing the null case
        cl = pathwayService.courseIntersection(null,null);
        assertEquals(cl.size(),0);

        //testing when cl1 is empty
        cl1 = new ArrayList<>();
        cl2 = new ArrayList<>();
        cl2.add(new CourseNode(new Course("","",""),0,null,null));
        cl = pathwayService.courseIntersection(cl1,cl2);
        assertEquals(cl.size(),0);

        //testing when cl2 is empty
        cl1 = new ArrayList<>();
        cl2 = new ArrayList<>();
        cl1.add(new CourseNode(new Course("","",""),0,null,null));
        cl = pathwayService.courseIntersection(cl1,cl2);
        assertEquals(cl.size(),0);

        //testing when cl1 and cl2 are not empty with unique courses
        cl1 = new ArrayList<>();
        cl2 = new ArrayList<>();
        cl1.add(new CourseNode(new Course("","",""),0,null,null));
        cl2.add(new CourseNode(new Course("1","",""),0,null,null));
        cl = pathwayService.courseIntersection(cl1,cl2);
        assertEquals(cl.size(),0);

        //testing when cl1 and cl2 are not empty with same courses
        cl1 = new ArrayList<>();
        cl2 = new ArrayList<>();
        cl1.add(new CourseNode(new Course("","",""),0,null,null));
        cl2.add(new CourseNode(new Course("","",""),0,null,null));
        cl = pathwayService.courseIntersection(cl1,cl2);
        assertEquals(cl.size(),1);

        //second test when cl1 and cl2 are not empty with same courses
        cl1 = new ArrayList<>();
        cl2 = new ArrayList<>();
        cl1.add(new CourseNode(new Course("","",""),0,null,null));
        cl1.add(new CourseNode(new Course("1","",""),0,null,null));
        cl1.add(new CourseNode(new Course("2","",""),0,null,null));
        cl2.add(new CourseNode(new Course("0","",""),0,null,null));
        cl2.add(new CourseNode(new Course("1","",""),0,null,null));
        cl2.add(new CourseNode(new Course("2","",""),0,null,null));
        cl = pathwayService.courseIntersection(cl1,cl2);
        assertEquals(cl.size(),2);
    }
    @Test
    public void testCourseUnion(){
        List<CourseNode> cl;

        //testing the empty case
        List<CourseNode> cl1 = new ArrayList<>();
        List<CourseNode> cl2 = new ArrayList<>();
        cl = pathwayService.courseUnion(cl1,cl2);
        assertEquals(cl.size(),0);

        //testing the null case
        cl = pathwayService.courseUnion(null,null);
        assertEquals(cl.size(),0);

        //testing when cl1 is empty
        cl1 = new ArrayList<>();
        cl2 = new ArrayList<>();
        cl2.add(new CourseNode(new Course("","",""),0,null,null));
        cl = pathwayService.courseUnion(cl1,cl2);
        assertEquals(cl.size(),1);

        //testing when cl2 is empty
        cl1 = new ArrayList<>();
        cl2 = new ArrayList<>();
        cl1.add(new CourseNode(new Course("","",""),0,null,null));
        cl = pathwayService.courseUnion(cl1,cl2);
        assertEquals(cl.size(),1);

        //testing when cl1 and cl2 are not empty with unique courses
        cl1 = new ArrayList<>();
        cl2 = new ArrayList<>();
        cl1.add(new CourseNode(new Course("","",""),0,null,null));
        cl2.add(new CourseNode(new Course("1","",""),0,null,null));
        cl = pathwayService.courseUnion(cl1,cl2);
        assertEquals(cl.size(),2);

        //testing when cl1 and cl2 are not empty with same courses
        cl1 = new ArrayList<>();
        cl2 = new ArrayList<>();
        cl1.add(new CourseNode(new Course("","",""),0,null,null));
        cl2.add(new CourseNode(new Course("","",""),0,null,null));
        cl = pathwayService.courseUnion(cl1,cl2);
        assertEquals(cl.size(),1);

        //second test when cl1 and cl2 are not empty with same courses
        cl1 = new ArrayList<>();
        cl2 = new ArrayList<>();
        cl1.add(new CourseNode(new Course("","",""),0,null,null));
        cl1.add(new CourseNode(new Course("1","",""),0,null,null));
        cl1.add(new CourseNode(new Course("2","",""),0,null,null));
        cl2.add(new CourseNode(new Course("0","",""),0,null,null));
        cl2.add(new CourseNode(new Course("1","",""),0,null,null));
        cl2.add(new CourseNode(new Course("2","",""),0,null,null));
        cl = pathwayService.courseUnion(cl1,cl2);
        assertEquals(cl.size(),4);
    }
    @Test
    public void testCourseSubtract(){
        List<CourseNode> cl;

        //testing the empty case
        List<CourseNode> cl1 = new ArrayList<>();
        List<CourseNode> cl2 = new ArrayList<>();
        cl = pathwayService.courseSubtract(cl1,cl2);
        assertEquals(cl.size(),0);

        //testing the null case
        cl = pathwayService.courseSubtract(null,null);
        assertEquals(cl.size(),0);

        //testing when cl1 is empty
        cl1 = new ArrayList<>();
        cl2 = new ArrayList<>();
        cl2.add(new CourseNode(new Course("","",""),0,null,null));
        cl = pathwayService.courseSubtract(cl1,cl2);
        assertEquals(cl.size(),0);

        //testing when cl2 is empty
        cl1 = new ArrayList<>();
        cl2 = new ArrayList<>();
        cl1.add(new CourseNode(new Course("","",""),0,null,null));
        cl = pathwayService.courseSubtract(cl1,cl2);
        assertEquals(cl.size(),1);

        //testing when cl1 and cl2 are not empty with unique courses
        cl1 = new ArrayList<>();
        cl2 = new ArrayList<>();
        cl1.add(new CourseNode(new Course("","",""),0,null,null));
        cl2.add(new CourseNode(new Course("1","",""),0,null,null));
        cl = pathwayService.courseSubtract(cl1,cl2);
        assertEquals(cl.size(),1);

        //testing when cl1 and cl2 are not empty with same courses
        cl1 = new ArrayList<>();
        cl2 = new ArrayList<>();
        cl1.add(new CourseNode(new Course("","",""),0,null,null));
        cl2.add(new CourseNode(new Course("","",""),0,null,null));
        cl = pathwayService.courseSubtract(cl1,cl2);
        assertEquals(cl.size(),0);

        //second test when cl1 and cl2 are not empty with same courses
        cl1 = new ArrayList<>();
        cl2 = new ArrayList<>();
        cl1.add(new CourseNode(new Course("","",""),0,null,null));
        cl1.add(new CourseNode(new Course("1","",""),0,null,null));
        cl1.add(new CourseNode(new Course("2","",""),0,null,null));
        cl2.add(new CourseNode(new Course("0","",""),0,null,null));
        cl2.add(new CourseNode(new Course("1","",""),0,null,null));
        cl2.add(new CourseNode(new Course("2","",""),0,null,null));
        cl = pathwayService.courseSubtract(cl1,cl2);
        assertEquals(cl.size(),1);
    }

    @Test
    public void testParseItemDetails(){
        // testing for case of valid json
        String jsonStr = "[{\"sequence\":\"1\",\"itemDetailType\":\"Course List\",\"courseList\":\"https://api.mcmaster.ca/academic-calendar/v2/course-lists/000000496\"}]";
        JSONArray js = new JSONArray(jsonStr);
        List<CourseNode> cl = pathwayService.parseItemDetail(js);
        assert(cl.size() > 0);

        // testing for case of empty json arr
        jsonStr = "[]";
        js = new JSONArray(jsonStr);
        cl = pathwayService.parseItemDetail(js);
        assertEquals(cl.size(), 0);
    }

    @Test
    public void testParseReqItems(){
        //testing for case of valid json
        String jsonStr = "[{\"sequence\":\"0010\",\"creditIncludeMode\":\"Include In All Statistics\",\"courseRankingScheme\":\"Chronological\",\"description\":\"Core Required Courses: 26 Units\",\"itemDetails\":[{\"sequence\":\"1\",\"itemDetailType\":\"Course List\",\"courseList\":\"https://api.mcmaster.ca/academic-calendar/v2/course-lists/000003999\",\"listInterp\":\"None\"}],\"minimumUnits\":26,\"shortDescription\":\"Core Required Courses\",\"maximumUnits\":26,\"type\":\"Course Requirement\"},{\"sequence\":\"0020\",\"creditIncludeMode\":\"Include In All Statistics\",\"connector\":\"AND\",\"courseRankingScheme\":\"Chronological\",\"description\":\"Chemistry Elective: 3-6 Units\\nCHEM 2E03 or both CHEM 2OA3 and 2OB3\",\"itemDetails\":[{\"sequence\":\"1\",\"itemDetailType\":\"Course List\",\"courseList\":\"https://api.mcmaster.ca/academic-calendar/v2/course-lists/000001905\",\"listInterp\":\"None\"}],\"minimumUnits\":3,\"shortDescription\":\"Chemistry Elective\",\"maximumUnits\":3,\"type\":\"Course Requirement\"},{\"sequence\":\"0030\",\"creditIncludeMode\":\"Include In All Statistics\",\"connector\":\"OR\",\"courseRankingScheme\":\"Chronological\",\"description\":\"Chemistry Elective: 3-6 Units\\nCHEM 2E03 or both CHEM 2OA3 and 2OB3\",\"itemDetails\":[{\"sequence\":\"1\",\"itemDetailType\":\"Course List\",\"courseList\":\"https://api.mcmaster.ca/academic-calendar/v2/course-lists/000001906\",\"listInterp\":\"None\"}],\"minimumUnits\":6,\"shortDescription\":\"Chemistry Elective\",\"maximumUnits\":6,\"type\":\"Course Requirement\"},{\"sequence\":\"0040\",\"creditIncludeMode\":\"Include In All Statistics\",\"connector\":\"AND\",\"courseRankingScheme\":\"Course Catalog\",\"description\":\"Statistics Course\",\"itemDetails\":[{\"sequence\":\"1\",\"itemDetailType\":\"Course List\",\"courseList\":\"https://api.mcmaster.ca/academic-calendar/v2/course-lists/000011939\",\"listInterp\":\"None\"}],\"minimumUnits\":3,\"shortDescription\":\"Statistics Course\",\"maximumUnits\":3,\"type\":\"Course Requirement\"},{\"sequence\":\"0050\",\"creditIncludeMode\":\"Include In All Statistics\",\"connector\":\"AND\",\"courseRankingScheme\":\"Course Catalog\",\"description\":\"Society Course\",\"itemDetails\":[{\"sequence\":\"1\",\"itemDetailType\":\"Course List\",\"courseList\":\"https://api.mcmaster.ca/academic-calendar/v2/course-lists/000011940\",\"listInterp\":\"None\"}],\"minimumUnits\":3,\"shortDescription\":\"Society Course\",\"maximumUnits\":3,\"type\":\"Course Requirement\"},{\"sequence\":\"0060\",\"creditIncludeMode\":\"Include In All Statistics\",\"connector\":\"AND\",\"courseRankingScheme\":\"Course Catalog\",\"description\":\"3 - 6 Units\",\"itemDetails\":[{\"sequence\":\"1\",\"itemDetailType\":\"Course List\",\"courseList\":\"https://api.mcmaster.ca/academic-calendar/v2/course-lists/000002101\",\"listInterp\":\"None\"}],\"minimumUnits\":3,\"shortDescription\":\"Engineering and Society Focus Electives\",\"maximumUnits\":6,\"type\":\"Course Requirement\"}]";
        JSONArray js = new JSONArray(jsonStr);
        LevelGroup cl = pathwayService.parseReqItems(js,false);
        assert(cl.getReqGrp().size()>0);

        //testing for case of empty json arr
        jsonStr = "[]";
        js = new JSONArray(jsonStr);
        cl = pathwayService.parseReqItems(js,false);
        assertNull(cl);
    }

    @Test
    public void testChangeCourseYear(){
        // testing if course year changes
        Course c = new Course("","","");
        CourseNode cn = new CourseNode(c,1,new ArrayList<>(),new ArrayList<>());
        List<CourseNode> cns = new ArrayList<>();
        cns.add(cn);
        CourseGroup cg = new CourseGroup(cns,1);
        List<CourseGroup> cgs = new ArrayList<>();
        cgs.add(cg);
        List<Boolean> reqs = new ArrayList<>();
        reqs.add(true);
        RequirementGroup rg = new RequirementGroup(cgs,reqs);
        List<RequirementGroup> rgs = new ArrayList<>();
        rgs.add(rg);
        LevelGroup lg = new LevelGroup(rgs);
        lg = pathwayService.changeCourseYear(lg,2);
        for (RequirementGroup trg : lg.getReqGrp()){
            for (CourseGroup tcg : trg.getCourseGroups()){
                for (CourseNode tcn : tcg.getCourses()){
                    assertEquals(tcn.getYears(),2);
                }
            }
        }
    }
    @Test
    public void testParseDegree(){
        //testing for valid degree code
        assertNotNull(pathwayService.parseDegree("HCOMPSCICO",false));

        //testing for invalid degree code
        assertNull(pathwayService.parseDegree("invalid_code",false));
    }
    @Test
    public void testParseDegreePlan(){
        //testing for valid degree code
        assertNotNull(pathwayService.parseDegreePlan("HCOMPSCICO",false));

        //testing for invalid degree code
        assertNull(pathwayService.parseDegreePlan("invalid_code",false));
    }
    @Test
    public void testAddToDB(){
        String body = "{1:[],2:[]}";
        String id = "test";
        JsonSchedule schedule = new JsonSchedule();
        schedule.setJson(body);
        schedule.setUserId(id);
        List<JsonSchedule> r = new ArrayList<>();
        r.add(schedule);
        when(pathwayService.addToDB(body,id)).thenReturn(true);

        when(degreeRepository.findByUserId(id)).thenReturn(r);

    }


}
