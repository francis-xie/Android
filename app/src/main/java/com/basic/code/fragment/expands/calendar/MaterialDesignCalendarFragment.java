
package com.basic.code.fragment.expands.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basic.date.CalendarDate;
import com.basic.date.CalendarDateView;
import com.basic.page.annotation.Page;
import com.basic.face.utils.DensityUtils;
import com.basic.face.utils.ThemeUtils;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "MaterialDesign风格日历")
public class MaterialDesignCalendarFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.calendarDateView)
    CalendarDateView calendarDateView;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_material_design_calendar;
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
            textView.setBackgroundResource(R.drawable.bg_calendar_material_design_item);

            textView.setText(String.valueOf(calendarDate.day));

            if (calendarDate.monthFlag != 0) {
                textView.setTextColor(0xFF9299A1);
            } else {
                if (calendarDate.isToday() && calendarDate.equals(calendarDateView.getSelectCalendarDate())) {
                    textView.setTextColor(ThemeUtils.getMainThemeColor(getContext()));
                } else {
                    textView.setTextColor(ThemeUtils.resolveColor(getContext(), R.attr.face_config_color_content_text));
                }
            }
            return convertView;
        });

        calendarDateView.setOnCalendarSelectedListener((view, postion, calendarDate) -> XToastUtils.toast("选中:" + calendarDate.formatDate()));

        calendarDateView.setOnTodaySelectStatusChangedListener((todayView, isSelected) -> {
            TextView view = todayView.findViewById(R.id.tv_text);
            if (isSelected) {
                view.setTextColor(ThemeUtils.resolveColor(getContext(), R.attr.face_config_color_content_text));
            } else {
                view.setTextColor(ThemeUtils.getMainThemeColor(getContext()));
            }
        });

        calendarDateView.setOnMonthChangedListener((view, position, date) -> tvTitle.setText(String.format("%d年%d月", date.year, date.month)));

        CalendarDate date = CalendarDate.get(new Date());
        tvTitle.setText(String.format("%d年%d月", date.year, date.month));
    }

    @OnClick({R.id.iv_previous, R.id.iv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_previous:
                calendarDateView.previous();
                break;
            case R.id.iv_next:
                calendarDateView.next();
                break;
            default:
                break;
        }
    }
}
