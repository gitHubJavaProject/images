package com.img.images.model;

import lombok.Data;

@Data
public class User extends BaseModel {
    /**
     * 启用
     */
    public static final int STATUS_START = 1;
    /**
     * 禁用
     */
    public static final int STATUS_STOP = 2;
    /**
     * 名字
     */
    private String name;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String pwd;
    /**
     * 角色
     */
    private String roleName;
    /**
     * 状态
     */
    private Integer status;
    /**
     *头图
     */
    private String header;
}
