package com.basic.code.utils.update;

import androidx.annotation.NonNull;

import com.basic.code.utils.ToastUtils;
import com.basic.http2.Http;
import com.basic.http2.HttpSDK;
import com.basic.http2.callback.DownloadProgressCallBack;
import com.basic.http2.callback.SimpleCallBack;
import com.basic.http2.exception.ApiException;
import com.basic.renew.proxy.IUpdateHttpService;
import com.basic.tools.file.FileUtils;
import com.basic.tools.net.JsonUtil;

import java.util.Map;

/**
 * Http2实现的请求更新
 */
public class HttpUpdateHttpServiceImpl implements IUpdateHttpService {

    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, Object> params, @NonNull final IUpdateHttpService.Callback callBack) {
        Http.get(url)
                .params(params)
                .keepJson(true)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String response) throws Throwable {
                        callBack.onSuccess(response);
                    }
                    @Override
                    public void onError(ApiException e) {
                        callBack.onError(e);
                    }
                });
    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, Object> params, @NonNull final IUpdateHttpService.Callback callBack) {
        Http.post(url)
                .upJson(JsonUtil.toJson(params))
                .keepJson(true)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String response) throws Throwable {
                        callBack.onSuccess(response);
                    }

                    @Override
                    public void onError(ApiException e) {
                        callBack.onError(e);
                    }
                });
    }

    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final IUpdateHttpService.DownloadCallback callback) {
        HttpSDK.addRequest(url, Http.downLoad(url)
                .savePath(path)
                .saveName(fileName)
                .isUseBaseUrl(false)
                .execute(new DownloadProgressCallBack<String>() {
                    @Override
                    public void onStart() {
                        callback.onStart();
                    }

                    @Override
                    public void onError(ApiException e) {
                        callback.onError(e);
                    }

                    @Override
                    public void update(long downLoadSize, long totalSize, boolean done) {
                        callback.onProgress(downLoadSize / (float) totalSize, totalSize);
                    }

                    @Override
                    public void onComplete(String path) {
                        callback.onSuccess(FileUtils.getFileByPath(path));
                    }
                }));
    }

    @Override
    public void cancelDownload(@NonNull String url) {
        ToastUtils.info("已取消更新");
        HttpSDK.cancelRequest(url);
    }
}
