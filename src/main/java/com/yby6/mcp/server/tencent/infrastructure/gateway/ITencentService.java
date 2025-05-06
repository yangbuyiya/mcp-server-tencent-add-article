package com.yby6.mcp.server.tencent.infrastructure.gateway;

import com.yby6.mcp.server.tencent.infrastructure.gateway.dto.AddArticleRequest;
import com.yby6.mcp.server.tencent.infrastructure.gateway.dto.AddArticleResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface ITencentService {
    
    
    /**
     * 发布文章到腾讯云开发者社区
     *
     * @param cookie   Cookie
     * @param request  发文请求
     * @return 发文响应
     */
    @POST("developer/api/article/addArticle")
    @Headers({
        "accept: application/json, text/plain, */*",
        "accept-language: zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7",
        "content-type: application/json",
        "origin: https://cloud.tencent.com",
        "priority: u=1, i",
        "referer: https://cloud.tencent.com/developer/article/write-new?draftId=213590",
        "sec-ch-ua: \"Google Chrome\";v=\"135\", \"Not-A.Brand\";v=\"8\", \"Chromium\";v=\"135\"",
        "sec-ch-ua-mobile: ?0",
        "sec-ch-ua-platform: \"Windows\"",
        "sec-fetch-dest: empty",
        "sec-fetch-mode: cors",
        "sec-fetch-site: same-origin",
        "sec-gpc: 1",
        "user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36"
    })
    Call<AddArticleResponse> addArticle(
        @Header("Cookie") String cookie,
        @Body AddArticleRequest request
    );
}
