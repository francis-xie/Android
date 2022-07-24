
package com.basic.code.fragment.expands.calendar;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.basic.date.CalendarDate;
import com.basic.date.CalendarDateView;
import com.basic.page.annotation.Page;
import com.basic.face.utils.DensityUtils;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.adapter.SimpleListViewAdapter;
import com.basic.code.base.BaseFragment;

import java.util.Date;

import butterknife.BindView;

/**

 * @since 2019-05-29 19:53
 */
@Page(name = "叮叮日历")
public class DingDingCalendarFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.calendarDateView)
    CalendarDateView calendarDateView;
    @BindView(R.id.listview)
    ListView listview;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ding_ding_calendar;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        calendarDateView.setAdapter((convertView, parentView, calendarDate) -> {
            TextView textView;
            if (convertView == null) {
                convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.adapter_calendar_item, null);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DensityUtils.dp2px(48), DensityUtils.dp2px(48));
                convertView.setLayoutParams(params);
            }

            textView = convertView.findViewById(R.id.tv_text);
            textView.setBackgroundResource(R.drawable.bg_calendar_ding_ding_item);

            textView.setText(String.valueOf(calendarDate.day));
            if (calendarDate.monthFlag != 0) {
                textView.setTextColor(0xFF9299A1);
            } else {
                textView.setTextColor(0xFFFFFFFF);
            }

            return convertView;
        });

        calendarDateView.setOnCalendarSelectedListener((view, position, calendarDate) -> tvTitle.setText(String.format("%d/%d/%d", calendarDate.year, calendarDate.month, calendarDate.day)));

        calendarDateView.setOnMonthChangedListener((view, postion, date) -> tvTitle.setText(String.format("%d/%d/%d", date.year, date.month, date.day)));

        CalendarDate data = CalendarDate.get(new Date());
        tvTitle.setText(String.format("%d/%d/%d", data.year, data.month, data.day));


        listview.setAdapter(new SimpleListViewAdapter(getContext(), DemoDataProvider.getDemoData()));

    }


}
