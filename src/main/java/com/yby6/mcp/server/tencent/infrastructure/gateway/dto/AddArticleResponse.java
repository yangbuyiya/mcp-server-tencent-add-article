package com.yby6.mcp.server.tencent.infrastructure.gateway.dto;

import lombok.Data;

/**
 * 腾讯云开发者社区发布文章响应DTO
 * 用于封装发布文章后的返回结果
 */
@Data
public class AddArticleResponse {
    /**
     * 文章ID
     * 发布成功后返回的文章唯一标识
     */
    private Long articleId;

    /**
     * 发布状态
     * 0: 成功
     * 非0: 失败
     */
    private Integer status;
}
