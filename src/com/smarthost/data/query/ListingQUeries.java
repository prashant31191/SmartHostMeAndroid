package com.smarthost.data.query;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.smarthost.data.DatabaseHelper;
import com.smarthost.data.Listing;
import com.smarthost.loaders.BroadcastSender;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: davidredding
 * Date: 3/3/14
 * Time: 2:28 PM
 */
public class ListingQueries {

    public static Listing findListingForLocalId(Context context, Integer id) throws SQLException
    {
        return getDao(context).queryForId(id);
    }

    public static Listing findListingForApiId(Context context, String api_id) throws SQLException
    {
        Dao<Listing,?> dao = getDao(context);
        List<Listing> listings = dao.queryBuilder().where().eq("api_id", api_id).query();
        return listings.size() > 0 ? listings.get(0) : null;
    }

    public static List<Listing> findListingForSearchQuery(Context context, String searchQuery) throws SQLException
    {
        Dao<Listing,?> dao = getDao(context);
        return dao.queryBuilder().where().like("searchQuery", searchQuery).query();

    }

    public static Listing findListingForApiIdAndQuery(Context context, String api_id, String searchQuery) throws SQLException
    {
        Dao<Listing,?> dao = getDao(context);
        List<Listing> listings = dao.queryBuilder().where().eq("api_id", api_id).and().eq("searchQuery", searchQuery).query();
        return listings.size() > 0 ? listings.get(0) : null;
    }




    public static List<Listing> all(Context context) throws SQLException
    {
        return getDao(context).queryBuilder().orderBy("name", true).query();
    }

    public static Dao<Listing, Integer> getDao(Context context) throws SQLException
    {
        return DatabaseHelper.getInstance(context).getDao(Listing.class);
    }

    public static void save(Context context, Listing Listing) throws SQLException
    {
        getDao(context).createOrUpdate(Listing);
    }

}

