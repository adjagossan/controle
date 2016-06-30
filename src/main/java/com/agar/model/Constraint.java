package com.agar.model;

/**
 * Created by SDEV2 on 27/06/2016.
 */
public class Constraint {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public Constraint(String name) {
        this.name = name;
    }

    public Constraint() {
    }
}
