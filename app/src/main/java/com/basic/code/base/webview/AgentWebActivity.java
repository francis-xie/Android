
package com.basic.code.base.webview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.basic.router.facade.Postcard;
import com.basic.router.facade.callback.NavCallback;
import com.basic.router.launcher.Router;
import com.basic.face.widget.slideback.SlideBack;
import com.basic.code.R;
import com.basic.code.base.BaseAppCompatActivity;
import com.basic.code.utils.XToastUtils;

import static com.basic.code.base.webview.AgentWebFragment.KEY_URL;

/**
 * 壳浏览器
 *

 * @since 2019/1/5 上午12:15
 */
public class AgentWebActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_web);

        SlideBack.withFixSize(this)
                .haveScroll(true)
                .callBack(this::finish)
                .register();

        loadWeb(getIntent());
    }

    /**
     * 加载web页面
     *
     * @param intent
     */
    private void loadWeb(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            Router.getInstance().build(uri).navigation(this, new NavCallback() {
                @Override
                public void onArrival(Postcard postcard) {
                    finish();
                }

                @Override
                public void onLost(Postcard postcard) {
                    loadUrl(uri.toString());
                }
            });
        } else {
            String url = intent.getStringExtra(KEY_URL);
            loadUrl(url);
        }
    }


    private void loadUrl(String url) {
        if (url != null) {
            openFragment(url);
        } else {
            XToastUtils.error("数据出错！");
            finish();
        }
    }


    private AgentWebFragment mAgentWebFragment;

    private void openFragment(String url) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container_frame_layout, mAgentWebFragment = AgentWebFragment.getInstance(url));
        ft.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //一定要保证 mAentWebFragemnt 回调
//		mAgentWebFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AgentWebFragment agentWebFragment = mAgentWebFragment;
        if (agentWebFragment != null) {
            if (((FragmentKeyDown) agentWebFragment).onFragmentKeyDown(keyCode, event)) {
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        loadWeb(getIntent());
    }

    @Override
    protected void onDestroy() {
        SlideBack.unregister(this);
        super.onDestroy();
    }

}
