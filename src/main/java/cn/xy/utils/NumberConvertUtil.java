package cn.xy.utils;

/**
 * 进制工具类
 * 各个进制间的转换方法, 基本上java的内置方法均可满足转换, 这里仅为笔记
 * Bi(binary): 二进制; Dec(decimal): 十进制; Hex(hexadecimal): 十六进制
 * @author xy
 */
public final class NumberConvertUtil {
    private NumberConvertUtil() {

    }

    /**
     * 十六进制转十进制
     * @param hex 注意: 这里的十六进制参数不能是"0xff"这种0x打头的, 只能传"ff";
     */
    public static int hexToDec(String hex) {
        return Integer.parseInt(hex, 16);
    }

    /**
     * 十六进制转二进制
     * @return
     */
    public static String hexToBin(String hex) {
        int dec = hexToDec(hex);
        return decToBin(dec);
    }

    /**
     * 十六进制转二进制, 并高位补0
     * @param hex
     * @param length
     * @return
     */
    public static String hexToBinAndAddZero(String hex, int length) {
        String str = hexToBin(hex);
        return String.format("%0" + length + "d", Integer.parseInt(str));
    }

    /**
     * 十进制转十六进制
     * @param dec
     * @return 返回的结果不带"0x", 如255返回的是"ff"
     */
    public static String decToHex(int dec) {
        return Integer.toHexString(dec);
    }

    /**
     * 十进制转二进制
     * @param dec
     * @return
     */
    public static String decToBin(int dec) {
        return Integer.toBinaryString(dec);
    }

    /**
     * 十进制转二进制, 并高位补0
     * @param dec
     * @param length 结果的长度
     * @return
     */
    public static String decToBinAndAddZero(int dec, int length) {
        String str = decToBin(dec);
        return String.format("%0" + length + "d", Integer.parseInt(str));
    }

    /**
     * 二进制转十进制
     * @param bin
     * @return
     */
    public static int binToDec(String bin) {
        return Integer.parseInt(bin, 2);
    }

    /**
     * 二进制转十六进制
     * @param bin
     * @return 返回的结果不带"0x", 如255返回的是"ff"
     */
    public static String binToHex(String bin) {
        int dec = binToDec(bin);
        return decToHex(dec);
    }

}
