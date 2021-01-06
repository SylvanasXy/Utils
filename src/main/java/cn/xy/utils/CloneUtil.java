package cn.xy.utils;

import java.io.*;

/**
 * 对象克隆工具类
 * @author xy
 */
public final class CloneUtil {
    private CloneUtil() {

    }

    /**
     * 利用序列化, 实现对象的深度克隆
     * 需要对象实现Serializable接口才能使用
     * 深度拷贝和浅拷贝参考例子(Map的深浅拷贝): https://www.cnblogs.com/leskang/p/7169233.html
     * @param obj
     * @param <T>
     * @return
     */
    public static <T extends Serializable> T clone(T obj) {
        T cloneObj = null;
        byte[] outArray = null;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ObjectOutputStream obs = new ObjectOutputStream(out)) {
            // 写入字节流
            obs.writeObject(obj);
            obs.flush();
            outArray = out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (ByteArrayInputStream ios = new ByteArrayInputStream(outArray);
             ObjectInputStream ois = new ObjectInputStream(ios)) {
            // 分配内存，写入原始对象，生成新对象
            // 返回生成的新对象
            cloneObj = (T) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cloneObj;
    }
}
