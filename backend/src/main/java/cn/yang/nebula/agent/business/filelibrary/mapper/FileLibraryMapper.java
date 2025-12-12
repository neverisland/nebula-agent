package cn.yang.nebula.agent.business.filelibrary.mapper;

import cn.yang.nebula.agent.business.filelibrary.dal.FileLibraryDo;
import cn.yang.nebula.agent.business.filelibrary.dto.FileLibraryPageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文件库 Mapper
 *
 * @author 未见清海
 */
@Mapper
public interface FileLibraryMapper {

    /**
     * 新增文件记录
     *
     * @param fileLibraryDo 文件数据
     * @return 插入数量
     */
    int insert(FileLibraryDo fileLibraryDo);

    /**
     * 分页统计
     *
     * @param query 查询入参
     * @return 数量
     */
    Integer selectPageCount(@Param("query") FileLibraryPageDto query);

    /**
     * 分页查询
     *
     * @param query 查询入参
     * @return 数据
     */
    List<FileLibraryDo> selectPageData(@Param("query") FileLibraryPageDto query);

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return 数据
     */
    FileLibraryDo selectById(String id);

    /**
     * 根据路径查询
     *
     * @param path 相对路径
     * @return 数据
     */
    FileLibraryDo selectByPath(@Param("path") String path);

    /**
     * 重命名
     *
     * @param fileLibraryDo 文件数据
     * @return 影响行数
     */
    int rename(FileLibraryDo fileLibraryDo);

    /**
     * 删除
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);
}

