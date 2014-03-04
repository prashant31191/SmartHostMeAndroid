package com.smarthost.network;

import android.content.Context;
import co.touchlab.android.superbus.BusHelper;
import com.google.common.base.Joiner;
import com.smarthost.data.Listing;
import com.smarthost.data.query.ListingQueries;
import com.smarthost.util.JSONHelper;
import com.smarthost.util.TLog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: davidredding
 * Date: 3/3/14
 * Time: 4:11 PM
 */
public class ListingHelper {


    public static List<Listing> loadListings(Context c, JSONArray jsonObject, String query) throws IOException {
        List<Listing> listings;
        List<Listing> dbListings = new ArrayList<Listing>();
        try {
            listings = parseListings(jsonObject, c);

            for (Listing listing : listings) {
                Listing dbListing = ListingQueries.findListingForApiIdAndQuery(c, listing.getApi_id(), query);

                if (dbListing == null) {
                    dbListing = new Listing();
                    // ListingQueries.save(c, dbListing);
                }

                dbListing.api_id = listing.api_id;
                dbListing.price = listing.price;
                dbListing.description = listing.description;
                dbListing.country= listing.country;
                dbListing.neighborhood= listing.neighborhood;
                dbListing.bedrooms= listing.bedrooms;
                dbListing.occupancy= listing.occupancy;
                dbListing.city= listing.city;
                dbListing.name= listing.name;
                dbListing.link= listing.link;
                dbListing.room_type= listing.room_type;
                dbListing.property_type= listing.property_type;
                dbListing.addrees= listing.addrees;
                dbListing.beds= listing.beds;
                dbListing.bathrooms= listing.bathrooms;
//                dbListing.latitude= listing.latitude;
                dbListing.latitude= listing.longitude;
                dbListing.state= listing.state;
                dbListing.street_name= listing.street_name;
//                dbListing.longitude= listing.longitude;
                dbListing.longitude= listing.latitude;
                dbListing.amenities= listing.amenities;
                dbListing.json = listing.json;
                dbListing.searchQuery = query;

                ListingQueries.save(c, dbListing);
                dbListings.add(dbListing);

            }


        } catch (SQLException e) {
            try {
                TLog.runtimeExceptionThrown(c, ListingHelper.class, e.getMessage(), e);
            } catch (FileNotFoundException e1) {
                throw new RuntimeException(e1);
            }
            throw new RuntimeException(e);
        } catch (JSONException e) {
            try {
                TLog.runtimeExceptionThrown(c, ListingHelper.class, e.getMessage(), e);
            } catch (FileNotFoundException e1) {
                throw new RuntimeException(e1);
            }
            throw new RuntimeException(e);
        }

        return dbListings;
    }

    public static List<Listing> parseListings(JSONArray obj, Context c) throws JSONException
    {
        ArrayList<Listing> result = new ArrayList<Listing>();

        for (int i = 0, l = obj.length(); i < l; i++)
        {
            JSONObject listingJSON = (JSONObject)obj.get(i);
            Listing Listing = fillListingBaseInfo(listingJSON);

            result.add(Listing);
        }

        return result;
    }

    private static Listing fillListingBaseInfo(JSONObject listingJson) throws JSONException
    {
        Listing listing = new Listing();

        listing.json = listingJson.toString();
        listing.api_id = listingJson.getString("id");
        listing.zipcode = JSONHelper.getNullableString(listingJson, "zipcode");
        listing.price= JSONHelper.getNullableInteger(listingJson, "price");
        listing.description= JSONHelper.getNullableString(listingJson, "description");
        listing.country= JSONHelper.getNullableString(listingJson, "country");
        listing.neighborhood= JSONHelper.getNullableString(listingJson, "neighborhood");
        listing.bedrooms= JSONHelper.getNullableInteger(listingJson, "bedrooms");
        listing.occupancy= JSONHelper.getNullableInteger(listingJson, "occupancy");

        listing.city = JSONHelper.getNullableString(listingJson, "city");
        listing.name= JSONHelper.getNullableString(listingJson, "name");
        listing.link= JSONHelper.getNullableString(listingJson, "link");
        listing.room_type= JSONHelper.getNullableString(listingJson, "room_type");
        listing.property_type= JSONHelper.getNullableString(listingJson, "property_type");
        listing.addrees= JSONHelper.getNullableString(listingJson, "addrees");
        listing.beds= JSONHelper.getNullableInteger(listingJson, "beds");
        listing.bathrooms= JSONHelper.getNullableInteger(listingJson, "bathrooms");

        listing.latitude= JSONHelper.getNullableString(listingJson, "latitude");
        listing.state= JSONHelper.getNullableString(listingJson, "state");
        listing.street_name= JSONHelper.getNullableString(listingJson, "street_name");
        listing.longitude= JSONHelper.getNullableString(listingJson, "longitude");

//        listing.amenities= JSONHelper.getNullableString(listingJson, "amenities");



        JSONArray amen = listingJson.getJSONArray("amenities");

        ArrayList<String> amenResults = new ArrayList<String>();

        for (int i = 0, l = amen.length(); i < l; i++)
        {
            amenResults.add((String) amen.get(i));
        }


        listing.amenities = Joiner.on(", ").join(amenResults);

        return listing;
    }


}

