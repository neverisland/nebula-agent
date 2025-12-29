package cn.yang.nebula.agent.business.user.controller;

import cn.yang.nebula.agent.business.user.dto.user.*;
import cn.yang.nebula.agent.enums.ErrorStatusCodeEnum;
import cn.yang.nebula.agent.business.user.dto.role.RoleDto;
import cn.yang.nebula.agent.business.user.dto.user.*;
import cn.yang.nebula.agent.business.user.entity.User;
import cn.yang.nebula.agent.business.user.enums.RoleEnum;
import cn.yang.nebula.agent.business.user.facade.RoleFacade;
import cn.yang.nebula.agent.business.user.facade.UserFacade;
import cn.yang.common.data.structure.enums.StatusCodeEnum;
import cn.yang.common.data.structure.exception.BusinessException;
import cn.yang.common.data.structure.utils.bean.BeanConvertUtils;
import cn.yang.common.data.structure.vo.page.PageResult;
import cn.yang.common.data.structure.vo.result.ResultFactory;
import cn.yang.common.data.structure.vo.result.ResultVo;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户身份接口
 *
 * @author : QingHai
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserFacade userFacade;

    @Resource
    private RoleFacade roleFacade;

    /**
     * 分页查询用户数据
     *
     * @param userPageQuery 分页查询入参
     * @return 用户列表数据
     */
    @PostMapping("/page")
    public ResultVo<PageResult<UserPageDto>> pageData(@RequestBody UserPageQuery userPageQuery) {
        if (userPageQuery == null || userPageQuery.getCurrent() == null || userPageQuery.getSize() == null) {
            throw new BusinessException(ErrorStatusCodeEnum.PARAMETER_VERIFICATION_EXCEPTION, "分页查询数据不能为空");
        }
        PageResult<UserPageDto> pageResult = userFacade.pageData(userPageQuery);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "success", pageResult);
    }

    /**
     * 新增用户数据
     */
    @PostMapping("/insert")
    @Transactional(rollbackFor = Exception.class, noRollbackFor = BusinessException.class)
    public ResultVo<String> insertData(@Validated @RequestBody UserInsertDto userInsertDto) {
        // 新增用户
        String userId = userFacade.insertUser(userInsertDto);
        
        // 分配角色
        AssignRoleDto assignRoleDto = new AssignRoleDto();
        assignRoleDto.setUserId(userId);
        assignRoleDto.setRoleIdList(userInsertDto.getRoleIdList());
        userFacade.assignRole(assignRoleDto);
        
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "新增成功", userId);
    }

    /**
     * 修改用户数据
     *
     * @param userUpdateDto 修改用户数据
     * @return 是否成功响应
     */
    @PostMapping("/update")
    @Transactional(rollbackFor = Exception.class, noRollbackFor = BusinessException.class)
    public ResultVo<?> updateDate(@Validated @RequestBody UserUpdateDto userUpdateDto) {
        // 更新用户基本信息
        userFacade.updateUser(userUpdateDto);
        
        // 更新用户角色
        AssignRoleDto assignRoleDto = new AssignRoleDto();
        assignRoleDto.setUserId(userUpdateDto.getId());
        assignRoleDto.setRoleIdList(userUpdateDto.getRoleIdList());
        userFacade.assignRole(assignRoleDto);
        
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "修改成功", null);
    }

    /**
     * 根据用户id获取用户详情
     *
     * @param id 用户id
     * @return 用户详情
     */
    @GetMapping("/selectById")
    public ResultVo<UserDto> selectById(@RequestParam("id") String id) {
        // 获取用户数据
        User user = userFacade.selectById(id);
        UserDto userDto = BeanConvertUtils.convert(user, UserDto.class);
        
        // 转换角色列表
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            List<RoleDto> roleDtos = BeanConvertUtils.convert(user.getRoles(), RoleDto.class);
            userDto.setRoles(roleDtos);
        }
        
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "success", userDto);
    }

    /**
     * 禁用/启用用户
     */
    @PostMapping("/disableEnableUser")
    public ResultVo<?> disableEnableUser(@Validated @RequestBody DisableEnableUserDto disableEnableUserDto) {
        userFacade.disableEnableUser(disableEnableUserDto);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "success");
    }

    /**
     * 根据用户id删除用户
     *
     * @param id 用户id
     * @return 请求响应
     */
    @GetMapping("/deleteById")
    @Transactional(rollbackFor = Exception.class, noRollbackFor = BusinessException.class)
    public ResultVo<?> deleteById(@RequestParam("id") String id) {
        // 删除用户数据
        userFacade.deleteById(id);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "success");
    }

    /**
     * 分配角色
     *
     * @param assignRoleDto 分配角色入参
     */
    @PostMapping("/assignRole")
    public ResultVo<?> assignRole(@Validated @RequestBody AssignRoleDto assignRoleDto) {
        userFacade.assignRole(assignRoleDto);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "分配成功");
    }
}
