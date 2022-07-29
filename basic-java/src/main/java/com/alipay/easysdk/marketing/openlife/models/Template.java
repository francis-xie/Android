// This file is auto-generated, don't edit it. Thanks.
package com.alipay.easysdk.marketing.openlife.models;

import com.basic.http.NameInMap;
import com.basic.http.HttpModel;
import com.basic.http.Validation;

public class Template extends HttpModel {
    @NameInMap("template_id")
    @Validation(required = true)
    public String templateId;

    @NameInMap("context")
    @Validation(required = true)
    public Context context;

    public static Template build(java.util.Map<String, ?> map) throws Exception {
        Template self = new Template();
        return HttpModel.build(map, self);
    }

    public Template setTemplateId(String templateId) {
        this.templateId = templateId;
        return this;
    }
    public String getTemplateId() {
        return this.templateId;
    }

    public Template setContext(Context context) {
        this.context = context;
        return this;
    }
    public Context getContext() {
        return this.context;
    }

}
