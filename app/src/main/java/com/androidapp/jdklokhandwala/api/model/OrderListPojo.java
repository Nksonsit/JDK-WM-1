package com.androidapp.jdklokhandwala.api.model;

/**
 * Created by ishan on 13-01-2017.
 */

public class OrderListPojo {
    String Name;
    String CategoryName;
    String UnitType;
    Double UnitValue;
    Double KGWeight;
    Double CurrentMarketPrice;
    Double Price;

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
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

    public Double getKGWeight() {
        return KGWeight;
    }

    public void setKGWeight(Double KGWeight) {
        this.KGWeight = KGWeight;
    }

    public Double getCurrentMarketPrice() {
        return CurrentMarketPrice;
    }

    public void setCurrentMarketPrice(Double currentMarketPrice) {
        CurrentMarketPrice = currentMarketPrice;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }
}
