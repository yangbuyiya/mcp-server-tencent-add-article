package com.yby6.mcp.server.tencent.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;

/**
 * 文章功能反应
 *
 * @author Yang Shuai
 * Create By 2025/05/05
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleFunctionResponse {

    /**
     * 文章ID
     * 发布成功后返回的文章唯一标识
     */
    @JsonProperty(required = true, value = "articleId")
    @JsonPropertyDescription("articleId")
    private Long articleId;
    
    /**
     * 发布状态
     * 0: 成功
     * 非0: 失败
     */
    @JsonProperty(required = true, value = "status")
    @JsonPropertyDescription("status")
    private Integer status;
    
    
    /**
     * 文章链接
     */
    @JsonProperty(required = true, value = "url")
    @JsonPropertyDescription("url")
    private String url;
    
}
