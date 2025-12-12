package cn.yang.nebula.agent.business.filelibrary.controller;

import cn.yang.nebula.agent.business.filelibrary.facade.FileLibraryFacade;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * 文件下载
 *
 * @author : QingHai
 */
@Slf4j
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileLibraryFacade fileLibraryFacade;

    /**
     * 下载 / 预览文件
     *
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("/**")
    public void downloadPreview(HttpServletRequest request, HttpServletResponse response) {
        // todo，进行限流

        // 获取全路径
        String fullPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String relativePath = fullPath.replaceFirst("/file/", "");
        relativePath = URLDecoder.decode(relativePath, StandardCharsets.UTF_8);
        log.info("文件下载:[{}]", relativePath);
        fileLibraryFacade.downloadPreview(relativePath, response);
    }
}
