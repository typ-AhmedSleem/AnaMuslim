/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.activities.names;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.typ.muslim.Keys;
import com.typ.muslim.R;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.models.AllahName;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class HolyNamesOfAllahActivity extends AppCompatActivity {

	// Runtime
	private NamesOfAllahAdapter adapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_names_of_allah);
		// Hide default actionBar
		if (getSupportActionBar() != null) getSupportActionBar().hide();
		// Setup adapter
		adapter = new NamesOfAllahAdapter(this);
		adapter.setOnItemClickListener((pos) -> startActivity(new Intent(this, HolyNameOfAllahDescActivity.class).putExtra(Keys.NAME_OF_ALLAH, adapter.getItem(pos))));
		// Setup views
		final RecyclerView rv = findViewById(R.id.rv_names_of_allah);
		rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
		rv.setItemAnimator(new SlideInUpAnimator(new AccelerateInterpolator()));
		rv.setLayoutManager(new LinearLayoutManager(this));
		rv.setAdapter(adapter);
		// Setup listeners
		((MaterialToolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finish());
	}

	private static class NamesOfAllahAdapter extends RecyclerArrayAdapter<AllahName> {

		public NamesOfAllahAdapter(Context context) {
			super(context, AManager.getAllahNames(context));
		}

		@Override
		public BaseViewHolder<AllahName> OnCreateViewHolder(ViewGroup parent, int viewType) {
			return new BaseViewHolder<AllahName>(parent, R.layout.item_name_of_allah) {
				@Override
				public void setData(AllahName nameOfAllah) {
					((MaterialTextView) $(R.id.stv_item_name_of_allah_ordinal)).setText(String.valueOf(nameOfAllah.getOrdinal()));
					((MaterialTextView) $(R.id.tv_item_name_of_allah)).setText(nameOfAllah.getName());
				}
			};
		}
	}
}
