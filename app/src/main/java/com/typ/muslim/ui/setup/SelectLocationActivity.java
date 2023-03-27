package com.typ.muslim.ui.setup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.StickyHeaderDecoration;
import com.typ.muslim.R;
import com.typ.muslim.app.Extras;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.LocationManager;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.Country;
import com.typ.muslim.models.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SelectLocationActivity extends AppCompatActivity {

    // TODO: [2/7/2023] Make locations selection be in 2 steps (1st-Country, 2nd-City).

    // Runtime
    private CitiesAdapter citiesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        // Init runtime
        final LocationManager locationManager = new LocationManager(this);
        this.citiesAdapter = new CitiesAdapter(this, locationManager);
        // Init toolbar
        final MaterialToolbar toolbar = findViewById(R.id.toolbar);
        final SearchView searchView = (SearchView) toolbar.getMenu().getItem(0).getActionView();
        searchView.setQueryHint(getString(R.string.enter_city_name));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                citiesAdapter.refreshLocationsWithQuery(query);
                return true;
            }
        });
        toolbar.setNavigationOnClickListener(v -> {
            if (!searchView.isIconified()) searchView.setIconified(true);
            else {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
        // Init citiesRV
        final EasyRecyclerView rvCities = findViewById(R.id.erv_search_cities);
        rvCities.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvCities.setItemAnimator(new DefaultItemAnimator());
        rvCities.addItemDecoration(createStickyHeaderDecoration());
        citiesAdapter.setOnItemClickListener(position -> {
            setResult(Activity.RESULT_OK, new Intent().putExtra(Extras.EXTRA_LOCATION, citiesAdapter.getItem(position)));
            finish();
        });
        rvCities.setAdapter(citiesAdapter);
    }

    private StickyHeaderDecoration createStickyHeaderDecoration() {
        return new StickyHeaderDecoration(new StickyHeaderDecoration.IStickyHeaderAdapter<HeaderItemView>() {
            @Override
            public long getHeaderId(int position) {
                return Objects.hash(citiesAdapter.getItem(position).getCountryCode());
            }

            @Override
            public HeaderItemView onCreateHeaderViewHolder(ViewGroup parent) {
                return new HeaderItemView(parent);
            }

            @Override
            public void onBindHeaderViewHolder(HeaderItemView headerItemView, int i) {
                headerItemView.setData(citiesAdapter.getItem(i));
            }
        });
    }

    private static class HeaderItemView extends BaseViewHolder<Location> {

        public HeaderItemView(ViewGroup parent) {
            super(parent, android.R.layout.simple_list_item_1);
        }

        @Override
        public void setData(Location location) {
            ((TextView) itemView).setText(location.getCountryName());
            ((TextView) itemView).setTextColor(ResMan.getColor(getContext(), R.color.colorPrimary));
            itemView.setBackgroundColor(ResMan.getColor(getContext(), R.color.white));
        }
    }

    private static final class CitiesAdapter extends RecyclerArrayAdapter<Location> {

        private final List<Location> locations = new ArrayList<>();

        public CitiesAdapter(Context context, LocationManager locationManager) {
            super(context);
            if (locations.size() == 0) fillLocationsList(locationManager);
            this.refreshLocationsWithQuery(null);
        }

        private void fillLocationsList(LocationManager locationManager) {
            locations.clear();
            for (Country country : locationManager.getAllCountries()) locations.addAll(locationManager.getCitiesOfCountry(country.getCode()));
        }

        /**
         * Refresh contents of RecyclerView by clearing it and add new cities
         */
        public void refreshLocationsWithQuery(@Nullable String query) {
            if (this.locations.size() == 0) return;
            // Refresh data of RV
            this.removeAll();
            this.addAll(TextUtils.isEmpty(query) ? this.locations : queryLocations(query)); // Add locations.
        }

        private List<Location> queryLocations(String query) {
            query = query.toLowerCase(Locale.ROOT).trim();
            final List<Location> queriedLocations = new ArrayList<>();
            for (Location loc : this.locations) {
                if (queriedLocations.contains(loc)) continue;
                if (loc != null && (loc.getCityName().toLowerCase().trim().contains(query) || loc.getCountryName().toLowerCase().trim().contains(query))) {
                    queriedLocations.add(loc);
                }
            }
            AManager.log("refreshLocationsWithQuery", "Query: %s | Filtered to: %d location", query, queriedLocations.size());
            return queriedLocations;
        }

        @Override
        public BaseViewHolder<Location> OnCreateViewHolder(ViewGroup parent, int viewType) {
            return new CityViewHolder(parent);
        }

        private static final class CityViewHolder extends BaseViewHolder<Location> {

            // Views
            private final MaterialTextView tvCity;
            private final MaterialTextView tvTimezone;

            public CityViewHolder(ViewGroup parent) {
                super(parent, R.layout.item_search_city);
                tvCity = $(R.id.tv_city_name);
                tvTimezone = $(R.id.tv_timezone);
            }

            @Override
            public void setData(Location location) {
                tvCity.setText(location.getCityName());
                tvTimezone.setText(String.format(Locale.getDefault(), "GMT %.1f", location.getTimezone()));
            }
        }
    }

}
