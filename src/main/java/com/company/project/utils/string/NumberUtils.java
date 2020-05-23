package com.company.project.utils.string;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 字符串的帮助类，提供静态方法，不可以实例化。
 */
public class NumberUtils {
    /**
     * 禁止实例化
     */
    private NumberUtils() {
    }

    /**
     * double  相加运算
     *
     * @param d1
     * @param d2
     * @return
     */
    public static Double add(Double d1, Double d2) {
        BigDecimal bigDecimal = new BigDecimal(d1.toString());
        BigDecimal bigDecimal2 = new BigDecimal(d2.toString());
        BigDecimal add = bigDecimal.add(bigDecimal2);
        return add.doubleValue();
    }

    /**
     * double  减法运算
     *
     * @param d1
     * @param d2
     * @return
     */
    public static Double sub(Double d1, Double d2) {
        BigDecimal bigDecimal = new BigDecimal(d1.toString());
        BigDecimal bigDecimal2 = new BigDecimal(d2.toString());
        BigDecimal add = bigDecimal.subtract(bigDecimal2);
        return add.doubleValue();
    }

    /**
     * double  乘法运算
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double mul(double d1, double d2) {
        //
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * double  除法运算
     *
     * @param d1
     * @param d2
     * @param len
     * @return
     */
    public static double div(double d1, double d2, int len) {
        if (d2 == 0) {
            return 0;
        }
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * double 进行四舍五入操作
     *
     * @param d
     * @param len
     * @return
     */
    public static double round(double d, int len) {
        BigDecimal b1 = new BigDecimal(d);
        BigDecimal b2 = new BigDecimal(1);
        // 任何一个数字除以1都是原数字
        // ROUND_HALF_UP是BigDecimal的一个常量，表示进行四舍五入的操作
        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * /转换文件大小
     */
    public static String FormetFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("0.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }
}
