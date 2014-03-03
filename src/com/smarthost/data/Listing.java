package com.smarthost.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * User: davidredding
 * Date: 3/2/14
 * Time: 7:00 PM
 */
public class Listing {


    @SerializedName("zipcode")
    String zipcode;

    @SerializedName("price")
    int price;

    @SerializedName("description")
    String description;


    @SerializedName("country")
    String country;

    @SerializedName("neighborhood")
    String neighborhood;

    @SerializedName("bedrooms")
    int bedrooms;

    @SerializedName("occupancy")
    int occupancy;

    @SerializedName("city")
    String city;

    @SerializedName("name")
    String name;

    @SerializedName("link")
    String link;

    @SerializedName("room_type")
    String room_type;

    @SerializedName("property_type")
    String property_type;

    @SerializedName("addrees")
    String addrees;

    @SerializedName("beds")
    int beds;

    @SerializedName("bathrooms")
    int bathrooms;

    @SerializedName("latitude")
    String latitude;

    @SerializedName("state")
    String state;

    @SerializedName("street_name")
    String street_name;

    @SerializedName("id")
    String id;

    @SerializedName("longitude")
    String longitude;


//    Might not need the tag...should be able to parse this like before?
//    @SerializedName("amenities")
    ArrayList<String> amenities;

    /*
     "zipcode": "19103",
    "price": 500,
    "description": "The Residences at The Ritz Carlton Philadelphia provides all of the legendary Ritz-Carlton amenities. ",
    "country": "United States of America",
    "neighborhood": "Rittenhouse",
    "bedrooms": 2,
    "occupancy": 4,
    "city": "Philadelphia",
    "name": "Residences at the Ritz Carlton",
    "link": "http://airbnb.com/rooms/2115785",
    "room_type": "Entire home/apt",
    "property_type": "Apartment",
    "addrees": "Market Street, Philadelphia, Pennsylvania 19103",
    "beds": 2,
    "bathrooms": 2,
    "latitude": -75.16531582686208,
    "state": "Pennsylvania",
    "street_name": "Market Street",
    "id": "air2115785",
    "longitude": 39.951911922870686,
    "amenities": [
      "Air Conditioning",
      "Breakfast",
      "Buzzer/Wireless Intercom",
      "Cable/Satellite",
      "Clothes Dryer",
      "Clothes Washer",
      "Doorman",
      "Elevator",
      "Family/Kid Friendly",
      "Gym",
      "Heating",
      "Hot Tub",
      "Internet",
      "Kitchen",
      "Parking",
      "Pets Allowed",
      "Pool",
      "Suitable For Events",
      "TV",
      "WiFi"
    ]
     */


    public Listing(String zipcode, int price, String description, String country, String neighborhood, int bedrooms, int occupancy, String city, String name, String link, String room_type, String property_type, String addrees, int beds, int bathrooms, String latitude, String state, String street_name, String id, String longitude, ArrayList<String> amenities) {
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
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public ArrayList<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(ArrayList<String> amenities) {
        this.amenities = amenities;
    }
}
