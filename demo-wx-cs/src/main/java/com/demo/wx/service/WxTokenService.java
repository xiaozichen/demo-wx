package com.demo.wx.service;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Service
public class WxTokenService {

    public  boolean check(String token, String timestamp, String nonce, String signature) {
        String[] strs = new String[]{token, timestamp, nonce};
        Arrays.sort(strs);
        String sh1 = sha1(strs[0] + strs[1] + strs[2]);

        return sh1.equalsIgnoreCase(signature);
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
