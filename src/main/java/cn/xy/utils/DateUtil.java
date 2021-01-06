package cn.xy.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 时间处理类 - 使用的是java8的LocalDateTime, 所以jdk版本必须是jdk8+
 * 这里主要作用是做笔记, 了解LocalDateTime的使用
 * TODO: 还有很多实用的方法这里没有一一写出来, 参考下面的参考链接和TemporalAdjusters.java
 * 参考链接:
 *      https://www.jianshu.com/p/ff9d40a4a356
 *      https://blog.csdn.net/hatsune_miku_/article/details/78620562
 * @author xy
 */
public final class DateUtil {
    private DateUtil() {

    }

    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前时间, 格式为yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String now() {
        return now(DATETIME_FORMAT);
    }

    /**
     * 根据格式化获取当前时间
     *
     * @param format 格式化文本
     * @return
     */
    public static String now(String format) {
        return LocalDateTime.now().format(dateFormatter(format));
    }


    /**
     * 得到指定时间的LocalDateTime对象
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return
     */
    public static LocalDateTime newTime(int year, int month, int day) {
        return LocalDateTime.of(year, month, day, 0, 0, 0);
    }

    /**
     * 得到指定时间的LocalDateTime对象
     *
     * @param year   年
     * @param month  月
     * @param day    日
     * @param hour   时
     * @param minute 分
     * @param second 秒
     * @return
     */
    public static LocalDateTime newTime(int year, int month, int day, int hour, int minute, int second) {
        return LocalDateTime.of(year, month, day, hour, minute, second);
    }

    /**
     * LocalDateTime转成Date对象
     *
     * @param obj
     * @return
     */
    public static Date covertToDate(LocalDateTime obj) {
        return Date.from(obj.atZone(DEFAULT_ZONE_ID).toInstant());
    }

    /**
     * String转成Date对象
     * 本质是先转成LocalDateTime对象, 再由LocalDateTime对象转成Date对象
     *
     * @param time   时间
     * @param format 格式化文本, 需和date参数格式对应, 否则会报错
     * @return
     */
    public static Date covertToDate(String time, String format) {
        return covertToDate(covertToLocalDateTime(time, format));
    }

    /**
     * Date转成LocalDateTime对象
     *
     * @param date
     * @return
     */
    public static LocalDateTime covertToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), DEFAULT_ZONE_ID);
    }

    /**
     * String转成LocalDateTime对象
     *
     * @param time   时间
     * @param format 格式化文本, 需和date参数格式对应, 否则会报错
     * @return
     */
    public static LocalDateTime covertToLocalDateTime(String time, String format) {
        return LocalDateTime.parse(time, dateFormatter(format));
    }

    /**
     * 操作LocalDateTime对象, 增加 / 减少时间 - 上面方法的扩展
     *
     * @param obj
     * @param unit       可以点进ChronoUnit.java中查看, 有更多的操作对象
     * @param countValue 增加 / 减少的时间, 支持负数, 负数即为减少
     * @return
     */
    public static LocalDateTime countDateTime(LocalDateTime obj, ChronoUnit unit, long countValue) {
        return obj.plus(countValue, unit);
    }

    /**
     * 计算时间差 (endTime - startTime)
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param unit      操作对象(year / month / day ....)
     * @return
     */
    public static long countTimeDifference(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit unit) {
        return unit.between(startTime, endTime);
    }

    /**
     * 修改LocalDateTime对象中的指定值
     *
     * @param obj
     * @param field    操作对象: 年(year) / 月(month) / 日(day) / 时(hour) / 分(minute) / 秒(second)
     * @param newValue 新值
     * @return
     */
    public static LocalDateTime modifyDate(LocalDateTime obj, String field, long newValue) {
        field = field.toLowerCase();
        switch (field) {
            case "year":
                modifyDate(obj, ChronoField.YEAR, newValue);
                // 还有这么一种写法, 上面的写法内部实现也是调用该方法, 其他操作对象同理
                // obj.withYear((int) newValue);
                break;
            case "month":
                modifyDate(obj, ChronoField.MONTH_OF_YEAR, newValue);
                break;
            case "day":
                modifyDate(obj, ChronoField.DAY_OF_MONTH, newValue);
            case "hour":
                modifyDate(obj, ChronoField.HOUR_OF_DAY, newValue);
                break;
            case "minute":
                modifyDate(obj, ChronoField.MINUTE_OF_HOUR, newValue);
                break;
            case "second":
                modifyDate(obj, ChronoField.SECOND_OF_MINUTE, newValue);
                break;
            default:
                break;
        }
        return obj;
    }

    /**
     * 修改LocalDateTime对象中的指定值 - 上面方法的扩展
     * <p>
     * 注意:
     * ChronnFiled.java中还有许多操作对象, 且有些存在操作同一对象, 却有多种不同的操作对象;
     * 如操作"日"的话, 则存在DAY_OF_WEEK, DAY_OF_MONTH, DAY_OF_YEAR三种不同的操作对象;
     * 这三种操作对象的区别是: 三者的取值范围不同, 源码里写的比较清楚:
     * - DAY_OF_WEEK: 1 - 7, 在"周"的层次下对值进行修改;
     * - DAY_OF_MONTH: 1 - 28 / 31, 在"月"的层次下对值进行修改;
     * - DAY_OF_YEAR: 1 - 365 / 366, 在"年"的层次下对值进行修改;
     * 这样的话, 由于取值范围的不同 & 层次不同, 所以这三种操作对象所传递的值 & 最终得到的结果均不相同;
     * example:
     * LocalDateTime time = LocalDateTime.of(2018, 8, 13, 0, 0, 0); // 2018-08-13 00:00:00(星期一)
     * time.with(ChronoField.DAY_OF_WEEK, 3);   // 2018-08-15 00:00:00 -> 星期三
     * time.with(ChronoField.DAY_OF_MONTH, 3);  // 2018-08-03 00:00:00 -> 直接改变了日期
     * time.with(ChronoField.DAY_OF_YEAR, 3);   // 2018-01-03 00:00:00 -> 从2018年开始算, 第三天
     *
     * @param obj
     * @param field    可以点进ChronoField.java中查看, 有更多的操作对象
     * @param newValue 新值
     * @return
     */
    public static LocalDateTime modifyDate(LocalDateTime obj, ChronoField field, long newValue) {
        return obj.with(field, newValue);
    }

    /**
     * 比较时间大小
     *
     * @param obj
     * @param needCompareDate 待比较的时间
     * @return 0: 相等 -1: 小于 1: 大于
     */
    public static int compareDate(LocalDateTime obj, LocalDateTime needCompareDate) {
        return obj.compareTo(needCompareDate);
    }

    /**
     * 判断待比较时间是否在前
     *
     * @param obj
     * @param needCompareDate 待比较的时间
     * @return true: 待比较时间在前
     */
    public static boolean isBefore(LocalDateTime obj, LocalDateTime needCompareDate) {
        return obj.isBefore(needCompareDate);
    }

    /**
     * 判断待比较时间是否在后
     *
     * @param obj
     * @param needCompareDate 待比较的时间
     * @return true: 待比较时间在后
     */
    public static boolean isAfter(LocalDateTime obj, LocalDateTime needCompareDate) {
        return obj.isAfter(needCompareDate);
    }

    /**
     * 判断时间是否相等
     *
     * @param obj
     * @param needCompareDate 待比较的时间
     * @return true: 时间相等
     */
    public static boolean isEqual(LocalDateTime obj, LocalDateTime needCompareDate) {
        return obj.isEqual(needCompareDate);
    }

    /**
     * LocalDateTime时间格式化
     *
     * @param format 格式化文本
     * @return
     */
    private static DateTimeFormatter dateFormatter(String format) {
        return DateTimeFormatter.ofPattern(format);
    }

}
