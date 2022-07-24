
package com.basic.router.enums;

import com.basic.router.annotation.AutoWired;

/**
 * 被{@link AutoWired}标注的字段的类型
 *

 * @since 2018/5/17 上午12:32
 */
public enum TypeKind {
    // Base type
    BOOLEAN,
    BYTE,
    SHORT,
    INT,
    LONG,
    CHAR,
    FLOAT,
    DOUBLE,

    // Other type
    STRING,
    PARCELABLE,
    OBJECT
}
