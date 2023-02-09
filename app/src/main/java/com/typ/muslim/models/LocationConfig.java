/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import android.os.Bundle;

import com.typ.muslim.app.Extras;
import com.typ.muslim.core.praytime.enums.AsrMethod;
import com.typ.muslim.core.praytime.enums.CalculationMethod;
import com.typ.muslim.core.praytime.enums.HigherLatitudesMethod;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Locale;

/**
 * Model class representing config of parent location
 */
public class LocationConfig implements Serializable {

    /**
     * CalculationMethod used in this city to calculate PrayTimes
     */
    private CalculationMethod calculationMethod;
    /**
     * AsrMethod this city follows to calculate PrayTimes
     */
    private AsrMethod asrMethod;
    /**
     * HigherLatitudes method used if this city uses high-lat method. Default is NONE
     */
    private HigherLatitudesMethod higherLatitude;

    public LocationConfig(CalculationMethod calculationMethod, AsrMethod asrMethod, HigherLatitudesMethod higherLatitude) {
        this.calculationMethod = calculationMethod;
        this.asrMethod = asrMethod;
        this.higherLatitude = higherLatitude;
    }

    public LocationConfig(int calculationMethod, int asrMethod, int higherLatitude) {
        this.calculationMethod = CalculationMethod.valueOf(calculationMethod);
        this.asrMethod = AsrMethod.valueOf(asrMethod);
        this.higherLatitude = HigherLatitudesMethod.valueOf(higherLatitude);
    }

    public CalculationMethod getCalculationMethod() {
        return calculationMethod;
    }

    public LocationConfig setCalculationMethod(CalculationMethod calculationMethod) {
        this.calculationMethod = calculationMethod;
        return this;
    }

    public AsrMethod getAsrMethod() {
        return asrMethod;
    }

    public LocationConfig setAsrMethod(AsrMethod asrMethod) {
        this.asrMethod = asrMethod;
        return this;
    }

    public HigherLatitudesMethod getHigherLatitude() {
        return higherLatitude;
    }

    public LocationConfig setHigherLatitude(HigherLatitudesMethod higherLatitude) {
        this.higherLatitude = higherLatitude;
        return this;
    }

    @NotNull
    @Override public String toString() {
        return "LocationConfig{" +
                "calculationMethod=" + calculationMethod +
                ", asrMethod=" + asrMethod +
                ", higherLatitude=" + higherLatitude +
                '}';
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Extras.EXTRA_LOCATION_CONFIG, this);
        return bundle;
    }

    public String toDisplayableString() {
        return String.format(Locale.getDefault(), "%s - %s - %s", calculationMethod.name(), asrMethod.name(), higherLatitude.name());
    }
}
