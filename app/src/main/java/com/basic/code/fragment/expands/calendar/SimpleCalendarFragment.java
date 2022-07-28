
package com.basic.code.fragment.expands.calendar;

import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basic.refresh.layout.RefreshLayouts;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.basic.refresh.layout.api.RefreshLayout;
import com.basic.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.adapter.NewsCardViewListAdapter;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.Utils;
import com.basic.code.widget.MaterialFooter;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

@Page(name = "简单的日历控件\n支持自定义样式")
public class SimpleCalendarFragment extends BaseFragment implements CalendarView.OnCalendarSelectListener {

    @BindView(R.id.refreshLayout)
    RefreshLayouts refreshLayout;
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.calendarLayout)
    CalendarLayout calendarLayout;


    private NewsCardViewListAdapter mAdapter;

    private TitleBar mTitleBar;

    @Override
    protected TitleBar initTitle() {
        mTitleBar = super.initTitle();
        mTitleBar.addAction(new TitleBar.TextAction("复杂") {
            @SingleClick
            @Override
            public void performAction(View view) {
                openPage(ComplexCalendarFragment.class);
            }
        });
        return mTitleBar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_simple_calendar;
    }

    @Override
    protected void initViews() {
        mTitleBar.setTitle(calendarView.getCurMonth() + "月" + calendarView.getCurDay() + "日");

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter = new NewsCardViewListAdapter());
        refreshLayout.setRefreshFooter(new MaterialFooter(getContext()));

        initCalendarView();
    }

    private void initCalendarView() {
        final int year = calendarView.getCurYear();
        final int month = calendarView.getCurMonth();
        Map<String, Calendar> map = new HashMap<>();
        for (int j = 5; j < 10; j++) {
            map.put(getSchemeCalendar(year, month, j, Color.RED).toString(), getSchemeCalendar(year, month, j, Color.RED));
        }
        for (int i = 10; i < 28; i++) {
            map.put(getSchemeCalendar(year, month, i, Color.TRANSPARENT).toString(), getSchemeCalendar(year, month, i, Color.TRANSPARENT));
        }
        calendarView.setSchemeDate(map);
    }

    public Calendar getSchemeCalendar(int year, int month, int day, int color) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        return calendar;
    }

    @Override
    protected void initListeners() {
        mAdapter.setOnItemClickListener((itemView, item, position) -> Utils.goWeb(getContext(), item.getDetailUrl()));

        calendarView.setOnCalendarSelectListener(this);

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    mAdapter.refresh(DemoDataProvider.getDemoNewInfos());
                    refreshLayout.finishRefresh();
                }, 1000);

            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(() -> {
                    mAdapter.loadMore(DemoDataProvider.getDemoNewInfos());
                    refreshLayout.finishLoadMore();
                }, 1000);

            }
        });
        refreshLayout.autoRefresh();
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTitleBar.setTitle(calendar.getMonth() + "月" + calendar.getDay() + "日");
        if (isClick) {
            if (calendarLayout.isExpand()) {
                calendarLayout.shrink();
            }
        }
    }

}
