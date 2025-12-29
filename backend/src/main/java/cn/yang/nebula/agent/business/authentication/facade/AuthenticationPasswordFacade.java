package cn.yang.nebula.agent.business.authentication.facade;

import cn.yang.nebula.agent.business.authentication.dto.PasswordLoginDto;
import cn.yang.nebula.agent.business.authentication.dto.ResetPasswordDto;

/**
 * 账号密码认证业务接口
 *
 * @author : QingHai
 */
public interface AuthenticationPasswordFacade {

    /**
     * 账号密码登录,获取可以认证的身份
     *
     * @param passwordLoginDto 账号密码信息
     * @return 身份id
     */
    String passwordLogin(PasswordLoginDto passwordLoginDto);

    /**
     * 重置密码，如果用户无密码会进行新增用户密码
     *
     * @param resetPasswordDto 重置密码入参
     */
    void resetPassword(ResetPasswordDto resetPasswordDto);
}
