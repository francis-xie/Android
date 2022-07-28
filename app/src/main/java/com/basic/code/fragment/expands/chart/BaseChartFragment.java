package com.basic.code.fragment.expands.chart;

import com.github.mikephil.charting.charts.Chart;
import com.basic.aop.annotation.Permission;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;

import static com.basic.aop.consts.PermissionConsts.STORAGE;

/**
 * 基础图表
 */
public abstract class BaseChartFragment extends BaseFragment {

    protected final String[] parties = new String[]{
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    /**
     * 初始化图表的样式
     */
    protected abstract void initChartStyle();


    /**
     * 初始化图表的 标题 样式
     */
    protected abstract void initChartLabel();

    /**
     * 设置图表数据
     *
     * @param count 一组数据的数量
     * @param range
     */
    protected abstract void setChartData(int count, float range);

    /**
     * 图表保存
     *
     * @param chart
     * @param name
     */
    @Permission(STORAGE)
    public void saveToGallery(Chart chart, String name) {
        if (chart.saveToGallery(name + "_" + System.currentTimeMillis(), 70)) {
            XToastUtils.toast("保存成功!");

        } else {
            XToastUtils.toast("保存失败!");
        }
    }

}
