/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.activities.quran;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mpt.android.stv.Slice;
import com.mpt.android.stv.SpannableTextView;
import com.typ.muslim.R;
import com.typ.muslim.features.quran.Quran;
import com.typ.muslim.features.quran.models.QuranAyah;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.utils.DisplayUtils;

import java.util.List;
import java.util.Locale;

public class QuranReaderActivity extends AppCompatActivity {

	private int svn = -1;
	private Slice svs;
	private SpannableTextView stvQuran;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Content view
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_quran_reader);
        stvQuran = findViewById(R.id.stv_quran_reader_page);
        stvQuran.reset();
        // Add Quran verses
        final List<QuranAyah> ayat = Quran.getAyatOfSurah(this, 1);
        for (QuranAyah ayah : ayat) {
            stvQuran.addSlice(new Slice.Builder(String.format(Locale.getDefault(), "%s [*] ", ayah.getContent(this)))
                    .setSliceId(ayah.getNumber())
                    .setOnTextClick((view, slice) -> {
                        // Remove last selection if any has selection
                        if (svn != -1 && svs != null) stvQuran.replaceSliceAt(svn - 1, svs);
                        // Update selection
                        svs = slice;
                        svn = slice.getSliceId();
                        // Build selected verse slice
                        final Slice ss = new Slice.Builder(slice.getText())
                                .textSize(slice.getTextSize())
                                .textColor(ResMan.getColor(this, R.color.green))
                                .style(Typeface.BOLD)
                                .setCornerRadius(10)
                                .setSliceId(-slice.getSliceId())
                                .backgroundColor(ResMan.getColor(this, R.color.bg_input_box))
                                .build();
                        // Replace old slice then display it
                        stvQuran.replaceSliceAt(svn - 1, ss);
                        stvQuran.display();
                    })
                    .textColor(ResMan.getColor(this, R.color.darkAdaptiveColor))
					.textSize(DisplayUtils.sp2px(this, 25f))
					.build());
		}
		// Display Quran
		stvQuran.display();
	}


}
