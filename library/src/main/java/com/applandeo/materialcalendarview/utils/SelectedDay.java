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
    private View mView;
    private Calendar mCalendar;

    public SelectedDay(View view, Calendar calendar) {
        mView = view;
        mCalendar = calendar;
    }

    public View getView() {
        return mView;
    }

    public void setView(View View) {
        this.mView = mView;
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

