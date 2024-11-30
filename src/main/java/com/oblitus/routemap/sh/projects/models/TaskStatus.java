package com.oblitus.routemap.sh.projects.models;

public enum TaskStatus{
    TODO("todo"),
    IN_PROGRESS("in-progress"),
    DONE("done");

    String value;
    TaskStatus(String value){
        this.value = value;
    }
}
