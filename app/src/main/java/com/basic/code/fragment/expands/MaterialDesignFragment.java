package com.basic.code.fragment.expands;

import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.activity.SettingsActivity;
import com.basic.code.base.BaseSimpleListFragment;
import com.basic.code.fragment.expands.materialdesign.BehaviorFragment;
import com.basic.code.fragment.expands.materialdesign.BottomSheetDialogFragment;
import com.basic.code.fragment.expands.materialdesign.ConstraintLayoutFragment;
import com.basic.code.fragment.expands.materialdesign.DrawerLayoutFragment;
import com.basic.code.fragment.expands.materialdesign.ToolBarFragment;
import com.basic.tools.app.ActivityUtils;

import java.util.List;

import static com.basic.code.base.BaseActivity.KEY_SUPPORT_SLIDE_BACK;

@Page(name = "Material Design", extra = R.drawable.ic_expand_material_design)
public class MaterialDesignFragment extends BaseSimpleListFragment {
    /**
     * 初始化例子
     *
     * @param lists
     * @return
     */
    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("ToolBar使用");
        lists.add("Behavior\n手势行为");
        lists.add("DrawerLayout + NavigationView\n常见主页布局");
        lists.add("BottomSheetDialog");
        lists.add("ConstraintLayout\n约束布局");
        lists.add("设置页面");
        return lists;
    }

    /**
     * 条目点击
     *
     * @param position
     */
    @Override
    protected void onItemClick(int position) {
        switch(position) {
            case 0:
                openPage(ToolBarFragment.class);
                break;
            case 1:
                openPage(BehaviorFragment.class);
                break;
            case 2:
                openNewPage(DrawerLayoutFragment.class, KEY_SUPPORT_SLIDE_BACK, false);
                break;
            case 3:
                openPage(BottomSheetDialogFragment.class);
                break;
            case 4:
                openPage(ConstraintLayoutFragment.class);
                break;
            case 5:
                ActivityUtils.startActivity(SettingsActivity.class);
                break;
            default:
                break;
        }
    }
}
