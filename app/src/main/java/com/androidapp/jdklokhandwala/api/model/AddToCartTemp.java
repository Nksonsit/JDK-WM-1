package com.androidapp.jdklokhandwala.api.model;

import java.math.BigDecimal;

/**
 * Created by ishan on 11-01-2017.
 */

public class AddToCartTemp {
    private Long CategoryID;
    private Long ProductID;
    private String Name;
    private String UnitType;
    private Double UnitValue;
    private String KgWeight;

    public AddToCartTemp(Long categoryID, Long productID, String name, String unitType, Double unitValue, String kgWeight) {
        CategoryID = categoryID;
        ProductID = productID;
        Name = name;
        UnitType = unitType;
        UnitValue = unitValue;
        KgWeight = kgWeight;
    }

    public Long getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(Long categoryID) {
        CategoryID = categoryID;
    }

    public Long getProductID() {
        return ProductID;
    }

    public void setProductID(Long productID) {
        ProductID = productID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUnitType() {
        return UnitType;
    }

    public void setUnitType(String unitType) {
        UnitType = unitType;
    }

    public Double getUnitValue() {
        return UnitValue;
    }

    public void setUnitValue(Double unitValue) {
        UnitValue = unitValue;
    }

    public String getKgWeight() {
        return KgWeight;
    }

    public void setKgWeight(String kgWeight) {
        KgWeight = kgWeight;
    }
}
