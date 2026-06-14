package com.hk.demo.app.service.opinion.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 本地文件存储实现。
 * 生产环境可替换为 OSS/S3/MinIO 等对象存储。
 */
@Service
public class LocalFileService implements FileService {

    private static final Logger log = LoggerFactory.getLogger(LocalFileService.class);

    private final Path baseDir;

    public LocalFileService(@Value("${app.file.storage-dir:./data/files}") String storageDir) {
        this.baseDir = Paths.get(storageDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(baseDir);
            log.info("[file] storage dir: {}", baseDir);
        } catch (IOException e) {
            throw new RuntimeException("无法创建文件存储目录: " + baseDir, e);
        }
    }

    @Override
    public String upload(MultipartFile file, String bizType) throws IOException {
        String fileId = UUID.randomUUID().toString().replace("-", "");
        String ext = getExtension(file.getOriginalFilename());
        String filename = fileId + (ext.isEmpty() ? "" : "." + ext);
        Path bizDir = baseDir.resolve(bizType);
        Files.createDirectories(bizDir);
        Path target = bizDir.resolve(filename);
        file.transferTo(target.toFile());
        log.info("[file] uploaded: fileId={}, name={}, size={}, bizType={}", fileId, file.getOriginalFilename(), file.getSize(), bizType);
        return fileId;
    }

    @Override
    public byte[] download(String fileId) throws IOException {
        Path file = findFile(fileId);
        if (file == null) {
            throw new RuntimeException("文件不存在: " + fileId);
        }
        return Files.readAllBytes(file);
    }

    @Override
    public void delete(String fileId) {
        Path file = findFile(fileId);
        if (file != null) {
            try {
                Files.deleteIfExists(file);
                log.info("[file] deleted: fileId={}", fileId);
            } catch (IOException e) {
                log.warn("[file] delete failed: fileId={}", fileId, e);
            }
        }
    }

    private Path findFile(String fileId) {
        try {
            return Files.walk(baseDir)
                .filter(Files::isRegularFile)
                .filter(p -> p.getFileName().toString().startsWith(fileId))
                .findFirst()
                .orElse(null);
        } catch (IOException e) {
            return null;
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}
