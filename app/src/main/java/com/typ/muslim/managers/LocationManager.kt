/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.managers

import android.content.Context
import android.location.Geocoder
import com.birjuvachhani.locus.Locus
import com.typ.muslim.db.LocationsDatabase
import com.typ.muslim.interfaces.GetLocationCallback
import com.typ.muslim.libs.TimezoneMapper
import com.typ.muslim.models.Country
import com.typ.muslim.models.Location
import java.util.TimeZone
import android.location.LocationManager as AndroidLocationManager

/**
 * [NOT YET COMPLETELY CODED]
 * Contains necessary code to get location information from [Offline Sqlite Database] - [Location Search] - [GPS auto detect]
 */
class LocationManager(val context: Context) {

    private val locationsDatabase: LocationsDatabase = LocationsDatabase(context)
    private val androidLocManager: AndroidLocationManager

    init {
        Locus.setLogging(true)
        androidLocManager = context.getSystemService(Context.LOCATION_SERVICE) as AndroidLocationManager
    }

    /**
     * Returns Location model with info returned from GPS location
     */
    fun getGPSLocation(callback: GetLocationCallback) {
        try {
            Locus.getCurrentLocation(context) {
                if (it.error == null) {
                    callback.onGetLocationFailed()
                    return@getCurrentLocation
                }
                if (it.location != null) {
                    try {
                        val loc = it.location!!
                        val addresses = Geocoder(context, LocaleManager.getCurrLocale(context)).getFromLocation(loc.latitude, loc.longitude, 1) ?: return@getCurrentLocation
                        callback.onGetLocationSucceed(
                            Location(
                                addresses[0].countryCode,
                                addresses[0].countryName,
                                addresses[0].subAdminArea,
                                loc.latitude,
                                loc.longitude,
                                (TimeZone.getTimeZone(TimezoneMapper.latLngToTimezoneString(loc.latitude, loc.longitude)).getOffset(System.currentTimeMillis()) / 1000 / 60 / 60).toDouble()
                            )
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                        callback.onGetLocationFailed()
                    }
                }
            }
        } catch (_: Throwable) {
            callback.onGetLocationFailed()
        }
    }

    /**
     * [NOT YET CODED]
     */
    fun searchMapsForCity(query: String?): List<*>? {
        // TODO: 2/21/21  Search for Location with given query and return result as List of locations
        return null
    }

    val allCountries: List<Country>
        /**
         * Gets all countries in the database as List of Countries. NULL if faced an error. 0 items list if no data was found.
         */
        get() = locationsDatabase.allCountries

    /**
     * Returns all cities found in database that follow the same CountryCode. NULL if faced an error. 0 items list if no cities was found.
     */
    fun getCitiesOfCountry(countryCode: String?): List<Location> {
        return locationsDatabase.getCitiesOfCountry(countryCode)
    }

    /**
     * Returns all cities found in database its names matches the given query. NULL if faced an error. 0 items list if no cities was found.
     */
    fun searchForCities(query: String?): List<Location>? {
        return locationsDatabase.searchForCities(query)
    }

    companion object {
        // Statics
        private const val TAG = "LocationManager"

        private var INSTANCE: LocationManager? = null

        @JvmStatic
        fun getInstance(context: Context): LocationManager {
            if (INSTANCE == null) {
                synchronized(LocationManager::class) {
                    if (INSTANCE == null) {
                        INSTANCE = LocationManager(context)
                    }
                }
            }
            return INSTANCE!!
        }

        @JvmStatic
        fun getGPSLocation(context: Context, callback: GetLocationCallback) = getInstance(context).getGPSLocation(callback)
    }
}