package cn.yang.nebula.agent.business.file.space.service;

import cn.yang.common.data.structure.utils.bean.BeanConvertUtils;
import cn.yang.nebula.agent.business.file.space.dto.FileSpaceAllocateDto;
import cn.yang.nebula.agent.business.file.space.dto.FileSpaceInsertDto;
import cn.yang.nebula.agent.business.file.space.dto.FileSpaceUpdateDto;
import cn.yang.nebula.agent.business.file.space.entity.FileSpace;
import cn.yang.nebula.agent.business.file.space.facade.FileSpaceFacade;
import cn.yang.nebula.agent.business.file.space.repository.FileSpaceRepository;
import cn.yang.nebula.agent.business.file.space.dto.FileSpacePageQueryDto;
import cn.yang.nebula.agent.business.file.space.vo.FileSpaceVo;
import cn.yang.nebula.agent.business.file.space.vo.FileSpaceSelectVo;
import cn.yang.nebula.agent.business.file.space.vo.FileSpaceStatisticsVo;
import cn.yang.common.data.structure.vo.page.PageResult;
import cn.yang.nebula.agent.business.authentication.facade.AuthenticationFacade;
import cn.yang.nebula.agent.business.file.library.repository.FileLibraryRepository;
import org.springframework.util.CollectionUtils;
import java.util.List;
import cn.yang.nebula.agent.enums.ErrorStatusCodeEnum;
import cn.yang.common.data.structure.exception.BusinessException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 个人空间业务层
 *
 * @author : QingHai
 */
@Service
public class FileSpaceService implements FileSpaceFacade {

    @Resource
    private FileSpaceRepository fileSpaceRepository;

    @Resource
    private AuthenticationFacade authenticationFacade;

    @Resource
    private FileLibraryRepository fileLibraryRepository;

    /**
     * 新增个人空间
     *
     * @param fileSpaceInsertDto 新增入参
     * @return id
     */
    @Override
    public String insertFileSpace(FileSpaceInsertDto fileSpaceInsertDto) {
        FileSpace fileSpace = BeanConvertUtils.convert(fileSpaceInsertDto, FileSpace.class);
        return fileSpaceRepository.insertData(fileSpace);
    }

    /**
     * 更新个人空间
     *
     * @param fileSpaceUpdateDto 更新入参
     */
    @Override
    public void updateFileSpace(FileSpaceUpdateDto fileSpaceUpdateDto) {
        FileSpace fileSpace = BeanConvertUtils.convert(fileSpaceUpdateDto, FileSpace.class);
        fileSpaceRepository.updateData(fileSpace);
    }

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<FileSpaceVo> page(FileSpacePageQueryDto query) {
        String currentUserId = authenticationFacade.getCurrentUserId();
        query.setCreateUserId(currentUserId);

        PageResult<FileSpace> pageResult = fileSpaceRepository.page(query);
        if (CollectionUtils.isEmpty(pageResult.getList())) {
            return PageResult.empty(query);
        }

        List<FileSpaceVo> list = BeanConvertUtils.convert(pageResult.getList(), FileSpaceVo.class);
        return pageResult.transLate(list);
    }

    /**
     * 删除个人空间
     *
     * @param id id
     */
    @Override
    public void deleteFileSpace(String id) {
        String currentUserId = authenticationFacade.getCurrentUserId();
        // 获取数据
        FileSpace fileSpace = fileSpaceRepository.selectById(id);
        if (!currentUserId.equals(fileSpace.getCreateUserId())) {
            throw new BusinessException(ErrorStatusCodeEnum.PERMISSION_ERROR, "无权限.");
        }
        fileSpaceRepository.deleteById(id);
    }

    /**
     * 分配文件至个人空间
     *
     * @param allocateDto 分配入参
     */
    @Override
    public void allocateFilesToSpace(FileSpaceAllocateDto allocateDto) {
        String currentUserId = authenticationFacade.getCurrentUserId();
        // 校验空间是否属于自己
        FileSpace fileSpace = fileSpaceRepository.selectById(allocateDto.getSpaceId());
        if (!currentUserId.equals(fileSpace.getCreateUserId())) {
            throw new BusinessException(ErrorStatusCodeEnum.PERMISSION_ERROR, "无权分配.");
        }

        // 分配文件
        fileLibraryRepository.updateSpaceId(currentUserId, allocateDto.getSpaceId(), allocateDto.getFileIds());
    }

    /**
     * 获取当前用户的所有文件空间列表
     *
     * @return 列表
     */
    @Override
    public List<FileSpaceSelectVo> selectFileSpaces() {
        String currentUserId = authenticationFacade.getCurrentUserId();
        List<FileSpace> list = fileSpaceRepository.selectListByUserId(currentUserId);
        return BeanConvertUtils.convert(list, FileSpaceSelectVo.class);
    }

    /**
     * 获取空间统计信息
     *
     * @return 统计结果
     */
    @Override
    public FileSpaceStatisticsVo getFileSpaceStatistics() {
        String currentUserId = authenticationFacade.getCurrentUserId();
        Integer spaceCount = fileSpaceRepository.countByUserId(currentUserId);

        return FileSpaceStatisticsVo.builder()
                .spaceCount(spaceCount)
                .build();
    }
}
