package com.androidapp.jdklokhandwala.api.model;

/**
 * Created by ishan on 17-01-2017.
 */

public class Calculator {
    int Type;
    String Name;

    public Calculator(int type, String name) {
        Type = type;
        Name = name;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
