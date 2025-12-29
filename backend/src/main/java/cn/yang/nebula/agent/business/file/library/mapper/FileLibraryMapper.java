package cn.yang.nebula.agent.business.file.library.mapper;

import cn.yang.nebula.agent.business.file.library.dal.FileLibraryDo;
import cn.yang.nebula.agent.business.file.library.dto.FileLibraryPageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文件库 Mapper
 *
 * @author QingHai
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
     * 批量更新空间id
     *
     * @param spaceId      空间id
     * @param fileIds      文件id列表
     * @param updateUserId 修改人id
     * @return 影响行数
     */
    int updateSpaceId(@Param("spaceId") String spaceId, @Param("fileIds") List<String> fileIds, @Param("updateUserId") String updateUserId);

    /**
     * 删除
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

    /**
     * 清除文件的空间id（设置为null）
     *
     * @param fileIds      文件id列表
     * @param updateUserId 修改人id
     * @return 影响行数
     */
    int clearSpaceId(@Param("fileIds") List<String> fileIds, @Param("updateUserId") String updateUserId);

    /**
     * 统计用户文件数量
     *
     * @param userId 用户id
     * @return 文件数量
     */
    Integer countByUserId(@Param("userId") String userId);

    /**
     * 统计用户总存储大小
     *
     * @param userId 用户id
     * @return 总存储大小（字节）
     */
    Long sumSizeByUserId(@Param("userId") String userId);

    /**
     * 根据id列表查询
     *
     * @param ids id列表
     * @return 文件列表
     */
    List<FileLibraryDo> selectByIds(@Param("ids") List<String> ids);

    /**
     * 根据空间ID和用户ID查询文件列表
     *
     * @param spaceId 空间ID
     * @param userId 用户ID
     * @return 文件列表
     */
    List<FileLibraryDo> selectBySpaceIdAndUserId(@Param("spaceId") String spaceId, @Param("userId") String userId);
}

