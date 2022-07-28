
package com.basic.code.fragment.expands.webview;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;

import com.basic.web.core.Web;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.code.R;
import com.basic.code.base.webview.BaseWebViewFragment;
import com.basic.code.utils.Utils;
import com.basic.code.utils.XToastUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**

 * @since 2019-05-26 18:13
 */
@Page(name = "简单的JS通信")
public class JsWebViewFragment extends BaseWebViewFragment {

    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_js_webview;
    }

    @Override
    protected Web createWeb() {
        Web web = Utils.createWeb(this, flContainer, "file:///android_asset/jsTest.html");
        //注入接口,供JS调用
        web.getJsInterfaceHolder().addJavaObject("Android", new AndroidInterface());
        return web;
    }


    @SingleClick
    @OnClick({R.id.btn_js_no_param, R.id.btn_js_one_param, R.id.btn_js_more_param, R.id.btn_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_js_no_param:
                mWeb.getJsAccessEntrace().quickCallJs("callByAndroidNoParam");
                break;
            case R.id.btn_js_one_param:
                mWeb.getJsAccessEntrace().quickCallJs("callByAndroidOneParam", "Hello ! WEB");
                break;
            case R.id.btn_js_more_param:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mWeb.getJsAccessEntrace().quickCallJs("callByAndroidMoreParams", value -> Log.e("zhiqiang", "这里是调用JS方法的返回值:" + value), getJson(), "say！", " Hello! Web");
                }
                break;
            case R.id.btn_clear:
                mWeb.getJsAccessEntrace().quickCallJs("clearLog");
                break;
            default:
                break;
        }
    }

    private String getJson() {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", 1);
            jsonObject.put("name", "Web");
            jsonObject.put("age", 18);
            result = jsonObject.toString();
        } catch (Exception e) {

        }

        return result;
    }


    /**
     * 注入到JS里的对象接口
     */
    public static class AndroidInterface {

        @JavascriptInterface
        public void callAndroid(final String msg) {
            XToastUtils.toast("这是Js调用Android的方法，内容:" + msg);
        }

    }

}
