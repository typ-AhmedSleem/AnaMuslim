package com.typ.muslim.models;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.typ.muslim.app.Extras;

import java.io.Serializable;
import java.util.Objects;

/**
 * Model class representing Location data
 */
public class Location implements Serializable {

    /**
     * Code of country this location belongs to
     */
    private String countryCode;
    /**
     * Name of country this location belongs to
     */
    private String countryName;
    /**
     * Name code of this location found in Database.
     */
    private String cityName;
    /**
     * Latitude of this location
     */
    private double latitude;
    /**
     * Longitude of this location
     */
    private double longitude;
    /**
     * Timezone of this location
     */
    private double timezone;
    /**
     * Config of this location
     */
    private LocationConfig config;

    public Location() {
    }

    /**
     * Creates new Location Model Class with parameterized info
     */

    public Location(String countryCode, String countryName, String cityName, double latitude, double longitude, double timezone) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.cityName = cityName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
    }

    public Location(String countryCode, String countryName, String cityName, double latitude, double longitude, double timezone, LocationConfig config) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.cityName = cityName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
        this.config = config;
    }


    @NonNull
    public String getCountryCode() {
        return countryCode;
    }


    public Location setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    @NonNull
    public String getCountryName() {
        return countryName;
    }

    public Location setCountryName(String countryName) {
        this.countryName = countryName;
        return this;
    }

    @NonNull
    public String getCityName() {
        return cityName;
    }

    public Location setCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Location setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Location setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public double getTimezone() {
        return timezone;
    }

    public Location setTimezone(double timezone) {
        this.timezone = timezone;
        return this;
    }

    public LocationConfig getConfig() {
        return config;
    }

    public Location setConfig(LocationConfig config) {
        this.config = config;
        return this;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Extras.EXTRA_LOCATION, this);
        return bundle;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Double.compare(location.latitude, latitude) == 0 &&
                Double.compare(location.longitude, longitude) == 0 &&
                Objects.equals(countryCode, location.countryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryCode, latitude, longitude);
    }

    @NonNull
    @Override
    public String toString() {
        return "Location{" +
                "countryCode='" + countryCode + '\'' +
                ", countryName='" + countryName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", timezone=" + timezone +
                ", config=" + config +
                '}';
    }


}
