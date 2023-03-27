/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class AllahName implements Serializable {

    private final int ordinal;
    private final String name;
    private final String desc;

    public AllahName(int ordinal, String name, String desc) {
        this.ordinal = ordinal;
        this.name = name;
        this.desc = desc;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof AllahName)) return false;
        AllahName allahName = (AllahName) o;
        return getOrdinal() == allahName.getOrdinal();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrdinal());
    }

    @NonNull
    @Override
    public String toString() {
        return "AllahName{" +
                "ordinal=" + ordinal +
                ", getName='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
