package com.yby6.mcp.server.tencent.domain.adapter;


import com.yby6.mcp.server.tencent.domain.model.ArticleFunctionRequest;
import com.yby6.mcp.server.tencent.domain.model.ArticleFunctionResponse;

import java.io.IOException;

public interface ITencentPort {

    /**
     * 发布文章到Tencent
     *
     * @param request 发文请求
     * @return 发文响应
     */
    ArticleFunctionResponse writeArticle(ArticleFunctionRequest request) throws IOException;

}
