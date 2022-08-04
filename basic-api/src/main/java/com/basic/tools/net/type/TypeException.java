
package com.basic.tools.net.type;

/**
 * <pre>
 *     desc   : 类型出错

 *     time   : 2018/4/28 上午12:51
 * </pre>
 */
public class TypeException extends RuntimeException {
    public TypeException() {
    }

    public TypeException(String message) {
        super(message);
    }

    public TypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeException(Throwable cause) {
        super(cause);
    }

}
