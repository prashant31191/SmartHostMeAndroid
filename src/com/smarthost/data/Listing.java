package com.smarthost.data;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User: davidredding
 * Date: 3/2/14
 * Time: 7:00 PM
 */
public class Listing implements Serializable{



    @DatabaseField(generatedId = true, unique = true)
    public long db_id;

    @DatabaseField
    public String searchQuery;

    @SerializedName("zipcode")
    @DatabaseField
    public String zipcode;

    @SerializedName("price")
    @DatabaseField
    public int price;

    @SerializedName("description")
    @DatabaseField
    public String description;


    @SerializedName("country")
    @DatabaseField
    public String country;

    @SerializedName("neighborhood")
    @DatabaseField
    public String neighborhood;

    @SerializedName("bedrooms")
    @DatabaseField
    public int bedrooms;

    @SerializedName("occupancy")
    @DatabaseField
    public int occupancy;

    @SerializedName("city")
    @DatabaseField
    public String city;

    @SerializedName("name")
    @DatabaseField
    public String name;

    @SerializedName("link")
    @DatabaseField
    public String link;

    @SerializedName("room_type")
    @DatabaseField
    public String room_type;

    @SerializedName("property_type")
    @DatabaseField
    public String property_type;

    @SerializedName("addrees")
    @DatabaseField
    public String addrees;

    @SerializedName("beds")
    @DatabaseField
    public int beds;

    @SerializedName("bathrooms")
    @DatabaseField
    public int bathrooms;

    @SerializedName("latitude")
    @DatabaseField
    public String latitude;

    @SerializedName("state")
    @DatabaseField
    public String state;

    @SerializedName("street_name")
    @DatabaseField
    public String street_name;

    @SerializedName("id")
    @DatabaseField(unique = true)
    public String api_id;

    @SerializedName("longitude")
    @DatabaseField
    public String longitude;

    @SerializedName("amenities")
    @DatabaseField
    public String amenities;

    public Listing(){

    }

    public Listing(long db_id, String searchQuery, String zipcode, int price, String description, String country, String neighborhood, int bedrooms, int occupancy, String city, String name, String link, String room_type, String property_type, String addrees, int beds, int bathrooms, String latitude, String state, String street_name, String api_id, String longitude, String amenities) {
        this.db_id = db_id;
        this.searchQuery = searchQuery;
        this.zipcode = zipcode;
        this.price = price;
        this.description = description;
        this.country = country;
        this.neighborhood = neighborhood;
        this.bedrooms = bedrooms;
        this.occupancy = occupancy;
        this.city = city;
        this.name = name;
        this.link = link;
        this.room_type = room_type;
        this.property_type = property_type;
        this.addrees = addrees;
        this.beds = beds;
        this.bathrooms = bathrooms;
        this.latitude = latitude;
        this.state = state;
        this.street_name = street_name;
        this.api_id = api_id;
        this.longitude = longitude;
        this.amenities = amenities;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getAddrees() {
        return addrees;
    }

    public void setAddrees(String addrees) {
        this.addrees = addrees;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet_name() {
        return street_name;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public long getId() {
        return db_id;
    }

    public void setId(long id) {
        this.db_id= id;
    }

    public String getApi_id() {
        return api_id;
    }

    public void setApi_id(String api_id) {
        this.api_id = api_id;
    }
}
