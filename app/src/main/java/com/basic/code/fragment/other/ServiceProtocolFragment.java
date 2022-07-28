
package com.basic.code.fragment.other;

import android.widget.TextView;

import com.basic.aop.annotation.MemoryCache;
import com.basic.page.annotation.Page;
import com.basic.router.annotation.AutoWired;
import com.basic.router.launcher.Router;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.tools.resource.ResUtils;
import com.basic.tools.resource.ResourceUtils;

import butterknife.BindView;

/**
 * 服务协议【本地加载】
 */
@Page
public class ServiceProtocolFragment extends BaseFragment {

    public static final String KEY_PROTOCOL_TITLE = "key_protocol_title";

    /**
     * 用户协议asset本地保存路径
     */
    private static final String ACCOUNT_PROTOCOL_ASSET_PATH = "protocol/account_protocol.txt";

    /**
     * 隐私政策asset本地保存路径
     */
    private static final String PRIVACY_PROTOCOL_ASSET_PATH = "protocol/privacy_protocol.txt";

    @AutoWired(name = KEY_PROTOCOL_TITLE)
    String title;
    @BindView(R.id.tv_protocol_text)
    TextView tvProtocolText;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_service_protocol;
    }

    @Override
    protected void initArgs() {
        Router.getInstance().inject(this);
    }

    @Override
    protected TitleBar initTitle() {
        return super.initTitle().setTitle(title).setImmersive(true);
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        if (title.equals(ResUtils.getString(R.string.title_user_protocol))) {
            tvProtocolText.setText(getAccountProtocol());
        } else {
            tvProtocolText.setText(getPrivacyProtocol());
        }
    }

    @MemoryCache("account_protocol")
    private String getAccountProtocol() {
        return ResourceUtils.readStringFromAssert(ACCOUNT_PROTOCOL_ASSET_PATH);
    }

    @MemoryCache("privacy_protocol")
    private String getPrivacyProtocol() {
        return ResourceUtils.readStringFromAssert(PRIVACY_PROTOCOL_ASSET_PATH);
    }

}
