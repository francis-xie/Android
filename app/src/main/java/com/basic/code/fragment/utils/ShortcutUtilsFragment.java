
package com.basic.code.fragment.utils;

import android.view.View;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.utils.ResUtils;
import com.basic.face.utils.ShortcutUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.grouplist.FACEGroupListView;
import com.basic.code.R;
import com.basic.code.activity.SearchComponentActivity;
import com.basic.code.activity.SettingsActivity;
import com.basic.code.base.BaseFragment;
import com.basic.code.fragment.utils.shortcut.ShortcutReceiver;
import com.basic.code.utils.Utils;

import butterknife.BindView;

/**
 * 快捷方式创建工具类
 *

 * @since 2021/10/6 3:33 PM
 */
@Page(name = "ShortcutUtils", extra = R.drawable.ic_util_shortcut)
public class ShortcutUtilsFragment extends BaseFragment {

    @BindView(R.id.groupListView)
    FACEGroupListView groupListView;

    @Override
    protected TitleBar initTitle() {
        return super.initTitle().setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                popToBack();
                Utils.syncMainPageStatus();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shortcututils;
    }

    @Override
    protected void initViews() {
        FACEGroupListView.newSection(getContext())
                .setDescription("支持7.0以上版本的桌面快捷方式的创建")
                .addItemView(groupListView.createItemView("增加搜索至桌面快捷方式"), v -> createSearchShortcut())
                .addItemView(groupListView.createItemView("增加设置至桌面快捷方式"), v -> createSettingShortcut())
                .addTo(groupListView);
    }

    /**
     * 创建搜索桌面快捷方式
     */
    private void createSearchShortcut() {
        ShortcutUtils.addPinShortcut(getContext(),
                SearchComponentActivity.class,
                "shortcut_search_id",
                R.drawable.ic_action_search, ResUtils.getString(R.string.shortcut_label_search), ShortcutReceiver.class);
    }

    /**
     * 创建搜索桌面快捷方式s
     */
    private void createSettingShortcut() {
        ShortcutUtils.addPinShortcut(getContext(),
                SettingsActivity.class,
                "shortcut_setting_id",
                R.drawable.ic_action_setting, ResUtils.getString(R.string.shortcut_label_setting), ShortcutReceiver.class);
    }
}
