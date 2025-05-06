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

/**
 * 腾讯云开发者社区端口适配器实现
 * 
 * 该实现类负责将领域层的请求转换为基础设施层的具体实现。
 * 作为端口适配器模式的具体实现，它：
 * 1. 实现了ITencentPort接口
 * 2. 负责领域模型和DTO之间的转换
 * 3. 处理与腾讯云API的具体交互
 * 
 * @author yby6
 * @version 1.0.0
 */
@Slf4j
@Component
public class TencentPort implements ITencentPort {
    
    /** 腾讯云API服务接口 */
    @Resource
    private ITencentService iTencentService;
    
    /** 腾讯云API配置属性 */
    @Resource
    private TencentApiProperties tencentApiProperties;
    
    /**
     * 发布文章到腾讯云开发者社区
     * 
     * 该方法实现了文章发布的具体逻辑：
     * 1. 将领域模型转换为API请求DTO
     * 2. 调用腾讯云API服务
     * 3. 处理响应结果并转换为领域模型
     * 
     * 主要步骤：
     * - 记录请求参数日志
     * - 构建API请求对象
     * - 设置文章分类、标签等属性
     * - 执行API调用
     * - 处理响应结果
     * - 构建返回对象
     * 
     * @param request 文章发布请求，包含文章标题、内容等信息
     * @return 文章发布响应，包含发布结果和文章ID等信息
     * @throws IOException 当发布过程中发生IO异常时抛出
     */
    @Override
    public ArticleFunctionResponse writeArticle(ArticleFunctionRequest request) throws IOException {
        log.info("接收到的参数: {}", request.toString());
        
        // 构建API请求对象
        AddArticleRequest addArticleRequest = new AddArticleRequest();
        addArticleRequest.setTitle(request.getTitle());
        addArticleRequest.setPlain(request.getMarkdowncontent());
        addArticleRequest.setContent(addArticleRequest.getContent());
        addArticleRequest.setSourceType(1);  // 设置为原创
        addArticleRequest.setClassifyIds(List.of(2));  // 设置文章分类
        addArticleRequest.setTagIds(List.of(18126));  // 设置文章标签
        addArticleRequest.setLongtailTag(List.of("mcp"));  // 设置长尾标签
        addArticleRequest.setColumnIds(List.of(101806));  // 设置专栏ID
        addArticleRequest.setOpenComment(1);  // 开启评论
        addArticleRequest.setCloseTextLink(0);  // 允许文本链接
        addArticleRequest.setUserSummary(request.getUserSummary());
        addArticleRequest.setPic("");  // 设置封面图片
        addArticleRequest.setSourceDetail(new HashMap<>());  // 设置来源详情
        addArticleRequest.setZoneName("");  // 设置专区名称
        
        // 执行API调用
        Call<AddArticleResponse> call = iTencentService.addArticle(tencentApiProperties.getCookie(), addArticleRequest);
        Response<AddArticleResponse> response = call.execute();
        
        // 记录请求和响应日志
        log.info("\n\n请求腾讯云开发者社区发布文章\n req:{} \nres:{}", JSON.toJSONString(addArticleRequest), JSON.toJSONString(response));
        
        if (response.isSuccessful()) {
            log.info("腾讯云开发者社区发布文章成功: {}", JSON.toJSONString(response.body()));
            
            // 处理成功响应
            AddArticleResponse articleResponseDTO = response.body();
            if (null == articleResponseDTO) return null;
            
            // 构建返回对象
            ArticleFunctionResponse articleFunctionResponse = new ArticleFunctionResponse();
            articleFunctionResponse.setStatus(articleResponseDTO.getStatus());
            articleFunctionResponse.setArticleId(articleResponseDTO.getArticleId());
            articleFunctionResponse.setUrl("https://cloud.tencent.com/developer/article/" + articleResponseDTO.getArticleId());
            
            return articleFunctionResponse;
        }
        
        // 处理失败响应
        log.error("腾讯云开发者社区发布文章失败: {}", JSON.toJSONString(response));
        return null;
    }
}
