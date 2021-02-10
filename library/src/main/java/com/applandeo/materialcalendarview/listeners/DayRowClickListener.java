package com.applandeo.materialcalendarview.listeners;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.applandeo.materialcalendarview.CalendarUtils;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.R;
import com.applandeo.materialcalendarview.adapters.CalendarPageAdapter;
import com.applandeo.materialcalendarview.utils.CalendarProperties;
import com.applandeo.materialcalendarview.utils.DateUtils;
import com.applandeo.materialcalendarview.utils.DayColorsUtils;
import com.applandeo.materialcalendarview.utils.SelectedDay;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * This class is responsible for handle click events
 * <p>
 * Created by Mateusz Kornakiewicz on 24.05.2017.
 */

public class DayRowClickListener implements AdapterView.OnItemClickListener {

    private CalendarPageAdapter mCalendarPageAdapter;

    private CalendarProperties mCalendarProperties;
    private int mPageMonth;

    public DayRowClickListener(CalendarPageAdapter calendarPageAdapter, CalendarProperties calendarProperties, int pageMonth) {
        mCalendarPageAdapter = calendarPageAdapter;
        mCalendarProperties = calendarProperties;
        mPageMonth = pageMonth < 0 ? 11 : pageMonth;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Calendar day = new GregorianCalendar();
        day.setTime((Date) adapterView.getItemAtPosition(position));

        if(mCalendarProperties.getSelectionDisabled()) {
            return;
        }

        if (mCalendarProperties.getOnDayClickListener() != null) {
            onClick(day);
        }

        switch (mCalendarProperties.getCalendarType()) {
            case CalendarView.ONE_DAY_PICKER:
                selectOneDay(view, day);
                break;

            case CalendarView.MANY_DAYS_PICKER:
                selectManyDays(view, day);
                break;

            case CalendarView.RANGE_PICKER:
                selectRange(view, day);
                break;

            case CalendarView.CLASSIC:
                mCalendarPageAdapter.setSelectedDay(new SelectedDay(view, day));
        }
    }

    private void selectOneDay(View view, Calendar day) {
        SelectedDay previousSelectedDay = mCalendarPageAdapter.getSelectedDay();

        LinearLayout linearLayout = view.findViewById(R.id.container);
        TextView dayLabel = view.findViewById(R.id.dayLabel);
        TextView dayDescription = view.findViewById(R.id.dayDescription);

        if (isAnotherDaySelected(previousSelectedDay, day)) {
            selectDay(linearLayout, dayLabel, dayDescription, day);
            reverseUnselectedColor(view, previousSelectedDay);
        }
    }

    private void selectManyDays(View view, Calendar day) {
        LinearLayout linearLayout = view.findViewById(R.id.container);
        TextView dayLabel = view.findViewById(R.id.dayLabel);
        TextView dayDescription = view.findViewById(R.id.dayDescription);

        if (isCurrentMonthDay(day) && isActiveDay(day)) {
            SelectedDay selectedDay = new SelectedDay(view, day);

            if (!mCalendarPageAdapter.getSelectedDays().contains(selectedDay)) {
                DayColorsUtils.setSelectedDayColors(linearLayout, dayLabel, dayDescription, mCalendarProperties);
            } else {
                reverseUnselectedColor(view, selectedDay);
            }

            mCalendarPageAdapter.addSelectedDay(selectedDay);
        }
    }

    private void selectRange(View view, Calendar day) {
        LinearLayout linearLayout = view.findViewById(R.id.container);
        TextView dayLabel = (TextView) view.findViewById(R.id.dayLabel);
        TextView dayDescription = (TextView) view.findViewById(R.id.dayDescription);

        if (!isCurrentMonthDay(day) || !isActiveDay(day)) {
            return;
        }

        List<SelectedDay> selectedDays = mCalendarPageAdapter.getSelectedDays();

        if (selectedDays.size() > 1) {
            clearAndSelectOne(linearLayout, dayLabel, dayDescription, day);
        }

        if (selectedDays.size() == 1) {
            selectOneAndRange(linearLayout, dayLabel, dayDescription, day);
        }

        if (selectedDays.isEmpty()) {
            selectDay(linearLayout, dayLabel, dayDescription, day);
        }
    }

    private void clearAndSelectOne(LinearLayout linearLayout, TextView dayLabel, TextView dayDescription, Calendar day) {
        Stream.of(mCalendarPageAdapter.getSelectedDays()).forEach(selectedDay -> reverseUnselectedColor(linearLayout, selectedDay));
        selectDay(linearLayout, dayLabel, dayDescription, day);
    }

    private void selectOneAndRange(LinearLayout linearLayout, TextView dayLabel, TextView dayDescription, Calendar day) {
        SelectedDay previousSelectedDay = mCalendarPageAdapter.getSelectedDay();

        Stream.of(CalendarUtils.getDatesRange(previousSelectedDay.getCalendar(), day))
                .filter(calendar -> !mCalendarProperties.getDisabledDays().contains(calendar))
                .forEach(calendar -> mCalendarPageAdapter.addSelectedDay(new SelectedDay(linearLayout, calendar)));

        if (isOutOfMaxRange(previousSelectedDay.getCalendar(), day)) {
            return;
        }

        DayColorsUtils.setSelectedDayColors(linearLayout, dayLabel, dayDescription,  mCalendarProperties);

        mCalendarPageAdapter.addSelectedDay(new SelectedDay(linearLayout, day));
        mCalendarPageAdapter.notifyDataSetChanged();
    }

    private void selectDay(LinearLayout linearLayout, TextView dayLabel, TextView dayDescription, Calendar day) {
        DayColorsUtils.setSelectedDayColors(linearLayout, dayLabel, dayDescription, mCalendarProperties);
        mCalendarPageAdapter.setSelectedDay(new SelectedDay(linearLayout, day));
    }

    private void reverseUnselectedColor(View view, SelectedDay selectedDay) {
        DayColorsUtils.setupDay(view, selectedDay.getCalendar(),
                DateUtils.getCalendar(), mCalendarProperties, mPageMonth, false);
    }

    private boolean isCurrentMonthDay(Calendar day) {
        return day.get(Calendar.MONTH) == mPageMonth && isBetweenMinAndMax(day);
    }

    private boolean isActiveDay(Calendar day) {
        return !mCalendarProperties.getDisabledDays().contains(day);
    }

    private boolean isBetweenMinAndMax(Calendar day) {
        return !((mCalendarProperties.getMinimumDate() != null && day.before(mCalendarProperties.getMinimumDate()))
                || (mCalendarProperties.getMaximumDate() != null && day.after(mCalendarProperties.getMaximumDate())));
    }

    private boolean isOutOfMaxRange(Calendar firstDay, Calendar lastDay) {
        // Number of selected days plus one last day
        int numberOfSelectedDays = CalendarUtils.getDatesRange(firstDay, lastDay).size() + 1;
        int daysMaxRange = mCalendarProperties.getMaximumDaysRange();

        return daysMaxRange != 0 && numberOfSelectedDays >= daysMaxRange;
    }

    private boolean isAnotherDaySelected(SelectedDay selectedDay, Calendar day) {
        return selectedDay != null && !day.equals(selectedDay.getCalendar())
                && isCurrentMonthDay(day) && isActiveDay(day);
    }

    private void onClick(Calendar day) {
        if (mCalendarProperties.getEventDays() == null) {
            createEmptyEventDay(day);
            return;
        }

        Stream.of(mCalendarProperties.getEventDays())
                .filter(eventDate -> eventDate.getCalendar().equals(day))
                .findFirst()
                .ifPresentOrElse(this::callOnClickListener, () -> createEmptyEventDay(day));
    }

    private void createEmptyEventDay(Calendar day) {
        EventDay eventDay = new EventDay(day);
        callOnClickListener(eventDay);
    }

    private void callOnClickListener(EventDay eventDay) {
        boolean enabledDay = mCalendarProperties.getDisabledDays().contains(eventDay.getCalendar())
                || !isBetweenMinAndMax(eventDay.getCalendar());

        eventDay.setEnabled(enabledDay);
        mCalendarProperties.getOnDayClickListener().onDayClick(eventDay);
    }
}
