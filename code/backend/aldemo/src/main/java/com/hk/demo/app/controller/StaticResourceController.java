package com.hk.demo.app.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 静态资源补充控制器，提供项目初始化阶段的最小可用入口。
 */
@RestController
public class StaticResourceController {

    /**
     * 浏览器通常会自动请求 favicon.ico。
     * 当前项目初始化阶段暂未提供图标文件，这里显式返回 204，避免产生无意义的异常日志。
     *
     * @return 空响应
     */
    @GetMapping("/favicon.ico")
    public ResponseEntity<Void> favicon() {
        return ResponseEntity.noContent().build();
    }

    /**
     * 项目根路径欢迎页。
     * 仅用于初始化完成后的快速可用性确认，正式业务接入后可移除或改写。
     *
     * @return 欢迎页 HTML
     */
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String welcome() {
        return "<!DOCTYPE html>"
                + "<html lang=\"zh-CN\"><head><meta charset=\"UTF-8\"/>"
                + "<title>公司培训项目（aldemo）</title></head>"
                + "<body style=\"font-family:sans-serif;margin:48px;\">"
                + "<h1>公司培训项目</h1>"
                + "<p>aldemo 后端服务已启动。</p>"
                + "<p>API 接入请参考 <code>code/backend/aldemo</code> 下的接口文档。</p>"
                + "</body></html>";
    }
}
