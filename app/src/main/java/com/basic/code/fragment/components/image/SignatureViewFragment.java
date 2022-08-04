
package com.basic.code.fragment.components.image;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.basic.page.annotation.Page;
import com.basic.face.widget.imageview.SignatureView;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "SignatureView\n电子签名工具")
public class SignatureViewFragment extends BaseFragment {

    @BindView(R.id.signature_view)
    SignatureView signatureView;
    @BindView(R.id.iv_signature)
    ImageView ivSignature;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_signatureview;
    }

    @Override
    protected void initViews() {
    }

    @OnClick({R.id.btn_clear, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                signatureView.clear();
                break;
            case R.id.btn_submit:
                onSubmit(signatureView);
                break;
            default:
                break;
        }
    }

    private void onSubmit(SignatureView signatureView) {
        Bitmap bitmap = signatureView.getSnapshot();
        if (bitmap != null) {
            showSignatureImg(bitmap);
        } else {
            ToastUtils.warning("您未签名~");
        }

    }

    private void showSignatureImg(Bitmap bitmap) {
        ivSignature.setImageBitmap(bitmap);
    }

}
