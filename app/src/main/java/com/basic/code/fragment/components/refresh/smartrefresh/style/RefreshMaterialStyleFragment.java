
package com.basic.code.fragment.components.refresh.smartrefresh.style;

import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.basic.refresh.header.MaterialHeader;
import com.basic.refresh.layout.SmartRefreshLayout;
import com.basic.refresh.layout.adapter.SmartViewHolder;
import com.basic.refresh.layout.constant.RefreshState;
import com.basic.page.annotation.Page;
import com.basic.face.adapter.recyclerview.BaseRecyclerAdapter;
import com.basic.face.adapter.recyclerview.RecyclerViewHolder;
import com.basic.face.utils.WidgetUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import java.util.Arrays;

import butterknife.BindView;

/**

 * @since 2018/12/7 上午1:43
 */
@Page(name = "Material Design风格")
public class RefreshMaterialStyleFragment extends BaseFragment implements SmartViewHolder.OnItemClickListener{

    private TitleBar mTitleBar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    private MaterialHeader mMaterialHeader;

    /**
     * 点击
     *
     * @param itemView
     * @param position
     */
    @Override
    public void onItemClick(View itemView, int position) {
        if (!RefreshState.None.equals(mRefreshLayout.getState())) {
            return;
        }

        switch (Item.values()[position]) {
            case 内容不偏移:
                mRefreshLayout.setEnableHeaderTranslationContent(false);
                break;
            case 内容跟随偏移:
                mRefreshLayout.setEnableHeaderTranslationContent(true);
                break;
            case 打开背景:
                mMaterialHeader.setShowBezierWave(true);
                break;
            case 关闭背景:
                mMaterialHeader.setShowBezierWave(false);
                break;
            case 蓝色主题:
                setThemeColor(R.color.colorPrimary);
                break;
            case 绿色主题:
                setThemeColor(android.R.color.holo_green_light);
                break;
            case 红色主题:
                setThemeColor(android.R.color.holo_red_light);
                break;
            case 橙色主题:
                setThemeColor(android.R.color.holo_orange_light);
                break;
            default:
                break;
        }
        mRefreshLayout.autoRefresh();
    }

    private void setThemeColor(int colorPrimary) {
        mRefreshLayout.setPrimaryColorsId(colorPrimary, android.R.color.white);
        mTitleBar.setBackgroundColor(ContextCompat.getColor(getContext(), colorPrimary));
        if (Build.VERSION.SDK_INT >= 21) {
            if (getActivity() != null) {
                getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), colorPrimary));
            }
        }
    }

    private enum Item {
        打开背景(R.string.item_style_bezier_on),
        关闭背景(R.string.item_style_bezier_off),
        内容不偏移(R.string.item_style_content_translation_off),
        内容跟随偏移(R.string.item_style_content_translation_on),
        橙色主题(R.string.item_style_theme_orange_abstract),
        红色主题(R.string.item_style_theme_red_abstract),
        绿色主题(R.string.item_style_theme_green_abstract),
        蓝色主题(R.string.item_style_theme_blue_abstract),
        ;
        public int nameId;
        Item(@StringRes int nameId) {
            this.nameId = nameId;
        }
    }

    @Override
    protected TitleBar initTitle() {
        mTitleBar = super.initTitle();
        return mTitleBar;
    }
    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_material_style;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        mMaterialHeader = (MaterialHeader)mRefreshLayout.getRefreshHeader();

        View view = findViewById(R.id.recyclerView);
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            WidgetUtils.initRecyclerView(recyclerView);

            recyclerView.setAdapter(new BaseRecyclerAdapter<Item>(Arrays.asList(Item.values())) {
                @Override
                protected void bindData(@NonNull RecyclerViewHolder holder, int position, Item item) {
                    holder.text(android.R.id.text1, item.name());
                    holder.text(android.R.id.text2, item.nameId);
                    holder.textColorId(android.R.id.text2, R.color.face_config_color_light_blue_gray);
                }

                @Override
                protected int getItemLayoutId(int viewType) {
                    return android.R.layout.simple_list_item_2;
                }
            });
        }
        mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
    }
}
