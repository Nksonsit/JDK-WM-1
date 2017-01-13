package com.androidapp.jdklokhandwala.api.model;

import android.support.annotation.Nullable;

/**
 * Created by ishan on 10-01-2017.
 */

public class BookmarkPojo extends Bookmark{
    private Long id;
    private Long CategoryId;
    private String Name;
    private String Image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(Long categoryId) {
        CategoryId = categoryId;
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

    @Nullable
    @Override
    public Long id() {
        return id;
    }

    @Nullable
    @Override
    public Long CategoryId() {
        return CategoryId;
    }

    @Nullable
    @Override
    public String Name() {
        return Name;
    }

    @Nullable
    @Override
    public String Image() {
        return Image;
    }

    @Override
    public String toString() {
        return "Bookmark{"
                + "id=" + id + ", "
                + "CategoryId=" + CategoryId + ", "
                + "Name=" + Name + ", "
                + "Image=" + Image
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Bookmark) {
            Bookmark that = (Bookmark) o;
            return ((this.id == null) ? (that.id() == null) : this.id.equals(that.id()))
                    && ((this.CategoryId == null) ? (that.CategoryId() == null) : this.CategoryId.equals(that.CategoryId()))
                    && ((this.Name == null) ? (that.Name() == null) : this.Name.equals(that.Name()))
                    && ((this.Image == null) ? (that.Image() == null) : this.Image.equals(that.Image()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= (id == null) ? 0 : this.id.hashCode();
        h *= 1000003;
        h ^= (CategoryId == null) ? 0 : this.CategoryId.hashCode();
        h *= 1000003;
        h ^= (Name == null) ? 0 : this.Name.hashCode();
        h *= 1000003;
        h ^= (Image == null) ? 0 : this.Image.hashCode();
        return h;
    }
}
