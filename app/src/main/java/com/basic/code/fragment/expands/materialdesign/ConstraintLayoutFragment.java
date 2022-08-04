
package com.basic.code.fragment.expands.materialdesign;

import android.os.Build;

import com.basic.page.annotation.Page;
import com.basic.page.core.PageOption;
import com.basic.code.R;
import com.basic.code.base.BaseSimpleListFragment;
import com.basic.code.fragment.expands.materialdesign.constraintlayout.ConstraintLayoutConstraintSetFragment;
import com.basic.code.fragment.expands.materialdesign.constraintlayout.ConstraintLayoutContainerFragment;
import com.basic.code.fragment.expands.materialdesign.constraintlayout.ConstraintLayoutGroupFragment;
import com.basic.code.fragment.expands.materialdesign.constraintlayout.ConstraintLayoutPlaceholderFragment;
import com.basic.code.utils.ToastUtils;

import java.util.List;

import static com.basic.code.fragment.expands.materialdesign.constraintlayout.ConstraintLayoutContainerFragment.KEY_LAYOUT_ID;
import static com.basic.code.fragment.expands.materialdesign.constraintlayout.ConstraintLayoutContainerFragment.KEY_TITLE;

@Page(name = "ConstraintLayout\n约束布局")
public class ConstraintLayoutFragment extends BaseSimpleListFragment {

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("对齐方式");
        lists.add("尺寸约束");
        lists.add("链约束");
        lists.add("导航线Guideline使用");
        lists.add("栅栏Barrier使用");
        lists.add("分组Group使用");
        lists.add("占位符Placeholder使用");
        lists.add("ConstraintSet实现切换动画");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                gotoExample(mSimpleData.get(position), R.layout.layout_constraint_align);
                break;
            case 1:
                gotoExample(mSimpleData.get(position), R.layout.layout_constraint_size);
                break;
            case 2:
                gotoExample(mSimpleData.get(position), R.layout.layout_constraint_chain);
                break;
            case 3:
                gotoExample(mSimpleData.get(position), R.layout.layout_constraint_guideline);
                break;
            case 4:
                gotoExample(mSimpleData.get(position), R.layout.layout_constraint_barrier);
                break;
            case 5:
                openPage(ConstraintLayoutGroupFragment.class);
                break;
            case 6:
                openPage(ConstraintLayoutPlaceholderFragment.class);
                break;
            case 7:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    ToastUtils.warning("当前手机版本过低，暂不支持");
                } else {
                    openPage(ConstraintLayoutConstraintSetFragment.class);
                }
                break;
            default:
                break;
        }
    }


    private void gotoExample(String title, int layoutId) {
        PageOption.to(ConstraintLayoutContainerFragment.class)
                .putString(KEY_TITLE, title)
                .putInt(KEY_LAYOUT_ID, layoutId)
                .open(this);
    }
}
