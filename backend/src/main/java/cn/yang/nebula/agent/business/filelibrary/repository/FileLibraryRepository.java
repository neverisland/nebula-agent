package cn.yang.nebula.agent.business.filelibrary.repository;

import cn.yang.common.data.structure.annotation.assignment.BaseDataAssignment;
import cn.yang.common.data.structure.annotation.assignment.DataOperationTypeEnum;
import cn.yang.common.data.structure.exception.NullDataException;
import cn.yang.common.data.structure.utils.bean.BeanConvertUtils;
import cn.yang.common.data.structure.vo.page.PageResult;
import cn.yang.foundational.capability.id.generator.IdGenerator;
import cn.yang.nebula.agent.business.filelibrary.dal.FileLibraryDo;
import cn.yang.nebula.agent.business.filelibrary.dto.FileLibraryPageVo;
import cn.yang.nebula.agent.business.filelibrary.dto.FileLibraryPageDto;
import cn.yang.nebula.agent.business.filelibrary.entity.FileLibrary;
import cn.yang.nebula.agent.business.filelibrary.mapper.FileLibraryMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

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
        Integer count = fileLibraryMapper.selectPageCount(fileLibraryPageDto);
        if (count == null || count == 0) {
            return new PageResult<>(fileLibraryPageDto);
        }
        List<FileLibraryDo> data = fileLibraryMapper.selectPageData(fileLibraryPageDto);
        List<FileLibrary> fileLibraryList = BeanConvertUtils.convert(data, FileLibrary.class);
        return new PageResult<>(fileLibraryPageDto, fileLibraryList, count);
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
     * @param id   主键
     * @param name 新名称
     * @return 影响行数
     */
    @BaseDataAssignment(DataOperationTypeEnum.UPDATE)
    public int rename(FileLibrary fileLibrary) {
        FileLibraryDo fileLibraryDo = BeanConvertUtils.convert(fileLibrary, FileLibraryDo.class);
        return fileLibraryMapper.rename(fileLibraryDo);
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

