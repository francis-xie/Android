
package com.basic.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 存储相关常量

 * @date 2018/2/20 下午4:33
 */
public final class MemoryConstants {

    /**
     * Byte 与 Byte 的倍数
     */
    public static final int BYTE = 1;
    /**
     * KB 与 Byte 的倍数
     */
    public static final int KB   = 1024;
    /**
     * MB 与 Byte 的倍数
     */
    public static final int MB   = 1048576;
    /**
     * GB 与 Byte 的倍数
     */
    public static final int GB   = 1073741824;

    @IntDef({BYTE, KB, MB, GB})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Unit {
    }
}
