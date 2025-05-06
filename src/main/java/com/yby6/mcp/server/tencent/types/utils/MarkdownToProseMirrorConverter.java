package com.yby6.mcp.server.tencent.types.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;

import java.util.UUID;

public class MarkdownToProseMirrorConverter {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Parser parser = Parser.builder().build();
    private static final TextContentRenderer textRenderer = TextContentRenderer.builder().build();
    
    private MarkdownToProseMirrorConverter() {
        // 私有构造函数，防止实例化
    }
    
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
