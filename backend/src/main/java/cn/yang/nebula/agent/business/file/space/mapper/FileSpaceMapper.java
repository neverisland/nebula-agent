package cn.yang.nebula.agent.business.file.space.mapper;

import cn.yang.nebula.agent.business.file.space.dal.FileSpaceDo;
import cn.yang.nebula.agent.business.file.space.dto.FileSpacePageQueryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 个人空间数据接口层
 *
 * @author : 未见清海
 */
@Mapper
public interface FileSpaceMapper {

    /**
     * 查询分页数据
     *
     * @param query 查询条件
     * @return 数据
     */
    List<FileSpaceDo> selectPageData(@Param("query") FileSpacePageQueryDto query);

    /**
     * 新增数据
     *
     * @param fileSpaceDo 新增数据
     * @return 新增条数
     */
    int insert(FileSpaceDo fileSpaceDo);

    /**
     * 根据id修改
     *
     * @param fileSpaceDo fileSpaceDo
     * @return int
     */
    int updateById(FileSpaceDo fileSpaceDo);
}
