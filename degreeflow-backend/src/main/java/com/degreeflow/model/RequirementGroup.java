package com.degreeflow.model;

import java.util.ArrayList;
import java.util.List;

/**
 * requirement group represents the relationship between a list of course groups
 * every list of integer within the reqIds represents a group of courseGroups in which only one is needed
 */

public class RequirementGroup {
    private List<CourseGroup> reqGroup;
    private List<List<Integer>> reqIds;

    /**
     * adds course group along with requirement param
     * @param cg - course group to be added
     * @param isReq - if course group is a new requirement, if it is then a new entry (list<integer>) is added. if it isn't, then it gets added to the previous requirement group
     */
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
