package com.yby6.mcp.server.tencent.infrastructure.adapter;

import com.alibaba.fastjson.JSON;
import com.yby6.mcp.server.tencent.domain.adapter.ITencentPort;
import com.yby6.mcp.server.tencent.domain.model.ArticleFunctionRequest;
import com.yby6.mcp.server.tencent.domain.model.ArticleFunctionResponse;
import com.yby6.mcp.server.tencent.infrastructure.gateway.ITencentService;
import com.yby6.mcp.server.tencent.infrastructure.gateway.dto.AddArticleRequest;
import com.yby6.mcp.server.tencent.infrastructure.gateway.dto.AddArticleResponse;
import com.yby6.mcp.server.tencent.types.properties.TencentApiProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class TencentPort implements ITencentPort {
    
    @Resource
    private ITencentService iTencentService;
    
    @Resource
    private TencentApiProperties tencentApiProperties;
    
    /**
     * 发布文章到Tencent
     *
     * @param request 发文请求
     * @return 发文响应
     */
    @Override
    public ArticleFunctionResponse writeArticle(ArticleFunctionRequest request) throws IOException {
        log.info("接收到的参数: {}", request.toString());
        
        AddArticleRequest addArticleRequest = new AddArticleRequest();
        addArticleRequest.setTitle(request.getTitle());
        addArticleRequest.setPlain(request.getMarkdowncontent());
        addArticleRequest.setContent(addArticleRequest.getContent());
        addArticleRequest.setSourceType(1);
        addArticleRequest.setClassifyIds(List.of(2));
        addArticleRequest.setTagIds(List.of(18126));
        addArticleRequest.setLongtailTag(List.of("mcp"));
        addArticleRequest.setColumnIds(List.of(101806));
        addArticleRequest.setOpenComment(1);
        addArticleRequest.setCloseTextLink(0);
        addArticleRequest.setUserSummary(request.getUserSummary());
        addArticleRequest.setPic("");
        addArticleRequest.setSourceDetail(new HashMap<>());
        addArticleRequest.setZoneName("");
        
        Call<AddArticleResponse> call = iTencentService.addArticle(tencentApiProperties.getCookie(), addArticleRequest);
        Response<AddArticleResponse> response = call.execute();
        
        log.info("\n\n请求腾讯云开发者社区发布文章\n req:{} \nres:{}", JSON.toJSONString(addArticleRequest), JSON.toJSONString(response));
        
        if (response.isSuccessful()) {
            log.info("腾讯云开发者社区发布文章成功: {}", JSON.toJSONString(response.body()));
            
            AddArticleResponse articleResponseDTO = response.body();
            if (null == articleResponseDTO) return null;
            
            ArticleFunctionResponse articleFunctionResponse = new ArticleFunctionResponse();
            articleFunctionResponse.setStatus(articleResponseDTO.getStatus());
            articleFunctionResponse.setArticleId(articleResponseDTO.getArticleId());
            articleFunctionResponse.setUrl("https://cloud.tencent.com/developer/article/" + articleResponseDTO.getArticleId());
            
            return articleFunctionResponse;
        }
        log.error("腾讯云开发者社区发布文章失败: {}", JSON.toJSONString(response));
        return null;
    }
}
