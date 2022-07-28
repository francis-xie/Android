
package com.basic.code.fragment.expands.materialdesign;

import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.basic.page.annotation.Page;
import com.basic.page.config.AppPageConfig;
import com.basic.page.model.PageInfo;
import com.basic.face.utils.DensityUtils;
import com.basic.face.utils.WidgetUtils;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.adapter.SimpleRecyclerAdapter;
import com.basic.code.adapter.WidgetItemAdapter;
import com.basic.code.base.BaseSimpleListFragment;
import com.basic.code.fragment.expands.materialdesign.bottom.DemoBottomSheetDialog;

import java.util.Collections;
import java.util.List;

@Page(name = "BottomSheetDialog")
public class BottomSheetDialogFragment extends BaseSimpleListFragment {
    /**
     * 初始化例子
     *
     * @param lists
     * @return
     */
    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("List");
        lists.add("Grid");
        lists.add("DialogFragment");
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
                showBottomSheetListDialog(true);
                break;
            case 1:
                showBottomSheetListDialog(false);
                break;
            case 2:
                DemoBottomSheetDialog.newInstance().show(getFragmentManager());
                break;
            default:
                break;
        }
    }

    private void showBottomSheetListDialog(boolean isList) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bottom_sheet, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        if (isList) {
            initDialogList(recyclerView);
        } else {
            initDialogGrid(recyclerView);
        }

        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        WidgetUtils.transparentBottomSheetDialogBackground(dialog);
    }

    private void initDialogList(RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        SimpleRecyclerAdapter adapter = new SimpleRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        adapter.refresh(DemoDataProvider.getDemoData());
    }

    private void initDialogGrid(RecyclerView recyclerView) {
        WidgetUtils.initGridRecyclerView(recyclerView, 3, DensityUtils.dp2px(2));

        WidgetItemAdapter widgetItemAdapter = new WidgetItemAdapter(sortPageInfo(AppPageConfig.getInstance().getComponents()));
        recyclerView.setAdapter(widgetItemAdapter);
    }

    /**
     * 进行排序
     *
     * @param pageInfoList
     * @return
     */
    private List<PageInfo> sortPageInfo(List<PageInfo> pageInfoList) {
        Collections.sort(pageInfoList, (o1, o2) -> o1.getClassPath().compareTo(o2.getClassPath()));
        return pageInfoList;
    }


}
