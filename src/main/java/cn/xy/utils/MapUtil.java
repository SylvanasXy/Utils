package cn.xy.utils;

import java.util.*;

/**
 * Map操作类
 *
 * @author xy
 */
public final class MapUtil {
    private MapUtil() {

    }

    /**
     * 给Map中的key/value进行排序
     * 前提条件: key&value必须继承了Comparable接口, 并实现了compareTo()
     * @param map 待排序的Map
     * @param sortType asc: 升序(默认) desc: 降序
     * @param sortCol key: 给键进行排序 value: 给值进行排序(默认)
     * @return
     * @throws IllegalArgumentException
     */
    public static <K extends Comparable, V extends Comparable> Map<K, V> sortMap(Map<K, V> map, String sortType, String sortCol) {
        //默认为升序排序
        sortType = sortType == null || "".equals(sortType.trim()) ? "asc" : sortType.toLowerCase().trim();
        //默认为值进行排序
        sortCol = sortCol == null || "".equals(sortCol.trim()) ? "value" : sortCol.toLowerCase().trim();

        if (!"asc".equals(sortType) || !"desc".equals(sortType)) {
            throw new IllegalArgumentException("sortType is illegal!");
        }
        if (!"key".equals(sortCol) || !"value".equals(sortCol)) {
            throw new IllegalArgumentException("sortCol is illegal!");
        }

        //为lambda服务的变量
        String type = sortType;
        String col = sortCol;

        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, (o1, o2) -> {
            if (type.equals("asc")) {
                //升序
                if (col.equals("value")) {
                    return (o1.getValue()).compareTo(o2.getValue());
                } else {
                    return (o1.getKey()).compareTo(o2.getKey());
                }
            } else {
                //降序
                if (col.equals("value")) {
                    return (o2.getValue()).compareTo(o1.getValue());
                } else {
                    return (o2.getKey()).compareTo(o1.getKey());
                }
            }
        });

        //LinkedHashMap特性和LinkedList相似, 链表结构, 有序
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}