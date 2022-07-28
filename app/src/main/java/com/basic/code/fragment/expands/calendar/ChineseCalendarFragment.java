
package com.basic.code.fragment.expands.calendar;

import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.TextView;

import com.basic.date.CalendarDate;
import com.basic.date.CalendarDateView;
import com.basic.date.ChinaDateUtils;
import com.basic.page.annotation.Page;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.adapter.SimpleListViewAdapter;
import com.basic.code.base.BaseFragment;

import java.util.Date;

import butterknife.BindView;

@Page(name = "农历日历")
public class ChineseCalendarFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.calendarDateView)
    CalendarDateView calendarDateView;
    @BindView(R.id.list)
    ListView listView;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chinese_calendar;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        calendarDateView.setAdapter((convertView, parentView, calendarDate) -> {

            if (convertView == null) {
                convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.adapter_calendar_chinese, null);
            }

            TextView chinaText = convertView.findViewById(R.id.chinaText);
            TextView text = convertView.findViewById(R.id.text);
            text.setText(String.valueOf(calendarDate.day));

            if (calendarDate.monthFlag != 0) {
                text.setTextColor(0xFF9299A1);
            } else {
                text.setTextColor(0xFF444444);
            }
            chinaText.setText(calendarDate.chinaDay);

            return convertView;
        });

        calendarDateView.setOnCalendarSelectedListener((view, position, date) -> tvTitle.setText(ChinaDateUtils.oneDay(date.year, date.month, date.day)));

        calendarDateView.setOnMonthChangedListener((view, position, date) -> tvTitle.setText(ChinaDateUtils.oneDay(date.year, date.month, date.day)));

        CalendarDate data = CalendarDate.get(new Date());
        tvTitle.setText(ChinaDateUtils.oneDay(data.year, data.month, data.day));


        listView.setAdapter(new SimpleListViewAdapter(getContext(), DemoDataProvider.getDemoData()));
    }


}
