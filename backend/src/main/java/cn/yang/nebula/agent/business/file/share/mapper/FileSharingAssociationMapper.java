package cn.yang.nebula.agent.business.file.share.mapper;

import cn.yang.nebula.agent.business.file.share.dal.FileSharingAssociationDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分享关联 Mapper
 *
 * @author 未见清海
 */
@Mapper
public interface FileSharingAssociationMapper {

    /**
     * 批量新增关联
     *
     * @param list 关联列表
     * @return 影响行数
     */
    int insertBatch(@Param("list") List<FileSharingAssociationDo> list);

    /**
     * 根据分享ID删除关联
     *
     * @param shareId 分享ID
     * @return 影响行数
     */
    int deleteByShareId(@Param("shareId") String shareId);

    /**
     * 根据分享ID查询关联列表
     *
     * @param shareId 分享ID
     * @return 关联列表
     */
    List<FileSharingAssociationDo> selectByShareId(@Param("shareId") String shareId);
}
