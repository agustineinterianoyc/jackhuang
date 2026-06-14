package com.hk.demo.app.controller.opinion.attachment;

import com.hk.demo.api.response.ApiResponse;
import com.hk.demo.core.response.ApiResponseFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * 附件上传 mock 控制器。
 *
 * 当前为 Q-A3 默认假设：临时打桩。
 * 真实文件服务接口就绪后替换为对外部服务的代理调用。
 *
 * 上传成功后返回伪 fileId / fileName / fileSize；前端在保存征集任务时把这些信息回传。
 */
@RestController
@RequestMapping("/api/opinion/attachment")
public class OpinionAttachmentMockController {

    /**
     * 上传附件（mock）。
     *
     * @param file 上传文件
     * @return 伪文件元数据
     */
    @PostMapping("/upload")
    public ApiResponse<UploadResultVO> upload(@RequestParam("file") MultipartFile file) {
        UploadResultVO vo = new UploadResultVO();
        vo.setFileId("mock-" + UUID.randomUUID().toString().replace("-", ""));
        vo.setFileName(file.getOriginalFilename());
        vo.setFileSize(file.getSize());
        return ApiResponseFactory.success(vo);
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
