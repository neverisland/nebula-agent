package cn.yang.nebula.agent.business.file.space.facade;

import cn.yang.nebula.agent.business.file.space.dto.FileSpaceAllocateDto;
import cn.yang.nebula.agent.business.file.space.dto.FileSpaceInsertDto;
import cn.yang.nebula.agent.business.file.space.dto.FileSpacePageQueryDto;
import cn.yang.nebula.agent.business.file.space.dto.FileSpaceUpdateDto;
import cn.yang.nebula.agent.business.file.space.vo.FileSpaceSelectVo;
import cn.yang.nebula.agent.business.file.space.vo.FileSpaceStatisticsVo;
import cn.yang.nebula.agent.business.file.space.vo.FileSpaceVo;
import cn.yang.nebula.agent.vo.page.PageResult;

import java.util.List;

/**
 * 个人空间业务接口层
 *
 * @author : QingHai
 */
public interface FileSpaceFacade {

    /**
     * 新增个人空间
     *
     * @param fileSpaceInsertDto 新增入参
     * @return id
     */
    String insertFileSpace(FileSpaceInsertDto fileSpaceInsertDto);

    /**
     * 更新个人空间
     *
     * @param fileSpaceUpdateDto 更新入参
     */
    void updateFileSpace(FileSpaceUpdateDto fileSpaceUpdateDto);

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<FileSpaceVo> page(FileSpacePageQueryDto query);

    /**
     * 删除个人空间
     *
     * @param id id
     */
    void deleteFileSpace(String id);

    /**
     * 分配文件至个人空间
     *
     * @param allocateDto 分配入参
     */
    void allocateFilesToSpace(FileSpaceAllocateDto allocateDto);

    /**
     * 获取当前用户的所有文件空间列表
     *
     * @return 列表
     */
    List<FileSpaceSelectVo> selectFileSpaces();

    /**
     * 获取空间统计信息
     *
     * @return 统计结果
     */
    FileSpaceStatisticsVo getFileSpaceStatistics();
}
