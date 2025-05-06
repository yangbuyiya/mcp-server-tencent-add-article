package com.yby6.mcp.server.tencent.types.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 腾讯云API配置属性类
 * 
 * 该配置类用于管理腾讯云开发者社区API的配置属性。
 * 使用Spring Boot的配置属性机制，从配置文件中读取相关配置。
 * 配置前缀为"tencent.api"。
 * 
 * 主要功能：
 * 1. 管理API认证信息（Cookie）
 * 2. 管理文章分类信息
 * 3. 提供配置属性的访问方法
 * 
 * @author yby6
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "tencent.api")
@Component
public class TencentApiProperties {

    /**
     * 认证Cookie
     * 
     * 用于腾讯云开发者社区API的身份验证。
     * 在配置文件中通过tencent.api.cookie属性设置。
     * 该值在应用启动时会被验证，不能为空。
     */
    private String cookie;

    /**
     * 文章分类信息
     * 
     * 用于管理文章的分类配置。
     * 在配置文件中通过tencent.api.categories属性设置。
     * 可以包含多个分类的配置信息。
     */
    private String categories;

    /**
     * 获取认证Cookie
     * 
     * @return 认证Cookie字符串
     */
    public String getCookie() {
        return cookie;
    }

    /**
     * 设置认证Cookie
     * 
     * @param cookie 认证Cookie字符串
     */
    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    /**
     * 获取文章分类信息
     * 
     * @return 分类信息字符串
     */
    public String getCategories() {
        return categories;
    }

    /**
     * 设置文章分类信息
     * 
     * @param categories 分类信息字符串
     */
    public void setCategories(String categories) {
        this.categories = categories;
    }
}
