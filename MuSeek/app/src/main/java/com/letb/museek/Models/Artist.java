package com.letb.museek.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by marina.titova on 20.12.15.
 */
public class Artist implements Serializable {
    @SerializedName("name")
    private String name;

    @SerializedName("pic")
    private String pic;

    private List<String> imagesSizesAsc;

    public Artist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setImagesSizesAsc(List<String> imagesSizesAsc) {
        this.imagesSizesAsc = imagesSizesAsc;
        this.pic = imagesSizesAsc.get(2);
    }
}
