package com.androidapp.jdklokhandwala.api.model;

/**
 * Created by ishan on 05-01-2017.
 */

public class AboutUs {
    int AboutUsID;
    String Description;
    String UpdatedDate;
    int UpdatedDateInt;

    public int getAboutUsID() {
        return AboutUsID;
    }

    public void setAboutUsID(int aboutUsID) {
        AboutUsID = aboutUsID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public int getUpdatedDateInt() {
        return UpdatedDateInt;
    }

    public void setUpdatedDateInt(int updatedDateInt) {
        UpdatedDateInt = updatedDateInt;
    }
}
