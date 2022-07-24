
package com.basic.code.fragment.expands.alibaba;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.TangramEngine;
import com.tmall.wireless.tangram.structure.viewcreator.ViewHolderCreator;
import com.tmall.wireless.tangram.util.IInnerImageSetter;
import com.basic.page.annotation.Page;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.imageview.ImageLoader;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.fragment.expands.alibaba.tangram.CustomAnnotationView;
import com.basic.code.fragment.expands.alibaba.tangram.NoBackgroundView;
import com.basic.code.fragment.expands.alibaba.tangram.model.CustomCell;
import com.basic.code.fragment.expands.alibaba.tangram.model.CustomCellView;
import com.basic.code.fragment.expands.alibaba.tangram.support.CustomClickSupport;
import com.basic.code.fragment.expands.alibaba.tangram.CustomInterfaceView;
import com.basic.code.fragment.expands.alibaba.tangram.support.CustomExposureSupport;
import com.basic.code.fragment.expands.alibaba.tangram.viewholder.CustomHolderCell;
import com.basic.code.fragment.expands.alibaba.tangram.viewholder.CustomViewHolder;
import com.basic.code.utils.Utils;
import com.basic.tools.resource.ResourceUtils;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.BindView;

/**
 * Tangram-Android使用步骤
 * <p>
 * 1.初始化 Tangram 环境
 * 2.初始化 TangramBuilder
 * 3.注册自定义的卡片和组件
 * 4.生成TangramEngine实例
 * 5.绑定业务 support 类到 engine
 * 6.绑定 recyclerView
 * 7.监听 recyclerView 的滚动事件
 * 8.设置悬浮类型布局的偏移（可选）
 * 9.设置卡片预加载的偏移量（可选）
 * 10.加载数据并传递给 engine
 *

 * @since 2020/4/6 11:41 PM
 */
@Page(name = "Tangram-Android\n动态化多布局组件")
public class TangramAndroidFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private TangramEngine mEngine;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_common_recycleview;
    }

    @Override
    protected void initViews() {
        // 1.初始化 Tangram 环境
        TangramBuilder.init(getContext(), new IInnerImageSetter() {
            @Override
            public <IMAGE extends ImageView> void doLoadImageUrl(@NonNull IMAGE view,
                                                                 @Nullable String url) {
                ImageLoader.get().loadImage(view, url);
            }
        }, ImageView.class);

        // 2.初始化 TangramBuilder
        TangramBuilder.InnerBuilder builder = TangramBuilder.newInnerBuilder(getContext());

        // 3.注册自定义的卡片和组件
        /// 使用接口方式的自定义View
        builder.registerCell("InterfaceCell", CustomInterfaceView.class);
        builder.registerCell("NoBackground", NoBackgroundView.class);
        /// 使用注解方式的自定义View
        builder.registerCell("AnnotationCell", CustomAnnotationView.class);
        /// 自定义model组件
        builder.registerCell("CustomCell", CustomCell.class, CustomCellView.class);
        builder.registerCell("CustomHolderCell", CustomHolderCell.class,
                new ViewHolderCreator<>(R.layout.tangram_item_holder, CustomViewHolder.class, TextView.class));

        // 4.生成TangramEngine实例
        mEngine = builder.build();

        // 5.绑定业务 support 类到 engine
        mEngine.addSimpleClickSupport(new CustomClickSupport());
        mEngine.addExposureSupport(new CustomExposureSupport());

        // 6.绑定 recyclerView
        mEngine.bindView(recyclerView);

        // 7.监听 recyclerView 的滚动事件
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 在 scroll 事件中触发 engine 的 onScroll，内部会触发需要异步加载的卡片去提前加载数据
                mEngine.onScrolled();
            }
        });

        // 8.设置悬浮类型布局的偏移（可选）
        mEngine.getLayoutManager().setFixOffset(0, 40, 0, 0);

        // 9.设置卡片预加载的偏移量（可选）
        mEngine.setPreLoadNumber(3);

        // 10.加载数据并传递给 engine
        String json = ResourceUtils.readStringFromAssert("alibaba/data.json");
        try {
            JSONArray data = new JSONArray(json);
            mEngine.setData(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        // 退出的时候销毁 engine
        mEngine.destroy();
        super.onDestroyView();
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("Github") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://github.com/alibaba/Tangram-Android");
            }
        });
        return titleBar;
    }
}
