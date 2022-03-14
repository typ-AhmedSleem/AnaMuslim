/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import static com.typ.muslim.enums.StageStatus.DONE;
import static com.typ.muslim.enums.StageStatus.FAILED;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.StringRes;

import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.enums.StageStatus;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.StageInfo;

import java.util.Locale;

public class StageItemView extends RelativeLayout {

    // Runtime
    private StageInfo stageInfo;
    // Views
    private final RelativeLayout container;
    private final ImageView indicatorIV;
    private final MaterialTextView stageTitleTV;

    public StageItemView(Context context) {
        super(context, null);
        // Setup views
        inflate(context, R.layout.layout_stage_item, this);
        container = findViewById(R.id.rl_siv_container);
        indicatorIV = findViewById(R.id.stage_indicator_iv);
        stageTitleTV = findViewById(R.id.stage_title_tv);
    }

    public StageItemView(Context context, AttributeSet attrs) {
        this(context);
    }

    public StageItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context);
    }

    public StageItemView(Context context, StageInfo stageInfo) {
        this(context);
        this.setStage(stageInfo);
    }

    public StageItemView setStage(StageInfo stageInfo) {
        // Update runtime
        this.stageInfo = stageInfo;
        // Update views
        if (this.stageInfo != null) {
            this.changeIndicator(this.stageInfo.getStatus());
            this.updateTitle(this.stageInfo.getStepTitle());
        }
        return this;
    }

    public StageItemView changeIndicator(StageStatus status) {
        // TODO: 2021-05-27 add color transition to change between prev status color and next status color
        if (this.stageInfo == null) return this;
        this.stageInfo.updateStatus(status);
        switch (status) {
            case INFO:
                this.indicatorIV.setVisibility(INVISIBLE);
                this.container.setBackgroundTintList(ColorStateList.valueOf(ResMan.getColor(getContext(), R.color.ef_colorPrimaryDark)));
                this.stageTitleTV.setTextColor(ResMan.getColor(getContext(), R.color.white));
                this.stageTitleTV.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                break;
            case WAITING:
                this.indicatorIV.setImageDrawable(null);
                this.indicatorIV.setBackgroundResource(R.drawable.shape_coming_pray);
                this.stageTitleTV.setTextColor(ResMan.getColor(getContext(), R.color.darkAdaptiveColor));
                this.indicatorIV.setBackgroundTintList(ColorStateList.valueOf(ResMan.getColor(getContext(), R.color.darkAdaptiveColor)));
                break;
            case WORKING:
                this.indicatorIV.setColorFilter(ResMan.getColor(getContext(), R.color.scv_surround_color_default));
                this.indicatorIV.setImageResource(R.drawable.ic_arrow_to_right);
                this.indicatorIV.setBackgroundResource(R.drawable.shape_coming_pray);
                this.indicatorIV.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                this.container.setBackgroundTintList(ColorStateList.valueOf(ResMan.getColor(getContext(), R.color.scv_surround_color_default)));
                this.stageTitleTV.setTextColor(ResMan.getColor(getContext(), R.color.white));
                break;
            case DONE:
                this.indicatorIV.setColorFilter(ResMan.getColor(getContext(), R.color.green));
                this.indicatorIV.setImageResource(R.drawable.ic_done);
                this.indicatorIV.setBackgroundResource(R.drawable.shape_passed_pray);
                this.indicatorIV.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                this.container.setBackgroundTintList(ColorStateList.valueOf(ResMan.getColor(getContext(), R.color.green)));
                this.stageTitleTV.setTextColor(ResMan.getColor(getContext(), R.color.white));
                break;
            case FAILED:
            case CANCELLED:
                this.indicatorIV.setColorFilter(ResMan.getColor(getContext(), R.color.switcher_off_color));
                this.indicatorIV.setImageResource(R.drawable.ic_close);
                this.indicatorIV.setBackgroundResource(R.drawable.shape_passed_pray);
                this.indicatorIV.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                this.container.setBackgroundTintList(ColorStateList.valueOf(ResMan.getColor(getContext(), R.color.switcher_off_color)));
                this.stageTitleTV.setTextColor(ResMan.getColor(getContext(), R.color.white));
                break;
        }
        return this;
    }

    public StageItemView updateTitle(@StringRes int titleResId) {
        this.stageTitleTV.setText(titleResId);
        return this;
    }

    public StageInfo getStageInfo() {
        return stageInfo;
    }

    public void updateStatus(StageStatus status) {
        // Some checks at first
        if (status == null || (this.stageInfo != null && this.stageInfo.getStatus() == StageStatus.INFO)) return;
        // Update indicator
        this.changeIndicator(status);
        // Update title
        if (this.stageInfo == null) this.stageTitleTV.setText(R.string.failed);
        else this.stageTitleTV.setText(String.format(Locale.getDefault(), "%s: %s",
                ResMan.getString(getContext(), this.stageInfo.getStepTitle()),
                ResMan.getString(getContext(),
                        status == DONE ? R.string.succeed : status == FAILED ? R.string.failed : R.string.cancelled)));
    }

    @Override
    public String toString() {
        return "StageItemView{" +
                "stageInfo=" + stageInfo +
                ", container=" + container +
                ", indicatorIV=" + indicatorIV +
                ", stageTitleTV=" + stageTitleTV +
                '}';
    }
}
