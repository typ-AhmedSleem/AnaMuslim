/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.fragments.wizard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.transition.MaterialSharedAxis;
import com.irozon.alertview.AlertActionStyle;
import com.irozon.alertview.AlertStyle;
import com.irozon.alertview.AlertView;
import com.irozon.alertview.objects.AlertAction;
import com.irozon.sneaker.Sneaker;
import com.typ.muslim.R;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.LocationConfig;

public class SelectPrayConfigFragment extends Fragment {

    // Statics
    private static final String TAG = "SelectPrayConfigFragment";
    // Config params
    private int calcMethod = -1;
    private int asrMethod = -1;
    private int hlMethod = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup Enter/Exit transitions
        setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.X, true));
        setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.X, true));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_wizard_pray_config, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View fragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragmentView, savedInstanceState);
        // Calc method
        final MaterialAutoCompleteTextView actCalcMethod = (MaterialAutoCompleteTextView) ((TextInputLayout) fragmentView.findViewById(R.id.til_config_calc_method)).getEditText();
        if (actCalcMethod != null) {
            actCalcMethod.setAdapter(new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1, ResMan.getStringArray(requireContext(),
                    R.array.CalcMethodDescs)));
            actCalcMethod.setOnItemClickListener((parent, view, position, id) -> calcMethod = position);
        }
        // Asr method
        final MaterialAutoCompleteTextView actAsrMethod = (MaterialAutoCompleteTextView) ((TextInputLayout) fragmentView.findViewById(R.id.til_config_asr_method)).getEditText();
        if (actAsrMethod != null) {
            actAsrMethod.setAdapter(new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1, ResMan.getStringArray(requireContext(),
                    R.array.AsrMethodDescs)));
            actAsrMethod.setOnItemClickListener((parent, view, position, id) -> asrMethod = position);
        }
        // HigherLat method
        final MaterialAutoCompleteTextView actLatMethod = (MaterialAutoCompleteTextView) ((TextInputLayout) fragmentView.findViewById(R.id.til_config_high_lat_method)).getEditText();
        if (actLatMethod != null) {
            actLatMethod.setAdapter(new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1, ResMan.getStringArray(requireContext(),
                    R.array.HigherLatMethodDescs)));
            actLatMethod.setOnItemClickListener((parent, view, position, id) -> hlMethod = position);
        }
        // Next button
        fragmentView.findViewById(R.id.btn_continue).setOnClickListener(v -> {
            final LocationConfig config = buildConfig();
            if (config == null) {
                Sneaker.with(requireActivity())
                        .autoHide(true)
                        .setDuration(3000)
                        .setTitle(getString(R.string.missing_params))
                        .setMessage(getString(R.string.msg_missing_params))
                        .sneakError();
            } else {
                final AlertView confirmationAlert = new AlertView(getString(R.string.confirm_configuration), getString(R.string.msg_confirm_config), AlertStyle.BOTTOM_SHEET);
                confirmationAlert.addAction(new AlertAction(getString(R.string.wait_to_take_a_look_on_it), AlertActionStyle.DEFAULT, alertAction -> {
                }));
                confirmationAlert.addAction(new AlertAction(getString(R.string.confirm_and_continue), AlertActionStyle.POSITIVE, alertAction -> {
                    // Save current config
                    if (AMSettings.saveConfiguration(requireContext(), config)) {
                        // Navigate to next fragment
                        Navigation.findNavController(fragmentView).navigate(R.id.action_selectPrayConfigFragment_to_previewPrayTimeFragment);
                    } else Toast.makeText(requireContext(), "Can't save configuration.", Toast.LENGTH_SHORT).show();
                }));
                confirmationAlert.show((AppCompatActivity) requireActivity());
            }
        });
    }

    @Nullable
    private LocationConfig buildConfig() {
        if (calcMethod == -1 || asrMethod == -1 || hlMethod == -1) return null;
        return new LocationConfig(calcMethod, asrMethod, hlMethod);
    }

}
