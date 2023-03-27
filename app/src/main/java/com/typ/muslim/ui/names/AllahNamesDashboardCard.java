/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.names;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.models.AllahName;
import com.typ.muslim.ui.BottomSheets;
import com.typ.muslim.ui.home.DashboardCard;

public class AllahNamesDashboardCard extends DashboardCard {

	// Runtime
	private AllahName allahName;
	// Views
	private MaterialTextView tvNameNumber, tvName, tvNameDesc;

	public AllahNamesDashboardCard(Context context) {
		super(context);
	}

	public AllahNamesDashboardCard(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AllahNamesDashboardCard(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void prepareRuntime(Context context) {
		this.getRandomizedName();
	}

	@Override
	public void prepareCardView(Context context) {
		// Inflate view
		inflate(getContext(), R.layout.layout_name_of_allah_card, this);
		// Init views
		tvNameNumber = findViewById(R.id.tv_name_of_allah_ordinal);
		tvName = findViewById(R.id.tv_allah_name_content);
		tvNameDesc = findViewById(R.id.tv_allah_name_desc);
		// Refresh ui
		if (!isInEditMode()) refreshUI();
	}

	@Override
	public void refreshUI() {
		// Show data in views
		tvNameNumber.setText(String.valueOf(allahName.getOrdinal() + 1));
		tvName.setText(allahName.getName());
		tvNameDesc.setText(allahName.getDesc());
	}

	private void getRandomizedName() {
		final AllahName allahName = AManager.getAllahName(getContext(), -2);
		if (allahName.equals(this.allahName)) this.getRandomizedName();
		else this.allahName = allahName;
	}

	@Override
	public boolean onLongClick(View v) {
		if (isBottomSheetShown) return false;
		BottomSheets.newAllahNames(getContext(),
				this.allahName,
				isShown -> this.isBottomSheetShown = isShown,
				newAllahName -> {
					allahName = newAllahName;
					refreshUI();
				}).show();
		return true;
	}

	public AllahName getHoldingName() {
		return allahName;
	}
}
