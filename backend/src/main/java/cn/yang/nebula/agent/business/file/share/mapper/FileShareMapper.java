package cn.yang.nebula.agent.business.file.share.mapper;

import cn.yang.nebula.agent.business.file.share.dal.FileShareDo;
import cn.yang.nebula.agent.business.file.share.po.FileSharePageQueryPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分享记录 Mapper
 *
 * @author 未见清海
 */
@Mapper
public interface FileShareMapper {

    /**
     * 新增分享记录
     *
     * @param fileShareDo 分享记录
     * @return 影响行数
     */
    int insert(FileShareDo fileShareDo);

    /**
     * 更新分享记录
     *
     * @param fileShareDo 分享记录
     * @return 影响行数
     */
    int update(FileShareDo fileShareDo);

    /**
     * 根据ID删除分享记录
     *
     * @param id 分享ID
     * @return 影响行数
     */
    int deleteById(@Param("id") String id);

    /**
     * 根据ID批量删除分享记录
     *
     * @param ids ID列表
     * @return 影响行数
     */
    int deleteBatchIds(@Param("ids") List<String> ids);

    /**
     * 根据ID查询分享记录
     *
     * @param id 分享ID
     * @return 分享记录
     */
    FileShareDo selectById(@Param("id") String id);

    /**
     * 分页查询数据
     *
     * @param query 查询条件
     * @return 数据列表
     */
    List<FileShareDo> selectPageData(@Param("query") FileSharePageQueryPo query);

    /**
     * 增加访问次数
     *
     * @param id 分享ID
     * @return 影响行数
     */
    int incrementVisitCount(@Param("id") String id);

    /**
     * 增加下载次数
     *
     * @param id 分享ID
     * @return 影响行数
     */
    int incrementDownloadCount(@Param("id") String id);

    /**
     * 查询需要标记为过期的分享记录
     * 条件：启用了过期时间、过期时间已到、状态未标记为过期
     *
     * @param currentDate 当前日期
     * @return 需要标记过期的分享ID列表
     */
    List<String> selectExpiredShareIds(@Param("currentDate") java.time.LocalDate currentDate);

    /**
     * 批量更新分享过期状态
     *
     * @param ids 分享ID列表
     * @return 影响行数
     */
    int batchUpdateExpiredStatus(@Param("ids") List<String> ids);
}
