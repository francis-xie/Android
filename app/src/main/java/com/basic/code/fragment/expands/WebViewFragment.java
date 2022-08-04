
package com.basic.code.fragment.expands;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.basic.aop.annotation.Permission;
import com.basic.page.annotation.Page;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.base.BaseSimpleListFragment;
import com.basic.code.base.web.PageWebViewFragment;
import com.basic.code.fragment.expands.webview.JsWebViewFragment;
import com.basic.code.fragment.expands.webview.TBSX5Fragment;
import com.basic.code.fragment.expands.webview.TbsWebFileReaderFragment;
import com.basic.code.utils.Utils;
import com.basic.tools.app.IntentUtils;
import com.basic.tools.app.PathUtils;

import java.util.List;

import static com.basic.aop.consts.PermissionConsts.STORAGE;

@Page(name = "web浏览器", extra = R.drawable.ic_expand_web)
public class WebViewFragment extends BaseSimpleListFragment {

    public static final int REQUEST_SELECT_FILE = 1000;

    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("使用系统默认API调用");
        lists.add("直接显示调用");
        lists.add("文件下载");
        lists.add("input标签文件上传");
        lists.add("电话、信息、邮件");
        lists.add("地图定位");
        lists.add("视频播放");
        lists.add("简单的JS通信");
        lists.add("腾讯X5能力线上调试");
        lists.add("腾讯X5文件浏览器（点击选择文件）");
        lists.add("腾讯X5文件浏览器（加载在线pdf文档）");
        lists.add("腾讯X5文件浏览器（加载在线word文档）");
        lists.add("腾讯X5文件浏览器（加载在线excel文档）");
        return lists;
    }

    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                systemApi("https://www.baidu.com/");
                break;
            case 1:
                PageWebViewFragment.openUrl(this, "https://www.baidu.com/");
                break;
            case 2:
                Utils.goWeb(getContext(), "http://android.myapp.com/");
                break;
            case 3:
                Utils.goWeb(getContext(), "file:///android_asset/upload_file/uploadfile.html");
                break;
            case 4:
                Utils.goWeb(getContext(), "file:///android_asset/sms/sms.html");
                break;
            case 5:
                Utils.goWeb(getContext(), "https://map.baidu.com/mobile/webapp/index/index/#index/index/foo=bar/vt=map");
                break;
            case 6:
                Utils.goWeb(getContext(), "https://v.youku.com/v_show/id_XMjY1Mzc4MjU3Ng==.html?tpa=dW5pb25faWQ9MTAzNzUzXzEwMDAwMV8wMV8wMQ&refer=sousuotoufang_market.qrwang_00002944_000000_QJFFvi_19031900");
                break;
            case 7:
                openPage(JsWebViewFragment.class);
                break;
            case 8:
                TBSX5Fragment.openUrl(this, "http://debugtbs.qq.com/");
                break;
            case 9:
                chooseFile();
                break;
            case 10:
                TbsWebFileReaderFragment.show(this, "https://gitee.com/zhiqiang/Resource/raw/master/file/test.pdf");
                break;
            case 11:
                TbsWebFileReaderFragment.show(this, "https://gitee.com/zhiqiang/Resource/raw/master/file/test.docx");
                break;
            case 12:
                TbsWebFileReaderFragment.show(this, "https://gitee.com/zhiqiang/Resource/raw/master/file/test.xlsx");
                break;
            default:
                break;
        }
    }


    @Permission(STORAGE)
    private void chooseFile() {
        startActivityForResult(IntentUtils.getDocumentPickerIntent(IntentUtils.DocumentType.ANY), REQUEST_SELECT_FILE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择系统图片并解析
        if (requestCode == REQUEST_SELECT_FILE) {
            if (data != null) {
                Uri uri = data.getData();
                TbsWebFileReaderFragment.show(this, PathUtils.getFilePathByUri(uri));
            }
        }
    }


    /**
     * 以系统API的方式请求浏览器
     *
     * @param url
     */
    public void systemApi(final String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("Github") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://github.com/Justson/Web");
            }
        });
        return titleBar;
    }


}
