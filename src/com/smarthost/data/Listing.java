package com.smarthost.data;

import com.google.gson.annotations.SerializedName;

/**
 * User: davidredding
 * Date: 3/2/14
 * Time: 7:00 PM
 */
public class Listing {

    @SerializedName("price")
    int price;

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;


    public Listing(int price, String id, String name) {
        this.price = price;
        this.id = id;
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
