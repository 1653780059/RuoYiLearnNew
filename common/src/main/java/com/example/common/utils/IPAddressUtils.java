package com.example.common.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * @author 16537
 * @Classname IPAddressUtils
 * @Description 获取地址工具类
 * @Version 1.0.0
 * @Date 2022/9/30 9:47
 */
public class IPAddressUtils {

    /**
     * 根据IP地址获取地理位置
     */
    public static String getAddressByIP(String ip) {
        if (StringUtils.isBlank(ip)) {
            return "";
        }
        if ("127.0.0.1".equals(ip)) {
            return "本地";
        }
        String url = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?resource_id=6006&format=json&query=" + ip;
        HttpResponse res = HttpRequest.get(url).execute();
        if (200 != res.getStatus()) {
            return "获取位置失败";
        } else {
            JSONObject resJson = JSONObject.parseObject(res.body());
            JSONArray resArr = JSONArray.parseArray(resJson.getString("data"));
            resJson = JSONObject.parseObject("" + resArr.get(0));
            return resJson.getString("location");
        }
    }


}
