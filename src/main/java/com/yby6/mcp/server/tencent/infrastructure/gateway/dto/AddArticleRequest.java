package com.yby6.mcp.server.tencent.infrastructure.gateway.dto;

import com.yby6.mcp.server.tencent.types.utils.MarkdownToProseMirrorConverter;
import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 腾讯云开发者社区发布文章请求DTO
 * 用于封装发布文章时需要的所有参数
 */
@Data
public class AddArticleRequest {
    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容（JSON格式）
     * 包含富文本编辑器的完整内容结构
     */
    private String content;

    /**
     * 文章纯文本内容
     * 用于SEO和摘要展示
     */
    private String plain;

    /**
     * 文章来源类型
     * 1: 原创
     */
    private Integer sourceType;

    /**
     * 文章分类ID列表
     * 用于文章分类管理
     */
    private List<Integer> classifyIds;

    /**
     * 文章标签ID列表
     * 用于文章标签管理
     */
    private List<Integer> tagIds;

    /**
     * 长尾标签列表
     * 用于SEO优化
     */
    private List<String> longtailTag;

    /**
     * 专栏ID列表
     * 用于文章专栏管理
     */
    private List<Integer> columnIds;

    /**
     * 是否开启评论
     * 1: 开启
     * 0: 关闭
     */
    private Integer openComment;

    /**
     * 是否关闭文本链接
     * 1: 关闭
     * 0: 开启
     */
    private Integer closeTextLink;

    /**
     * 用户摘要
     * 用于文章预览展示
     */
    private String userSummary;

    /**
     * 文章封面图片URL
     */
    private String pic;

    /**
     * 来源详情
     * 用于记录文章来源的详细信息
     */
    private Map<String, Object> sourceDetail;

    /**
     * 专区名称
     */
    private String zoneName;

    /**
     * 草稿ID
     * 如果是编辑已有草稿，需要提供此ID
     */
    private Long draftId;
    
    /**
     * 获取ProseMirror格式的内容
     * 如果content为空，则自动将plain转换为ProseMirror格式
     *
     * @return ProseMirror格式的内容
     */
    public String getContent() {
        return MarkdownToProseMirrorConverter.convert(plain);
    }
}
