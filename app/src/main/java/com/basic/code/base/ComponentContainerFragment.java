package com.basic.code.base;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.umeng.analytics.MobclickAgent;
import com.basic.aop.annotation.MemoryCache;
import com.basic.aop.annotation.SingleClick;
import com.basic.aop.cache.XMemoryCache;
import com.basic.page.base.PageContainerListFragment;
import com.basic.page.base.PageFragment;
import com.basic.page.core.PageOption;
import com.basic.page.enums.CoreAnim;
import com.basic.face.utils.DrawableUtils;
import com.basic.face.utils.ThemeUtils;
import com.basic.face.utils.WidgetUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.actionbar.TitleUtils;
import com.basic.code.R;
import com.basic.code.adapter.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.basic.code.adapter.SimpleAdapter.KEY_SUB_TITLE;
import static com.basic.code.adapter.SimpleAdapter.KEY_TITLE;

/**
 * 解决输入法内存泄漏, 修改列表样式为主副标题显示
 */
public abstract class ComponentContainerFragment extends PageContainerListFragment {

    @Override
    protected void initPage() {
        initTitle();
        initViews();
        initListeners();
    }

    protected TitleBar initTitle() {
        return TitleUtils.addTitleBarDynamic((ViewGroup) getRootView(), getPageTitle(), v -> popToBack())
                .setLeftImageDrawable(getNavigationBackDrawable(R.attr.face_actionbar_ic_navigation_back));
    }

    @MemoryCache
    private Drawable getNavigationBackDrawable(int navigationBackId) {
        return DrawableUtils.getSupportRTLDrawable(ThemeUtils.resolveDrawable(getContext(), navigationBackId));
    }

    @Override
    protected void initData() {
        mSimpleData = initSimpleData(mSimpleData);

        List<Map<String, String>> data = new ArrayList<>();
        for (String content : mSimpleData) {
            Map<String, String> item = new HashMap<>();
            int index = content.indexOf("\n");
            if (index > 0) {
                item.put(KEY_TITLE, String.valueOf(content.subSequence(0, index)));
                item.put(KEY_SUB_TITLE, String.valueOf(content.subSequence(index + 1, content.length())));
            } else {
                item.put(KEY_TITLE, content);
                item.put(KEY_SUB_TITLE, "");
            }
            data.add(item);
        }

        getListView().setAdapter(new SimpleAdapter(getContext(), data));
        WidgetUtils.clearAllViewBackground(getListView());
        initSimply();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        onItemClick(view, position);
    }

    @SingleClick
    private void onItemClick(View view, int position) {
        onItemClick(position);
    }

    @Override
    public void onDestroyView() {
//        KeyboardUtils.fixSoftInputLeaks(getContext());

        getListView().setOnItemClickListener(null);
        super.onDestroyView();
    }

    /**
     * 打开一个新的页面
     *
     * @param name
     * @param <T>
     * @return
     */
    public <T extends PageFragment> Fragment openNewPage(String name) {
        return new PageOption(name)
                .setAnim(CoreAnim.slide)
                .setNewActivity(true)
                .open(this);
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        //屏幕旋转时刷新一下title
        super.onConfigurationChanged(newConfig);
        XMemoryCache.getInstance().clear();
        ViewGroup root = (ViewGroup) getRootView();
        if (root.getChildAt(0) instanceof TitleBar) {
            root.removeViewAt(0);
            initTitle();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getPageName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getPageName());
    }
}
