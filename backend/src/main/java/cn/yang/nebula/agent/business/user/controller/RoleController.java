package cn.yang.nebula.agent.business.user.controller;

import cn.yang.nebula.agent.business.user.dto.role.*;
import cn.yang.nebula.agent.enums.ErrorStatusCodeEnum;
import cn.yang.nebula.agent.business.user.dto.role.*;
import cn.yang.nebula.agent.business.user.facade.RoleFacade;
import cn.yang.common.data.structure.enums.StatusCodeEnum;
import cn.yang.common.data.structure.exception.BusinessException;
import cn.yang.common.data.structure.vo.page.PageResult;
import cn.yang.common.data.structure.vo.result.ResultFactory;
import cn.yang.common.data.structure.vo.result.ResultVo;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限接口
 *
 * @author : QingHai
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleFacade roleFacade;


    /**
     * 角色分页查询
     *
     * @param rolePageQuery 角色分页查询入参
     * @return 分页返回数据
     */
    @PostMapping("/page")
    public ResultVo<PageResult<RolePageDto>> pageData(@RequestBody RolePageQuery rolePageQuery) {
        if (rolePageQuery.getCurrent() == null || rolePageQuery.getSize() == null) {
            throw new BusinessException(ErrorStatusCodeEnum.PARAMETER_VERIFICATION_EXCEPTION, "分页查询数据不能为空");
        }

        PageResult<RolePageDto> rolePageDto = roleFacade.pageData(rolePageQuery);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, rolePageDto);
    }

    /**
     * 新增自定义角色
     *
     * @param roleInsertDto 自定义角色入参
     * @return 返回响应
     */
    @PostMapping("/insert")
    public ResultVo<String> insertData(@Validated @RequestBody RoleInsertDto roleInsertDto) {
        String roleId = roleFacade.insertData(roleInsertDto);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "success", roleId);
    }

    /**
     * 修改自定义角色
     *
     * @param roleUpdateDto 修改的自定义角色入参
     * @return 返回响应
     */
    @PostMapping("/update")
    public ResultVo<String> updateData(@Validated @RequestBody RoleUpdateDto roleUpdateDto) {
        roleFacade.updateData(roleUpdateDto);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "success", "修改角色成功");
    }

    /**
     * 获取角色详情数据
     *
     * @param id 角色id
     * @return 角色详情
     */
    @GetMapping("/selectById")
    public ResultVo<RoleDto> selectById(@RequestParam("id") String id) {
        RoleDto roleDto = roleFacade.selectById(id);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "success", roleDto);
    }

    /**
     * 获取所有角色列表
     *
     * @return 角色列表
     */
    @GetMapping("/selectAll")
    public ResultVo<List<RoleDto>> selectAll() {
        List<RoleDto> roleDtoList = roleFacade.selectAll();
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "success", roleDtoList);
    }

    /**
     * 根据角色id删除自定义角色
     *
     * @param id 角色id
     * @return 请求响应
     */
    @GetMapping("/deleteById")
    public ResultVo<?> deleteById(@RequestParam("id") String id) {
        roleFacade.deleteById(id);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "success");
    }
}
