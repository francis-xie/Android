package com.freak.printtool.hardware.module.wifi.adapter;


import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.emis.venus.R;
import com.freak.printtool.hardware.module.wifi.adapter.bean.PrinterSettingBean;

import java.util.List;

public class PrinterSettingAdapter extends BaseQuickAdapter<PrinterSettingBean, BaseViewHolder> {
    public PrinterSettingAdapter(int layoutResId, @Nullable List<PrinterSettingBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PrinterSettingBean item) {
        helper.setText(R.id.text_view_ip, item.getIp());
        if (item.isSelect()) {
            helper.getView(R.id.linear_layout_ip).setSelected(true);
        } else {
            helper.getView(R.id.linear_layout_ip).setSelected(false);
        }
    }
}
