package com.hk.demo.app.controller.opinion.attachment;

import com.hk.demo.api.response.ApiResponse;
import com.hk.demo.app.service.opinion.file.FileService;
import com.hk.demo.core.response.ApiResponseFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 附件控制器 — 文件上传/下载。
 */
@RestController
@RequestMapping("/api/opinion/attachment")
public class OpinionAttachmentController {

    private final FileService fileService;

    public OpinionAttachmentController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 上传附件。
     *
     * @param file 上传文件
     * @return 文件元数据
     */
    @PostMapping("/upload")
    public ApiResponse<UploadResultVO> upload(@RequestParam("file") MultipartFile file) {
        try {
            String fileId = fileService.upload(file, "opinion");
            UploadResultVO vo = new UploadResultVO();
            vo.setFileId(fileId);
            vo.setFileName(file.getOriginalFilename());
            vo.setFileSize(file.getSize());
            return ApiResponseFactory.success(vo);
        } catch (Exception e) {
            com.hk.demo.api.enums.ResultCode code = com.hk.demo.api.enums.ResultCode.SYSTEM_ERROR;
            return ApiResponseFactory.fail(code);
        }
    }

    /**
     * 下载附件。
     *
     * @param fileId 文件 ID
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> download(@PathVariable("fileId") String fileId) {
        try {
            byte[] data = fileService.download(fileId);
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(fileId, StandardCharsets.UTF_8) + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 上传响应。
     */
    public static class UploadResultVO {
        private String fileId;
        private String fileName;
        private Long fileSize;

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Long getFileSize() {
            return fileSize;
        }

        public void setFileSize(Long fileSize) {
            this.fileSize = fileSize;
        }
    }
}
