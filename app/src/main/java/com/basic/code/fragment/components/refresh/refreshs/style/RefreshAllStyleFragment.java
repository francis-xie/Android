
package com.basic.code.fragment.components.refresh.refreshs.style;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.basic.refresh.layout.RefreshLayouts;
import com.basic.refresh.layout.adapter.SmartViewHolder;
import com.basic.refresh.layout.api.RefreshHeader;
import com.basic.refresh.layout.constant.RefreshState;
import com.basic.page.annotation.Page;
import com.basic.face.adapter.recyclerview.BaseRecyclerAdapter;
import com.basic.face.adapter.recyclerview.RecyclerViewHolder;
import com.basic.face.utils.WidgetUtils;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import butterknife.BindView;

/**

 * @since 2018/12/7 下午2:32
 */
@Page(name = "样式大全")
public class RefreshAllStyleFragment extends BaseFragment implements SmartViewHolder.OnItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayouts mRefreshLayout;

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

        Item item = Item.values()[position];
        RefreshHeader header = getRefreshHeader(item);
        if (header != null) {
            mRefreshLayout.setRefreshHeader(header);
            mRefreshLayout.autoRefresh();
        }
    }

    private RefreshHeader getRefreshHeader(Item item) {
        try {
            Class<?> headerClass = Class.forName("com.basic.refresh.header." + item.name());
            Constructor<?> constructor = headerClass.getConstructor(Context.class);
            return (RefreshHeader) constructor.newInstance(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private enum Item {
        BezierCircleHeader(R.string.item_head_style_bezier_circle),
        DeliveryHeader(R.string.item_head_style_delivery),
        DropBoxHeader(R.string.item_head_style_drop_box),
        FunGameBattleCityHeader(R.string.item_head_style_fun_game_battle_city),
        FunGameHitBlockHeader(R.string.item_head_style_fun_game_hit_block),
        PhoenixHeader(R.string.item_head_style_phoenix),
        StoreHouseHeader(R.string.item_head_style_store_house),
        TaurusHeader(R.string.item_head_style_taurus),
        WaterDropHeader(R.string.item_head_style_water_drop),
        WaveSwipeHeader(R.string.item_head_style_wave_swipe);

        public int nameId;

        Item(@StringRes int nameId) {
            this.nameId = nameId;
        }
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_all_style;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        WidgetUtils.initRecyclerView(mRecyclerView);

        mRecyclerView.setAdapter(new BaseRecyclerAdapter<Item>(Arrays.asList(Item.values())) {
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
        mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果

    }

}
