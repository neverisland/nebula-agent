package cn.yang.nebula.agent.business.file.space.service;

import cn.yang.common.data.structure.utils.bean.BeanConvertUtils;
import cn.yang.nebula.agent.business.file.space.dto.FileSpaceInsertDto;
import cn.yang.nebula.agent.business.file.space.dto.FileSpaceUpdateDto;
import cn.yang.nebula.agent.business.file.space.entity.FileSpace;
import cn.yang.nebula.agent.business.file.space.facade.FileSpaceFacade;
import cn.yang.nebula.agent.business.file.space.repository.FileSpaceRepository;
import cn.yang.nebula.agent.business.file.space.dto.FileSpacePageQueryDto;
import cn.yang.nebula.agent.business.file.space.vo.FileSpaceVo;
import cn.yang.common.data.structure.vo.page.PageResult;
import cn.yang.nebula.agent.business.authentication.facade.AuthenticationFacade;
import org.springframework.util.CollectionUtils;
import java.util.List;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 个人空间业务层
 *
 * @author : 未见清海
 */
@Service
public class FileSpaceService implements FileSpaceFacade {

    @Resource
    private FileSpaceRepository fileSpaceRepository;

    @Resource
    private AuthenticationFacade authenticationFacade;

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
}
