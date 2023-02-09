/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.fragments.wizard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.transition.MaterialSharedAxis;
import com.jraska.falcon.Falcon;
import com.typ.muslim.R;
import com.typ.muslim.libs.EnhancedScaleTouchListener;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.systems.ColorSystem;
import com.typ.muslim.utils.DisplayUtils;

import libs.mjn.scaletouchlistener.ScaleTouchListener;

public class ThemeSelectorFragment extends Fragment {

    // Statics
    private static final String TAG = "ThemeSelectorFragment";
    // Runtime
    private ColorSystem colorSystem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.X, true));
        setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.X, false));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_wizard_theme_selector, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Inject ColorSystem
        //ColorSystem.adaptContainer((ViewGroup) view);
        ImageView imgThemeSwitch = view.findViewById(R.id.img_theme_switch);
        view.findViewById(R.id.btn_theme_light).setOnTouchListener(new EnhancedScaleTouchListener(100, 0.95f, 0.8f) {
            @Override
            public void onClick(View v, float x, float y) {
                // Buttons Switcher
                ((MaterialButton) v).setStrokeWidthResource(R.dimen.stroke_width_button_selected);
                ((MaterialButton) view.findViewById(R.id.btn_theme_dark)).setStrokeWidth(0);
                // Fake Screen
                Bitmap fakeScreenBitmap = Falcon.takeScreenshotBitmap(requireActivity());
                imgThemeSwitch.setImageBitmap(fakeScreenBitmap);
                imgThemeSwitch.setVisibility(View.VISIBLE);
                // Circular Reveal
                Animator animator = ViewAnimationUtils.createCircularReveal(imgThemeSwitch, (int) x, (int) y, DisplayUtils.getScreenH(requireActivity()), 0f);
                animator.setDuration(500);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        // Change Application Theme
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        imgThemeSwitch.setImageBitmap(null);
                        imgThemeSwitch.setVisibility(View.GONE);
                    }
                });
                animator.start();
            }
        });
        view.findViewById(R.id.btn_theme_dark).setOnTouchListener(new EnhancedScaleTouchListener(100, 0.95f, 1f) {
            @Override
            public void onClick(View v, float x, float y) {
                // Buttons Switcher
                ((MaterialButton) v).setStrokeWidthResource(R.dimen.stroke_width_button_selected);
                ((MaterialButton) view.findViewById(R.id.btn_theme_light)).setStrokeWidth(0);
                // Fake Screen
                Bitmap fakeScreenBitmap = Falcon.takeScreenshotBitmap(requireActivity());
                imgThemeSwitch.setImageBitmap(fakeScreenBitmap);
                imgThemeSwitch.setVisibility(View.VISIBLE);
                // Circular Reveal
                Animator animator = ViewAnimationUtils.createCircularReveal(imgThemeSwitch, (int) x, (int) y, DisplayUtils.getScreenH(requireActivity()), 0);
                animator.setDuration(400);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        // Change Application Theme
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        imgThemeSwitch.setImageBitmap(null);
                        imgThemeSwitch.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();
            }
        });
        view.findViewById(R.id.btn_theme_red).setOnTouchListener(new EnhancedScaleTouchListener(100, 0.95f, 0.8f) {
            @Override
            public void onClick(View v, float x, float y) {
                // Buttons Switcher
                ((MaterialButton) v).setStrokeWidthResource(R.dimen.stroke_width_button_selected);
                ((MaterialButton) view.findViewById(R.id.btn_theme_dark)).setStrokeWidth(0);
                // Fake Screen
                Bitmap fakeScreenBitmap = Falcon.takeScreenshotBitmap(requireActivity());
                imgThemeSwitch.setImageBitmap(fakeScreenBitmap);
                imgThemeSwitch.setVisibility(View.VISIBLE);
                // Circular Reveal
                Animator animator = ViewAnimationUtils.createCircularReveal(imgThemeSwitch, (int) x, (int) y, DisplayUtils.getScreenH(requireActivity()), 0f);
                animator.setDuration(500);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        // Change Application Theme
                        view.setBackgroundColor(ResMan.getColor(requireContext(), R.color.colorAccent));
                        v.setBackgroundColor(ResMan.getColor(requireContext(), R.color.colorPrimary));
                        view.findViewById(R.id.btn_theme_dark).setBackgroundColor(ResMan.getColor(requireContext(), R.color.colorText));
                        view.findViewById(R.id.btn_theme_red).setBackgroundColor(ResMan.getColor(requireContext(), R.color.colorText));
                        view.findViewById(R.id.btn_theme_teal).setBackgroundColor(ResMan.getColor(requireContext(), R.color.colorText));
                        view.findViewById(R.id.btn_theme_blue).setBackgroundColor(ResMan.getColor(requireContext(), R.color.colorText));
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        imgThemeSwitch.setImageBitmap(null);
                        imgThemeSwitch.setVisibility(View.GONE);
                    }
                });
                animator.start();
            }
        });
        view.findViewById(R.id.btn_theme_teal).setOnTouchListener(new ScaleTouchListener(new ScaleTouchListener.Config(100, 0.95f, 1f)) {
            @Override
            public void onClick(View v) {
            }
        });
        view.findViewById(R.id.btn_theme_green).setOnTouchListener(new ScaleTouchListener(new ScaleTouchListener.Config(100, 0.95f, 0.8f)) {
            @Override
            public void onClick(View v) {

            }
        });
        view.findViewById(R.id.btn_theme_blue).setOnTouchListener(new ScaleTouchListener(new ScaleTouchListener.Config(100, 0.95f, 0.8f)) {
            @Override
            public void onClick(View v) {

            }
        });
        view.findViewById(R.id.fab_go_back).setOnTouchListener(new ScaleTouchListener(new ScaleTouchListener.Config(100, 0.95f, 0.8f)) {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigateUp();
            }
        });
        view.findViewById(R.id.fab_go_next).setOnTouchListener(new ScaleTouchListener(new ScaleTouchListener.Config(100, 0.95f, 0.8f)) {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
