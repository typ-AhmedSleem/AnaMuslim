/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import static com.typ.muslim.enums.FormatPatterns.DATE_NORMAL;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvale.switcher.SwitcherX;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver;
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager;
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendar;
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter;
import com.mpt.android.stv.Slice;
import com.mpt.android.stv.SpannableTextView;
import com.typ.muslim.R;
import com.typ.muslim.enums.CalendarDots;
import com.typ.muslim.enums.PrayStatus;
import com.typ.muslim.enums.SoMReminderFreq;
import com.typ.muslim.features.calendar.HijriCalendar;
import com.typ.muslim.features.calendar.models.HijriDate;
import com.typ.muslim.interfaces.OnIslamicEventClickListener;
import com.typ.muslim.interfaces.ResultCallback;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.IslamicEvents;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.ActionItem;
import com.typ.muslim.models.AllahName;
import com.typ.muslim.models.IslamicEvent;
import com.typ.muslim.features.prays.models.Pray;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.ui.names.HolyNamesOfAllahActivity;
import com.typ.muslim.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator;
import kotlin.Pair;

public class BottomSheets {

    // API interfaces
    public static ActionSelectorBottomSheet newActions(Context context, @StringRes int title, @StringRes int subtitle, ResultCallback<Integer> listener, ResultCallback<Boolean> showHideCallback, ActionItem... actions) {
        return new ActionSelectorBottomSheet(context, title, subtitle, listener, showHideCallback, actions);
    }
    public static HijriCalendarBottomSheet newHijriCalendar(Context context, View.OnClickListener actionClickListener, OnIslamicEventClickListener eventClickListener, ResultCallback<Boolean> showHideCallback) {
        return new HijriCalendarBottomSheet(context, actionClickListener, eventClickListener, showHideCallback);
    }

    public static AnswerPrayedBottomSheet newAnswerPrayed(Context context, Pray whichPray, ResultCallback<Pair<PrayStatus, Boolean>> callback, ResultCallback<Boolean> showHideCallback) {
        return new AnswerPrayedBottomSheet(context, whichPray, callback, showHideCallback);
    }

    public static AllahNamesBottomSheet newAllahNames(Context context, AllahName allahName, ResultCallback<Boolean> showHideCallback, ResultCallback<AllahName> selectedNameCallback) {
        return new AllahNamesBottomSheet(context, allahName, selectedNameCallback, showHideCallback);
    }

    public static SoMReminderBottomSheet newSoMReminder(Context context, SoMReminderFreq freq, ResultCallback<SoMReminderFreq> freqChangeCallback, ResultCallback<Boolean> showHideCallback) {
        return new SoMReminderBottomSheet(context, freq, showHideCallback, freqChangeCallback);
    }

    public static EventDetailsBottomSheet newIslamicEventDetails(Context context, IslamicEvent event) {
        return new EventDetailsBottomSheet(context, event);
    }

    // BottomSheets
    public static abstract class BaseBottomSheet extends BottomSheetBehavior.BottomSheetCallback {

        // Global Runtime and views
        public final Context context;
        final View bsView;
        final BottomSheetDialog bs;
        private final ResultCallback<Boolean> showHideCallback;

        public BaseBottomSheet(Context context, ResultCallback<Boolean> showHideCallback, boolean fitToContents) {
            // Set global runtime
            this.context = context;
            this.showHideCallback = showHideCallback;
            // Prepare BottomSheet
            bsView = onCreateView();
            // Create BottomSheetDialog
            bs = new BottomSheetDialog(context);
            bs.setContentView(bsView);
            bs.getBehavior().setFitToContents(fitToContents);
            bs.getBehavior().setGestureInsetBottomIgnored(true);
            bs.getBehavior().setDraggable(!fitToContents);
            bs.getBehavior().addBottomSheetCallback(this);
            bs.getBehavior().setSaveFlags(BottomSheetBehavior.SAVE_ALL);
            bs.setOnDismissListener(dialog -> {
                // Notify callback
                if (showHideCallback != null) showHideCallback.onResult(false);
            });
            // Prepare inner views and setup listeners
            prepareInnerViews();
            setupListeners();
            // Prepare runtime
            prepareRuntime();
        }

        public void prepareRuntime() {
        }

        @NonNull
        public abstract View onCreateView();

        public abstract void prepareInnerViews();

        public abstract void bindInnerViews();

        public abstract void setupListeners();

        public final <T extends View> T findViewById(@IdRes int id) {
            return this.bsView.findViewById(id);
        }

        @ColorInt
        public final int getColor(@ColorRes int colorRes) {
            return ResMan.getColor(context, colorRes);
        }

        @NonNull
        public final String getString(@StringRes int stringRes) {
            return ResMan.getString(context, stringRes);
        }

        public final View inflate(@LayoutRes int layoutResId) {
            return View.inflate(context, layoutResId, null);
        }

        @Override
        public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {

        }

        @Override
        public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {

        }

        public BaseBottomSheet show() {
            // Notify callback
            if (showHideCallback != null) showHideCallback.onResult(true);
            // Show the bs
            this.bs.show();
            return this;
        }

        public BaseBottomSheet cancel() {
            // Notify callback
            if (showHideCallback != null) showHideCallback.onResult(false);
            // Cancel the bs
            this.bs.cancel();
            return this;
        }

    }

    public static class ActionSelectorBottomSheet extends BaseBottomSheet {

        // Statics
        private static final String TAG = ActionSelectorBottomSheet.class.getSimpleName();
        // Runtime
        private final @StringRes
        int title;
        private final @StringRes
        int subtitle;
        // Listeners
        private final ResultCallback<Integer> listener;
        private final ActionsAdapter adapter;
        // Views
        private MaterialTextView tvTitle, tvSubtitle;
        private RecyclerView rvActions;

        ActionSelectorBottomSheet(Context context, @StringRes int title, @StringRes int subtitle, ResultCallback<Integer> listener, ResultCallback<Boolean> showHideCallback, ActionItem... actions) {
            super(context, showHideCallback, true);
            // Prepare runtime
            this.title = title;
            this.subtitle = subtitle;
            this.listener = listener;
            adapter = new ActionsAdapter(this.context, Arrays.asList(actions));
            // Bind data to views
            bindInnerViews();
        }

        @NonNull
        @NotNull
        @Override
        public View onCreateView() {
            return inflate(R.layout.bs_action_selector);
        }

        @Override
        public void prepareInnerViews() {
            tvTitle = findViewById(R.id.tv_title);
            tvSubtitle = findViewById(R.id.tv_subtitle);
            rvActions = findViewById(R.id.rv_actions);
        }

        @Override
        public void bindInnerViews() {
            tvTitle.setText(this.title);
            tvSubtitle.setText(this.subtitle);
            rvActions.setLayoutManager(new LinearLayoutManager(context));
            rvActions.setItemAnimator(new DefaultItemAnimator());
            rvActions.setAdapter(adapter);
        }

        @Override
        public void setupListeners() {

        }

        class ActionsAdapter extends RecyclerArrayAdapter<ActionItem> {

            public ActionsAdapter(Context context, List<ActionItem> actions) {
                super(context, actions);
            }

            @Override
            public ActionViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new ActionViewHolder(parent);
            }

            class ActionViewHolder extends BaseViewHolder<ActionItem> {

                // Views
                private final ImageView imgIcon;
                private final MaterialTextView tvTitle;

                public ActionViewHolder(ViewGroup parent) {
                    super(parent, R.layout.item_action);
                    imgIcon = $(R.id.img_action_icon);
                    tvTitle = $(R.id.tv_action_title);
                }

                @Override
                public void setData(ActionItem action) {
                    this.imgIcon.setImageResource(action.icon);
                    this.tvTitle.setText(action.text);
                    this.itemView.setOnClickListener(v -> {
                        if (listener != null) listener.onResult(action.id);
                        cancel();
                    });
                }
            }

        }
    }

    public static class HijriCalendarBottomSheet extends BaseBottomSheet {

        // todo: Make the BottomSheet responsive to state changes like telegram

        // Statics
        private static final String TAG = "HijriCalendarBottomSheet";
        // Callbacks
        private final View.OnClickListener actionClickListener;
        private final OnIslamicEventClickListener eventClickListener;
        // Runtime and Adapters
        private int dayOfWeek;
        private int pastDaysInWeek;
        private int futureDaysInWeek;
        private CalendarViewManager cvm;
        private IslamicEventsAdapter adapter;
        private List<Date> datesThisWeek;
        private List<HijriDate> hijriDatesThisWeek;
        // Views
        private SingleRowCalendar rvHijriDates;
        private EasyRecyclerView ervIslamicEvents;
        private MaterialButton btnShowEvents, btnOpenFullscreen;

        HijriCalendarBottomSheet(Context context, View.OnClickListener actionClickListener, OnIslamicEventClickListener eventClickListener, ResultCallback<Boolean> showHideCallback) {
            super(context, showHideCallback, false);
            this.actionClickListener = actionClickListener;
            this.eventClickListener = eventClickListener;
            // Setup listeners
            setupListeners();
            // Bind data to views
            bindInnerViews();
        }

        @Override
        public void prepareRuntime() {
            // Do some calculations required for runtime
            dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 1;
            pastDaysInWeek = dayOfWeek == 1 ? 0 : dayOfWeek - 1;
            futureDaysInWeek = 7 - dayOfWeek;
            // Prepare runtime and required data for variables
            final HijriDate hijriDateToday = HijriCalendar.getToday();
            datesThisWeek = com.michalsvec.singlerowcalendar.utils.DateUtils.INSTANCE.getDates(pastDaysInWeek, futureDaysInWeek, true);
            hijriDatesThisWeek = HijriCalendar.getHijriDates(datesThisWeek);
            final IslamicEvent eventToday = IslamicEvents.getEventIn(hijriDateToday);
            // Setup ViewManagers & Adapters
            cvm = new CalendarViewManager() {
                @Override
                public int setCalendarViewResourceId(int position, @NotNull Date date, boolean isSelected) {
                    // todo: care of position, date and selection
                    return R.layout.item_row_hijri_cal;
                }

                @Override
                public void bindDataToCalendarView(@NotNull SingleRowCalendarAdapter.CalendarViewHolder vh, @NotNull Date date, int position, boolean isSelected) {
                    // Check if date is today
                    final boolean isToday = DateUtils.isToday(date);
                    if (isSelected) AManager.log(TAG, "Date[%s] | isToday[%s]", DATE_NORMAL.format(new Timestamp(date)), isToday);
                    // Get views of item
//                    SurroundCardView container = (SurroundCardView) vh.itemView;
//                    MaterialTextView tvDayName = vh.itemView.findViewById(R.id.tv_row_cal_day_name),
//                            tvHijriDayNumber = vh.itemView.findViewById(R.id.tv_row_cal_day),
//                            tvHijriMonthName = vh.itemView.findViewById(R.id.tv_row_cal_month_name);
//                    // Highlight today with stroke
//                    if (isToday) container.setSurroundStrokeColor(R.color.nextPrayCardSurfaceStartColor);
//                    // Change colors according to selection
//                    if (!isSelected) container.setSurrounded(false);
//                    // Init views
//                    if (isToday) tvDayName.setText(R.string.today);
//                    else tvDayName.setText(DateUtils.getDayName(new Timestamp(date), "3"));
//                    tvHijriDayNumber.setText(String.valueOf(hijriDatesThisWeek.get(position).getDay()));
//                    tvHijriMonthName.setText(hijriDatesThisWeek.get(position).getMonthName());
                }
            };
            adapter = new IslamicEventsAdapter(context, eventToday);
        }

        @NonNull
        @NotNull
        @Override
        public View onCreateView() {
            return inflate(R.layout.bs_hijri_calendar);
        }

        @Override
        public void prepareInnerViews() {
            rvHijriDates = findViewById(R.id.rv_hijri_dates);
            ervIslamicEvents = findViewById(R.id.erv_islamic_events);
            btnShowEvents = findViewById(R.id.btn_show_events);
            btnOpenFullscreen = findViewById(R.id.btn_open_full_cal);
        }

        @Override
        public void bindInnerViews() {
            // Init dates week calendar
            rvHijriDates.setCalendarViewManager(cvm);
            rvHijriDates.setPastDaysCount(pastDaysInWeek);
            rvHijriDates.setFutureDaysCount(futureDaysInWeek);
            rvHijriDates.setDeselection(false);
            rvHijriDates.setLongPress(false);
            rvHijriDates.setMultiSelection(false);
            rvHijriDates.setCalendarSelectionManager((position, date) -> true);
            rvHijriDates.setCalendarChangesObserver(new CalendarChangesObserver() {
                @Override
                public void whenWeekMonthYearChanged(@NotNull String s, @NotNull String s1, @NotNull String s2, @NotNull String s3, @NotNull Date date) {

                }

                @Override
                public void whenSelectionChanged(boolean isSelected, int pos, @NotNull Date date) {
                    if (isSelected) adapter.clearThenSet(IslamicEvents.getEventIn(hijriDatesThisWeek.get(pos)));
                }

                @Override
                public void whenCalendarScrolled(int i, int i1) {

                }

                @Override
                public void whenSelectionRestored() {

                }

                @Override
                public void whenSelectionRefreshed() {

                }
            });
            rvHijriDates.setDates(datesThisWeek);
            rvHijriDates.init();
            rvHijriDates.select(dayOfWeek - 1);
            rvHijriDates.smoothScrollToPosition(dayOfWeek - 1);
            // Init events rv
            ervIslamicEvents.setAdapter(adapter);
            ervIslamicEvents.setItemAnimator(new FlipInTopXAnimator());
            ervIslamicEvents.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            adapter.clearThenSet(IslamicEvents.getEventIn(hijriDatesThisWeek.get(dayOfWeek - 1)));
        }

        @Override
        public void setupListeners() {
            // Create click listeners for actions
            if (actionClickListener != null) {
                bs.cancel();
                btnShowEvents.setOnClickListener(actionClickListener);
                btnOpenFullscreen.setOnClickListener(actionClickListener);
            }
        }

        @Override
        public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {
            AManager.log(TAG, "onStateChanged: " + newState);
        }

        @Override
        public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {
            AManager.log(TAG, "onSlide: " + slideOffset);
        }

        class IslamicEventsAdapter extends RecyclerArrayAdapter<IslamicEvent> {

            public IslamicEventsAdapter(Context context, IslamicEvent event) {
                super(context);
                // Setup click listener
                this.setOnItemClickListener(pos -> {
                    if (eventClickListener != null) eventClickListener.onEventClicked(getItem(pos));
                });
            }

            @Override
            public BaseViewHolder<IslamicEvent> OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new BaseViewHolder<IslamicEvent>(parent, R.layout.item_islamic_event) {

                    @Override
                    public void setData(IslamicEvent event) {
                        if (event == null) return;
                        // Init views
                        MaterialCardView cont = (MaterialCardView) itemView;
                        SpannableTextView sptvHijriDay = $(R.id.tv_islamic_hijri_date);
                        MaterialTextView tvEventTitle = $(R.id.tv_islamic_event_title);
                        MaterialTextView tvEventGregDate = $(R.id.tv_islamic_event_greg_date);
                        // Colorize the event card (surface and texts)
                        int pos = getAbsoluteAdapterPosition();
                        CalendarDots dot = CalendarDots.of(pos % CalendarDots.values().length);
                        cont.setCardBackgroundColor(dot.getSurfaceColor(context));
                        sptvHijriDay.setTextColor(dot.getOnSurfaceColor(context));
                        tvEventTitle.setTextColor(dot.getOnSurfaceColor(context));
                        tvEventGregDate.setTextColor(dot.getOnSurfaceColor(context));
                        // Show data in views
                        tvEventTitle.setText(event.getTitleStringResId());
                        tvEventGregDate.setText(HijriCalendar.toGregorian(event).getFormatted(DATE_NORMAL));
                        sptvHijriDay.addSlice(new Slice(new Slice.Builder(event.getDay() + "\n")
                                .style(Typeface.BOLD).textSize(50).textColor(dot.getOnSurfaceColor(context))));
                        sptvHijriDay.addSlice(new Slice(new Slice.Builder(event.getMonthName()).textSize(30).textColor(dot.getOnSurfaceColor(context))));
                        sptvHijriDay.display();
                    }
                };
            }

            public void clearThenSet(IslamicEvent event) {
                if (event == null) ervIslamicEvents.showEmpty();
                else {
                    if (getCount() > 0) remove(0);
                    this.add(event);
                    if (getCount() > 0) ervIslamicEvents.showRecycler();
                    else ervIslamicEvents.showEmpty();
                }
            }
        }

    }

    public static class AnswerPrayedBottomSheet extends BaseBottomSheet {

        // Statics
        private static final String TAG = AnswerPrayedBottomSheet.class.getSimpleName();
        // Runtime
        private final Pray whichPray;
        // Callback
        private final ResultCallback<Pair<PrayStatus, Boolean>> callback;
        // Views
        private RadioGroup rgWhen, rgWhere, rgIsJama3a;
        private MaterialButton btnSubmit;
        private MaterialTextView tvTitle2;

        AnswerPrayedBottomSheet(Context context, Pray whichPray, ResultCallback<Pair<PrayStatus, Boolean>> callback, ResultCallback<Boolean> showHideCallback) {
            super(context, showHideCallback, true);
            this.callback = callback;
            this.whichPray = whichPray;
            // Bind data to views
            setupListeners();
            bindInnerViews();
        }

        @Override
        public void prepareRuntime() {
        }

        @NonNull
        @NotNull
        @Override
        public View onCreateView() {
            return View.inflate(context, R.layout.bs_answer_prayed, null);
        }

        @Override
        public void prepareInnerViews() {
            this.rgWhen = findViewById(R.id.radio_group_prayed_when);
            this.rgWhere = findViewById(R.id.radio_group_prayed_where);
            this.tvTitle2 = findViewById(R.id.t3);
            this.btnSubmit = findViewById(R.id.btn_submit);
        }

        @Override
        public void bindInnerViews() {
            rgWhere.setVisibility(View.GONE);
            tvTitle2.setVisibility(View.GONE);
        }

        @Override
        public void setupListeners() {
            // Selection change listener
            rgWhen.setOnCheckedChangeListener((group, checkedId) -> {
                boolean hasPrayed = checkedId == R.id.radio_prayed_on_time || checkedId == R.id.radio_prayed_late;
                if (!hasPrayed) rgWhere.clearCheck();
                rgWhere.setVisibility(hasPrayed ? View.VISIBLE : View.GONE);
                tvTitle2.setVisibility(hasPrayed ? View.VISIBLE : View.GONE);
            });
//			rgIsJama3a.setOnCheckedChangeListener((group, checkedId) -> {
//
//			});
            // Click listeners
            btnSubmit.setOnClickListener(v -> {
                // Get status
                final @IdRes int ansWhen = rgWhen.getCheckedRadioButtonId();
                if (ansWhen == -1) {
                    // Requires selection
                    Toast.makeText(context, getString(R.string.when_did_you_pray), Toast.LENGTH_SHORT).show();
                } else if (ansWhen == R.id.radio_prayed_on_time && rgWhere.getCheckedRadioButtonId() == -1) {
                    // Requires selection
                    Toast.makeText(context, getString(R.string.where_did_you_pray), Toast.LENGTH_SHORT).show();
                } else {
                    // Answered
                    PrayStatus status = PrayStatus.FORGOT;
                    if (ansWhen == R.id.radio_prayed_on_time) status = PrayStatus.ON_TIME;
                    else if (ansWhen == R.id.radio_prayed_late) status = PrayStatus.DELAYED;
                    // Fire the callback with result
                    fireCallback(status, rgWhere.getCheckedRadioButtonId() == R.id.radio_prayed_at_mosque && rgWhere.getVisibility() == View.VISIBLE);
                }
            });
        }

        void fireCallback(PrayStatus status, boolean atMosque) {
            if (callback != null) callback.onResult(new Pair<>(status, atMosque));
            this.cancel();
        }

    }

    public static class AllahNamesBottomSheet extends BaseBottomSheet {

        // todo: Start the BottomSheet in {half_expanded} state showing the name and if user expanded it to {full_screen} or clicked {showAllNames} -> animate the views switch between allNames and nameView

        // Callbacks
        private final ResultCallback<AllahName> selectedNameCallback;
        // Runtime
        private AllahName allahName;
        // Views
        private MaterialTextView tvNameOrdinal,
                tvNameContent,
                tvNameDesc;
        private MaterialButton btnNext,
                btnPrev,
                btnRandomize,
                btnShowAll,
                btnShare;

        public AllahNamesBottomSheet(Context context, AllahName allahName, ResultCallback<AllahName> selectedNameCallback, ResultCallback<Boolean> showHideCallback) {
            super(context, showHideCallback, true);
            this.allahName = allahName;
            this.selectedNameCallback = selectedNameCallback;
            // Bind data to views
            bindInnerViews();
        }

        @NonNull
        @NotNull
        @Override
        public View onCreateView() {
            return inflate(R.layout.bs_allah_names);
        }

        @Override
        public void prepareInnerViews() {
            // TextViews
            tvNameOrdinal = findViewById(R.id.tv_name_of_allah_ordinal);
            tvNameContent = findViewById(R.id.tv_allah_name_content);
            tvNameDesc = findViewById(R.id.tv_allah_name_desc);
            // Action Buttons
            btnNext = findViewById(R.id.btn_prev_name);
            btnPrev = findViewById(R.id.btn_next_name);
            btnRandomize = findViewById(R.id.btn_random_name);
            btnShowAll = findViewById(R.id.btn_show_all_names);
            btnShare = findViewById(R.id.btn_share);
        }

        @Override
        public void bindInnerViews() {
            tvNameOrdinal.setText(String.valueOf(allahName.getOrdinal() + 1));
            tvNameContent.setText(allahName.getName());
            tvNameDesc.setText(allahName.getDesc());
        }

        @Override
        public void setupListeners() {
            btnNext.setOnClickListener(v -> {
                // Update runtime and UI here
                allahName = AManager.getAllahName(context, allahName.getOrdinal() + 1);
                bindInnerViews();
                // Update card runtime and UI also
                selectedNameCallback.onResult(allahName);
            });
            btnPrev.setOnClickListener(v -> {
                // Update runtime and UI here
                allahName = AManager.getAllahName(context, allahName.getOrdinal() - 1);
                bindInnerViews();
                // Update card runtime and UI also
                selectedNameCallback.onResult(allahName);
            });
            btnRandomize.setOnClickListener(v -> {
                // Update runtime and UI here
                final AllahName ranAllahName = AManager.getRandomizedAllahName(context);
                allahName = allahName.equals(ranAllahName) ? AManager.getRandomizedAllahName(context) : ranAllahName;
                bindInnerViews();
                // Update card runtime and UI also
                selectedNameCallback.onResult(allahName);
            });
            btnShowAll.setOnClickListener(v -> context.startActivity(new Intent(context, HolyNamesOfAllahActivity.class)));
            btnShare.setOnClickListener(v -> {
                // todo: code this
                Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();
            });
        }

    }

    public static class SoMReminderBottomSheet extends BaseBottomSheet {

        // Runtime
        private final SoMReminderFreq reminderFreq;
        // Callbacks
        private final ResultCallback<SoMReminderFreq> freqChangeCallback;
        // Views
        private SpannableTextView stvFreq;
        private SwitcherX switcherEnable;
        private MaterialButton btnFreq;

        SoMReminderBottomSheet(Context context, SoMReminderFreq reminderFreq, ResultCallback<Boolean> showHideCallback, ResultCallback<SoMReminderFreq> freqChangeCallback) {
            super(context, showHideCallback, true);
            this.reminderFreq = reminderFreq;
            this.freqChangeCallback = freqChangeCallback;
        }

        @Override
        public View onCreateView() {
            return inflate(R.layout.bs_som_reminder_settings);
        }

        @Override
        public void prepareInnerViews() {

        }

        @Override
        public void bindInnerViews() {

        }

        @Override
        public void setupListeners() {

        }
    }

    public static class EventDetailsBottomSheet extends BaseBottomSheet {

        // Runtime
        private final IslamicEvent event;
        // Views
        private MaterialTextView tvHijriDate, tvEventTitle, tvEventDesc;
        private MaterialButton btnShare;

        EventDetailsBottomSheet(Context context, IslamicEvent event) {
            super(context, null, true);
            // Runtime
            this.event = event;
            // Setup listeners
            this.setupListeners();
            // Bind views
            this.bindInnerViews();
        }

        @NonNull
        @NotNull
        @Override
        public View onCreateView() {
            return inflate(R.layout.bs_islamic_event_details);
        }

        @Override
        public void prepareInnerViews() {
            tvEventTitle = findViewById(R.id.tv_islamic_event_title);
            tvHijriDate = findViewById(R.id.tv_islamic_event_date);
            tvEventDesc = findViewById(R.id.tv_islamic_event_desc);
            btnShare = findViewById(R.id.btn_share_event);
        }

        @Override
        public void bindInnerViews() {
            // Title and Desc
            tvEventTitle.setText(event.getTitleStringResId());
            tvEventDesc.setText(event.getDescStringResId());
            // Date (Hijri + Georg)
            tvHijriDate.setText(String.format(Locale.getDefault(),
                    "%d %s %d%s | %s",
                    event.getDay(),
                    event.getMonthName(),
                    event.getYear(),
                    getString(R.string.H),
                    event.toGregorian().getFormatted(DATE_NORMAL)));
        }

        @Override
        public void setupListeners() {
            this.btnShare.setOnClickListener(v -> {
                // todo: Share the event
                Toast.makeText(context, "TODO[Sharing: " + event + "]", Toast.LENGTH_SHORT).show();
            });
        }
    }

}
