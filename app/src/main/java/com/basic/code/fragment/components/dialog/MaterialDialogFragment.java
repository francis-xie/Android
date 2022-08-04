
package com.basic.code.fragment.components.dialog;

import android.text.InputType;

import com.basic.page.annotation.Page;
import com.basic.face.widget.dialog.materialdialog.GravityEnum;
import com.basic.face.widget.dialog.materialdialog.MaterialDialog;
import com.basic.face.widget.dialog.materialdialog.simplelist.MaterialSimpleListAdapter;
import com.basic.face.widget.dialog.materialdialog.simplelist.MaterialSimpleListItem;
import com.basic.code.R;
import com.basic.code.base.BaseSimpleListFragment;
import com.basic.code.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

@Page(name = "MaterialDialog\n对话框")
public class MaterialDialogFragment extends BaseSimpleListFragment {
    private Thread thread;

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("简单的提示性对话框");
        lists.add("简单的确认对话框");
        lists.add("带输入框的对话框");
        lists.add("类似系统的上下文菜单ContextMenu的Dialog");
        lists.add("带单选项的Dialog");
        lists.add("带多选项的Dialog");
        lists.add("带水平Loading进度条的Dialog");
        lists.add("带圆形Loading的Dialog");
        lists.add("带图标条目的Dialog");
        lists.add("自定义对话框");
        return lists;
    }

    private void startThread(Runnable run) {
        if (thread != null) {
            thread.interrupt();
        }
        thread = new Thread(run);
        thread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (thread != null && !thread.isInterrupted() && thread.isAlive()) {
            thread.interrupt();
        }
    }

    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                //简单的提示性对话框
                showSimpleTipDialog();
                break;
            case 1:
                //简单的确认对话框
                showSimpleConfirmDialog();
                break;
            case 2:
                //带输入框的对话框
                showInputDialog();
                break;
            case 3:
                //类似系统的上下文菜单ContextMenu的Dialog
                showContextMenuDialog();
                break;
            case 4:
                //带单选项的Dialog
                showSingleChoiceDialog();
                break;
            case 5:
                //带多选项的Dialog
                showMultiChoiceDialog();
                break;
            case 6:
                //带水平Loading进度条的Dialog
                showHorizontalLoadingProgressDialog();
                break;
            case 7:
                //带圆形Loading的Dialog
                showCircleLoadingProgressDialog();
                break;
            case 8:
                //带图标条目的Dialog
                showPictureItemDialog();
                break;
            case 9:
                //自定义弹窗
                showCustomDialog();
                break;
            default:
                break;
        }
    }



    /**
     * 简单的提示性对话框
     */
    private void showSimpleTipDialog() {
        new MaterialDialog.Builder(getContext())
                .iconRes(R.drawable.icon_tip)
                .title(R.string.tip_infos)
                .content(R.string.content_simple_confirm_dialog)
                .positiveText(R.string.lab_submit)
                .show();
    }

    /**
     * 简单的确认对话框
     */
    private void showSimpleConfirmDialog() {
        new MaterialDialog.Builder(getContext())
                .content(R.string.tip_bluetooth_permission)
                .positiveText(R.string.lab_yes)
                .negativeText(R.string.lab_no)
                .show();
    }

    /**
     * 带输入框的对话框
     */
    private void showInputDialog() {
        new MaterialDialog.Builder(getContext())
                .iconRes(R.drawable.icon_warning)
                .title(R.string.tip_warning)
                .content(R.string.content_warning)
                .inputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                                | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .input(
                        getString(R.string.hint_please_input_password),
                        "",
                        false,
                        ((dialog, input) -> ToastUtils.toast(input.toString())))
                .inputRange(3, 5)
                .positiveText(R.string.lab_continue)
                .negativeText(R.string.lab_change)
                .onPositive((dialog, which) -> ToastUtils.toast("你输入了:" + dialog.getInputEditText().getText().toString()))
                .cancelable(false)
                .show();
    }

    /**
     * 类似系统的上下文菜单ContextMenu的Dialog
     */
    private void showContextMenuDialog() {
        new MaterialDialog.Builder(getContext())
                .title(R.string.tip_options)
                .items(R.array.menu_values)
                .itemsCallback((dialog, itemView, position, text) -> ToastUtils.toast(position + ": " + text))
                .show();
    }

    /**
     * 带单选项的Dialog
     */
    private void showSingleChoiceDialog() {
        new MaterialDialog.Builder(getContext())
                .title(R.string.tip_router_setting)
                .items(R.array.router_choice_entry)
                .itemsCallbackSingleChoice(
                        0,
                        (dialog, itemView, which, text) -> {
                            ToastUtils.toast(which + ": " + text);
                            return true;
                        })
                .positiveText(R.string.lab_choice)
                .negativeText(R.string.lab_cancel)
                .show();
    }

    /**
     * 带多选项的Dialog
     */
    private void showMultiChoiceDialog() {
        new MaterialDialog.Builder(getContext())
                .title(R.string.tip_router_setting)
                .items(R.array.router_choice_entry)
                .itemsCallbackMultiChoice(
                        new Integer[]{0, 1},
                        (dialog, which, text) -> {
                            StringBuilder sb = new StringBuilder("选中：\n");
                            for (int i = 0; i < which.length; i ++){
                                sb.append(which[i]).append(":").append(text[i]).append("\n");
                            }
                            ToastUtils.toast(sb.toString());
                            return true;
                        })
                .positiveText(R.string.lab_choice)
                .negativeText(R.string.lab_cancel)
                .show();
    }


    /**
     * 带水平Loading进度条的Dialog
     */
    private void showHorizontalLoadingProgressDialog() {
        new MaterialDialog.Builder(getContext())
                .title(R.string.tip_update)
                .content(R.string.content_downloading)
                .contentGravity(GravityEnum.CENTER)
                .progress(false, 150, true)
                .cancelListener(dialog -> {
                    if (thread != null) {
                        thread.interrupt();
                    }
                })
                .showListener(dialog -> updateProgress((MaterialDialog) dialog))
                .negativeText(R.string.lab_cancel)
                .show();
    }

    /**
     * 更新进度条
     * @param dialogInterface
     */
    private void updateProgress(MaterialDialog dialogInterface) {
        final MaterialDialog dialog = dialogInterface;
        startThread(() -> {
            while (dialog.getCurrentProgress() != dialog.getMaxProgress()
                    && !Thread.currentThread().isInterrupted()) {
                if (dialog.isCancelled()) {
                    break;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    break;
                }
                dialog.incrementProgress(1);
            }
            getActivity().runOnUiThread(() -> {
                thread = null;
                dialog.setContent(R.string.tip_download_finished);
            });
        });
    }

    /**
     * 带圆形Loading的Dialog
     */
    private void showCircleLoadingProgressDialog() {
        new MaterialDialog.Builder(getContext())
                .iconRes(R.drawable.icon_tip)
                .limitIconToDefaultSize()
                .title(R.string.tip_infos)
                .content(R.string.content_wait_for_receive_data)
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .negativeText(R.string.lab_cancel)
                .show();
    }

    /**
     * 带图标条目的Dialog
     */
    private void showPictureItemDialog() {
        List<MaterialSimpleListItem> list = new ArrayList<>();
        list.add(new MaterialSimpleListItem.Builder(getContext())
                .content(R.string.lab_edit)
                .icon(R.drawable.icon_edit)
                .iconPaddingDp(8)
                .build());
        list.add(new MaterialSimpleListItem.Builder(getContext())
                .content(R.string.lab_add)
                .icon(R.drawable.icon_add)
                .build());
        list.add(new MaterialSimpleListItem.Builder(getContext())
                .content(R.string.lab_delete)
                .icon(R.drawable.icon_delete)
                .build());
        list.add(new MaterialSimpleListItem.Builder(getContext())
                .content(R.string.lab_update)
                .icon(R.drawable.icon_update)
                .iconPaddingDp(8)
                .build());
        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(list)
                .setOnItemClickListener((dialog, index, item) -> ToastUtils.toast(item.getContent().toString()));
        new MaterialDialog.Builder(getContext()).adapter(adapter, null).show();
    }

    /**
     * 显示自定义对话框
     */
    private void showCustomDialog() {
        new MaterialDialog.Builder(getContext())
                .customView(R.layout.dialog_custom, true)
                .title("自定义对话框")
                .positiveText(R.string.lab_submit)
                .negativeText(R.string.lab_cancel)
                .show();

    }
}
