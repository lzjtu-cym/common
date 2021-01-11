package com.cym.springboot.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cym.springboot.common.utils.ResponseUtil;
import com.cym.springboot.entity.UserInfo;
import com.cym.springboot.service.IUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Arrays;

/**
 * 用户表(UserInfo)表控制层
 *
 * @author chenyumin
 * @since 2021-01-11 21:22:38
 */
@RestController
@Api(description = "用户表 - 控制层")
@RequestMapping("userInfo")
public class UserInfoController {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);
    /**
     * 服务对象
     */
    @Autowired
    private IUserInfoService userInfoService;

    @ApiOperation(value = "用户表 - 通过id查询", notes = "用户查询")
    @GetMapping("get-by-id")
    public Object getById(@RequestParam("id") String id) {
        try {
            return ResponseUtil.successObjectResponse(userInfoService.getById(id));
        } catch (Exception e) {
            logger.error("查询失败", e);
            return ResponseUtil.faliedResponse("查询失败！", e.getMessage());
        }
    }

    @ApiOperation(value = "用户表 - 条件查询", notes = "用户ids查询")
    @GetMapping("gets")
    public Object getListData(@ModelAttribute UserInfo userInfo) {
        try {
            return ResponseUtil.successObjectResponse(userInfoService.list(new QueryWrapper<UserInfo>()));
        } catch (Exception e) {
            logger.error("查询失败", e);
            return ResponseUtil.faliedResponse("查询失败！", e.getMessage());
        }
    }

    @ApiOperation(value = "用户表 - 新增", notes = "用户新增")
    @PostMapping("posts")
    public Object insert(@ModelAttribute UserInfo userInfo) {
        try {
            userInfoService.save(userInfo);
            return ResponseUtil.successResponse("新增成功！");
        } catch (Exception e) {
            logger.error("新增失败", e);
            return ResponseUtil.faliedResponse("新增失败！", e.getMessage());
        }
    }

    @ApiOperation(value = "用户表 - 修改", notes = "用户修改")
    @PostMapping("edit")
    public Object update(@ModelAttribute UserInfo userInfo) {
        try {
           userInfoService.updateById(userInfo);
            return ResponseUtil.successResponse("新增成功！");
        } catch (Exception e) {
            logger.error("修改失败", e);
            return ResponseUtil.faliedResponse("修改失败！", e.getMessage());
        }
    }
    
    @ApiOperation(value = " - 删除", notes = "用户id删除")
    @DeleteMapping("deletes")
    public Object delete(@RequestParam("id") String id) {
        try {
            userInfoService.removeById(id);
            return ResponseUtil.successResponse("删除成功！");
        } catch (Exception e) {
            logger.error("删除失败", e);
            return ResponseUtil.faliedResponse("删除失败！", e.getMessage());
        }
    }

    @ApiOperation(value = "用户表 - 删除", notes = "用户ids删除，多个逗号隔开")
    @DeleteMapping("deletes-by-id")
    public Object deletesById(@RequestParam("ids") String ids) {
        try {
           userInfoService.removeByIds(Arrays.asList(ids.split(",")));
            return ResponseUtil.successResponse("新增成功！");
        } catch (Exception e) {
            logger.error("删除失败", e);
            return ResponseUtil.faliedResponse("删除失败！", e.getMessage());
        }
    }
}