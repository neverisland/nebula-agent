package cn.yang.nebula.agent.business.file.library.repository;

import cn.yang.common.data.structure.annotation.assignment.BaseDataAssignment;
import cn.yang.common.data.structure.annotation.assignment.DataOperationTypeEnum;
import cn.yang.common.data.structure.exception.NullDataException;
import cn.yang.common.data.structure.utils.bean.BeanConvertUtils;
import cn.yang.common.data.structure.vo.page.PageResult;
import cn.yang.common.data.structure.vo.page.PageUtils;
import cn.yang.foundational.capability.id.generator.IdGenerator;
import cn.yang.nebula.agent.business.file.library.dal.FileLibraryDo;
import cn.yang.nebula.agent.business.file.library.dto.FileLibraryPageDto;
import cn.yang.nebula.agent.business.file.library.entity.FileLibrary;
import cn.yang.nebula.agent.business.file.library.mapper.FileLibraryMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 文件库 仓储层
 *
 * @author 未见清海
 */
@Component
public class FileLibraryRepository {

    @Resource
    private FileLibraryMapper fileLibraryMapper;

    @Resource
    private IdGenerator idGenerator;

    /**
     * 新增文件记录
     *
     * @param fileLibrary 文件数据
     * @return 主键id
     */
    @BaseDataAssignment
    public String insert(FileLibrary fileLibrary) {
        FileLibraryDo fileLibraryDo = BeanConvertUtils.convert(fileLibrary, FileLibraryDo.class);
        fileLibraryDo.setId(idGenerator.getId());
        fileLibraryMapper.insert(fileLibraryDo);
        return fileLibraryDo.getId();
    }

    /**
     * 分页查询数据
     *
     * @param fileLibraryPageDto 查询条件
     * @return 分页结果
     */
    public PageResult<FileLibrary> pageData(FileLibraryPageDto fileLibraryPageDto) {
        PageResult<FileLibraryDo> fileLibraryDoPageResult = PageUtils.doSelectPage(fileLibraryPageDto, () ->
                fileLibraryMapper.selectPageData(fileLibraryPageDto));
        List<FileLibraryDo> list = fileLibraryDoPageResult.getList();
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.empty(fileLibraryPageDto);
        }

        List<FileLibrary> convert = BeanConvertUtils.convert(list, FileLibrary.class);
        return fileLibraryDoPageResult.transLate(convert);
    }

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return 文件
     * @throws NullDataException 未找到
     */
    public FileLibrary selectById(String id) throws NullDataException {
        FileLibraryDo fileLibraryDo = fileLibraryMapper.selectById(id);
        if (fileLibraryDo == null) {
            throw new NullDataException("文件不存在");
        }
        return BeanConvertUtils.convert(fileLibraryDo, FileLibrary.class);
    }

    /**
     * 根据路径查询
     *
     * @param path 相对路径
     * @return 文件
     * @throws NullDataException 未找到
     */
    public FileLibrary selectByPath(String path) throws NullDataException {
        FileLibraryDo fileLibraryDo = fileLibraryMapper.selectByPath(path);
        if (fileLibraryDo == null) {
            throw new NullDataException("文件不存在");
        }
        return BeanConvertUtils.convert(fileLibraryDo, FileLibrary.class);
    }

    /**
     * 重命名
     *
     * @param fileLibrary 文件
     */
    @BaseDataAssignment(DataOperationTypeEnum.UPDATE)
    public void rename(FileLibrary fileLibrary) {
        FileLibraryDo fileLibraryDo = BeanConvertUtils.convert(fileLibrary, FileLibraryDo.class);
        fileLibraryMapper.rename(fileLibraryDo);
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return 影响行数
     */
    public int deleteById(String id) {
        return fileLibraryMapper.deleteById(id);
    }
}

