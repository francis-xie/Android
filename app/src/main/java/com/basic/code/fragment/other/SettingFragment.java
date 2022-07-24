
package com.basic.code.fragment.other;

import android.content.Intent;

import com.basic.aop.cache.XMemoryCache;
import com.basic.page.annotation.Page;
import com.basic.face.widget.dialog.DialogLoader;
import com.basic.face.widget.textview.supertextview.SuperTextView;
import com.basic.code.R;
import com.basic.code.activity.MainActivity;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.SettingSPUtils;
import com.basic.tools.app.AppUtils;

import butterknife.BindView;

/**

 * @since 2019-09-17 17:51
 */
@Page(name = "设置")
public class SettingFragment extends BaseFragment {
    @BindView(R.id.stv_switch_custom_theme)
    SuperTextView stvSwitchCustomTheme;
    @BindView(R.id.stv_switch_custom_font)
    SuperTextView stvSwitchCustomFont;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        stvSwitchCustomTheme.setSwitchIsChecked(SettingSPUtils.getInstance().isUseCustomTheme());
        stvSwitchCustomTheme.setOnSuperTextViewClickListener(superTextView -> stvSwitchCustomTheme.setSwitchIsChecked(!stvSwitchCustomTheme.getSwitchIsChecked(), false));
        stvSwitchCustomTheme.setSwitchCheckedChangeListener((buttonView, isChecked) -> {
            SettingSPUtils.getInstance().setIsUseCustomTheme(isChecked);
            XMemoryCache.getInstance().clear();
            popToBack();
            //重启主页面
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        stvSwitchCustomFont.setSwitchIsChecked(SettingSPUtils.getInstance().isUseCustomFont());
        stvSwitchCustomFont.setOnSuperTextViewClickListener(superTextView -> stvSwitchCustomFont.setSwitchIsChecked(!stvSwitchCustomFont.getSwitchIsChecked(), false));
        stvSwitchCustomFont.setSwitchCheckedChangeListener((buttonView, isChecked) -> {
            DialogLoader.getInstance().showTipDialog(getContext(), -1, "切换字体", "切换字体需重启App后生效, 点击“重启”应用将自动重启！", "重启", (dialog, which) -> {
                SettingSPUtils.getInstance().setIsUseCustomFont(isChecked);
                //重启app
                AppUtils.rebootApp(500);
            });
        });
    }
}
