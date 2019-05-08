package com.demo.wx.service;

import com.alibaba.fastjson.JSONObject;
import com.demo.wx.bean.AccessToken;
import com.demo.wx.constant.WeixinFinalValue;
import com.demo.wx.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Service
public class WxTokenService {

    private static   AccessToken accessToken ;

    @Value("${wx.appid}")
    private  static  String APPID;

    @Value("${wx.appsecret}")
    private static String APPSECRET;



    public  boolean check(String token, String timestamp, String nonce, String signature) {
        String[] strs = new String[]{token, timestamp, nonce};
        Arrays.sort(strs);
        String sh1 = sha1(strs[0] + strs[1] + strs[2]);

        return sh1.equalsIgnoreCase(signature);
    }
    /**
     * 获取token
     * by 罗召勇 Q群193557337
     */
    private static void getToken() {
        String url = WeixinFinalValue.ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
        String tokenStr = Util.get(url);
        JSONObject jsonObject = JSONObject.parseObject(tokenStr) ;
        String token = jsonObject.getString("access_token");
        String expireIn = jsonObject.getString("expires_in");
        //创建token对象,并存起来。
        accessToken = new AccessToken(token, expireIn);
    }

    /**
     * 向处暴露的获取token的方法
     *
     * @return by 罗召勇 Q群193557337
     */
    public static String getAccessToken() {
        if (accessToken == null || accessToken.isExpired()) {
            getToken();
        }
        return accessToken.getAccessToken();
    }

    private static String sha1(String str) {
        try {
            //获取加密对象
            MessageDigest md = MessageDigest.getInstance("sha1");
            //加密
            byte[] digest = md.digest(str.getBytes());
            //加密处理
            char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(chars[(b >> 4) & 15]);
                sb.append(chars[b & 15]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
