package com.yby6.mcp.server.tencent.domain.service;


import com.alibaba.fastjson.JSON;
import com.yby6.mcp.server.tencent.domain.adapter.ITencentPort;
import com.yby6.mcp.server.tencent.domain.model.ArticleFunctionRequest;
import com.yby6.mcp.server.tencent.domain.model.ArticleFunctionResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class TencentArticleService {
    
    @Resource
    private ITencentPort port;
    
    /**
     * 发布文章到腾讯云开发者社区
     *
     * @param request 发文请求
     * @return 发文响应
     */
    @Tool(description = "发布文章到腾讯云开发者社区")
    public ArticleFunctionResponse saveArticle(ArticleFunctionRequest request) throws IOException {
        try {
            log.info("腾讯云开发者社区发帖参数：{}", JSON.toJSONString(request));
            return port.writeArticle(request);
        } catch (Exception e) {
            log.error("腾讯云开发者社区发帖失败 ", e);
        }
        return null;
    }
    
    
}
