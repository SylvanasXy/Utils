package cn.xy.utils;

import java.io.*;

/**
 * 对象序列化工具类, 对象必须实现Serializable接口才行
 *
 * @author xy
 */
public final class SerializeUtil {
    private SerializeUtil() {

    }

    /**
     * 对象序列化成二进制字节流
     * @param obj
     * @return
     */
    public static byte[] serialize(Object obj) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字节流反序列化成对象
     * @param bytes
     * @return
     */
    public static Object unSerialize(byte[] bytes) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
