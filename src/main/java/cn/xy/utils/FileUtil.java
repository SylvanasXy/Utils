package cn.xy.utils;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;

/**
 * 文件操作类
 * @author xy
 */
public final class FileUtil {
    private FileUtil() {

    }

    /**
     * 二进制流转文件(图片/视频等)
     * @param bytes 二进制byte数组
     * @param filePath 文件保存路径
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    public static boolean bytesToFileAndSave(byte[] bytes, String filePath, String fileName) throws IOException {
        boolean result = false;
        // 将字符串转换成二进制，用于显示图片
        // 将上面生成的图片格式字符串 imgStr，还原成图片显示
        // 可以是任何图片格式.jpg,.png等
        if (bytes.length > 0) {
            try (InputStream in = new ByteArrayInputStream(bytes);
                 FileOutputStream fos = new FileOutputStream(new File(filePath, fileName))) {
                byte[] b = new byte[1024];
                int nRead;
                while ((nRead = in.read(b)) != -1) {
                    fos.write(b, 0, nRead);
                }
                fos.flush();
                result = true;
            }
        }
        return result;
    }

    /**
     * 文件(图片/视频等)转成byte数组
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    public static byte[] fileToBytes(String filePath) throws IOException {
        byte[] data;
        try (FileImageInputStream input = new FileImageInputStream(new File(filePath));
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buf = new byte[1024];
            int numBytesRead;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
        }
        return data;
    }
}
