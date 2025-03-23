package com.degreeflow.model;

import java.util.ArrayList;
import java.util.List;

public class RequirementGroup {
    private List<CourseGroup> reqGroup;
    private List<List<Integer>> reqIds;

    public void addCourseGroup(CourseGroup cg, boolean isReq){
        if (isReq){
            List<Integer> id = new ArrayList<>();
            id.add(this.reqGroup.size());
            this.reqIds.add(id);
        }else{
            this.reqIds.get(this.reqGroup.size()-1).add(this.reqGroup.size());
        }
        this.reqGroup.add(cg);
    }

    public List<CourseGroup> getCourseGroups(){
        return this.reqGroup;
    }

    public List<List<Integer>> getReqLst(){
        return this.reqIds;
    }

    public RequirementGroup(List<CourseGroup> cg, List<Boolean> reqs){
        this.reqGroup = new ArrayList<>();
        this.reqIds = new ArrayList<>();
        for(int i=0;i<cg.size();i++){
            this.addCourseGroup(cg.get(i),reqs.get(i));
        }
    }
}
