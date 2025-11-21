package cn.yang.nebula.agent.business.authentication.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.yang.nebula.agent.business.authentication.facade.AuthorizationFacade;
import cn.yang.nebula.agent.business.user.entity.User;
import cn.yang.nebula.agent.business.user.enums.AccountNonLockedEnum;
import cn.yang.nebula.agent.business.user.enums.EnabledEnum;
import cn.yang.nebula.agent.business.user.facade.UserFacade;
import cn.yang.nebula.agent.enums.ErrorStatusCodeEnum;
import cn.yang.common.data.structure.exception.BusinessException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 授权业务层
 *
 * @author : 未见清海
 */
@Service
public class AuthorizationService implements AuthorizationFacade {

    @Resource
    private UserFacade userFacade;

    /**
     * 根据用户id获取授权用户ID
     *
     * @param userId 用户id
     * @return 授权用户ID
     */
    @Override
    public String getAuthorizedIdentity(String userId) {
        // 校验用户是否正常
        User user = userFacade.selectById(userId);
        if (!EnabledEnum.ENABLE.getEnabled().equals(user.getEnabled())) {
            throw new BusinessException(ErrorStatusCodeEnum.AUTHENTICATION_ERROR, "用户已禁用,登录失败");
        }
        if (!AccountNonLockedEnum.ENABLE.getEnabled().equals(user.getAccountNonLocked())) {
            throw new BusinessException(ErrorStatusCodeEnum.AUTHENTICATION_ERROR, "用户已锁定,请稍后再试");
        }
        // 直接返回用户ID用于授权
        return userId;
    }

    /**
     * 授权根据用户ID
     *
     * @param userId 用户ID
     * @return 授权标识
     */
    @Override
    public String authorizationByIdentityId(String userId) {
        StpUtil.login(userId);
        return StpUtil.getTokenValue();
    }
}
