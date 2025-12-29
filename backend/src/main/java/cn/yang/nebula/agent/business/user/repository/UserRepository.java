package cn.yang.nebula.agent.business.user.repository;

import cn.yang.nebula.agent.business.user.dal.UserDo;
import cn.yang.nebula.agent.business.user.dal.UserRoleDo;
import cn.yang.nebula.agent.business.user.dto.role.RoleDto;
import cn.yang.nebula.agent.business.user.dto.user.UserPageDto;
import cn.yang.nebula.agent.business.user.dto.user.UserPageQuery;
import cn.yang.nebula.agent.business.user.entity.Role;
import cn.yang.nebula.agent.business.user.entity.User;
import cn.yang.nebula.agent.business.user.mapper.UserMapper;
import cn.yang.nebula.agent.business.user.mapper.UserRoleMapper;
import cn.yang.common.data.structure.annotation.assignment.BaseDataAssignment;
import cn.yang.common.data.structure.annotation.assignment.DataOperationTypeEnum;
import cn.yang.common.data.structure.exception.NullDataException;
import cn.yang.common.data.structure.utils.bean.BeanConvertUtils;
import cn.yang.common.data.structure.vo.page.PageResult;
import cn.yang.foundational.capability.id.generator.IdGenerator;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户 仓储层
 *
 * @author QingHai
 */
@Component
public class UserRepository {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleRepository roleRepository;

    @Resource
    private IdGenerator idGenerator;

    /**
     * 分页查询数据
     *
     * @param userPageQuery 用户查询信息
     * @return 分页返回数据
     */
    public PageResult<UserPageDto> pageData(UserPageQuery userPageQuery) {
        Integer count = userMapper.selectPageCount(userPageQuery);

        if (count > 0) {
            List<UserDo> userDoList = userMapper.selectPageData(userPageQuery);
            List<UserPageDto> userPageDtoList = BeanConvertUtils.convert(userDoList, UserPageDto.class);
            // 填充角色信息
            fillRoles(userPageDtoList);
            return new PageResult<>(userPageQuery, userPageDtoList, count);
        } else {
            return new PageResult<>(userPageQuery);
        }
    }

    /**
     * 填充角色信息到用户分页DTO列表
     *
     * @param userPageDtoList 用户分页DTO列表
     */
    private void fillRoles(List<UserPageDto> userPageDtoList) {
        if (CollectionUtils.isEmpty(userPageDtoList)) {
            return;
        }
        List<String> userIds = userPageDtoList.stream().map(UserPageDto::getId).collect(Collectors.toList());
        List<UserRoleDo> userRoleDos = userRoleMapper.selectByUserIds(userIds);

        if (CollectionUtils.isEmpty(userRoleDos)) {
            return;
        }

        List<String> roleIds = userRoleDos.stream()
                .map(UserRoleDo::getRoleId)
                .distinct()
                .collect(Collectors.toList());
        List<Role> roles = roleRepository.selectByIds(roleIds);
        
        // Map<RoleId, Role>
        Map<String, Role> roleMap = roles.stream()
                .collect(Collectors.toMap(Role::getId, r -> r));
        
        // Map<UserId, List<Role>>
        Map<String, List<Role>> userRoleMap = new HashMap<>();
        for (UserRoleDo userRoleDo : userRoleDos) {
            String userId = userRoleDo.getUserId();
            String roleId = userRoleDo.getRoleId();
            Role role = roleMap.get(roleId);
            if (role != null) {
                userRoleMap.computeIfAbsent(userId, k -> new ArrayList<>()).add(role);
            }
        }
        
        // 填充到DTO
        for (UserPageDto dto : userPageDtoList) {
            List<Role> userRoles = userRoleMap.get(dto.getId());
            if (!CollectionUtils.isEmpty(userRoles)) {
                List<RoleDto> roleDtos = BeanConvertUtils.convert(userRoles, RoleDto.class);
                dto.setRoles(roleDtos);
            }
        }
    }

    /**
     * 根据用户名获取用户数据
     *
     * @param username 用户名
     * @return 用户数据
     */
    @Cacheable(cacheNames = "user-username", key = "#username")
    public User selectByUsername(String username) throws NullDataException {
        UserDo userDo = userMapper.selectByUsername(username);
        if (userDo == null) {
            throw new NullDataException("用户数据不存在");
        }
        return BeanConvertUtils.convert(userDo, User.class);
    }

    /**
     * 根据手机号获取用户数据
     *
     * @param phone 手机号
     * @return 用户数据
     */
    @Cacheable(cacheNames = "user-phone", key = "#phone")
    public User selectByPhone(String phone) throws NullDataException {
        UserDo userDo = userMapper.selectByPhone(phone);
        if (userDo == null) {
            throw new NullDataException("用户数据不存在");
        }
        return BeanConvertUtils.convert(userDo, User.class);
    }

    /**
     * 新增用户数据
     *
     * @param user 用户实体
     * @return 用户id
     */
    @BaseDataAssignment
    public String insertData(User user) {
        UserDo userDo = BeanConvertUtils.convert(user, UserDo.class);
        userDo.setId(idGenerator.getId());
        userMapper.insert(userDo);
        return userDo.getId();
    }

    /**
     * 根据用户id获取用户数据
     *
     * @param id 用户id
     * @return 用户详情数据
     */
    public User selectById(String id) throws NullDataException {
        UserDo userDo = userMapper.selectById(id);
        if (userDo == null) {
            throw new NullDataException("用户不存在");
        }
        User user = BeanConvertUtils.convert(userDo, User.class);
        
        // 填充角色
        List<UserRoleDo> userRoleDos = userRoleMapper.selectByUserId(id);
        if (!CollectionUtils.isEmpty(userRoleDos)) {
            List<String> roleIds = userRoleDos.stream()
                    .map(UserRoleDo::getRoleId)
                    .collect(Collectors.toList());
            List<Role> roles = roleRepository.selectByIds(roleIds);
            user.setRoles(roles);
        }
        return user;
    }

    /**
     * 分配角色
     *
     * @param userId     用户id
     * @param roleIdList 角色id列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignRole(String userId, List<String> roleIdList) {
        // 删除旧的关联
        userRoleMapper.deleteByUserId(userId);

        // 新增新的关联
        if (CollectionUtils.isEmpty(roleIdList)) {
            return;
        }
        
        List<UserRoleDo> userRoleDos = new ArrayList<>();
        for (String roleId : roleIdList) {
            UserRoleDo userRoleDo = new UserRoleDo();
            userRoleDo.setId(idGenerator.getId());
            userRoleDo.setUserId(userId);
            userRoleDo.setRoleId(roleId);
            userRoleDos.add(userRoleDo);
        }
        userRoleMapper.batchInsert(userRoleDos);
    }

    /**
     * 修改用户数据
     *
     * @param updateUser 待修改的用户数据
     */
    @BaseDataAssignment(DataOperationTypeEnum.UPDATE)
    public void updateData(User updateUser) {
        UserDo userDo = BeanConvertUtils.convert(updateUser, UserDo.class);
        userMapper.updateById(userDo);
    }

    /**
     * 禁用启用用户数据
     *
     * @param user 待修改禁用状态的用户
     */
    @BaseDataAssignment(DataOperationTypeEnum.UPDATE)
    public void disableEnableUser(User user) {
        UserDo userDo = BeanConvertUtils.convert(user, UserDo.class);
        userMapper.disableEnableUser(userDo);
    }

    /**
     * 根据用户名 / 手机号 获取用户数据
     *
     * @param username 用户名 / 手机号
     * @return 用户数据
     */
    public User selectByUsernameOrPhone(String username) throws NullDataException {
        UserDo userDo = userMapper.selectByUsernameOrPhone(username);
        if (userDo == null) {
            throw new NullDataException("用户不存在");
        }
        return BeanConvertUtils.convert(userDo, User.class);
    }

    /**
     * 根据用户id删除用户数据
     *
     * @param id 用户id
     */
    public int deleteById(String id) {
        return userMapper.deleteById(id);
    }
}
