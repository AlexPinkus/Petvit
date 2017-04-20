package com.alexpinkus.petvit.model;

import com.alexpinkus.petvit.widget.DaysOfWeek;

/**
 * Created by AlexP on 19-Jan-17.
 */

public class AlarmItem {
    private boolean showNotify=false;

    private String mTitle;
    private int mHour;
    private int mMinute;
    private int mGrams;
    private int mDaysOfWeek;
    private boolean mActive=false;


    //Constructor vacío.
    public AlarmItem() {
        //Empty constructor.
    }

    //Constructor para asignar todas las propiedades.
    public AlarmItem(String title, int hour, int minute, int grams,int daysOfWeek, boolean active)
    {
        mTitle=title;
        mHour=hour;
        mMinute=minute;
        mGrams=grams;
        mDaysOfWeek=daysOfWeek;
        mActive=active;
    }

    public AlarmItem(boolean showNotify, String title) {
        this.showNotify = showNotify;
        this.mTitle = title;
    }

    public boolean isShowNotify() {
        return showNotify;
    }
    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    //Getters y setters para cada propiedad de nuestra clase.
    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }

    public int getHour() {
        return mHour;
    }
    public void setHour(int hour) {
        mHour = hour;
    }

    public int getMinute() {
        return mMinute;
    }
    public void setMinute(int minute) {
        mMinute = minute;
    }

    public int getGrams() {
        return mGrams;
    }
    public void setGrams(int grams) {
        mGrams = grams;
    }

    public boolean isActive() {
        return mActive;
    }
    public void setActive(boolean active) {
        mActive = active;
    }

    public int getDaysOfWeek() {
        return mDaysOfWeek;
    }
    public void setDaysOfWeek(int daysOfWeek) {
        mDaysOfWeek = daysOfWeek;
    }

    @Override
    public String toString() {
        return mTitle+":"+pad(mHour)+":"+ pad(mMinute);
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
}
