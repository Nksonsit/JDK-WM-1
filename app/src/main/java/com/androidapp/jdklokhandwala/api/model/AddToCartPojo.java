package com.androidapp.jdklokhandwala.api.model;

import android.support.annotation.Nullable;

/**
 * Created by ishan on 10-01-2017.
 */

public class AddToCartPojo extends AddToCart {

    private Long id;
    private Long CategoryID;
    private Long ProductID;
    private String Name;
    private String UnitType;
    private String UnitTypes;
    private Double UnitValue;
    private Double KgWeight;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUnitTypes() {
        return UnitTypes;
    }

    public void setUnitTypes(String unitTypes) {
        UnitTypes = unitTypes;
    }

    public Double getUnitValue() {
        return UnitValue;
    }

    public void setUnitValue(Double unitValue) {
        UnitValue = unitValue;
    }

    public Double getKgWeight() {
        return KgWeight;
    }

    public void setKgWeight(Double kgWeight) {
        KgWeight = kgWeight;
    }

    @Nullable
    @Override
    public Long id() {
        return id;
    }

    @Nullable
    @Override
    public Long CategoryID() {
        return CategoryID;
    }

    @Nullable
    @Override
    public Long ProductID() {
        return ProductID;
    }

    @Nullable
    @Override
    public String Name() {
        return Name;
    }

    @Nullable
    @Override
    public String UnitType() {
        return UnitType;
    }

    @Nullable
    @Override
    public String UnitTypes() {
        return UnitTypes;
    }

    @Nullable
    @Override
    public Double UnitValue() {
        return UnitValue;
    }

    @Nullable
    @Override
    public Double KgWeight() {
        return KgWeight;
    }

    @Override
    public String toString() {
        return "AddToCart{"
                + "id=" + id + ", "
                + "CategoryID=" + CategoryID + ", "
                + "ProductID=" + ProductID + ", "
                + "Name=" + Name + ", "
                + "UnitType=" + UnitType + ", "
                + "UnitTypes=" + UnitTypes + ", "
                + "UnitValue=" + UnitValue + ", "
                + "KgWeight=" + KgWeight
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof AddToCart) {
            AddToCart that = (AddToCart) o;
            return ((this.id == null) ? (that.id() == null) : this.id.equals(that.id()))
                    && ((this.CategoryID == null) ? (that.CategoryID() == null) : this.CategoryID.equals(that.CategoryID()))
                    && ((this.ProductID == null) ? (that.ProductID() == null) : this.ProductID.equals(that.ProductID()))
                    && ((this.Name == null) ? (that.Name() == null) : this.Name.equals(that.Name()))
                    && ((this.UnitType == null) ? (that.UnitType() == null) : this.UnitType.equals(that.UnitType()))
                    && ((this.UnitTypes == null) ? (that.UnitTypes() == null) : this.UnitTypes.equals(that.UnitTypes()))
                    && ((this.UnitValue == null) ? (that.UnitValue() == null) : this.UnitValue.equals(that.UnitValue()))
                    && ((this.KgWeight == null) ? (that.KgWeight() == null) : this.KgWeight.equals(that.KgWeight()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : this.id.hashCode();
        h *= 1000003;
        h ^= (CategoryID == null) ? 0 : this.CategoryID.hashCode();
        h *= 1000003;
        h ^= (ProductID == null) ? 0 : this.ProductID.hashCode();
        h *= 1000003;
        h ^= (Name == null) ? 0 : this.Name.hashCode();
        h *= 1000003;
        h ^= (UnitType == null) ? 0 : this.UnitType.hashCode();
        h *= 1000003;
        h ^= (UnitTypes == null) ? 0 : this.UnitTypes.hashCode();
        h *= 1000003;
        h ^= (UnitValue == null) ? 0 : this.UnitValue.hashCode();
        h *= 1000003;
        h ^= (KgWeight == null) ? 0 : this.KgWeight.hashCode();
        return h;
    }
}
