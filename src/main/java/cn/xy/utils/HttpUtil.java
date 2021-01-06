package cn.xy.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.*;
import java.util.Map;

/**
 * http工具类
 *
 * @author xy
 */
public final class HttpUtil {
    private HttpUtil() {

    }

    /**
     * 发送GET请求, 使用的是jdk原生的HttpURLConnection
     * @param url 请求地址
     * @param params 参数列表
     * @return
     * @throws Exception
     */
    public static String sendGetRequest(String url, Map<String, Object> params) {
        try {
            //参数转为 key1=value1&key2=value2 的格式
            String paramsStr = buildParamsStr(params);
            //拼接url
            url = "".equals(paramsStr.trim()) ? url : url.concat("?").concat(paramsStr);
            System.out.println("get request url:" + url);

            //获取HttpURLConnection
            HttpURLConnection connection = getHttpURLConnection(url, "GET", false);
            //获取响应结果
            String response = getResponse(connection.getInputStream(), "UTF-8");
            //关闭连接
            connection.disconnect();
            System.out.println("send get request success, response:" + response);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("send get request error, error message:" + e.toString());
            return e.toString();
        }
    }

    /**
     * 发送POST请求, 使用的是jdk原生的HttpURLConnection
     * @param url 请求地址
     * @param params 参数列表
     * @param sendJson 是否发送json格式参数
     * @return
     */
    public static String sendPostRequest(String url, Map<String, Object> params, boolean sendJson) {
        try {
            //获取HttpURLConnection
            HttpURLConnection connection = getHttpURLConnection(url, "POST", sendJson);

            //输出参数
            PrintWriter pw = new PrintWriter(new BufferedOutputStream(connection.getOutputStream()));
            String paramsStr;
            if (sendJson) {
                //map转为json字符串
                //TODO 这里使用了jackson包把map转为json字符串
                ObjectMapper om = new ObjectMapper();
                paramsStr = om.writeValueAsString(params);
            } else {
                //参数转为 key1=value1&key2=value2 的格式
                paramsStr = buildParamsStr(params);
            }
            System.out.println("post request params: " + paramsStr);
            pw.write(paramsStr);
            pw.flush();
            pw.close();

            //获取响应结果
            String response = getResponse(connection.getInputStream(), "UTF-8");
            //关闭连接
            connection.disconnect();
            System.out.println("send post request success, response:" + response);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("send post request error, error message:" + e.toString());
            return e.toString();
        }
    }

    /**
     * 发送tcp请求
     * @param bytes 二进制流
     * @param receiveIp 接收方ip
     * @param receivePort 接收方port
     * @throws IOException
     */
    public static void sendTCPRequest(byte[] bytes, String receiveIp, int receivePort) throws IOException {
        Socket socket = new Socket(receiveIp, receivePort);
        OutputStream os = socket.getOutputStream();
        os.write(bytes);
        os.close();
    }

    /**
     * 发送udp请求
     * @param bytes 二进制流
     * @param sendPort 发送方port
     * @param receiveIp 接受方ip
     * @param receivePort 接收方port
     * @throws IOException
     */
    public static void sendUDPRequest(byte[] bytes, int sendPort, String receiveIp, int receivePort) throws IOException {
        DatagramSocket s = new DatagramSocket(null);
        s.setReuseAddress(true);
        s.bind(new InetSocketAddress(sendPort));
        DatagramPacket p = new DatagramPacket(bytes, 0, bytes.length, new InetSocketAddress(receiveIp, receivePort));
        s.send(p);
        s.close();
    }

    /**
     * 利用Socket检测TCP端口是否被占用
     * 注意: 只能检测TCP的端口, UDP的端口检测无效 - 怎么都会返回false
     * @param host 检测本地则为127.0.0.1
     * @param port
     * @return true: 端口被占用 false: 端口未被占用
     */
    public static boolean checkTCPPort(String host, int port) {
        boolean result;
        try {
            //利用socket连接端口, 看是否能连接成功
            Socket tcp = new Socket(InetAddress.getByName(host), port);
            //能走到这一步表示连接成功, 则表示端口被占用了
            result = true;
            //一定要关闭, 否则端口会因为这个方法被占用
            tcp.close();
        } catch (UnknownHostException e) {
            throw new RuntimeException("UnknownHostException!");
        } catch (IOException e) {
            //走到这里, 是因为Connection Refused, 表示端口没被占用
            result = false;
        }
        return result;
    }

    /**
     * 利用DatagramSocket检测UDP端口是否被占用
     * 注意: 只能检测UDP的端口, TCP的端口检测无效 - 怎么都会返回false
     * @param host 检测本地则为127.0.0.1
     * @param port
     * @return true: 端口被占用 false: 端口未被占用
     */
    public static boolean checkUDPPort(String host, int port) {
        boolean result;
        try {
            DatagramSocket udp = new DatagramSocket(port, InetAddress.getByName(host));
            //走到这里, 表示端口未被占用
            result = false;
            //一定要关闭, 否则端口会因为这个方法被占用
            udp.close();
        } catch (UnknownHostException e) {
            throw new RuntimeException("UnknownHostException!");
        } catch (SocketException e) {
            //走到这里, 是因为Address already in use (Bind failed), 表示端口已被占用
            result = true;
        }
        return result;
    }

    /**
     * 检测端口占用情况(TCP & UDP全检测)
     * @param host
     * @param port
     * @return true: 被占用 false: 未被占用
     */
    public static boolean checkPort(String host, int port) {
        boolean tcpCheck = checkTCPPort(host, port);
        boolean udpCheck = checkUDPPort(host, port);
        if ((tcpCheck && !udpCheck) || (!tcpCheck && udpCheck)) {
            return true;
        }
        return false;
    }


    /**
     * 生成HttpURLConnection
     * @param url 请求地址
     * @param requestMethod 请求类型: GET / POST
     * @param sendJson 是否发送json数据, post请求会使用
     * @return
     * @throws Exception
     */
    private static HttpURLConnection getHttpURLConnection(String url, String requestMethod, boolean sendJson) throws Exception {
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();

        //设置HttpURLConnection属性
        //请求方式
        connection.setRequestMethod(requestMethod);
        /*
         * 设置为true, 则可以使用connection.getOutputStream().write()
         * GET请求用不到这个, 因为参数直接追加在url地址后面, 所以默认为false
         * POST请求的参数是放在http的body中, 因此需要在建立连接后, 往服务端写数据, 所以默认为true
         */
        connection.setDoOutput(requestMethod.equalsIgnoreCase("GET") ? false : true);
        /*
         * 设置为true, 则可以使用connection.getInputStream().read()
         * GET / POST请求均要获取服务端的响应, 所以默认为true
         */
        connection.setDoInput(true);
        //发送json格式的数据和发送form表单的数据差别就在于Content-Type属性值
        String contentType = sendJson ? "application/json; charset=UTF-8" : "application/x-www-form-urlencoded; charset=UTF-8";
        connection.setRequestProperty("Content-Type", contentType);
        return connection;
    }

    /**
     * 解析请求的响应
     * @param in connection.getInputStream()
     * @param encode 编码, 默认UTF-8
     * @return
     * @throws Exception
     */
    private static String getResponse(InputStream in, String encode) throws Exception {
        //默认编码为UTF-8
        encode = encode == null || "".equals(encode.trim()) ? "UTF-8" : encode;
        StringBuilder sb = new StringBuilder();
        //读取响应
        BufferedReader br = new BufferedReader(new InputStreamReader(in, encode));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        //关闭流
        br.close();
        return sb.toString();
    }

    /**
     * 构建参数列表, GET / POST均适用
     * 结构: key1=value1&key2=value2
     * @param params
     * @return
     */
    private static String buildParamsStr(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
                if (i++ != params.size() - 1) {
                    sb.append("&");
                }
            }
        }
        return sb.toString();
    }
}