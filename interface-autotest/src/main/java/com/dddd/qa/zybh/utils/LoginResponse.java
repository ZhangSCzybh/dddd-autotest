package com.dddd.qa.zybh.utils;

import lombok.Data;

/**
 * 登录接口响应实体类
 */
@Data
public class LoginResponse {
    private int code;
    private String msg;
    private UserInfo result;

    @Data
    public static class UserInfo {
        private Integer adminId;
        private String loginName;
        private Integer departmentId;
        private Integer roleId;

    }
} 