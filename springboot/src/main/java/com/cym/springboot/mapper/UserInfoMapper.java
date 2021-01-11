package com.cym.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cym.springboot.entity.UserInfo;
import org.springframework.stereotype.Repository;

/**
 * 用户表(UserInfo)表数据库访问层
 *
 * @author chenyumin
 * @since 2021-01-11 21:22:36
 */
@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}