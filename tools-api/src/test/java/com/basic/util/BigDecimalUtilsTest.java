
package com.basic.tools;

import org.junit.Test;

import java.math.BigDecimal;

import static com.basic.tools.common.BigDecimalUtils.formatMoney;
import static com.basic.tools.common.BigDecimalUtils.splitAndFormatMoney;
import static org.junit.Assert.assertEquals;

/**
 
 * @since 2018/6/27 下午2:54
 */
public class BigDecimalUtilsTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

        System.out.println("金额0：         " + formatMoney(new BigDecimal(0)));
        System.out.println("金额0.0 ：         " + formatMoney(new BigDecimal(0.0)));
        System.out.println("金额0.00：         " + formatMoney(new BigDecimal(0.00)));
        System.out.println("金额0.58：         " + formatMoney(new BigDecimal(0.58)));
        System.out.println("金额5.58：         " + formatMoney(new BigDecimal(5.58)));
        System.out.println("金额5.54：          " + formatMoney(new BigDecimal(5.54)));
        System.out.println("金额512322.555555111：          " + formatMoney(new BigDecimal(512322.555555111)));
        System.out.println("金额512322.555555111：          " + splitAndFormatMoney(new BigDecimal(512322.555555111)));
        System.out.println("金额3423423425.54：     " + formatMoney(new BigDecimal(3423423425.54)));
        System.out.println("金额3423423425.58：      " + formatMoney(new BigDecimal(3423423425.58)));
        System.out.println("金额1000000.543453：     " + formatMoney(new BigDecimal(1000000.543453)));
        System.out.println("金额9343788754.573453：     " + formatMoney(new BigDecimal(-9343788754.573453)));
        System.out.println("金额9343788756.577：     " + formatMoney(new BigDecimal(-9343788756.577)));
        System.out.println("金额-343788756.577：     " + formatMoney(new BigDecimal(-343788756.577)));
        System.out.println("金额-34756.54：     " + formatMoney(new BigDecimal(-34756.54)));
        System.out.println("金额-34756.556：     " + formatMoney(new BigDecimal(-34756.556)));

        //DateUtils.rollDay(new Date(), -15);
        //直接使用浮点数进行计算，得到的结果是有问题的
        //System.out.println(0.01 + 0.05);

        //使用了BigDecimal类进行计算后，可以做到精确计算
        //System.out.println(BigDecimalMoney.add(0.0000000000005, 0.00000001));
    }
}
