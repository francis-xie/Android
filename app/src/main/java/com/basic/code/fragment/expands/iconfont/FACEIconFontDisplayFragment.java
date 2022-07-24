
package com.basic.code.fragment.expands.iconfont;

import androidx.recyclerview.widget.RecyclerView;

import com.basic.page.annotation.Page;
import com.basic.face.utils.DensityUtils;
import com.basic.face.utils.WidgetUtils;
import com.basic.code.R;
import com.basic.code.adapter.IconFontGridAdapter;
import com.basic.code.base.BaseFragment;
import com.basic.code.widget.iconfont.FACEIconFont;

import butterknife.BindView;

/**

 * @since 2019-10-13 18:55
 */
@Page(name = "FACEIconFont展示")
public class FACEIconFontDisplayFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_face_iconfont_display;
    }

    @Override
    protected void initViews() {
        WidgetUtils.initGridRecyclerView(recyclerView, 3, DensityUtils.dp2px(2));

        recyclerView.setAdapter(new IconFontGridAdapter(FACEIconFont.Icon.values()));
    }

}
