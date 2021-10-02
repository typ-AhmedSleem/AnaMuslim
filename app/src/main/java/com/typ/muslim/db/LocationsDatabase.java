/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.db;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.typ.muslim.models.Country;
import com.typ.muslim.models.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Works with Locations Offline Database
 */
public class LocationsDatabase extends SQLiteAssetHelper {

    public LocationsDatabase(@NonNull Context context) {
        super(context, "locations.sqlite", null, 3);
    }

    /**
     * Gets all countries in the database as List of Countries. NULL if faced an error. 0 items list if no data was found.
     */
    public List<Country> getAllCountries() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT DISTINCT country_code,en_country FROM locations", null);
        if (cursor == null) return null;
        if (cursor.getCount() == 0) return new ArrayList<>();
        List<Country> countries = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            countries.add(new Country(cursor.getString(0), cursor.getString(1)));
            cursor.moveToNext();
        }
        cursor.close();
        return countries;
    }

    /**
     * Returns a Country model if found any countries that matches the given query. NULL if no country found
     *
     * @deprecated Use {@link #getAllCountries()} then filter them in the activity.
     */
    @Deprecated
    @Nullable
    public Country searchForCountry(String matchWhat) {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT DISTINCT country_code,en_country FROM locations WHERE " +
                "en_country=" + buildEqualSQL(matchWhat, true) +
                "ar_country=" + buildEqualSQL(matchWhat, true) +
                "country_code=" + buildEqualSQL(matchWhat, false), null);
        if (cursor == null) return null; // No Data Found.
        if (cursor.getCount() == 0) return null; // No Data Found.
        cursor.moveToFirst(); // Move to first row.
        Country foundCountry = new Country(cursor.getString(0), cursor.getString(1));
        cursor.close(); // Close the cursor to release resources.
        return foundCountry;
    }

    /**
     * Returns all Cities found in database that holds the same CountryCode. NULL if faced an error. 0 items list if no cities was found.
     */
    @Nullable
    public List<Location> getCitiesOfCountry(String countryCode) {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT DISTINCT en_country,en_city,lat,loong,time_zone FROM locations WHERE countryCode=" + buildEqualSQL(countryCode, false) + " order by country_code", null);
        if (cursor == null) return null;
        List<Location> cities = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fixme Missing [NameCode-CalculationMethod-AsrMethod-HigherLatitudes] in the city info.
            cities.add(new Location(countryCode, cursor.getString(0), cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getDouble(4)));
            cursor.moveToNext();
        }
        cursor.close();
        return cities;
    }

    /**
     * Only used here to help while writing code
     */
    private String buildEqualSQL(String query, boolean withOr) {
        return withOr ? "'" + query + "' OR " : "'" + query + "'";
    }

    /**
     * Only used here to help while writing code
     */
    private String buildLikeSQL(String query, boolean withOr) {
        return withOr ? "'" + query + "%' OR " : "'" + query + "%'";
    }

    /**
     * Returns all cities found in database its names matches the given query. NULL if faced an error. 0 items list if no cities was found.
     */
    public List<Location> searchForCities(String query) {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT DISTINCT country_code,en_country,en_city,lat,loong,time_zone FROM locations WHERE en_city LIKE " + buildLikeSQL(query, true) + " ar_city LIKE " + buildLikeSQL(query, false) + " order by country_code", null);
        if (cursor == null) return null;
        List<Location> cities = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fixme Missing [NameCode-CalculationMethod-AsrMethod-HigherLatitudes] in the city info.
            cities.add(new Location(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getDouble(5)));
            cursor.moveToNext();
        }
        cursor.close();
        return cities;
    }
}
