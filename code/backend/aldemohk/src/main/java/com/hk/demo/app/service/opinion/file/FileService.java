package com.hk.demo.app.service.opinion.file;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * 文件服务接口 — 统一文件上传/下载/删除。
 * 当前实现为本地文件存储，后续可替换为 OSS/S3 等。
 */
public interface FileService {

    /**
     * 上传文件，返回文件 ID（UUID）。
     */
    String upload(MultipartFile file, String bizType) throws IOException;

    /**
     * 按文件 ID 获取文件字节数据。
     */
    byte[] download(String fileId) throws IOException;

    /**
     * 删除文件。
     */
    void delete(String fileId);
}
