
package com.basic.code.fragment.components.image;

import android.os.Bundle;

import com.basic.page.annotation.Page;
import com.basic.page.enums.CoreAnim;
import com.basic.face.utils.ResUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.image.preview.view.SmoothImageView;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;

import static com.basic.code.fragment.components.image.DrawablePreviewFragment.DRAWABLE_ID;

@Page(name = "资源图片预览", params = {DRAWABLE_ID}, anim = CoreAnim.none)
public class DrawablePreviewFragment extends BaseFragment {

    public final static String DRAWABLE_ID = "drawable_id";
    @BindView(R.id.photoView)
    SmoothImageView mImageView;

    private int mDrawableId;

    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @Override
    protected void initArgs() {
        Bundle args = getArguments();
        if (args != null) {
            mDrawableId = args.getInt(DRAWABLE_ID, -1);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_drawable_preview;
    }

    @Override
    protected void initViews() {
        mImageView.setImageDrawable(ResUtils.getDrawable(getContext(), mDrawableId));
        mImageView.setMinimumScale(0.5F);
    }

    @Override
    public void onDestroyView() {
        if (mImageView != null) {
            mImageView.setImageBitmap(null);
        }
        super.onDestroyView();
    }
}
