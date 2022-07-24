
package com.basic.tools;

import org.junit.Test;

import static com.basic.tools.data.ArrayUtils.indexOf;
import static com.basic.tools.data.ArrayUtils.lastIndexOf;
import static org.junit.Assert.assertEquals;

/**

 * @since 2018/6/27 下午6:24
 */
public class ArrayUtilsTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

        byte[] org = "kadeadedkkcfdededghkk".getBytes();
        byte[] search = "kk".getBytes();
        int firstIndex = indexOf(org, search);
        int lastIndex = lastIndexOf(org, search);

        System.out.println("firstIndex=" + firstIndex + ", lastIndex=" + lastIndex);

        long t1 = 0;
        long t2 = 0;
        int f1 = 0;
        int f2 = 0;
        for (int i = 0; i < 10000; i++) {
            long s1 = System.nanoTime();
            f1 = indexOf(org, search, 0);
            long s2 = System.nanoTime();
            f2 = indexOf(org, search);
            long s3 = System.nanoTime();
            t1 = t1 + (s2 - s1);
            t2 = t2 + (s3 - s2);
        }
        System.out.println("kmp=" + t1 / 10000 + ",ali=" + t2 / 10000);
        System.out.println("f1=" + f1 + ",f2=" + f2);
    }
}
