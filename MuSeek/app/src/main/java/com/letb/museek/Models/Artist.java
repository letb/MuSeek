package com.letb.museek.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by marina.titova on 20.12.15.
 */
public class Artist implements Serializable {
    @SerializedName("name")
    private String name;

    @SerializedName("pic")
    private String pic;

    public Artist(String name, String pic) {
        this.name = name;
        this.pic = pic;
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

}
