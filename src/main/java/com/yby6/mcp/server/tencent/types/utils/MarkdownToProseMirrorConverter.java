package com.yby6.mcp.server.tencent.types.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;

import java.util.UUID;

/**
 * Markdown到ProseMirror格式转换器
 * 
 * 该工具类用于将Markdown格式的文本转换为ProseMirror编辑器可用的JSON格式。
 * ProseMirror是一个富文本编辑器框架，需要特定的JSON结构来表示文档内容。
 * 
 * 主要功能：
 * 1. 解析Markdown文本为AST（抽象语法树）
 * 2. 将AST转换为ProseMirror格式的JSON
 * 3. 支持段落、文本、标题等基本元素的转换
 * 
 * 技术特点：
 * - 使用commonmark-java库解析Markdown
 * - 使用Jackson处理JSON
 * - 采用递归方式处理文档树结构
 * 
 * 使用示例：
 * ```java
 * String markdown = "# 标题\n这是一段文本";
 * String proseMirrorJson = MarkdownToProseMirrorConverter.convert(markdown);
 * ```
 * 
 * 注意事项：
 * 1. 输入必须是有效的Markdown格式
 * 2. 转换过程可能抛出RuntimeException
 * 3. 输出是符合ProseMirror规范的JSON字符串
 * 
 * @author yby6
 * @version 1.0.0
 * @since 2024/03/21
 */
public class MarkdownToProseMirrorConverter {
    /** 
     * JSON对象映射器
     * 用于创建和操作JSON节点，处理JSON序列化和反序列化
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /** 
     * Markdown解析器
     * 用于将Markdown文本解析为AST（抽象语法树）
     * 使用commonmark-java库的Parser实现
     */
    private static final Parser parser = Parser.builder().build();
    
    /** 
     * 文本渲染器
     * 用于将AST节点渲染为纯文本
     * 主要用于调试和日志记录
     */
    private static final TextContentRenderer textRenderer = TextContentRenderer.builder().build();
    
    /**
     * 私有构造函数
     * 
     * 防止工具类被实例化，所有方法都是静态的。
     * 符合工具类的最佳实践。
     */
    private MarkdownToProseMirrorConverter() {
        // 私有构造函数，防止实例化
    }
    
    /**
     * 将Markdown文本转换为ProseMirror格式
     * 
     * 该方法执行以下步骤：
     * 1. 解析Markdown文本为AST
     * 2. 创建ProseMirror文档结构
     * 3. 递归处理AST节点
     * 4. 返回JSON字符串
     * 
     * 转换过程：
     * - 首先创建文档根节点
     * - 然后递归处理所有子节点
     * - 最后将结果序列化为JSON字符串
     * 
     * 错误处理：
     * - 捕获所有可能的异常
     * - 将异常包装为RuntimeException
     * - 提供详细的错误信息
     * 
     * @param markdown 要转换的Markdown文本，不能为null
     * @return ProseMirror格式的JSON字符串
     * @throws RuntimeException 当转换过程中发生错误时抛出
     */
    public static String convert(String markdown) {
        try {
            // 解析 Markdown
            Node document = parser.parse(markdown);
            
            // 创建 ProseMirror 文档结构
            ObjectNode doc = objectMapper.createObjectNode();
            doc.put("type", "doc");
            
            ArrayNode content = objectMapper.createArrayNode();
            doc.set("content", content);
            
            // 处理文档节点
            processNode(document, content);
            
            return objectMapper.writeValueAsString(doc);
        } catch (Exception e) {
            throw new RuntimeException("转换失败", e);
        }
    }
    
    /**
     * 处理AST节点并转换为ProseMirror格式
     * 
     * 该方法递归处理不同类型的节点：
     * - 段落节点：创建段落结构
     * - 文本节点：创建文本内容
     * - 标题节点：创建标题结构
     * - 其他节点：递归处理子节点
     * 
     * 节点处理流程：
     * 1. 判断节点类型
     * 2. 创建对应的ProseMirror节点
     * 3. 设置节点属性
     * 4. 递归处理子节点
     * 
     * @param node 要处理的AST节点，不能为null
     * @param content 用于存储转换结果的JSON数组节点，不能为null
     */
    private static void processNode(Node node, ArrayNode content) {
        if (node instanceof Paragraph) {
            // 处理段落
            ObjectNode paragraph = createParagraphNode();
            ArrayNode paragraphContent = objectMapper.createArrayNode();
            paragraph.set("content", paragraphContent);
            
            // 处理段落内的子节点
            Node child = node.getFirstChild();
            while (child != null) {
                processNode(child, paragraphContent);
                child = child.getNext();
            }
            
            content.add(paragraph);
        } else if (node instanceof Text) {
            // 处理文本节点
            ObjectNode text = objectMapper.createObjectNode();
            text.put("type", "text");
            text.put("text", ((Text) node).getLiteral());
            content.add(text);
        } else if (node instanceof Heading) {
            // 处理标题
            ObjectNode heading = objectMapper.createObjectNode();
            heading.put("type", "heading");
            
            ObjectNode attrs = objectMapper.createObjectNode();
            attrs.put("level", ((Heading) node).getLevel());
            heading.set("attrs", attrs);
            
            ArrayNode headingContent = objectMapper.createArrayNode();
            heading.set("content", headingContent);
            
            // 处理标题内的子节点
            Node child = node.getFirstChild();
            while (child != null) {
                processNode(child, headingContent);
                child = child.getNext();
            }
            
            content.add(heading);
        } else {
            // 处理其他类型的节点
            Node child = node.getFirstChild();
            while (child != null) {
                processNode(child, content);
                child = child.getNext();
            }
        }
    }
    
    /**
     * 创建段落节点
     * 
     * 创建具有以下属性的段落节点：
     * - 唯一ID：使用UUID生成
     * - 文本对齐方式：默认为inherit
     * - 缩进级别：默认为0
     * - 文本颜色：默认为null
     * - 背景颜色：默认为null
     * - 拖拽句柄状态：默认为false
     * 
     * 节点结构：
     * ```json
     * {
     *   "type": "paragraph",
     *   "attrs": {
     *     "id": "uuid",
     *     "textAlign": "inherit",
     *     "indent": 0,
     *     "color": null,
     *     "background": null,
     *     "isHoverDragHandle": false
     *   }
     * }
     * ```
     * 
     * @return 配置好的段落节点
     */
    private static ObjectNode createParagraphNode() {
        ObjectNode paragraph = objectMapper.createObjectNode();
        paragraph.put("type", "paragraph");
        
        ObjectNode attrs = objectMapper.createObjectNode();
        attrs.put("id", UUID.randomUUID().toString());
        attrs.put("textAlign", "inherit");
        attrs.put("indent", 0);
        attrs.put("color", (String) null);
        attrs.put("background", (String) null);
        attrs.put("isHoverDragHandle", false);
        
        paragraph.set("attrs", attrs);
        return paragraph;
    }
}
