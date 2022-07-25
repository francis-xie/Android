// This file is auto-generated, don't edit it. Thanks.
package com.alipay.easysdk.marketing.openlife.models;

import com.basic.http.NameInMap;
import com.basic.http.HttpModel;
import com.basic.http.Validation;

public class Text extends HttpModel {
    @NameInMap("title")
    @Validation(required = true)
    public String title;

    @NameInMap("content")
    @Validation(required = true)
    public String content;

    public static Text build(java.util.Map<String, ?> map) throws Exception {
        Text self = new Text();
        return HttpModel.build(map, self);
    }

    public Text setTitle(String title) {
        this.title = title;
        return this;
    }
    public String getTitle() {
        return this.title;
    }

    public Text setContent(String content) {
        this.content = content;
        return this;
    }
    public String getContent() {
        return this.content;
    }

}
