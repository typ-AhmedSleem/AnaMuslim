/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.managers;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import androidx.annotation.Nullable;

import com.robin.locationgetter.EasyLocation;
import com.typ.muslim.db.LocationsDatabase;
import com.typ.muslim.interfaces.IGetLocationListener;
import com.typ.muslim.libs.TimezoneMapper;
import com.typ.muslim.models.Country;
import com.typ.muslim.models.Location;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.TimeZone;

/**
 * [NOT YET COMPLETELY CODED]
 * Contains necessary code to get location information from [Offline Sqlite Database] - [Location Search] - [GPS auto detect]
 */
public class LocationManager {

    // Statics
    private static final String TAG = "LocationManager";
    // Runtime Variables
    private Activity activity;
    private final LocationsDatabase locationsDatabase;
    private final android.location.LocationManager androidLocManager;

    public LocationManager(Activity activity) {
        this.activity = activity;
        this.locationsDatabase = new LocationsDatabase(activity);
        this.androidLocManager = (android.location.LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * Returns Location model with info returned from GPS location
     */
    public void getGPSLocation(IGetLocationListener listener) {
        EasyLocation easyLocation = new EasyLocation(activity, new EasyLocation.EasyLocationCallBack() {
            @Override public void permissionDenied() {
                AManager.log(TAG, "getGPSLocation: permissionDenied");
                listener.onPermissionDenied();
            }

            @Override public void locationSettingFailed() {
                AManager.log(TAG, "getGPSLocation: locationSettingFailed");
                listener.onGetLocationFailed();
            }

            @Override public void getLocation(@NotNull android.location.Location foundLoc) {
                try {
                    List<Address> addresses = new Geocoder(activity).getFromLocation(foundLoc.getLatitude(), foundLoc.getLongitude(), 2);
                    if (addresses == null) return;
                    listener.onGetLocationSucceed((new Location(addresses.get(0).getCountryCode(), addresses.get(0).getCountryName(), addresses.get(0).getSubAdminArea(), foundLoc.getLatitude(), foundLoc.getLongitude(),
                            (double) TimeZone.getTimeZone(TimezoneMapper.latLngToTimezoneString(foundLoc.getLatitude(), foundLoc.getLongitude())).getOffset(System.currentTimeMillis()) / 1000 / 60 / 60)));
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onGetLocationFailed();
                }
            }
        });
        easyLocation = null; // Finalize easyLocation instance to stop location tracking.
    }

    /**
     * [NOT YET CODED]
     */
    public List<?> searchMapsForCity(String query) {
        // TODO: 2/21/21  Search for Location with given query and return result as List of locations
        return null;
    }

    /**
     * Gets all countries in the database as List of Countries. NULL if faced an error. 0 items list if no data was found.
     */
    public List<Country> getAllCountries() {
        return locationsDatabase.getAllCountries();
    }

    /**
     * Returns all cities found in database that follow the same CountryCode. NULL if faced an error. 0 items list if no cities was found.
     */
    @Nullable
    public List<Location> getCitiesOfCountry(String countryCode) {
        return locationsDatabase.getCitiesOfCountry(countryCode);
    }

    /**
     * Returns all cities found in database its names matches the given query. NULL if faced an error. 0 items list if no cities was found.
     */
    @Nullable
    public List<Location> searchForCities(String query) {
        return locationsDatabase.searchForCities(query);
    }

}
