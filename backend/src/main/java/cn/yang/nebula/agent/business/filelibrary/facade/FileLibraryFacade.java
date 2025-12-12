package cn.yang.nebula.agent.business.filelibrary.facade;

import cn.yang.common.data.structure.vo.page.PageResult;
import cn.yang.nebula.agent.business.filelibrary.dto.FileLibraryPageVo;
import cn.yang.nebula.agent.business.filelibrary.dto.FileLibraryPageDto;
import cn.yang.nebula.agent.business.filelibrary.dto.FileLibraryRenameDto;
import cn.yang.nebula.agent.business.filelibrary.dto.FileLibraryUploadVo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

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
     * @param renameReq 入参
     */
    void rename(FileLibraryRenameDto renameReq);

    /**
     * 删除
     *
     * @param deleteReq 入参
     */
    void delete(String id);

    /**
     * 下载/预览
     *
     * @param relativePath 相对路径
     * @param response     响应
     */
    void downloadPreview(String relativePath, HttpServletResponse response);
}

