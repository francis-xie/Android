
package com.basic.code.fragment.components.refresh.sample.selection;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.basic.page.annotation.Page;
import com.basic.face.utils.WidgetUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@Page(name = "列表选择案例")
public class ListSelectionFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_selection_total1)
    TextView tvSelectionTotal1;
    @BindView(R.id.tv_selection_total2)
    TextView tvSelectionTotal2;

    private SelectionListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_selection;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("保存") {
            @Override
            public void performAction(View view) {
                List<SelectionItem> result = mAdapter.getSelectionResult();
                StringBuilder sb = new StringBuilder("选择结果:");
                for (SelectionItem item : result) {
                    sb.append("\n")
                            .append(item.subjectName)
                            .append(":")
                            .append(getSelectionName(item.selection));
                }
                ToastUtils.toast(sb.toString());
            }
        });
        return titleBar;
    }


    public String getSelectionName(int selection) {
        if (selection == 0) {
            return "符合";
        } else if (selection == 1) {
            return "不符合";
        } else {
            return "未选择";
        }
    }

    @Override
    protected void initViews() {
        WidgetUtils.initRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter = new SelectionListAdapter());
        mAdapter.refresh(getSelectionItems());
        updateSelectResult();
    }

    @Override
    protected void initListeners() {
        mAdapter.setOnSelectionChangedListener(item -> {
            updateSelectResult();
        });
    }

    private void updateSelectResult() {
        int[] count = mAdapter.getSelectionCount();
        tvSelectionTotal1.setText(String.valueOf(count[0]));
        tvSelectionTotal2.setText(String.valueOf(count[1]));
    }

    private List<SelectionItem> getSelectionItems() {
        List<SelectionItem> items = new ArrayList<>();
        items.add(new SelectionItem("项目名称", "符合", "不符合"));
        // TODO: 2020/9/2 模拟接口返回的数据 
        for (int i = 1; i <= 5; i++) {
            items.add(new SelectionItem("项目" + i));
        }
        return items;
    }
}
