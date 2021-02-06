package com.applandeo.materialcalendarview.utils;

import android.view.View;

import java.util.Calendar;

/**
 * This helper class represent a selected day when calendar is in a picker date mode.
 * It is used to remember a selected calendar cell.
 * <p>
 * Created by Mateusz Kornakiewicz on 23.05.2017.
 */

public class SelectedDay {
    private View mLabelView;
    private View mDescriptionView;
    private Calendar mCalendar;

    public SelectedDay(Calendar calendar) {
        mCalendar = calendar;
    }

    /**
     * @param labelView     View representing selected calendar cell
     * @param calendar Calendar instance representing selected cell date
     */
    public SelectedDay(View labelView, View descriptionView, Calendar calendar) {
        mLabelView = labelView;
        mDescriptionView = descriptionView;
        mCalendar = calendar;
    }

    /**
     * @return View representing selected calendar cell
     */
    public View getLabelView() {
        return mLabelView;
    }

    public void setLabelView(View view) {
        mLabelView = view;
    }

    public View getDescriptionView() {
        return mDescriptionView;
    }

    public void setDescriptionView(View descriptionView) {
        this.mDescriptionView = descriptionView;
    }

    /**
     * @return Calendar instance representing selected cell date
     */
    public Calendar getCalendar() {
        return mCalendar;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SelectedDay) {
            return getCalendar().equals(((SelectedDay) obj).getCalendar());
        }

        if(obj instanceof Calendar){
            return getCalendar().equals(obj);
        }

        return super.equals(obj);
    }
}

