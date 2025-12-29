package cn.yang.nebula.agent.business.file.space.mapper;

import cn.yang.nebula.agent.business.file.space.dal.FileSpaceDo;
import cn.yang.nebula.agent.business.file.space.dto.FileSpacePageQueryDto;
import cn.yang.nebula.agent.business.file.space.entity.FileSpace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 个人空间数据接口层
 *
 * @author : QingHai
 */
@Mapper
public interface FileSpaceMapper {

    /**
     * 查询分页数据
     *
     * @param query 查询条件
     * @return 数据
     */
    List<FileSpace> selectPageData(@Param("query") FileSpacePageQueryDto query);

    /**
     * 根据用户id查询所有空间
     *
     * @param userId 用户id
     * @return 列表
     */
    List<FileSpaceDo> selectListByUserId(@Param("userId") String userId);

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

    /**
     * 根据id查询
     *
     * @param id id
     * @return 数据
     */
    FileSpaceDo selectById(@Param("id") String id);

    /**
     * 根据id删除
     *
     * @param id id
     * @return 删除条数
     */
    int deleteById(@Param("id") String id);

    /**
     * 统计用户空间数量
     *
     * @param userId 用户id
     * @return 空间数量
     */
    Integer countByUserId(@Param("userId") String userId);

    /**
     * 根据id列表查询
     *
     * @param fileSpaceIdList id列表
     * @return 数据列表
     */
    List<FileSpaceDo> selectByIds(@Param("ids") List<String> fileSpaceIdList);
}
