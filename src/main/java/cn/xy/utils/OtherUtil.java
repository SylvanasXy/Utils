package cn.xy.utils;

/**
 * @description:
 * @author: xy
 */
public class OtherUtil {
    /**
     * 十六进制数据包添加空格
     * 例: 123456 -> 12 34 56
     * @param packet
     * @return
     */
    public static String hexPacketAddSpace(String packet) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = packet.length(); i < len; i++) {
            if (i % 2 != 0) {
                sb.append(packet.substring(i - 1, i + 1));
                if (i != len - 1) {
                    sb.append(" ");
                }
            }
        }
        return sb.toString();
    }
}
