package com.img.images.model;

import lombok.Data;

@Data
public class Image extends BaseModel{
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 展示图片
     */
    private String showImage;
    /**
     * 文件
     */
    private String fileUrl;
    /**
     * 类型
     */
    private Long type;
    /**
     * 类型字符串（按级别拼接用#分隔）（面包屑）
     */
    private String typeStr;
    /**
     * 标签(共享|原创)
     */
    private Integer tag;
    /**
     *关键字（用#分隔）
     */
    private String keys;
    /**
     * 文件大小
     */
    private String size;
    /**
     *宽
     */
    private Integer width;
    /**
     *高
     */
    private Integer height;
    /**
     * 作者（上传人）
     */
    private Long userId;
    /**
     * 下载量
     */
    private Long downloadNumber;
    /**
     * 收藏量
     */
    private Long collectionNumber;

}
