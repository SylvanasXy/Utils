package cn.xy.utils;

import java.util.regex.Pattern;

/**
 * 验证工具类
 *
 * @author xy
 */
public final class VerifyUtil {
    private VerifyUtil() {

    }

    /**
     * 验证手机号格式
     * @param phone 需要验证的手机号
     * @return
     */
    public static boolean verifyPhone(String phone) {
        return Pattern.matches("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$", phone);
    }

    /**
     * 验证email
     * @param email 需要验证的email
     * @return
     */
    public static boolean verifyEMail(String email) {
        return Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", email);
    }

    /**
     * 验证车牌号格式
     * @param carLicense 需要验证的车牌号
     * @return
     */
    public static boolean verifyCarLicense(String carLicense) {
        return Pattern.matches("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$", carLicense);
    }
}
