
package com.basic.code.fragment.components.dialog;

import android.text.InputType;

import com.basic.page.annotation.Page;
import com.basic.face.utils.KeyboardUtils;
import com.basic.face.widget.dialog.DialogLoader;
import com.basic.face.widget.dialog.materialdialog.MaterialDialog;
import com.basic.face.widget.dialog.strategy.InputInfo;
import com.basic.face.widget.dialog.strategy.impl.AlertDialogStrategy;
import com.basic.face.widget.dialog.strategy.impl.MaterialDialogStrategy;
import com.basic.code.R;
import com.basic.code.base.BaseSimpleListFragment;
import com.basic.code.utils.XToastUtils;

import java.util.List;

/**

 * @since 2018/11/15 上午12:14
 */
@Page(name = "Dialog构建策略")
public class DialogStrategyFragment extends BaseSimpleListFragment {
    /**
     * 初始化例子
     *
     * @param lists
     * @return
     */
    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("使用MaterialDialog策略显示");
        lists.add("使用AlertDialog策略显示");
        lists.add("简单的提示性对话框");
        lists.add("简单的确认对话框");
        lists.add("带输入框的对话框");
        lists.add("类似系统的上下文菜单ContextMenu的Dialog");
        lists.add("带单选项的Dialog");
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
                DialogLoader.getInstance().setIDialogStrategy(new MaterialDialogStrategy());
                break;
            case 1:
                DialogLoader.getInstance().setIDialogStrategy(new AlertDialogStrategy());
                break;
            case 2:
                DialogLoader.getInstance().showTipDialog(
                        getContext(),
                        getString(R.string.tip_infos),
                        getString(R.string.content_simple_confirm_dialog),
                        getString(R.string.lab_submit));
                break;
            case 3:
                DialogLoader.getInstance().showConfirmDialog(
                        getContext(),
                        getString(R.string.tip_bluetooth_permission),
                        getString(R.string.lab_yes),
                        (dialog, which) -> {
                            XToastUtils.toast("同意打开蓝牙！");
                            dialog.dismiss();
                        },
                        getString(R.string.lab_no),
                        (dialog, which) -> {
                            XToastUtils.toast("不同意打开蓝牙！");
                            dialog.dismiss();
                        }
                );
                break;
            case 4:
                DialogLoader.getInstance().showInputDialog(
                        getContext(),
                        R.drawable.icon_warning,
                        getString(R.string.tip_warning),
                        getString(R.string.content_warning),
                        new InputInfo(InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                                | InputType.TYPE_TEXT_FLAG_CAP_WORDS,
                                getString(R.string.hint_please_input_password)),
                        (dialog, input) -> XToastUtils.toast(input.toString()),
                        getString(R.string.lab_continue),
                        (dialog, which) -> {
                            KeyboardUtils.hideSoftInput(dialog);
                            dialog.dismiss();
                            if (dialog instanceof MaterialDialog) {
                                XToastUtils.toast("你输入了:" + ((MaterialDialog) dialog).getInputEditText().getText().toString());
                            }
                        },
                        getString(R.string.lab_change),
                        (dialog, which) -> {
                            KeyboardUtils.hideSoftInput(dialog);
                            dialog.dismiss();
                        });
                break;
            case 5:
                DialogLoader.getInstance().showContextMenuDialog(getContext(),
                        getString(R.string.tip_options),
                        R.array.menu_values,
                        (dialog, which) -> XToastUtils.toast("选择了第" + (which + 1) + "个"));
                break;
            case 6:
                DialogLoader.getInstance().showSingleChoiceDialog(
                        getContext(),
                        getString(R.string.tip_router_setting),
                        R.array.router_choice_entry,
                        0,
                        (dialog, which) -> XToastUtils.toast("选择了第" + (which + 1) + "个"),
                        getString(R.string.lab_yes),
                        getString(R.string.lab_no));
                break;
            default:
                break;
        }
    }
}
