package cn.yang.nebula.agent.business.user.mapper;

import cn.yang.nebula.agent.business.user.dal.UserRoleDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关联 Mapper
 *
 * @author QingHai
 */
@Mapper
public interface UserRoleMapper {
    
    /**
     * 根据用户ID列表查询用户角色关联
     *
     * @param userIds 用户ID列表
     * @return 用户角色关联列表
     */
    List<UserRoleDo> selectByUserIds(@Param("userIds") List<String> userIds);
    
    /**
     * 根据用户ID查询用户角色关联
     *
     * @param userId 用户ID
     * @return 用户角色关联列表
     */
    List<UserRoleDo> selectByUserId(@Param("userId") String userId);
    
    /**
     * 根据用户ID删除用户角色关联
     *
     * @param userId 用户ID
     * @return 删除的记录数
     */
    int deleteByUserId(@Param("userId") String userId);
    
    /**
     * 新增用户角色关联
     *
     * @param userRoleDo 用户角色关联
     * @return 插入的记录数
     */
    int insert(UserRoleDo userRoleDo);
    
    /**
     * 批量新增用户角色关联
     *
     * @param list 用户角色关联列表
     * @return 插入的记录数
     */
    int batchInsert(@Param("list") List<UserRoleDo> list);
}
