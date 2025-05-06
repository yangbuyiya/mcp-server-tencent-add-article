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

/**
 * 腾讯云开发者社区文章服务
 * 
 * 该服务类负责处理与腾讯云开发者社区文章相关的业务逻辑，
 * 包括文章的发布、更新等操作。作为领域服务层的一部分，
 * 它通过端口适配器模式与基础设施层进行交互。
 * 
 * @author yby6
 * @version 1.0.0
 */
@Slf4j
@Service
public class TencentArticleService {
    
    /** 腾讯云端口适配器，用于与基础设施层交互 */
    @Resource
    private ITencentPort port;
    
    /**
     * 发布文章到腾讯云开发者社区
     * 
     * 该方法是一个MCP工具方法，用于将文章发布到腾讯云开发者社区。
     * 主要功能：
     * 1. 接收文章发布请求
     * 2. 记录请求参数日志
     * 3. 通过端口适配器调用实际的文章发布服务
     * 4. 处理异常情况并返回响应
     * 
     * @param request 文章发布请求，包含文章标题、内容等信息
     * @return 文章发布响应，包含发布结果信息
     * @throws IOException 当发布过程中发生IO异常时抛出
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
