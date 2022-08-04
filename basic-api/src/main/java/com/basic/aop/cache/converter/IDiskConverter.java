
package com.basic.aop.cache.converter;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * <pre>
 *     desc   : 通用转换器接口

 *     time   : 2018/4/23 下午10:11
 * </pre>
 */
public interface IDiskConverter {

    /**
     * 读取
     *
     * @param is 输入流
     * @param type  读取数据后要转换的数据类型
     * @return
     */
    <T> T load(InputStream is, Type type);

    /**
     * 写入
     *
     * @param os 输出流
     * @param data 保存的数据
     * @return
     */
    boolean writer(OutputStream os, Object data);
}
