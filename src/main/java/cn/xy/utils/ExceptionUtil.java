package cn.xy.utils;

/**
 * 异常处理工具类
 * @author xy
 */
public final class ExceptionUtil {
    private ExceptionUtil() {

    }

    /**
     * 获取异常的详细信息, 可以日志输出, 也可以存入数据库
     * @param e
     */
    public static void getExceptionDetail(Exception e) {
        //获取异常详细信息 (异常信息, 文件名,方法名,异常出现的行数)
        StackTraceElement exceptionDetail = e.getStackTrace()[0];
        System.out.println("exception msg:" + e.toString()); //异常信息
        System.out.println("class name: " + exceptionDetail.getClassName()); //类名
        System.out.println("file name: " + exceptionDetail.getFileName()); //文件名
        System.out.println("method name: " + exceptionDetail.getMethodName()); //方法名
        System.out.println("exception line: " + exceptionDetail.getLineNumber()); //异常行数
    }
}
