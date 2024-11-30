package com.oblitus.routemap.sh.projects;

import com.oblitus.routemap.sh.projects.core.TaskTracker;

public class Main {
    public static void main(String[] args) {
        TaskTracker taskTracker = new TaskTracker();
        try {
            taskTracker.doAction(args);
        } catch (Exception e) {
        }
    }
    
}