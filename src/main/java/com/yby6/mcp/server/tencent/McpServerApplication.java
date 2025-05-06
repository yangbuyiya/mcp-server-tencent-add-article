package com.yby6.mcp.server.tencent;

import com.yby6.mcp.server.tencent.domain.service.TencentArticleService;
import com.yby6.mcp.server.tencent.infrastructure.gateway.ITencentService;
import com.yby6.mcp.server.tencent.types.properties.TencentApiProperties;
import jakarta.annotation.Resource;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.TimeUnit;

/**
 * 腾讯云开发者社区服务启动类
 *
 * 该应用是一个基于Spring Boot的服务器应用，主要用于与腾讯云开发者社区进行交互。
 * 主要功能包括：
 * 1. 提供腾讯云API的访问服务
 * 2. 集成MCP工具，支持文章保存等功能
 * 3. 管理腾讯云API的认证信息
 *
 * @author yby6
 * @version 1.0.0
 */
@SpringBootApplication
public class McpServerApplication implements CommandLineRunner {
    
    /** 腾讯云API基础URL */
    private static final String BASE_URL = "https://cloud.tencent.com/";
    
    /** 日志记录器 */
    private final Logger log = LoggerFactory.getLogger(McpServerApplication.class);
    
    /** 腾讯云API配置属性 */
    @Resource
    private TencentApiProperties tencentApiProperties;
    
    /**
     * 应用程序入口点
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
    
    /**
     * 配置并创建腾讯API服务实例
     *
     * 该方法创建了一个配置了超时设置的OkHttpClient，并使用Retrofit构建API服务接口。
     * 主要配置包括：
     * - 连接超时：30秒
     * - 读取超时：30秒
     * - 写入超时：30秒
     *
     * @return 配置好的腾讯API服务接口实例
     */
    @Bean
    public ITencentService tencentService() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        
        return retrofit.create(ITencentService.class);
    }
    
    /**
     * 注册MCP工具
     *
     * 该方法用于注册文章保存相关的MCP工具，使其可以被Spring AI框架调用。
     *
     * @param tencentArticleService 腾讯文章服务实例
     * @return 配置好的工具回调提供者
     */
    @Bean
    public ToolCallbackProvider tencentTools(TencentArticleService tencentArticleService) {
        return MethodToolCallbackProvider.builder().toolObjects(tencentArticleService).build();
    }
    
    /**
     * 应用程序启动时的初始化操作
     *
     * 该方法在应用启动时执行，主要用于：
     * 1. 检查腾讯云Cookie配置
     * 2. 验证认证信息的有效性
     *
     * @param args 命令行参数
     * @throws Exception 如果初始化过程中发生错误
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("check tencent cookie ...");
        String cookie = tencentApiProperties.getCookie();
        if (cookie == null || cookie.isEmpty()) {
            log.error("tencent cookie is empty");
        }
    }
}
