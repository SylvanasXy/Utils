package cn.xy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 执行linux命令时, 可用的工具类
 * 该工具类提供一个模板, 具体可根据实现修改
 * 参考链接: https://blog.csdn.net/eguid_1/article/details/51777716
 *
 * @author xy
 */
public final class ProcessUtilTemplate {
    private ProcessUtilTemplate() {

    }

    /**
     * 保存process信息, 以便后面销毁(终止)process以及监控线程
     */
    private static Map<String, Object> processMap = new HashMap<>();

    /**
     * 执行linux命令
     *
     * @param command 命令
     * @throws IOException
     */
    public static Process executeCommand(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        ProcessInfo info = new ProcessInfo(process.getInputStream(), "info");
        ProcessInfo error = new ProcessInfo(process.getErrorStream(), "error");
        processMap.put("process", process);
        processMap.put("processInfo", info);
        processMap.put("processError", error);
        info.start();
        error.start();
        return process;
    }

    /**
     * 销毁(终止)process及监控线程
     */
    public static void destoryProcess() {
        ((ProcessInfo) processMap.get("processInfo")).destroy();
        ((ProcessInfo) processMap.get("processError")).destroy();
        ((Process) processMap.get("process")).destroy();
    }

    /**
     * 该类为process执行时, 监控输出的error/info信息
     * 根据需要可以把该类单独提出来
     */
    static class ProcessInfo extends Thread {
        /**
         * 控制线程的结束
         */
        private boolean flag;

        /**
         * 读取process中的error/info信息
         */
        private BufferedReader br;

        /**
         * 类型: error & info
         */
        private String type;

        /**
         * 重写destroy(), 控制线程的结束
         */
        @Override
        @SuppressWarnings("deprecation")
        public void destroy() {
            flag = false;
        }

        public ProcessInfo(InputStream in, String type) {
            this.type = type;
            //error/info信息
            br = new BufferedReader(new InputStreamReader(in));
        }

        @Override
        public void run() {
            String msg;
            try {
                while (flag) {
                    if ((msg = br.readLine()) != null) {
                        if ("error".equals(type)) {
                            //使用时需要把这换成LOG.error();
                            System.out.println("process error:" + msg);
                        } else {
                            //使用时需要把这换成LOG.info();
                            System.out.println("process info:" + msg);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
