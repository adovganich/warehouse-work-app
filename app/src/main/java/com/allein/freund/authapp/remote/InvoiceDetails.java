package com.allein.freund.authapp.remote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by freund on 1/15/18.
 */

public class InvoiceDetails {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cost")
    @Expose
    private int cost;
    @SerializedName("amount")
    @Expose
    private int amount;

    public String toString() {
        return String.format("%s:%s:%s:%s", id, name, cost, amount);
    }
    public String getName(){
        return name;
    }
    public int getAmount(){
        return amount;
    }
    public int getCost(){
        return cost;
    }

}
