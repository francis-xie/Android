package com.basic.http2.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.basic.http2.annotation.ParamKey;
import com.basic.http2.logs.HttpLog;
import com.basic.http2.model.ApiResult;
import com.basic.http2.model.HttpRequest;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * http工具类
 */
public final class HttpUtils {

    public static final Charset UTF8 = Charset.forName("UTF-8");

    private static Gson sGson = new Gson();

    private HttpUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 把单个指定类型的对象 转换为 JSON 字符串
     *
     * @param src 需要序列化的对象
     * @return JSON 字符串
     */
    public static String toJson(Object src) {
        return sGson.toJson(src);
    }

    /**
     * 获取json的请求体
     *
     * @param object
     * @return
     */
    public static RequestBody getJsonRequestBody(Object object) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), HttpUtils.toJson(object));
    }

    /**
     * 获取json的请求体
     *
     * @param json
     * @return
     */
    public static RequestBody getJsonRequestBody(String json) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    }

    /**
     * 获取json的响应体
     *
     * @param json
     * @return
     */
    public static ResponseBody getJsonResponseBody(String json) {
        return ResponseBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    }

    /**
     * 获取响应body的byte流
     *
     * @param response
     * @return
     */
    @Nullable
    public static byte[] getResponseBody(@NonNull Response response) {
        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }
        byte[] source = null;
        try {
            source = body.bytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return source;
    }

    /**
     * 获取响应body的String
     *
     * @param response
     * @return
     * @throws IOException
     */
    @NonNull
    public static String getResponseBodyString(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            return "";
        }
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }
        return buffer.clone().readString(charset);
    }

    /**
     * 判断MediaType是否是text类型
     *
     * @param mediaType
     * @return
     */
    public static boolean isText(MediaType mediaType) {
        return mediaType != null && (mediaType.type() != null && "text".equals(mediaType.type()) || mediaType.subtype() != null && "json".equals(mediaType.subtype()));
    }

    /**
     * 判断请求响应内容是否是人能读懂的内容
     */
    public static boolean isPlaintext(MediaType mediaType) {
        if (mediaType == null) {
            return false;
        }
        if (mediaType.type() != null && "text".equals(mediaType.type())) {
            return true;
        }
        String subtype = mediaType.subtype();
        if (subtype != null) {
            subtype = subtype.toLowerCase();
            return subtype.contains("x-www-form-urlencoded") ||
                    subtype.contains("json") ||
                    subtype.contains("xml") ||
                    subtype.contains("html");
        }
        return false;
    }

    /**
     * 获取注解设置请求key的请求Json
     *
     * @param httpRequest
     * @return
     * @throws IllegalAccessException
     */
    public static String getAnnotationParamString(HttpRequest httpRequest) throws IllegalAccessException {
        TreeMap<String, Object> params = new TreeMap<>();
        Field[] fields = httpRequest.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            ParamKey paramKey = field.getAnnotation(ParamKey.class);
            if (paramKey != null) {
                params.put(paramKey.key(), field.get(httpRequest));
            } else {
                params.put(field.getName(), field.get(httpRequest));
            }
        }
        return HttpUtils.toJson(params);
    }

    /**
     * 更新请求body
     *
     * @param oldRequest
     * @param params
     * @return
     */
    public static Request updateRequestBody(Request oldRequest, HashMap<String, Object> params) {
        String requestBody = getRequestBodyString(oldRequest);
        try {
            JSONObject jsonObject = new JSONObject(requestBody);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
            return oldRequest.newBuilder().post(HttpUtils.getJsonRequestBody(jsonObject.toString())).build();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return oldRequest;
    }

    /**
     * 获取post请求的json
     *
     * @param request
     */
    public static String getRequestBodyString(Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            if (copy.body() == null) {
                return "";
            }
            copy.body().writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = copy.body().contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            return buffer.readString(charset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获得错误的返回
     *
     * @param oldResponse 旧返回
     * @param code        错误码
     * @param message     错误信息
     * @return
     */
    public static Response getErrorResponse(Response oldResponse, int code, String message) {
        ApiResult apiResult = new ApiResult().setCode(code).setMsg(message);
        return oldResponse.newBuilder().body(HttpUtils.getJsonResponseBody(HttpUtils.toJson(apiResult))).build();
    }

    //==============url 参数=====================//

    /**
     * 重置url上的请求参数
     *
     * @param oldRequest 拦截的旧请求
     * @param key        请求的key
     * @param value      参数值
     * @return
     */
    public static Request resetUrlParams(Request oldRequest, String key, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(key, value);
        return resetUrlParams(oldRequest, params);
    }

    /**
     * 重置url上的请求参数
     *
     * @param oldRequest 拦截的旧请求
     * @param params     参数
     * @return
     */
    public static Request resetUrlParams(Request oldRequest, Map<String, Object> params) {
        String url = HttpUtils.createUrlFromParams(HttpUtils.parseUrl(oldRequest.url().toString()), params);
        return oldRequest.newBuilder().url(url).build();
    }

    /**
     * 刷新url上的请求参数
     *
     * @param oldRequest 拦截的旧请求
     * @param key        请求的key
     * @param value      参数值
     * @return
     */
    public static Request updateUrlParams(Request oldRequest, String key, Object value) {
        return oldRequest.newBuilder()
                .url(updateUrlParams(oldRequest.url().toString(), key, value))
                .build();
    }

    /**
     * 刷新url上的请求参数
     *
     * @param oldRequest 拦截的旧请求
     * @param params     刷新的参数集合
     * @return
     */
    public static Request updateUrlParams(Request oldRequest, Map<String, Object> params) {
        return oldRequest.newBuilder()
                .url(updateUrlParams(oldRequest.url().toString(), params))
                .build();
    }

    /**
     * 更新url中的参数[保证参数不重复]
     *
     * @param url   请求的url
     * @param key   请求的key
     * @param value 参数值
     * @return
     */
    public static String updateUrlParams(String url, String key, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(key, value);
        return updateUrlParams(url, params);
    }

    /**
     * 更新url中的参数[保证参数不重复]
     *
     * @param url    请求的url
     * @param params 更新的参数
     * @return
     */
    public static String updateUrlParams(String url, Map<String, Object> params) {
        Map<String, String> newParams = new LinkedHashMap<>();
        Map<String, String> oldParams = getUrlParams(url);
        if (oldParams != null) {
            newParams.putAll(oldParams);
        }
        for (Map.Entry<String, Object> param : params.entrySet()) {
            newParams.put(param.getKey(), String.valueOf(param.getValue()));
        }

        StringBuilder sb = new StringBuilder();
        sb.append(parseUrl(url)).append("?");

        try {
            for (Map.Entry<String, String> param : newParams.entrySet()) {
                //对参数进行 utf-8 编码,防止头信息传中文
                String urlValue = URLEncoder.encode(param.getValue(), UTF8.name());
                sb.append(param.getKey()).append("=").append(urlValue).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            HttpLog.e(e);
        }
        return url;
    }


    /**
     * 解析前：https://xxx.xxx.xxx/app/chairdressing/skinAnalyzePower/skinTestResult?appId=10101
     * 解析后：https://xxx.xxx.xxx/app/chairdressing/skinAnalyzePower/skinTestResult
     *
     * @param url
     * @return
     */
    public static String parseUrl(String url) {
        // 如果URL不是空字符串
        if (!"".equals(url) && url.contains("?")) {
            url = url.substring(0, url.indexOf('?'));
        }
        return url;
    }

    /**
     * 将参数拼接到url中
     *
     * @param url    请求的url
     * @param params 参数
     * @return
     */
    public static String createUrlFromParams(String url, Map<String, Object> params) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(url);
            if (url.indexOf('&') > 0 || url.indexOf('?') > 0) {
                sb.append("&");
            } else {
                sb.append("?");
            }
            for (Map.Entry<String, Object> urlParams : params.entrySet()) {
                String urlValues = String.valueOf(urlParams.getValue());
                //对参数进行 utf-8 编码,防止头信息传中文
                String urlValue = URLEncoder.encode(urlValues, UTF8.name());
                sb.append(urlParams.getKey()).append("=").append(urlValue).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            HttpLog.e(e);
        }
        return url;
    }

    /**
     * 获取URL中参数 并返回Map[LinkedHashMap(有顺序)]
     *
     * @param url
     * @return
     */
    public static Map<String, String> getUrlParams(String url) {
        Map<String, String> params = null;
        try {
            String[] urlParts = url.split("\\?");
            if (urlParts.length > 1) {
                params = new LinkedHashMap<>();
                String query = urlParts[1];
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    String key = URLDecoder.decode(pair[0], UTF8.name());
                    String value = "";
                    if (pair.length > 1) {
                        value = URLDecoder.decode(pair[1], UTF8.name());
                    }
                    params.put(key, value);
                }
            }
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return params;
    }

}
