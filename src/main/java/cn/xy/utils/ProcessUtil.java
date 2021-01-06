package cn.xy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 实用的执行linux命令的工具类
 * 注意: 所用的命令都是linux命令, windows环境下无法使用
 *
 * @author xy
 */
public final class ProcessUtil {
    private ProcessUtil() {

    }

    /**
     * 通过端口号 + 进程关键字 杀死进程
     * 需要linux环境支持lsof命令, 如不支持该命令, 可用yum install -y lsof安装该命令
     * @param port 端口
     * @param force 是否强制杀死进程
     * @param processName
     * @throws IOException
     */
    public static void killProcessWithPort(int port, boolean force, String processName) throws IOException {
        Process process = Runtime.getRuntime().exec("lsof -i:" + port);
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String msg;
        String[] array;
        int pid;
        while ((msg = br.readLine()) != null) {
            //清除多余的空格
            msg = msg.replaceAll("\\s{2,}", " ");
            System.out.println("lsof command info:" + msg);
            if (msg.indexOf(processName) != -1) {
                array = msg.split(" ");
                pid = Integer.parseInt(array[1]);
                killProcessWithPID(pid, force);
            }
        }
    }

    /**
     * 通过pid杀死进程
     * @param pid
     * @param force 是否强制杀死进程
     * @throws IOException
     */
    public static void killProcessWithPID(int pid, boolean force) throws IOException {
        StringBuilder sb = new StringBuilder("kill ");
        //是否强制杀死进程
        if (force) {
            sb.append("-9 ");
        }
        sb.append(pid);
        Process process = Runtime.getRuntime().exec(sb.toString());
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String msg;
        //看是否有错误信息, 没有则表示杀死进程成功
        while ((msg = br.readLine()) != null) {
            System.out.println("kill command error:" + msg);
        }
        System.out.println("kill pid:" + pid + " process success!");
    }
}
