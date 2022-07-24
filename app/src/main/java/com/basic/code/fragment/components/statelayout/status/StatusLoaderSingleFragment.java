
package com.basic.code.fragment.components.statelayout.status;

import android.view.View;
import android.widget.LinearLayout;

import com.github.clans.fab.FloatingActionMenu;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.widget.statelayout.StatusLoader;
import com.basic.code.R;
import com.basic.code.fragment.components.statelayout.status.adapter.SingleViewAdapter;

import butterknife.BindView;
import butterknife.OnClick;

/**

 * @since 2020/4/29 11:54 PM
 */
@Page(name = "StatusLoader组件复用")
public class StatusLoaderSingleFragment extends BaseStatusLoaderFragment {

    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.fab_menu)
    FloatingActionMenu mFloatingActionMenu;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_status_loader;
    }

    @Override
    protected void initViews() {
        showLoading();
    }

    @SingleClick
    @OnClick({R.id.fab_loading, R.id.fab_empty, R.id.fab_error, R.id.fab_no_network, R.id.fab_content})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_loading:
                showLoading();
                break;
            case R.id.fab_empty:
                showEmpty();
                break;
            case R.id.fab_error:
                showError();
                break;
            case R.id.fab_no_network:
                showCustom();
                break;
            case R.id.fab_content:
                showContent();
                break;
            default:
                break;
        }
        mFloatingActionMenu.toggle(false);
    }


    @Override
    protected View getWrapView() {
        return llContent;
    }

    @Override
    protected StatusLoader.Adapter getStatusLoaderAdapter() {
        return new SingleViewAdapter();
    }
}
