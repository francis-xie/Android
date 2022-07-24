
package com.basic.code.fragment.other;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.basic.aop.annotation.MemoryCache;
import com.basic.aop.annotation.SingleClick;
import com.basic.dbms.FACEDataBaseRepository;
import com.basic.dbms.db.DBService;
import com.basic.page.annotation.Page;
import com.basic.page.config.AppPageConfig;
import com.basic.page.model.PageInfo;
import com.basic.face.adapter.recyclerview.RecyclerViewHolder;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.guidview.GuideCaseView;
import com.basic.face.widget.searchview.DefaultSearchFilter;
import com.basic.face.widget.searchview.MaterialSearchView;
import com.basic.code.R;
import com.basic.code.adapter.SearchRecordTagAdapter;
import com.basic.code.base.BaseFragment;
import com.basic.code.base.db.entity.SearchRecord;
import com.basic.code.utils.Utils;
import com.basic.tools.data.DateUtils;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**

 * @since 2019-12-02 23:32
 */
@Page(name = "组件搜索")
public class SearchComponentFragment extends BaseFragment implements RecyclerViewHolder.OnItemClickListener<SearchRecord> {

    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private DBService<SearchRecord> mDBService;
    private SearchRecordTagAdapter mAdapter;

    private View mAction;

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle().setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                popToBack();
                Utils.syncMainPageStatus();
            }
        });
        mAction = titleBar.addAction(new TitleBar.ImageAction(R.drawable.icon_action_query) {

            @SingleClick
            @Override
            public void performAction(View view) {
                mSearchView.showSearch();
            }
        });
        return titleBar;
    }

    @Override
    protected void initArgs() {
        mDBService = FACEDataBaseRepository.getInstance().getDataBase(SearchRecord.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_component;
    }

    @Override
    protected void initViews() {
        new GuideCaseView.Builder(getActivity())
                .title("点击按钮开始搜索")
                .focusOn(mAction)
                .showOnce("key_start_search")
                .show();

        mSearchView.setVoiceSearch(false);
        mSearchView.setEllipsize(true);
        mSearchView.setSuggestions(getPageSuggestions());
        mSearchView.setSearchFilter(new DefaultSearchFilter() {
            @Override
            protected boolean filter(String suggestion, String input) {
                return suggestion.toLowerCase().contains(input.toLowerCase());
            }
        });
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onQueryResult(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });
        mSearchView.setSubmitOnClick(true);

        recyclerView.setLayoutManager(Utils.getFlexboxLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter = new SearchRecordTagAdapter());
        refreshRecord();


    }

    @SingleClick
    @OnClick(R.id.iv_delete)
    public void onViewClicked(View view) {
        try {
            mDBService.deleteAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mAdapter.clear();
    }

    private void refreshRecord() {
        try {
            mAdapter.refresh(mDBService.queryAllOrderBy("time", false));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击查询结果之后
     *
     * @param query
     */
    private void onQueryResult(String query) {
        //直接跳转到指定页面
        openPage(query);
        try {
            SearchRecord record = mDBService.queryForColumnFirst("content", query);
            if (record == null) {
                record = new SearchRecord().setContent(query).setTime(DateUtils.getNowMills());
                mDBService.insert(record);
            } else {
                record.setTime(DateUtils.getNowMills());
                mDBService.updateData(record);
            }
            refreshRecord();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initListeners() {
        mAdapter.setOnItemClickListener(this);
    }


    @SingleClick(500)
    @Override
    public void onItemClick(View itemView, SearchRecord item, int position) {
        if (item != null) {
            openPage(item.getContent());
            try {
                mDBService.updateData(item.setTime(DateUtils.getNowMills()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        }
        super.onDestroyView();
    }


    @MemoryCache
    public String[] getPageSuggestions() {
        List<PageInfo> pages = AppPageConfig.getInstance().getPages();
        String[] array = new String[pages.size()];
        for (int i = 0; i < pages.size(); i++) {
            array[i] = pages.get(i).getName();
        }
        return array;
    }


}
