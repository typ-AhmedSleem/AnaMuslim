/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import android.os.Bundle;

import com.typ.muslim.Extras;
import com.typ.muslim.core.praytime.enums.AsrMethod;
import com.typ.muslim.core.praytime.enums.CalculationMethod;
import com.typ.muslim.core.praytime.enums.HigherLatitudes;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

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
    private HigherLatitudes higherLatitude;

    public LocationConfig(CalculationMethod calculationMethod, AsrMethod asrMethod, HigherLatitudes higherLatitude) {
        this.calculationMethod = calculationMethod;
        this.asrMethod = asrMethod;
        this.higherLatitude = higherLatitude;
    }

    public LocationConfig(int calculationMethod, int asrMethod, int higherLatitude) {
        this.calculationMethod = CalculationMethod.valueOf(calculationMethod);
        this.asrMethod = AsrMethod.valueOf(asrMethod);
        this.higherLatitude = HigherLatitudes.valueOf(higherLatitude);
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

    public HigherLatitudes getHigherLatitude() {
        return higherLatitude;
    }

    public LocationConfig setHigherLatitude(HigherLatitudes higherLatitude) {
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
}
