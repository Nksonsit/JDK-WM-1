package com.androidapp.jdklokhandwala.api.model;

import java.io.Serializable;

/**
 * Created by sagartahelyani on 28-12-2016.
 */

public class Category implements Serializable {
    int CategoryID;
    String Name;
    String Image;

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
