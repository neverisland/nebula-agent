package cn.yang.nebula.agent.utils.file;

import cn.yang.common.data.structure.exception.BusinessException;
import cn.yang.nebula.agent.business.file.library.entity.FileLibrary;
import cn.yang.nebula.agent.enums.ErrorStatusCodeEnum;
import cn.yang.nebula.agent.exception.CallException;
import cn.yang.nebula.agent.integrate.file.facade.FileIntegrateFacade;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件压缩工具类
 *
 * @author : QingHai
 */
@Slf4j
public class FileZipUtils {

    /**
     * 返回压缩文件流
     *
     * @param fileIntegrateFacade 文件集成服务
     * @param fileList            文件列表
     * @param response            响应
     */
    public static void returnCompressionStream(String shareName, FileIntegrateFacade fileIntegrateFacade, List<FileLibrary> fileList, HttpServletResponse response) {
        // 3. 设置响应头
        String zipFileName = (StringUtils.hasLength(shareName) ? shareName : "share") + ".zip";
        try {
            String encodedFileName = URLEncoder.encode(zipFileName, StandardCharsets.UTF_8);
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            log.error("设置响应头失败", e);
            throw new BusinessException(ErrorStatusCodeEnum.ABNORMAL_OPERATION, "设置响应头失败");
        }

        // 7. 流式压缩并输出
        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream(), StandardCharsets.UTF_8)) {
            byte[] buffer = new byte[8192]; // 8KB 缓冲区
            // 用于跟踪已使用的条目名称，避免重复
            Set<String> usedEntryNames = new HashSet<>();

            for (FileLibrary file : fileList) {
                try {
                    // 从 OSS 获取文件流
                    InputStream fileInputStream = fileIntegrateFacade.obtainFile(file.getPath());

                    // 创建 ZIP 条目名称，确保唯一性
                    String entryName = generateUniqueEntryName(file, usedEntryNames);
                    ZipEntry zipEntry = new ZipEntry(entryName);
                    zos.putNextEntry(zipEntry);

                    // 流式读取并写入 ZIP
                    int len;
                    while ((len = fileInputStream.read(buffer)) != -1) {
                        zos.write(buffer, 0, len);
                    }

                    zos.closeEntry();
                    fileInputStream.close();
                } catch (CallException e) {
                    log.warn("文件 {} 下载失败: {}", file.getPath(), e.getMessage());
                } catch (IOException e) {
                    log.error("压缩文件 {} 时发生错误", file.getPath(), e);
                }
            }

            // 清除资源
            zos.finish();
            response.flushBuffer();
        } catch (IOException e) {
            log.error("压缩文件流输出失败", e);
            throw new BusinessException(ErrorStatusCodeEnum.ABNORMAL_OPERATION, "文件下载失败.");
        }
    }

    /**
     * 生成唯一的 ZIP 条目名称
     * 优先使用文件名，如果重复则使用计数器，格式：image.png, image(1).png, image(2).png
     *
     * @param file 文件对象
     * @param usedEntryNames 已使用的条目名称集合
     * @return 唯一的条目名称
     */
    private static String generateUniqueEntryName(FileLibrary file, Set<String> usedEntryNames) {
        // 获取基础文件名
        String baseName = file.getName();
        if (!StringUtils.hasLength(baseName)) {
            // 如果没有文件名，从路径中提取
            if (StringUtils.hasLength(file.getPath())) {
                String path = file.getPath();
                int lastSlash = path.lastIndexOf('/');
                baseName = lastSlash >= 0 ? path.substring(lastSlash + 1) : path;
            } else {
                baseName = "file";
            }
        }

        // 优先尝试直接使用文件名
        String entryName = baseName;
        
        // 如果文件名已存在，使用计数器格式：image(1).png, image(2).png
        if (usedEntryNames.contains(entryName)) {
            int counter = 1;
            int lastDot = baseName.lastIndexOf('.');
            if (lastDot > 0) {
                // 有扩展名：image.png -> image(1).png
                String nameWithoutExt = baseName.substring(0, lastDot);
                String extension = baseName.substring(lastDot);
                entryName = nameWithoutExt + "(" + counter + ")" + extension;
                // 如果还是重复，继续增加计数器
                while (usedEntryNames.contains(entryName)) {
                    counter++;
                    entryName = nameWithoutExt + "(" + counter + ")" + extension;
                }
            } else {
                // 没有扩展名：file -> file(1)
                entryName = baseName + "(" + counter + ")";
                while (usedEntryNames.contains(entryName)) {
                    counter++;
                    entryName = baseName + "(" + counter + ")";
                }
            }
        }
        
        usedEntryNames.add(entryName);
        return entryName;
    }
}
