package com.degreeflow.model;

import java.util.List;

public class LevelGroup {
    private List<RequirementGroup> reqGrp;

    public void addReqGrp(RequirementGroup rg){
        this.reqGrp.add(rg);
    }
    public List<RequirementGroup> getReqGrp(){
        return this.reqGrp;
    }
    public LevelGroup(List<RequirementGroup> reqGrp){
        this.reqGrp = reqGrp;
    }
}
