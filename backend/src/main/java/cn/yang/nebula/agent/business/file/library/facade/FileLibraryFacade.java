package cn.yang.nebula.agent.business.file.library.facade;

import cn.yang.common.data.structure.vo.page.PageResult;
import cn.yang.nebula.agent.business.file.library.dto.FileLibraryPageDto;
import cn.yang.nebula.agent.business.file.library.vo.FileLibraryPageVo;
import cn.yang.nebula.agent.business.file.library.dto.FileLibraryRenameDto;
import cn.yang.nebula.agent.business.file.library.vo.FileLibraryStatisticsVo;
import cn.yang.nebula.agent.business.file.library.vo.FileLibraryUploadVo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件库 门面接口
 *
 * @author 未见清海
 */
public interface FileLibraryFacade {

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 上传结果
     */
    FileLibraryUploadVo upload(MultipartFile file);

    /**
     * 分页查询
     *
     * @param fileLibraryPageDto 查询条件
     * @return 分页数据
     */
    PageResult<FileLibraryPageVo> page(FileLibraryPageDto fileLibraryPageDto);

    /**
     * 重命名
     *
     * @param fileLibraryRenameDto 入参
     */
    void rename(FileLibraryRenameDto fileLibraryRenameDto);

    /**
     * 删除文件
     *
     * @param id 文件id
     */
    void delete(String id);

    /**
     * 下载/预览
     *
     * @param relativePath 相对路径
     * @param response     响应
     */
    void downloadPreview(String relativePath, HttpServletResponse response);

    /**
     * 将文件从空间移除（设置空间id为null）
     *
     * @param fileIds 文件id列表
     */
    void removeFromSpace(List<String> fileIds);

    /**
     * 获取文件库统计信息
     *
     * @return 统计结果
     */
    FileLibraryStatisticsVo getFileLibraryStatistics();
}
