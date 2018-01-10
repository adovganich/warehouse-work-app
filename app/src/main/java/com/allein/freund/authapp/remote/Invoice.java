package com.allein.freund.authapp.remote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by freund on 1/10/18.
 */

public class Invoice {
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("customer")
    @Expose
    private String customer;
    @SerializedName("positions")
    @Expose
    private Integer positions;
    @SerializedName("money")
    @Expose
    private Float money;

    public Integer getNumber() {
        return number;
    }

    public String getCustomer() {
        return customer;
    }

    public Integer getPositions() {
        return positions;
    }

    public Float getMoney() {
        return money;
    }
}
