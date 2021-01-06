package cn.xy.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信开发工具类
 * TODO 这里解析JSON使用了jackson包
 * TODO 很久前写的, 不知道api是否有变更
 *
 * @author xy
 */
public final class WeiXinUtil {
    private WeiXinUtil() {

    }

    /**
     * accessToken有效期为7200s, 所以把它存到redis中
     * 这是保存到redis中的key
     * redis value = accessToken,requestTimestamp
     * 根据 (nowTimestamp - requestTimestamp) 验证accessToken是否失效
     * TODO: 这个项目中未集成redis, 所以这里未把accessToken放到redis中
     */
    private static final String ACCESS_TOKEN = "wx_access_token";

    /**
     * 获取accessToken - GET
     * TODO 该方法有2个地方可以修改:
     * 1. 使用redis存储accessToken, 可以判断是否已过期
     * 2. 可以在解析响应结果json时, 判断是否返回的是invalid accessToken
     * 假如是的话, 再次请求方法, 获取最新的accessToken
     * @param appId
     * @param appSecret
     * @return
     */
    public static String getAccessToken(String appId, String appSecret) throws IOException {
        //api地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token";
        //参数列表
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "client_credential");
        params.put("appid", appId);
        params.put("secret", appSecret);

        //响应json
        String responseJson = HttpUtil.sendGetRequest(requestUrl, params);
        ObjectMapper om = new ObjectMapper();
        JsonNode note = om.readTree(responseJson);
        String accessToken = note.get("access_token").asText();
        return accessToken;
    }


    /**
     * 推送模板信息给单个用户 - POST
     * 参考链接: https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277
     * @param accessToken
     * @param templateId 模板信息id
     * @param openId 用户openId
     * @param url 该条消息的链接地址, 可以为空
     * @param paramsName 模板中的占位符名称, 没有的话则为null
     * @param paramsValue 模板中占位符的值, 没有的话则为null, 该集合必须和paramsName集合大小相同(size()相等)
     * @param paramsColor 模板中占位符的颜色, 没有的话则为null, 默认为黑色
     * @throws IOException
     */
    public static void sendTemplateMsgToUser(String accessToken, String templateId, String openId, String url,
                                             List<String> paramsName, List<String> paramsValue, List<String> paramsColor) throws IOException {
        //api地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
        Map<String, Object> params = new HashMap<>();
        //openId
        params.put("touser", openId);
        //模板id
        params.put("template_id", templateId);
        if (url != null && !"".equals(url.trim())) {
            params.put("url", url);
        }

        //模板中占位符数据 & 颜色
        Map<String, Object> data = new HashMap<>();
        boolean paramsNameNotEmpty = paramsName != null && paramsName.size() > 0;
        boolean paramsValueNotEmpty = paramsValue != null && paramsValue.size() > 0;
        if (paramsNameNotEmpty && paramsValueNotEmpty) {
            //验证模板参数列表是否为空
            if (paramsName.size() == paramsValue.size()) {
                Map<String, String> param;
                for (int i = 0, len = paramsName.size(); i < len; i++) {
                    param = new HashMap<>();
                    //填充paramValue
                    param.put("value", paramsValue.get(i));
                    //填充paramColor(该参数选填, 不填则默认为黑色)
                    if (i <= paramsColor.size() - 1) {
                        param.put("color", paramsColor.get(i));
                    }
                    //key = paramName, value = {value: xxx, color: xxx}
                    data.put(paramsName.get(i), param);
                }
            } else {
                throw new RuntimeException("paramsName集合和paramsValue集合大小不相等!");
            }
        }
        params.put("data", data);

        //响应json
        String responseJson = HttpUtil.sendPostRequest(requestUrl, params, true);
        ObjectMapper om = new ObjectMapper();
        JsonNode note = om.readTree(responseJson);
        int resultCode = note.get("errcode").asInt();
        if (resultCode == 0) {
            System.out.println("发送模板信息完成!");
        } else {
            System.out.println("发送模板信息失败, 错误码:" + resultCode);
        }
    }
}


