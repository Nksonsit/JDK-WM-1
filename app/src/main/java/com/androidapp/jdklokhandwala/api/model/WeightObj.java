package com.androidapp.jdklokhandwala.api.model;

/**
 * Created by ishan on 18-01-2017.
 */

public class WeightObj {
    String Name;
    String Weight;

    public WeightObj(String name, String weight) {
        Name = name;
        Weight = weight;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }
}
