/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.fragments.wizard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.transition.MaterialSharedAxis;
import com.typ.muslim.R;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.profile.ProfileManager;

public class WelcomeFragment extends Fragment {

    // Statics
    private static final String TAG = "WelcomeFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.X, false));
        setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.X, true));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_wizard_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final MaterialButton btnNext = view.findViewById(R.id.btn_get_started);
        // Hide button if user has profile
        if (ProfileManager.hasProfile(getContext())) btnNext.setVisibility(View.INVISIBLE);
        // Build next destination based on wizard steps completed
        final int destination;
        if (AMSettings.isLocationSet(getContext())) {
            // Check if config is set
            if (AMSettings.isConfigSet(getContext())) {
                // Next destination is 'Preview'
                btnNext.setText(R.string.continue_setup__preview);
                destination = R.id.action_welcomeFragment_to_previewPrayTimeFragment;
            } else {
                // Next destination is 'Configuration'
                btnNext.setText(R.string.continue_setup__config);
                destination = R.id.action_welcomeFragment_to_selectPrayConfigFragment;
            }
        } else {
            // Next destination is 'Location'
            btnNext.setText(R.string.start_setup__location);
            destination = R.id.action_welcomeFragment_to_locationSelectorFragment;
        }
        // Navigate to destination
        btnNext.setOnClickListener(v -> Navigation.findNavController(view).navigate(destination));
    }
}
