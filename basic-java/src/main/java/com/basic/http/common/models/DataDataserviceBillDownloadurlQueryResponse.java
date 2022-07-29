// This file is auto-generated, don't edit it. Thanks.
package com.basic.http.common.models;

import com.basic.http.HttpModel;
import com.basic.http.NameInMap;
import com.basic.http.Validation;

public class DataDataserviceBillDownloadurlQueryResponse extends HttpModel {
    // 响应原始字符串
    @NameInMap("http_body")
    @Validation(required = true)
    public String httpBody;

    @NameInMap("code")
    @Validation(required = true)
    public String code;

    @NameInMap("msg")
    @Validation(required = true)
    public String msg;

    @NameInMap("sub_code")
    @Validation(required = true)
    public String subCode;

    @NameInMap("sub_msg")
    @Validation(required = true)
    public String subMsg;

    @NameInMap("bill_download_url")
    @Validation(required = true)
    public String billDownloadUrl;

    public static DataDataserviceBillDownloadurlQueryResponse build(java.util.Map<String, ?> map) throws Exception {
        DataDataserviceBillDownloadurlQueryResponse self = new DataDataserviceBillDownloadurlQueryResponse();
        return HttpModel.build(map, self);
    }

    public DataDataserviceBillDownloadurlQueryResponse setHttpBody(String httpBody) {
        this.httpBody = httpBody;
        return this;
    }
    public String getHttpBody() {
        return this.httpBody;
    }

    public DataDataserviceBillDownloadurlQueryResponse setCode(String code) {
        this.code = code;
        return this;
    }
    public String getCode() {
        return this.code;
    }

    public DataDataserviceBillDownloadurlQueryResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }
    public String getMsg() {
        return this.msg;
    }

    public DataDataserviceBillDownloadurlQueryResponse setSubCode(String subCode) {
        this.subCode = subCode;
        return this;
    }
    public String getSubCode() {
        return this.subCode;
    }

    public DataDataserviceBillDownloadurlQueryResponse setSubMsg(String subMsg) {
        this.subMsg = subMsg;
        return this;
    }
    public String getSubMsg() {
        return this.subMsg;
    }

    public DataDataserviceBillDownloadurlQueryResponse setBillDownloadUrl(String billDownloadUrl) {
        this.billDownloadUrl = billDownloadUrl;
        return this;
    }
    public String getBillDownloadUrl() {
        return this.billDownloadUrl;
    }

}
