package com.psycheval.testapp;

public class ApprovedObject {

    private String name;
    private Boolean status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }



    public ApprovedObject(String name, Boolean status) {
        this.name = name;
        this.status = status;
    }




}
