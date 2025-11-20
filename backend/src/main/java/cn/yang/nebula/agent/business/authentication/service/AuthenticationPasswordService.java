package cn.yang.nebula.agent.business.authentication.service;

import cn.yang.nebula.agent.business.authentication.dto.PasswordLoginDto;
import cn.yang.nebula.agent.business.authentication.dto.ResetPasswordDto;
import cn.yang.nebula.agent.business.authentication.entity.AuthenticationPassword;
import cn.yang.nebula.agent.business.authentication.enums.PasswordTypeEnums;
import cn.yang.nebula.agent.business.authentication.facade.AuthenticationPasswordFacade;
import cn.yang.nebula.agent.business.authentication.facade.AuthorizationFacade;
import cn.yang.nebula.agent.business.authentication.repository.AuthenticationPasswordRepository;
import cn.yang.nebula.agent.enums.ErrorStatusCodeEnum;
import cn.yang.nebula.agent.business.user.entity.User;
import cn.yang.nebula.agent.business.user.facade.UserFacade;
import cn.yang.common.data.structure.exception.BusinessException;
import cn.yang.common.data.structure.exception.NullDataException;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author : 未见清海
 */
@Service
public class AuthenticationPasswordService implements AuthenticationPasswordFacade {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthenticationPasswordRepository authenticationPasswordRepository;

    @Resource
    private UserFacade userFacade;

    @Resource
    private AuthorizationFacade authorizationFacade;

    /**
     * 账号密码登录,获取可以认证的身份
     *
     * @param passwordLoginDto 账号密码信息
     * @return 身份id
     */
    @Override
    public String passwordLogin(PasswordLoginDto passwordLoginDto) {
        // 获取用户数据
        User user;
        try {
            user = userFacade.selectByUsernameOrPhone(passwordLoginDto.getUsername());
        } catch (NullDataException e) {
            throw new BusinessException(ErrorStatusCodeEnum.DATA_DOES_NOT_EXIST, "用户名或密码错误");
        }

        try {
            AuthenticationPassword AuthenticationPassword = authenticationPasswordRepository.selectByUserId(user.getId());
            if (passwordEncoder.matches(passwordLoginDto.getPassword(), AuthenticationPassword.getPassword())) {
                // 根据用户id获取可以授权的身份
                return authorizationFacade.getAuthorizedIdentity(user.getId());
            }
            // todo 多次失败锁定逻辑
            throw new BusinessException(ErrorStatusCodeEnum.AUTHENTICATION_ERROR, "用户名或密码错误");
        } catch (NullDataException e) {
            throw new BusinessException(ErrorStatusCodeEnum.ABNORMAL_OPERATION, "请更换其他方式登录.");
        }
    }

    /**
     * 重置密码，如果用户无密码会进行新增用户密码
     *
     * @param resetPasswordDto 重置密码入参
     */
    @Override
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        // 校验用户是否存在
        userFacade.selectById(resetPasswordDto.getUserId());
        // 获取密码数据
        try {
            AuthenticationPassword authenticationPassword = authenticationPasswordRepository.selectByUserId(resetPasswordDto.getUserId());
            // 修改密码认证数据
            authenticationPassword.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
            authenticationPassword.setPasswordType(PasswordTypeEnums.INITIAL_PASSWORD.getType());

            authenticationPasswordRepository.updateData(authenticationPassword);
        } catch (NullDataException e) {
            // 新增密码认证方式
            AuthenticationPassword authenticationPassword = new AuthenticationPassword();
            authenticationPassword.setUserId(resetPasswordDto.getUserId());
            authenticationPassword.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
            authenticationPassword.setPasswordType(PasswordTypeEnums.INITIAL_PASSWORD.getType());
            authenticationPasswordRepository.insertData(authenticationPassword);
        }
    }
}
