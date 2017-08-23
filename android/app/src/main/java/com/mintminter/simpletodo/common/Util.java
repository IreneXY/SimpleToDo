package com.mintminter.simpletodo.common;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.mintminter.simpletodo.R;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Irene on 8/22/17.
 */

public class Util {

    public static int getColor(Context context, int colorId){
        return context.getResources().getColor(colorId);
    }

    public static int getNormalPriorityColor(Context context){
        return getColor(context, R.color.priorityNormal);
    }

    public static int getLowPriorityColor(Context context){
        return getColor(context, R.color.priorityLow);
    }

    public static int getHighPriorityColor(Context context){
        return getColor(context, R.color.priorityHigh);
    }

    public static String getString(Context context, int stringId){
        return context.getResources().getString(stringId);
    }

    public static Drawable getDrawable(Context context, int drawableId){
        return context.getResources().getDrawable(drawableId);
    }

    public static String formatTime(long timeInMillis){
        Date date = new Date(timeInMillis);
        return new SimpleDateFormat("MMM dd, yyyy HH:mm").format(date);
    }

    public static class DecendingPriorityComparator implements Comparator<Event> {

        @Override
        public int compare(Event lhs, Event rhs) {
            if(lhs.mPriority != rhs.mPriority) {
                return rhs.mPriority - lhs.mPriority;
            }else{
                return (int) (lhs.mCreateTime - rhs.mCreateTime);
            }
        }
    }

    public static class AscendingCreateTimeComparator implements Comparator<Event> {

        @Override
        public int compare(Event lhs, Event rhs) {
            return (int) (lhs.mCreateTime - rhs.mCreateTime);
        }
    }

    public static class AscendingDoneTimeComparator implements Comparator<Event> {

        @Override
        public int compare(Event lhs, Event rhs) {
            return (int) (lhs.mDoneTime - rhs.mDoneTime);
        }
    }
}
