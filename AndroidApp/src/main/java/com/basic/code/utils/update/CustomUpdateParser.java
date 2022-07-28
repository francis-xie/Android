package com.basic.code.utils.update;

import com.basic.renew.entity.UpdateEntity;
import com.basic.renew.proxy.impl.AbstractUpdateParser;

/**
 * 版本更新信息自定义json解析器
 */
public class CustomUpdateParser extends AbstractUpdateParser {

    @Override
    public UpdateEntity parseJson(String json) throws Exception {
        // TODO: 2020-02-18 这里填写你需要自定义的json格式，如果使用默认的API就不需要设置
        return null;
    }

}
