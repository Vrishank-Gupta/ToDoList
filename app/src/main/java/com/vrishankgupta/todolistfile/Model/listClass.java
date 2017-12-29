package com.vrishankgupta.todolistfile.Model;

import java.io.Serializable;

/**
 * Created by vrishankgupta on 22/12/17.
 */

public class listClass implements Serializable {
    String task;
    boolean active;


    public listClass(String task, boolean active) {
        this.task = task;
        this.active = active;
    }

    public String getTask() {
        return task;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
//
//    @Override
//    public String toString() {
//        return new StringBuffer("Task: ").append(this.task).append(" IsActive:- ").append(this.active).toString();
//    }
}
