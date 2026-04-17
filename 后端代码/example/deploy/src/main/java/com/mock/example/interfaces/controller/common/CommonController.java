package com.mock.example.interfaces.controller.common;

import com.mock.example.common.config.ProjectConfig;
import com.mock.example.common.entity.Response;
import com.mock.example.common.utils.ServerUtil;
import com.mock.example.common.utils.StringUtil;
import com.mock.example.common.utils.file.FileUploadUtil;
import com.mock.example.common.utils.file.FileUtil;
import com.mock.example.interfaces.controller.BaseController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用控制层
 *
 * @author: Mock
 * @date: 2025-01-18 21:04:48
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CommonController extends BaseController {

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @GetMapping("common/download")
    public void fileDownload(
            String fileName,
            Boolean delete,
            HttpServletResponse response
    ) {
        try {
            if (!FileUtil.checkAllowDownload(fileName)) {
                throw new Exception(StringUtil.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = ProjectConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtil.setAttachmentResponseHeader(response, realFileName);
            FileUtil.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtil.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 【新增】通用上传请求 (处理图片、文件等)
     */
    @PostMapping("common/upload")
    public Response<Map<String, Object>> uploadFile(MultipartFile file) {
        try {
            // 获取上传文件路径 (默认在配置文件的 profile 中设置)
            String filePath = ProjectConfig.getUploadPath();

            // 上传并返回新文件名称
            String fileName = FileUploadUtil.upload(filePath, file);

            // 拼接完整的网络可访问URL
            String url = new com.mock.example.common.utils.ServerUtil().getDomain(com.mock.example.common.utils.ServletUtil.getRequest()) + fileName;

            // 封装前端组件需要的字段：fileName 和 url
            Map<String, Object> ajax = new HashMap<>();
            ajax.put("fileName", fileName);
            ajax.put("url", url);

            return new Response<>(ajax);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return new Response<>(500, "上传文件失败: " + e.getMessage());
        }
    }
}
