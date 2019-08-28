package com.allein.freund.authapp.remote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by freund on 1/10/18.
 */

public class Item {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return String.format("%s:%s", id, name);
    }
}
