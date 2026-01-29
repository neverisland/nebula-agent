package cn.yang.nebula.agent.business.authentication.controller;

import cn.yang.nebula.agent.business.authentication.facade.AuthorizationFacade;
import cn.yang.nebula.agent.enums.ErrorStatusCodeEnum;
import cn.yang.nebula.agent.enums.StatusCodeEnum;
import cn.yang.nebula.agent.exception.BusinessException;
import cn.yang.nebula.agent.vo.result.ResultFactory;
import cn.yang.nebula.agent.vo.result.ResultVo;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录测试
 *
 * @author : QingHai
 */
@RestController
@RequestMapping("/authentication")
public class TestAuthorizationController {


    @Resource
    private AuthorizationFacade authorizationFacade;

    /**
     * 测试登录使用
     *
     * @param userId 用户id
     * @return 登录结果
     */
    @GetMapping("/test/login")
    public ResultVo<String> login(@RequestParam("userId") @NotBlank(message = "用户id不能为空") String userId,
                                  @RequestParam("code") @NotBlank(message = "code不能为空") String code) {
        if (!"D143F3C8-83A1-D941-C0D0-05BAB2823668".equals(code)) {
            throw new BusinessException(ErrorStatusCodeEnum.PARAMETER_VERIFICATION_EXCEPTION, "校验异常");
        }
        String token = authorizationFacade.authorizationByIdentityId(userId);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "login success", token);
    }
}
