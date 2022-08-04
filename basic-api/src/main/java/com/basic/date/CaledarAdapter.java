package com.basic.date;

import android.view.View;
import android.view.ViewGroup;

/**
 * 日历的适配器
 */
public interface CaledarAdapter {
     View getView(View convertView, ViewGroup parentView, CalendarDate calendarDate);
}
