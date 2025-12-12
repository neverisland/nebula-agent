package cn.yang.nebula.agent.business.filelibrary.service;

import cn.yang.common.data.structure.exception.BusinessException;
import cn.yang.common.data.structure.exception.NullDataException;
import cn.yang.common.data.structure.utils.bean.BeanConvertUtils;
import cn.yang.common.data.structure.vo.page.PageResult;
import cn.yang.nebula.agent.business.authentication.facade.AuthenticationFacade;
import cn.yang.nebula.agent.business.filelibrary.dto.FileLibraryPageDto;
import cn.yang.nebula.agent.business.filelibrary.dto.FileLibraryPageVo;
import cn.yang.nebula.agent.business.filelibrary.dto.FileLibraryRenameDto;
import cn.yang.nebula.agent.business.filelibrary.dto.FileLibraryUploadVo;
import cn.yang.nebula.agent.business.filelibrary.entity.FileLibrary;
import cn.yang.nebula.agent.business.filelibrary.facade.FileLibraryFacade;
import cn.yang.nebula.agent.business.filelibrary.repository.FileLibraryRepository;
import cn.yang.nebula.agent.business.filelibrary.utils.ImageUtils;
import cn.yang.nebula.agent.enums.ErrorStatusCodeEnum;
import cn.yang.nebula.agent.exception.CallException;
import cn.yang.nebula.agent.integrate.file.facade.FileIntegrateFacade;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 文件库 业务层
 *
 * @author 未见清海
 */
@Slf4j
@Service
public class FileLibraryService implements FileLibraryFacade {

    @Resource
    private FileLibraryRepository fileLibraryRepository;

    @Resource
    private FileLibraryOssService fileLibraryOssService;

    @Resource
    private FileIntegrateFacade fileIntegrateFacade;

    @Value("${file.access.address:http://127.0.0.1:9000}")
    private String fileAccessAddress;

    private static final DateTimeFormatter DATE_PATH_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private static final String FILE_ACCESS_PREFIX = "/file/";

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 上传结果
     */
    @Override
    public FileLibraryUploadVo upload(MultipartFile file) {
        // 获取原始文件名称
        FileLibrary fileLibrary = buildFileLibrary(file);
        try {
            fileIntegrateFacade.upload(fileLibrary.getPath(), file);
            String thumbnailImage = uploadThumbnailImage(fileLibrary, file);
            fileLibrary.setThumbnails(thumbnailImage);
            // 上传图片
            fileLibraryRepository.insert(fileLibrary);
            FileLibraryUploadVo fileLibraryUploadVo = BeanConvertUtils.convert(fileLibrary, FileLibraryUploadVo.class);
            fileLibraryUploadVo.setUrl(fileAccessAddress + FILE_ACCESS_PREFIX + fileLibrary.getPath());
            fileLibraryUploadVo.setThumbnailsUrl(fileAccessAddress + FILE_ACCESS_PREFIX + fileLibrary.getThumbnails());

            return fileLibraryUploadVo;
        } catch (CallException e) {
            throw new BusinessException(ErrorStatusCodeEnum.ABNORMAL_OPERATION, "文件上传失败,请重试");
        }
    }

    /**
     * 上传缩略图
     *
     * @param fileLibrary 文件实体
     * @param file        文件
     * @return 缩略图文件路径
     */
    private String uploadThumbnailImage(FileLibrary fileLibrary, MultipartFile file) throws CallException {
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            return null;
        }

        String path = fileLibrary.getPath();
        String thumbnails = path.substring(0, path.lastIndexOf(".")) + "-thumbnail" + path.substring(path.lastIndexOf("."));
        // 缩略图
        try {
            if (file.getSize() >= 100 * 1024) {
                File compressImage = ImageUtils.compressImage(file, 0.1f);
                fileIntegrateFacade.upload(thumbnails, compressImage);
                if (!compressImage.delete()) {
                    log.warn("临时文件删除失败.");
                }
            } else {
                fileIntegrateFacade.upload(thumbnails, file);
            }
        } catch (IOException e) {
            log.error("获取压缩图片失败", e);
            throw new BusinessException(ErrorStatusCodeEnum.ABNORMAL_OPERATION, "文件上传失败,请重试");
        }
        return thumbnails;
    }

    /**
     * 根据文件构建实体
     *
     * @param file 上传的文件
     * @return 文件实体
     */
    private FileLibrary buildFileLibrary(MultipartFile file) {
        // 获取原始文件名称
        String originalFilename = file.getOriginalFilename();
        String fileName = StringUtils.hasText(originalFilename) ? originalFilename : "unknown";
        // 媒体类型
        String mimeType = StringUtils.hasText(file.getContentType()) ? file.getContentType() : URLConnection.guessContentTypeFromName(fileName);
        if (!StringUtils.hasText(mimeType)) {
            // 兜底的
            mimeType = "application/octet-stream";
        }
        // 后缀
        String extension = (!StringUtils.hasText(fileName) || !fileName.contains(".")) ? "" : fileName.substring(fileName.lastIndexOf("."));
        String datePath = LocalDate.now().format(DATE_PATH_FORMATTER);
        String relativePath = datePath + "/" + UUID.randomUUID().toString().replace("-", "") + extension;
        return new FileLibrary(fileName, mimeType, file.getSize(), relativePath);
    }

    /**
     * 分页查询
     *
     * @param fileLibraryPageDto 查询条件
     * @return 分页数据
     */
    @Override
    public PageResult<FileLibraryPageVo> page(FileLibraryPageDto fileLibraryPageDto) {

        PageResult<FileLibrary> fileLibraryPageResult = fileLibraryRepository.pageData(fileLibraryPageDto);
        List<FileLibrary> list = fileLibraryPageResult.getList();
        if (!CollectionUtils.isEmpty(list)) {
            List<FileLibraryPageVo> fileLibraryPageVos = list.stream().map(item -> {
                FileLibraryPageVo fileLibraryPageVo = BeanConvertUtils.convert(item, FileLibraryPageVo.class);
                fileLibraryPageVo.setUrl(fileAccessAddress + FILE_ACCESS_PREFIX + item.getPath());
                fileLibraryPageVo.setThumbnailsUrl(fileAccessAddress + FILE_ACCESS_PREFIX + item.getThumbnails());
                return fileLibraryPageVo;
            }).toList();

            return new PageResult<>(fileLibraryPageDto, fileLibraryPageVos, fileLibraryPageResult.getTotal());
        }

        return new PageResult<>(fileLibraryPageDto, List.of(), fileLibraryPageResult.getTotal());
    }

    /**
     * 重命名
     *
     * @param renameReq 入参
     */
    @Override
    public void rename(FileLibraryRenameDto renameReq) {
        if (renameReq == null || !StringUtils.hasText(renameReq.getId()) || !StringUtils.hasText(renameReq.getName())) {
            throw new BusinessException(ErrorStatusCodeEnum.PARAMETER_VERIFICATION_EXCEPTION, "文件id与名称不能为空");
        }
        FileLibrary fileLibrary = new FileLibrary();
        fileLibrary.setId(renameReq.getId());
        fileLibrary.setName(renameReq.getName());
        int count = fileLibraryRepository.rename(fileLibrary);
        if (count == 0) {
            throw new BusinessException(ErrorStatusCodeEnum.DATA_DOES_NOT_EXIST, "文件不存在");
        }
    }

    /**
     * 删除
     *
     * @param deleteReq 入参
     */
    @Override
    public void delete(String id) {
        if (!StringUtils.hasText(id)) {
            throw new BusinessException(ErrorStatusCodeEnum.PARAMETER_VERIFICATION_EXCEPTION, "文件id不能为空");
        }
        FileLibrary fileLibrary;
        try {
            fileLibrary = fileLibraryRepository.selectById(id);
        } catch (NullDataException e) {
            throw new BusinessException(ErrorStatusCodeEnum.DATA_DOES_NOT_EXIST, e.getMessage());
        }
        fileLibraryOssService.delete(fileLibrary.getPath());
        int count = fileLibraryRepository.deleteById(id);
        if (count == 0) {
            throw new BusinessException(ErrorStatusCodeEnum.DATA_DOES_NOT_EXIST, "文件不存在");
        }
    }

    /**
     * 下载/预览
     *
     * @param relativePath 相对路径
     * @param response     响应
     */
    @Override
    public void downloadPreview(String relativePath, HttpServletResponse response) {
        if (!StringUtils.hasText(relativePath)) {
            throw new BusinessException(ErrorStatusCodeEnum.PARAMETER_VERIFICATION_EXCEPTION, "文件路径不能为空");
        }

        try {
            FileLibrary fileLibrary = fileLibraryRepository.selectByPath(relativePath);
            // 获取请求头
            String mimeType = StringUtils.hasText(fileLibrary.getMimeType()) ? fileLibrary.getMimeType() : "application/octet-stream";
            String fileName = StringUtils.hasText(fileLibrary.getName()) ? fileLibrary.getName() : relativePath.substring(relativePath.lastIndexOf("/") + 1);

            InputStream inputStream = fileIntegrateFacade.obtainFile(fileLibrary.getPath());
            // 设置响应头
            response.setContentType(mimeType);
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", "inline; filename*=UTF-8''" + encodedFileName);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            // 文件传输
            try (ServletOutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
            } finally {
                inputStream.close();
            }

        } catch (NullDataException e) {
            throw new BusinessException(ErrorStatusCodeEnum.DATA_DOES_NOT_EXIST, e.getMessage());
        } catch (IOException | CallException e) {
            throw new BusinessException(ErrorStatusCodeEnum.ABNORMAL_OPERATION, "文件下载失败,请重试");
        }
    }


}

