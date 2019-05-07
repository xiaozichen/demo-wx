package com.demo.wx.controller;

import com.demo.wx.service.WxTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WxTokenController {

    private final WxTokenService wxTokenService;

    @Autowired
    public WxTokenController(WxTokenService wxTokenService) {
        this.wxTokenService = wxTokenService;
    }

    @GetMapping("/wxToken")
    @ResponseBody
    public String  getAccentToken(HttpServletRequest request){
        /*signature	微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
        timestamp	时间戳
        nonce	随机数
        echostr	随机字符串*/
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        String  token = "cs";
        if(wxTokenService.check( token, timestamp, nonce,signature)){
            System.err.println("接入成功");
            return echostr;
        }else {
            System.err.println("接入失败");
        }
        return  null ;

    }
}
