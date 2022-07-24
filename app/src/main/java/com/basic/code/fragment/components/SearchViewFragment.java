
package com.basic.code.fragment.components;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.View;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.utils.SnackbarUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.searchview.MaterialSearchView;
import com.basic.code.R;
import com.basic.code.activity.SearchViewActivity;
import com.basic.code.base.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**

 * @since 2019/1/2 下午4:49
 */
@Page(name = "搜索框", extra = R.drawable.ic_widget_search)
public class SearchViewFragment extends BaseFragment {
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_searchview;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.icon_action_query) {

            @Override
            @SingleClick
            public void performAction(View view) {
                mSearchView.showSearch();
            }
        });
        return titleBar;
    }

    @Override
    protected void initViews() {
        mSearchView.setVoiceSearch(false);
        mSearchView.setEllipsize(true);
        mSearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SnackbarUtils.Long(mSearchView, "Query: " + query).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });
        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
        mSearchView.setSubmitOnClick(true);
    }

    @OnClick(R.id.fab)
    void toSearchActivity(View v) {
        startActivity(new Intent(getContext(), SearchViewActivity.class));
    }

    @Override
    public void onDestroyView() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        }
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == Activity.RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    mSearchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
