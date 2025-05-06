package com.yby6.mcp.server.tencent.domain.adapter;

import com.yby6.mcp.server.tencent.domain.model.ArticleFunctionRequest;
import com.yby6.mcp.server.tencent.domain.model.ArticleFunctionResponse;

import java.io.IOException;

/**
 * 腾讯云开发者社区端口适配器接口
 * 
 * 该接口定义了与腾讯云开发者社区交互的端口适配器。
 * 作为领域驱动设计(DDD)中的端口，它隔离了领域层和基础设施层，
 * 使得领域层不依赖于具体的外部服务实现。
 * 
 * 主要功能：
 * 1. 提供文章发布等核心业务操作的抽象
 * 2. 定义领域层与基础设施层的交互契约
 * 3. 支持依赖倒置原则的实现
 * 
 * @author yby6
 * @version 1.0.0
 */
public interface ITencentPort {

    /**
     * 发布文章到腾讯云开发者社区
     * 
     * 该方法定义了文章发布的核心业务操作。
     * 作为端口适配器的一部分，它：
     * 1. 接收领域模型中的请求对象
     * 2. 通过基础设施层实现具体的发布操作
     * 3. 返回领域模型中的响应对象
     * 
     * @param request 文章发布请求，包含文章标题、内容等信息
     * @return 文章发布响应，包含发布结果和文章ID等信息
     * @throws IOException 当发布过程中发生IO异常时抛出
     */
    ArticleFunctionResponse writeArticle(ArticleFunctionRequest request) throws IOException;

}
