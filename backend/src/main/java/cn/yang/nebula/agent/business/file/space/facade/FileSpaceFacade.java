package cn.yang.nebula.agent.business.file.space.facade;

import cn.yang.nebula.agent.business.file.space.dto.FileSpaceInsertDto;
import cn.yang.nebula.agent.business.file.space.dto.FileSpaceUpdateDto;
import cn.yang.nebula.agent.business.file.space.dto.FileSpacePageQueryDto;
import cn.yang.nebula.agent.business.file.space.vo.FileSpaceVo;
import cn.yang.common.data.structure.vo.page.PageResult;

/**
 * 个人空间业务接口层
 *
 * @author : 未见清海
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
}
