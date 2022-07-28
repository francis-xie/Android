package com.basic.code.core.web;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.basic.code.R;
import com.basic.code.utils.ToastUtils;
import com.basic.router.facade.Postcard;
import com.basic.router.facade.callback.NavCallback;
import com.basic.router.launcher.Router;
import com.basic.face.widget.slideback.SlideBack;

import static com.basic.code.core.web.WebFragment.KEY_URL;

/**
 * 壳浏览器
 */
public class WebActivity extends AppCompatActivity {

    /**
     * 请求浏览器
     *
     * @param url
     */
    public static void goWeb(Context context, final String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(KEY_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_web);

        SlideBack.with(this)
                .haveScroll(true)
                .callBack(this::finish)
                .register();

        Uri uri = getIntent().getData();
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
            String url = getIntent().getStringExtra(KEY_URL);
            loadUrl(url);
        }
    }

    private void loadUrl(String url) {
        if (url != null) {
            openFragment(url);
        } else {
            ToastUtils.error("数据出错！");
            finish();
        }
    }

    private WebFragment mWebFragment;

    private void openFragment(String url) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container_frame_layout, mWebFragment = WebFragment.getInstance(url));
        ft.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebFragment webFragment = mWebFragment;
        if (webFragment != null) {
            if (((FragmentKeyDown) webFragment).onFragmentKeyDown(keyCode, event)) {
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        SlideBack.unregister(this);
        super.onDestroy();
    }
}
