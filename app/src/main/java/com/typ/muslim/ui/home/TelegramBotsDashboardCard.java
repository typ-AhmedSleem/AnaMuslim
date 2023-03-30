/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.mpt.android.stv.Slice;
import com.mpt.android.stv.SpannableTextView;
import com.typ.muslim.R;
import com.typ.muslim.features.telegram.TelegramBotsIntegration;
import com.typ.muslim.features.telegram.TelegramConstants;
import com.typ.muslim.features.telegram.TelegramHelper;
import com.typ.muslim.features.telegram.enums.TelegramChatType;
import com.typ.muslim.features.telegram.interfaces.TelegramRequestCallback;
import com.typ.muslim.features.telegram.models.TelegramBot;
import com.typ.muslim.features.telegram.models.TelegramChat;
import com.typ.muslim.features.telegram.models.TelegramError;
import com.typ.muslim.features.telegram.models.TelegramRequestResult;
import com.typ.muslim.libs.easyjava.data.EasyList;
import com.typ.muslim.managers.AManager;

import org.json.JSONObject;

import java.util.Locale;
import java.util.Objects;

public class TelegramBotsDashboardCard extends DashboardCard {

    // todo: Send AzanMessage to all registered chats when the onPrayTimeCame interface is called.
    // todo: Make user select which telegram chats he wanna send the Azan msg to.

    // Static
    private final String TAG = "TelegramBotsDashboardCard";
    // Runtime
    private EasyList<TelegramBot> myBots;
    // Views
    private SpannableTextView stvBots;
    private MaterialButton btnAction;

    public TelegramBotsDashboardCard(Context context) {
        super(context);
    }

    public TelegramBotsDashboardCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TelegramBotsDashboardCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void prepareRuntime(Context context) {
        myBots = new EasyList<>();
        if (!isInEditMode()) myBots = TelegramBotsIntegration.getMyBots(getContext(), false);
    }

    @Override
    public void prepareCardView(Context context) {
        // Inflate card view and change card color
        setStrokeColor(Color.TRANSPARENT);
        setRippleColorResource(R.color.ripple_telegram_card);
        setCardBackgroundColor(getColor(R.color.telegram_bg_color));
        inflate(context, R.layout.layout_telegram_bots_card, this);
        // Init views
        stvBots = $(R.id.stv_bots);
        btnAction = $(R.id.btn_bots);
        // Click listener
        btnAction.setOnClickListener(this);
        // Refresh UI
        refreshUI();
    }

    @Override
    public void refreshRuntime() {
        myBots = TelegramBotsIntegration.getMyBots(getContext(), false);
    }

    @Override
    public void refreshUI() {
        stvBots.reset();
        if (myBots.isEmpty()) {
            // No bots found
            btnAction.setText(R.string.add_new_bot);
            stvBots.addSlice(new Slice.Builder(getString(R.string.no_bots_yet))
                    .superscript()
                    .textSize(30)
                    .style(Typeface.BOLD)
                    .textColor(Color.WHITE)
                    .build());
            stvBots.addSlice(new Slice.Builder("\n" + getString(R.string.click_to_add_first_bot))
                    .textSize(25)
                    .textColor(getColor(R.color.bg_input_box))
                    .build());
        } else {
            // Found bots
            btnAction.setText(R.string.manage_bots);
            stvBots.addSlice(new Slice.Builder(String.format(Locale.getDefault(), "%d ", myBots.size()))
                    .superscript()
                    .textSize(60)
                    .style(Typeface.BOLD)
                    .textColor(Color.BLACK)
                    .build());
            stvBots.addSlice(new Slice.Builder(getString(myBots.size() == 1 ? R.string.bot : R.string.bots))
                    .superscript()
                    .textSize(60)
                    .textColor(getColor(R.color.white))
                    .build());
            stvBots.addSlice(new Slice.Builder("\n" + getString(R.string.click_to_manage_bots))
                    .textSize(25)
                    .textColor(getColor(R.color.brightSubtitleTextColor))
                    .build());
        }
        stvBots.display();
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), R.string.planned_in_next_versions, Toast.LENGTH_LONG).show();
        // todo: Open AddTelegramBotBottomSheet that makes user add his bot if there's no bots at all yet.
        // todo: Open TelegramBotsBottomSheet (if user already has bots) that handles all work on bots instead of an activity.
    }

    @Override
    public boolean onLongClick(View v) {
        if (myBots != null && !myBots.isEmpty()) {
            myBots.get(0).getChat(TelegramConstants.OUR_CHAT_ID, new TelegramRequestCallback() {
                @Override
                public void onFailed(TelegramError error) {
                    AManager.log(TAG, "onFailed: " + error.toString());
                }

                @Override
                public void onCancelled(boolean byUser) {
                    AManager.log(TAG, "onCancelled: " + byUser);
                }

                @Override
                public void onResponse(JSONObject response) {
                    if (response == null) return; // todo: show error to user
                    TelegramRequestResult result = TelegramBotsIntegration.parseResponse(response);
                    if (result.hasSucceed() && result.hasResult()) {
                        // Construct the TelegramChat object
                        TelegramChat chat = new TelegramChat(
                                result.optInt("id", 0),
                                TelegramChatType.of(result.optString("type", "private")),
                                Objects.requireNonNull(TelegramHelper.optChatPhoto(result.optJSONObject("photo", null))),
                                result.optString("title", ""),
                                result.optString("description", ""),
                                TelegramHelper.optPermissions(result.optJSONObject("permissions", null)));
                        AManager.log(TAG, "getChat: " + chat);
                    } else {
                        // todo: show request failed to user
                    }
                }
            });
        }
        return true;
    }
}
