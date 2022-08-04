package com.basic.code.fragment.components.popupwindow;

import android.view.Gravity;

import com.basic.page.annotation.Page;
import com.basic.face.widget.toast.XToast;
import com.basic.code.R;
import com.basic.code.base.BaseSimpleListFragment;
import com.basic.code.utils.ToastUtils;

import java.util.List;

@Page(name = "XToast\n多重样式的Toast显示")
public class XToastFragment extends BaseSimpleListFragment {

    @Override
    protected void initArgs() {
        super.initArgs();
        XToast.Config.get()
                //位置设置为居中
                .setGravity(Gravity.CENTER);
    }

    /**
     * 初始化例子
     *
     * @param lists
     * @return
     */
    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("ERROR_TOAST");
        lists.add("SUCCESS_TOAST");
        lists.add("INFO_TOAST");
        lists.add("WARNING_TOAST");
        lists.add("NORMAL_TOAST");
        return lists;
    }

    /**
     * 条目点击
     *
     * @param position
     */
    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                ToastUtils.error(R.string.error_message);
                break;
            case 1:
                ToastUtils.success(R.string.success_message);
                break;
            case 2:
                ToastUtils.info(R.string.info_message);
                break;
            case 3:
                ToastUtils.warning(R.string.warning_message);
                break;
            case 4:
                ToastUtils.toast(R.string.normal_message_without_icon);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        XToast.Config.get()
                //位置还原
                .resetGravity();
        super.onDestroyView();
    }
}
