package com.cym.springboot.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.cym.springboot.common.utils.RedisUtils;
import com.cym.springboot.service.ISmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Description: 发送短信接口实现类
 * @Date: 2021/2/1 11:18
 * @Author: cym
 * @Version: 1.0
 */
@Service
public class SmsServiceImpl implements ISmsService {
    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    /**
     * 阿里云获取的访问API密钥
     */
    @Value(value = "${spring.sms.accessKeyId}")
    private String accessKeyId;
    @Value(value = "${spring.sms.accessKeySecret}")
    private String accessKeySecret;

    /**
     * 短信签名,即最前面在【】这个中的文字,可以打开手机随便看一条商业短信,例：【淘宝】亲爱的用户.....
     */
    @Value(value = "${spring.sms.signName}")
    private String signName;

    /**
     * 短信模板,即在阿里云短信服务中自己创建的短信模板ID
     */
    @Value(value = "${spring.sms.templateCode}")
    private String templateCode;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void sendSms(String phones) throws Exception {
        logger.info("********************发送短信开始*****************");
        // 验证手机号码
        checkPhone(phones);
        // 初始化acsClient,需要填写自己的accessKeyId和accessSecret
        // regionId只有一个杭州,不支持其他地区
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        // 组装请求对象,这几项为默认的设置,不必修改
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        // 阿里云服务器域名
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");

        // 短信接收者手机号
        request.putQueryParameter("PhoneNumbers", phones);
        // 短信签名
        request.putQueryParameter("SignName", signName);
        // 模板ID
        request.putQueryParameter("TemplateCode", templateCode);

        // 构建短信模板参数替换,要求格式为json
        // 如果你的短信模板为：验证码为：${code},那么你的json为：{"code": "2345"}
        Map<String, String> jsonParam = new HashMap<>();
        jsonParam.put("code", makeCode());

        //此处我用的json格式化的依赖是fastjson,SpringBoot自带的是jackson
        request.putQueryParameter("TemplateParam", JSON.toJSONString(jsonParam));
        // 发送请求并获取响应,判断是否成功
        CommonResponse response = client.getCommonResponse(request);
        if (response.getHttpStatus() == 200) {
            // 将验证码缓存到redis中，2分钟过后自动清除该缓存
            redisUtils.setString(phones, 2L);
            logger.info("********************发送短信结束*****************");
        }
    }

    /**
     * 校验手机号码
     *
     * @param phones
     * @throws Exception
     */
    private void checkPhone(String phones) throws Exception {
        if (StringUtils.isEmpty(phones)) {
            throw new Exception("**********手机号不能为空!***********");
        }
        String[] phoneArr = phones.split(",");
        for (String phone : phoneArr) {
            if (!phone.matches("^1[3|4|5|7|8][0-9]{9}$")) {
                throw new Exception("**********手机号码格式不正确：" + phone);
            }
        }
        //判断用户输入的电话号码是否频繁发送
        if (isSendOfen(phones)) {
            logger.info("发送短信频繁，请稍后再试");
            throw new Exception("**********发送短信频繁，请稍后再试***********");
        }
    }

    /**
     * 判断验证功发送时候频繁
     *
     * @param phone
     * @return
     */
    private boolean isSendOfen(String phone) {
        if (ObjectUtils.isEmpty(redisUtils.getString(phone))) {
            return false;
        } else {
            //判断上一次记录的时间和当前时间进行对比，如果两次相隔时间小于120s，视为短信发送频繁
            Long time = (Long) redisUtils.getString(phone);
            //两次发送短信中间至少有2分钟的间隔时间
            if (time + 120 * 1000 >= System.currentTimeMillis()) {
                return true;
            }
            return false;
        }
    }

    /**
     * 验证短信
     *
     * @param phone
     * @param code
     * @return
     */
    public boolean validSmsCode(String phone, String code) {
        //取出所有有关该手机号的短信验证码
        if (ObjectUtils.isEmpty(redisUtils.getString(phone))) {
            logger.info("*************短信验证失败*************");
            return false;
        }
        String oldCode = (String) redisUtils.getString(phone);
        if (oldCode.equals(code)) {
            logger.info("*************短信验证成功*************");
            //删除掉该redis
            redisUtils.remove(phone);
            return true;
        }
        return false;
    }

    /**
     * 随机生成6位数的短信码
     *
     * @return
     */
    private String makeCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int next = random.nextInt(10);
            code.append(next);
        }
        return code.toString();
    }
}
