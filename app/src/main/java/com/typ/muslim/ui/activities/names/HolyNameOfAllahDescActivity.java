/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.activities.names;

import static com.typ.muslim.ui.AnimatedTextView.Direction.NEXT;
import static com.typ.muslim.ui.AnimatedTextView.Direction.PREV;

import android.os.Bundle;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.typ.muslim.R;
import com.typ.muslim.app.Keys;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.models.AllahName;
import com.typ.muslim.ui.AnimatedTextView;

import java.util.Locale;

public class HolyNameOfAllahDescActivity extends AppCompatActivity {

	// Runtime
	private AllahName nameOfAllah;
	// Views
	private AnimatedTextView atvName, atvDesc;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_name_of_allah_desc);
		// Hide default actionBar
		if (getSupportActionBar() != null) getSupportActionBar().hide();
		// Get passed NameOfAllah from extras
		nameOfAllah = (AllahName) getIntent().getSerializableExtra(Keys.NAME_OF_ALLAH);
		if (nameOfAllah == null) nameOfAllah = AManager.getAllahName(this, 0);
		// Setup views
		atvName = findViewById(R.id.atv_name_of_allah);
		atvDesc = findViewById(R.id.atv_name_of_allah_desc);
		final FloatingActionButton fabPrev = findViewById(R.id.fab_prev);
		final FloatingActionButton fabRand = findViewById(R.id.fab_rand);
		final FloatingActionButton fabNext = findViewById(R.id.fab_next);
		// Bind data to views
		bindDataToViews(AnimatedTextView.Direction.NONE);
		// Setup listeners
		fabPrev.setOnClickListener(v -> {
			// Update runtime and views
			nameOfAllah = getName(nameOfAllah.getOrdinal() - 1);
			bindDataToViews(PREV);
			// Hide or Show fabs
			if (nameOfAllah.getOrdinal() <= 0) fabPrev.hide();
			fabNext.show();
		});
		fabRand.setOnClickListener(v -> {
			// Update runtime and views
			AllahName randName = AManager.getRandomizedAllahName(this, nameOfAllah);
			final AnimatedTextView.Direction animDir = randName.getOrdinal() > nameOfAllah.getOrdinal() ? NEXT : PREV;
			nameOfAllah = randName;
			bindDataToViews(animDir);
			// Hide or Show fabs
			if (nameOfAllah.getOrdinal() >= 98) fabNext.hide();
			else fabNext.show();
			if (nameOfAllah.getOrdinal() <= 0) fabPrev.hide();
			else fabPrev.show();
		});
		fabNext.setOnClickListener(v -> {
			// Update runtime and views
			nameOfAllah = getName(nameOfAllah.getOrdinal() + 1);
			bindDataToViews(NEXT);
			// Hide or Show fabs
			if (nameOfAllah.getOrdinal() >= 98) fabNext.hide();
			fabPrev.show();
		});
		((MaterialToolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finish());
	}

	private void bindDataToViews(AnimatedTextView.Direction animDir) {
		atvName.setText(String.format(Locale.getDefault(), "%d %s", nameOfAllah.getOrdinal(), nameOfAllah.getName()), animDir);
		atvDesc.setText(nameOfAllah.getDesc(), animDir);
	}

	public AllahName getName(@IntRange(from = 0, to = 98) int ordinal) {
		if (ordinal < 0) ordinal = 0;
		else if (ordinal > 98) ordinal = 98;
		return AManager.getAllahName(this, ordinal);
	}

}
