package com.cym.springboot.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
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
     * 短信api的请求地址  固定
     */
    @Value("${spring.sms.domain}")
    private String domain;
    @Value("${spring.sms.regionId}")
    private String regionId;
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
        // 指定地域名称 短信API的就是 cn-hangzhou 不能改变  后边填写您的  accessKey 和 accessKey Secret
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        // 创建通用的请求对象
        CommonRequest request = new CommonRequest();
        // 指定请求方式
        request.setSysMethod(MethodType.POST);
        // 短信api的请求地址  固定
        request.setSysDomain(domain);
        //签名算法版本  固定
        request.setSysVersion("2017-05-25");
        //请求 API 的名称
        request.setSysAction("SendSms");
        //指定地域名称
        request.putQueryParameter("RegionId", regionId);
        // 要给哪个手机号发送短信  指定手机号
        request.putQueryParameter("PhoneNumbers", phones);
        // 您的申请签名
        request.putQueryParameter("SignName", signName);
        // 您申请的模板 code
        request.putQueryParameter("TemplateCode", templateCode);

        Map<String, Object> params = new HashMap<>();
        //这里的key就是短信模板中的 ${xxxx}
        String code = makeCode();
        params.put("code", code);

        // 放入参数  需要把 map转换为json格式  使用fastJson进行转换
        request.putQueryParameter("TemplateParam", JSON.toJSONString(params));

        try {
            CommonResponse response = client.getCommonResponse(request);
            if (response.getHttpStatus() == 200) {
                redisUtils.setString(phones, code + ":" + 2L);
            }
            logger.info(JSON.parseObject(response.getData(), Map.class).get("Message").toString());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
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
            String codeTime = (String) redisUtils.getString(phone);
            String[] strings = codeTime.split(":");
            //两次发送短信中间至少有2分钟的间隔时间
            if (Long.valueOf(strings[1]) + 120 * 1000 >= System.currentTimeMillis()) {
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
    @Override
    public boolean validSmsCode(String phone, String code) {
        //取出所有有关该手机号的短信验证码
        if (ObjectUtils.isEmpty(redisUtils.getString(phone))) {
            logger.info("*************短信验证失败*************");
            return false;
        }
        String codeTime = (String) redisUtils.getString(phone);
        String[] strings = codeTime.split(":");
        if (Long.valueOf(strings[0]).equals(code)) {
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
