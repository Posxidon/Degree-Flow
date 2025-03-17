package com.degreeflow.service;

import com.degreeflow.model.Course;
import com.degreeflow.model.CourseGroup;
import com.degreeflow.model.Degree;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PathwayService {

    public Degree parseDegreePlan(int degreeID){
        if (degreeID == 26811) {
            List<CourseGroup> cscg = new ArrayList<>();
            List<Course> cs1csCourses = new ArrayList<>();

            List<Course> cs1mCourses = new ArrayList<>();

            Course c1zb3 = new Course("1zb3", "1", "",1,null);
            Course c1za3 = new Course("1za3", "1", "",1,null);
            cs1mCourses.add(c1za3);
            cs1mCourses.add(c1zb3);

            CourseGroup cs1mathcg = new CourseGroup(cs1mCourses,1);


            List<CourseGroup> prereq2ac3 = new ArrayList<>();
            prereq2ac3.add(cs1mathcg);
            Course c2ac3 = new Course("2AC3", "1", "",2,prereq2ac3);
            cs1csCourses.add(c2ac3);

            cscg.add(new CourseGroup(cs1csCourses, 1));
            cscg.add(new CourseGroup(cs1mCourses, 2));

            return new Degree("Computer Science 1", cscg);
        }
        return null;
    }
    public Degree getCoop(){
        List<CourseGroup> coop = new ArrayList<>();
        List<Course> coop1 = new ArrayList<>();
        List<Course> coop2 = new ArrayList<>();
        coop1.add(new Course("coop","coop1","",1,null));
        coop2.add(new Course("coop","coop2","",2,null));
        coop.add(new CourseGroup(coop1,1));
        coop.add(new CourseGroup(coop2,1));
        return new Degree("coops",coop);

    }
    public List<CourseGroup> splitCourseGroup(CourseGroup cg, int courseSplitCnt){
        List<Course> c1 = new ArrayList<>();
        List<Course> c2 = new ArrayList<>();
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
//    public List<Course> generateDegreePlan(int degreeID, Degree coop,Degree summerCourses, int courseLoad){
//        Degree degree = parseDegreePlan(degreeID);
//        int totalCredit = 0;
//        for (CourseGroup cg : degree.getReqCourses()){
//            totalCredit += cg.getNumReq();
//        }
//        int years = Math.ceilDiv(totalCredit,courseLoad);
//        int currentYear = 4;
//        Degree scenario = new Degree();
//        for (int i = currentYear; i>0;i--){
//            for (CourseGroup cg : degree.getReqCourses()){
//                for (Course c : cg.getCourses()){
//                    if (c.getAntireqs().size() == 0){
//                        List<Course> temp = new ArrayList<>();
//                        temp.add(c);
//                        scenario.AddCourse(new CourseGroup(temp,1));
//                    }
//                }
//                for (Course c : cg.getCourses()){
//                    if (c.getAntireqs().size() == 0){
//                        List<Course> temp = new ArrayList<>();
//                        temp.add(c);
//                        scenario.AddCourse(new CourseGroup(temp,1));
//                    }
//                }
//            }
//        }
//    }
}
