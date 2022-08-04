
package com.basic.code.fragment.expands.webview;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.basic.aop.annotation.Permission;
import com.basic.aop.annotation.SingleClick;
import com.basic.aop.cache.XDiskCache;
import com.basic.page.annotation.Page;
import com.basic.page.base.PageActivity;
import com.basic.page.base.PageFragment;
import com.basic.page.core.PageOption;
import com.basic.router.annotation.AutoWired;
import com.basic.router.launcher.Router;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.base.web.x5.FileReaderView;
import com.basic.code.utils.ToastUtils;
import com.basic.tools.app.IntentUtils;
import com.basic.tools.file.FileUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Request;

import static com.basic.aop.consts.PermissionConsts.STORAGE;
import static com.basic.code.fragment.expands.webview.TbsWebFileReaderFragment.KEY_FILE_URI;

@Page(name = "腾讯X5文件浏览器", params = {KEY_FILE_URI})
public class TbsWebFileReaderFragment extends BaseFragment {

    /**
     * 文件的路径，可以是http/https在线文件
     */
    static final String KEY_FILE_URI = "key_file_uri";

    @BindView(R.id.file_reader_view)
    FileReaderView fileReaderView;

    @AutoWired(name = KEY_FILE_URI)
    String fileUri;

    @Override
    protected void initArgs() {
        super.initArgs();
        Router.getInstance().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tbs_web_file_reader;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.ic_share_white_24dp) {
            @SingleClick
            @Override
            public void performAction(View view) {
                shareFile(fileReaderView.getLoadFileUri());
            }
        });
        return titleBar;
    }

    /**
     * 分享文件
     *
     * @param uri 文件资源
     */
    private void shareFile(Uri uri) {
        if (uri == null) {
            ToastUtils.toast("文件加载失败！");
            return;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType(IntentUtils.DocumentType.ANY);
        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(intent, "分享到"));
    }

    @Override
    protected void initViews() {
        openFile(fileUri);
    }

    /**
     * 打开文件
     *
     * @param fileUri
     */
    @Permission(STORAGE)
    private void openFile(String fileUri) {
        if (!TextUtils.isEmpty(fileUri)) {
            //是网络文件
            if (fileUri.contains("http") || fileUri.contains("https")) {
                String cacheFilePath = XDiskCache.getInstance().load(fileUri);
                //文件缓存存在
                if (!TextUtils.isEmpty(cacheFilePath) && FileUtils.isFileExists(cacheFilePath)) {
                    fileReaderView.show(cacheFilePath);
                } else {
                    //没有缓存，下载
                    startDownload(fileReaderView, fileUri);
                }
            } else {
                fileReaderView.show(fileUri);
            }
        } else {
            ToastUtils.toast("文件路径无效！");
            popToBack();
        }
    }

    /**
     * 开始下载
     *
     * @param fileUri
     */
    private void startDownload(final FileReaderView fileReaderView, final String fileUri) {
        OkHttpUtils.get()
                .url(fileUri)
                .build()
                .execute(new FileCallBack(fileReaderView.getCacheFileDir(), fileReaderView.getFileNameByUrl(fileUri)) {
                    @Override
                    public void onBefore(Request request, int id) {
                        getMessageLoader("文件下载中...").show();
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        getMessageLoader().updateMessage("文件下载中(" + ((int) (progress * 100)) + "%)");
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        getMessageLoader().dismiss();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        getMessageLoader().dismiss();
                        //保存缓存文件的路径
                        String filePath = response.getPath();
                        XDiskCache.getInstance().save(fileUri, filePath);
                        fileReaderView.show(filePath);
                    }
                });
    }


    @Override
    public void onDestroyView() {
        if (fileReaderView != null) {
            fileReaderView.stop();
        }
        super.onDestroyView();
    }


    public static void show(PageFragment fragment, String path) {
        PageOption.to(TbsWebFileReaderFragment.class)
                .setNewActivity(true)
                .putString(KEY_FILE_URI, path)
                .open(fragment);
    }

    public static void show(PageActivity activity, String path) {
        PageOption.to(TbsWebFileReaderFragment.class)
                .setNewActivity(true)
                .putString(KEY_FILE_URI, path)
                .open(activity);
    }
}
