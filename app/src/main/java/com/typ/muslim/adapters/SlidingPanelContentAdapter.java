/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.typ.muslim.fragments.panel.MenuFragment;
import com.typ.muslim.fragments.panel.PraysFragment;
import com.typ.muslim.fragments.panel.QiblaFragment;
import com.typ.muslim.libs.easyjava.data.EasyList;
import com.typ.muslim.models.Pray;
import com.typ.muslim.models.PrayTimes;
import com.typ.muslim.ui.dashboard.prays.VerticalPraysDashboardCard;

import org.jetbrains.annotations.NotNull;

public class SlidingPanelContentAdapter extends FragmentStatePagerAdapter {

    private PrayTimes prays;
    private VerticalPraysDashboardCard.PrayNotifyMethodChangedCallback callback;


    public SlidingPanelContentAdapter(@NonNull @NotNull FragmentManager fm, PrayTimes prays, VerticalPraysDashboardCard.PrayNotifyMethodChangedCallback callback) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.prays = prays;
        this.callback = callback;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) return new PraysFragment(prays, callback); // Prays (Home).
        return position == 0 ? new QiblaFragment() /* Qibla (Left) */ : new MenuFragment() /* Menu (Right) */;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
