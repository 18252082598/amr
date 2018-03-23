package com.joymeter.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 请求参数的合法性检查
 *
 * @author
 */
public class RegexMatches {

    /**
     * 判断表号的合法性
     *
     * @param meterNo
     * @return
     */
    public static boolean isMeterNo(String meterNo) {
        Matcher isMeterNo = Pattern.compile("^\\d{8,20}$").matcher(meterNo);
        return isMeterNo.matches();
    }

    /**
     * 判断金额的合法性
     *
     * @param rechargeMoney
     * @return
     */
    public static boolean isNumeric(Double rechargeMoney) {
        Matcher isNumeric = Pattern.compile("^\\d+(\\.\\d+)?$").matcher(Double.toString(rechargeMoney));
        return isNumeric.matches();
    }

    /**
     * 判断金额的合法性
     *
     * @param value
     * @return
     */
    public static boolean isNumeric(final String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        Matcher isNumeric = Pattern.compile("^\\d+(\\.\\d+)?$").matcher(value);
        return isNumeric.matches();
    }

    /**
     * 判断金额的合法性
     *
     * @param value
     * @return
     */
    public static boolean isDouble(final String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        Matcher isNumeric = Pattern.compile("^[-//+]?//d+(//.//d*)?|//.//d+$").matcher(value);
        return isNumeric.matches();
    }

    /**
     * 验证格式为yyyy-MM-dd的日期
     *
     * @param date
     * @return
     */
    public static boolean isDate(String date) {
        String dateRegex = "^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)$";
        return Pattern.compile(dateRegex).matcher(date).matches();
    }

    /**
     * 验证格式为yyyy-MM-dd HH:mm:ss的日期
     *
     * @param datetime
     * @return
     */
    public static boolean isDateTime(String datetime) {
        String dateRegex = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))([\\s]{1})(([0-1]{1}[0-9]{1})|([2]{1}[0-3]{1}))([:])([0-5]{1}[0-9]{1})([:])([0-5]{1}[0-9]{1})$";
        return Pattern.compile(dateRegex).matcher(datetime).matches();
    }
}
