package cn.yang.nebula.agent.business.file.space.repository;

import cn.yang.common.data.structure.annotation.assignment.BaseDataAssignment;
import cn.yang.common.data.structure.exception.BusinessException;
import cn.yang.common.data.structure.utils.bean.BeanConvertUtils;
import cn.yang.common.data.structure.vo.page.PageResult;
import cn.yang.common.data.structure.vo.page.PageUtils;
import cn.yang.foundational.capability.id.generator.IdGenerator;
import cn.yang.nebula.agent.business.file.space.dal.FileSpaceDo;
import cn.yang.nebula.agent.business.file.space.dto.FileSpacePageQueryDto;
import cn.yang.nebula.agent.business.file.space.entity.FileSpace;
import cn.yang.nebula.agent.business.file.space.mapper.FileSpaceMapper;
import cn.yang.nebula.agent.enums.ErrorStatusCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 个人空间仓储层
 *
 * @author : 未见清海
 */
@Component
@RequiredArgsConstructor
public class FileSpaceRepository {

    private final FileSpaceMapper fileSpaceMapper;

    private final IdGenerator idGenerator;

    /**
     * 新增数据
     *
     * @param fileSpace 个人空间实体
     * @return id
     */
    @BaseDataAssignment
    public String insertData(FileSpace fileSpace) {
        FileSpaceDo fileSpaceDo = BeanConvertUtils.convert(fileSpace, FileSpaceDo.class);
        fileSpaceDo.setId(idGenerator.getId());
        fileSpaceMapper.insert(fileSpaceDo);
        return fileSpaceDo.getId();
    }

    /**
     * 更新数据
     *
     * @param fileSpace 个人空间实体
     */
    @BaseDataAssignment
    public void updateData(FileSpace fileSpace) {
        FileSpaceDo fileSpaceDo = BeanConvertUtils.convert(fileSpace, FileSpaceDo.class);
        fileSpaceMapper.updateById(fileSpaceDo);
    }

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页结果
     */
    public PageResult<FileSpace> page(FileSpacePageQueryDto query) {
        return PageUtils.doSelectPage(query, () ->
                fileSpaceMapper.selectPageData(query));
    }

    /**
     * 根据id查询数据
     *
     * @param id id
     * @return 个人空间实体
     */
    public FileSpace selectById(String id) {
        FileSpaceDo fileSpaceDo = fileSpaceMapper.selectById(id);
        if (fileSpaceDo == null) {
            throw new BusinessException(ErrorStatusCodeEnum.DATA_DOES_NOT_EXIST, "空间不存在");
        }
        return BeanConvertUtils.convert(fileSpaceDo, FileSpace.class);
    }

    /**
     * 根据id删除数据
     *
     * @param id id
     */
    public void deleteById(String id) {
        fileSpaceMapper.deleteById(id);
    }

    /**
     * 根据用户id查询所有空间
     *
     * @param userId 用户id
     * @return 列表
     */
    public List<FileSpace> selectListByUserId(String userId) {
        List<FileSpaceDo> fileSpaceDos = fileSpaceMapper.selectListByUserId(userId);
        return BeanConvertUtils.convert(fileSpaceDos, FileSpace.class);
    }
}
