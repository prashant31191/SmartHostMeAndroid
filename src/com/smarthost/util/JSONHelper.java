package com.smarthost.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: davidredding
 * Date: 3/3/14
 * Time: 4:26 PM
 */
public class JSONHelper {

    public static String getNullableString(JSONObject obj, String name) throws JSONException
    {
        if (obj.isNull(name))
            return null;
        else
            return obj.getString(name);
    }

    public static Long getNullableLong(JSONObject obj, String name) throws JSONException
    {
        return !obj.isNull(name) ? obj.getLong(name) : null;
    }

    public static Integer getNullableInteger(JSONObject obj, String name) throws JSONException
    {
        return !obj.isNull(name) ? obj.getInt(name) : null;
    }

    private String valueOf(Integer x)
    {
        return x != null ? String.valueOf(x) : null;
    }

    private String valueOf(Long x)
    {
        return x != null ? String.valueOf(x) : null;
    }

    private String valueOf(Boolean b)
    {
        if (b == null)
            return null;

        return Boolean.TRUE.equals(b) ? "1" : "0";
    }
}
