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
 */
@SpringBootApplication
public class McpServerApplication implements CommandLineRunner {
    
    private static final String BASE_URL = "https://cloud.tencent.com/";
    private final Logger log = LoggerFactory.getLogger(McpServerApplication.class);
    
    @Resource
    private TencentApiProperties tencentApiProperties;
    
    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
    
    
    /**
     * 腾讯API服务
     *
     * @return {@link ITencentService }
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
     */
    @Bean
    public ToolCallbackProvider tencentTools_saveArticle(TencentArticleService tencentArticleService) {
        return MethodToolCallbackProvider.builder().toolObjects(tencentArticleService).build();
    }
    
    
    @Override
    public void run(String... args) throws Exception {
        log.info("check tencent cookie ...");
        String cookie = tencentApiProperties.getCookie();
        if (cookie == null || cookie.isEmpty()) {
            log.error("tencent cookie is empty");
            System.exit(1);
        }
    }
    
}
