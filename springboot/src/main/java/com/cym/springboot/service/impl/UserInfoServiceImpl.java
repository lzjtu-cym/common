package com.cym.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cym.springboot.mapper.UserInfoMapper;
import com.cym.springboot.entity.UserInfo;
import com.cym.springboot.service.IUserInfoService;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户表(UserInfo)表服务实现类
 *
 * @author chenyumin
 * @since 2021-01-11 21:22:37
 */
@Service
@Transactional(rollbackFor = Exception.class) 
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {
    private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
}