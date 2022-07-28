
package com.basic.code.utils.update;

import androidx.annotation.NonNull;

import com.basic.renew.proxy.IUpdateHttpService;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 使用okhttp
 */
public class OkHttpUpdateHttpServiceImpl implements IUpdateHttpService {

    public OkHttpUpdateHttpServiceImpl() {
        OkHttpUtils.initClient(new OkHttpClient.Builder()
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .build());
    }

    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, Object> params, final @NonNull Callback callBack) {
        OkHttpUtils.get()
                .url(url)
                .params(transform(params))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBack.onError(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        callBack.onSuccess(response);
                    }
                });
    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, Object> params, final @NonNull Callback callBack) {
        OkHttpUtils.post()
                .url(url)
                .params(transform(params))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBack.onError(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        callBack.onSuccess(response);
                    }
                });
    }

    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, final @NonNull DownloadCallback callback) {
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(path, fileName) {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        callback.onProgress(progress, total);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onError(e);
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        callback.onSuccess(response);
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        callback.onStart();
                    }
                });
    }

    @Override
    public void cancelDownload(@NonNull String url) {

    }

    private Map<String, String> transform(Map<String, Object> params) {
        Map<String, String> map = new TreeMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            map.put(entry.getKey(), entry.getValue().toString());
        }
        return map;
    }


}