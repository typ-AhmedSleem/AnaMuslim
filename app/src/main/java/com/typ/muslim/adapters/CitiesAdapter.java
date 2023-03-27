/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.adapters;

import android.content.Context;
import android.view.ViewGroup;

import com.google.android.material.textview.MaterialTextView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.typ.muslim.R;
import com.typ.muslim.models.Location;

import java.util.List;

public class CitiesAdapter extends RecyclerArrayAdapter<Location> {

    // Runtime
    private final EasyRecyclerView erv;

    public CitiesAdapter(Context context, EasyRecyclerView erv) {
        super(context);
        this.erv = erv;
    }

    @Override
    public BaseViewHolder<Location> OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityViewHolder(parent);
    }

    /**
     * Refresh contents of RecyclerView by clearing it and add new cities
     */
    public void refresh(List<Location> cities) {
        if (cities == null) return;
        if (cities.size() == 0) {
            erv.showEmpty();
            return;
        }
        //erv.showProgress(); // Show Loading Progress.
        super.removeAll(); // Remove all items.
        super.addAll(cities); // Add new cities.
        erv.showRecycler(); // Show Recycler.
    }

    private static class CityViewHolder extends BaseViewHolder<Location> {

        // Views
        private final MaterialTextView tvCity;
        private final MaterialTextView tvCountry;

        public CityViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_city);
            tvCity = $(R.id.tv_city_name);
            tvCountry = $(R.id.tv_country_name);
        }

        @Override
        public void setData(Location location) {
            tvCity.setText(location.getCityName());
            tvCountry.setText(location.getCountryName());
        }
    }

}
