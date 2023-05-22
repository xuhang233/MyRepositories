package com.itheima.reggie.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;


/**
 * 短信发送工具类
 */
public class SendSms {

    public static void Send(String phone, String code) {

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI5tQZLrcenXTcnW6QpF91", "otVQLaXO7pv3HDOI5XtrVe9WbsXsVV");

        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setSignName("阿里云短信测试");//短信签名
        request.setTemplateCode("SMS_154950909");//短信模板
        request.setPhoneNumbers(phone);//手机号
        request.setTemplateParam("{\"code\":\"" + code + "\"}");

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }
    }
}