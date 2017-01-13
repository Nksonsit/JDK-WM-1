package com.androidapp.jdklokhandwala.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sagartahelyani on 29-12-2016.
 */

public class Product implements Serializable {
    int ProductID;
    String Name;
    String CodeValue;
    Double Weight;
    int UnitOfMeasureID;
    List<UnitMeasure> UnitsOfMeasure;

    public int getUnitOfMeasureID() {
        return UnitOfMeasureID;
    }

    public void setUnitOfMeasureID(int unitOfMeasureID) {
        UnitOfMeasureID = unitOfMeasureID;
    }

    public List<UnitMeasure> getUnitsOfMeasure() {
        return UnitsOfMeasure;
    }

    public void setUnitsOfMeasure(List<UnitMeasure> unitsOfMeasure) {
        UnitsOfMeasure = unitsOfMeasure;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCodeValue() {
        return CodeValue;
    }

    public void setCodeValue(String codeValue) {
        CodeValue = codeValue;
    }

    public Double getWeight() {
        return Weight;
    }

    public void setWeight(Double weight) {
        Weight = weight;
    }
}
