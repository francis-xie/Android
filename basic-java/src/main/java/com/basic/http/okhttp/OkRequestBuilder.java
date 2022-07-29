package com.basic.http.okhttp;

import com.basic.http.HttpRequest;
import okhttp3.Request;

import java.net.URL;
import java.util.Map;


public class OkRequestBuilder {
    private Request.Builder builder;

    public OkRequestBuilder(Request.Builder builder) {
        this.builder = builder;
    }

    public OkRequestBuilder url(URL url) {
        this.builder.url(url);
        return this;
    }

    public OkRequestBuilder header(Map<String, String> headers) {
        for (String headerName : headers.keySet()) {
            this.builder.header(headerName, headers.get(headerName));
        }
        return this;
    }

    public Request buildRequest(HttpRequest request) {
        String method = request.method.toUpperCase();
        OkRequestBody requestBody;
        switch (method) {
            case "DELETE":
                this.builder.delete();
                break;
            case "POST":
                requestBody = new OkRequestBody(request);
                this.builder.post(requestBody);
                break;
            case "PUT":
                requestBody = new OkRequestBody(request);
                this.builder.put(requestBody);
                break;
            case "PATCH":
                requestBody = new OkRequestBody(request);
                this.builder.patch(requestBody);
                break;
            default:
                this.builder.get();
                break;
        }
        return this.builder.build();
    }
}
