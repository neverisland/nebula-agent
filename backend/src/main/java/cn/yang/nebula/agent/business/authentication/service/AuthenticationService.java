package cn.yang.nebula.agent.business.authentication.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.yang.nebula.agent.business.authentication.entity.UserInfo;
import cn.yang.nebula.agent.business.authentication.facade.AuthenticationFacade;
import cn.yang.nebula.agent.business.user.entity.User;
import cn.yang.nebula.agent.business.user.facade.UserFacade;
import cn.yang.nebula.agent.utils.bean.BeanConvertUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 认证业务
 *
 * @author : QingHai
 */
@Service
public class AuthenticationService implements AuthenticationFacade {

    @Resource
    private UserFacade userFacade;


    /**
     * 根据用户id获取可进行的认证方式
     *
     * @param userId 用户id
     * @return 用户可进行的认证方式列表
     */
    @Override
    public List<Integer> selectAuthenticationMethod(String userId) {
        // todo

        return List.of();
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 当前登录用户信息
     */
    @Override
    public UserInfo getCurrentUserInfo() {
        // 获取当前登录用户ID
        String userId = getCurrentUserId();
        // 获取用户信息数据
        User user = userFacade.selectById(userId);
        // 组装当前用户信息数据
        UserInfo userInfo = BeanConvertUtils.convert(user, UserInfo.class);
        userInfo.setUserId(userId);
        return userInfo;
    }

    /**
     * 获取当前登录用户ID
     *
     * @return 用户ID
     */
    @Override
    public String getCurrentUserId() {
        return String.valueOf(StpUtil.getLoginId());
    }

    /**
     * 校验当前用户是否已经认证
     *
     * @return 是否认证
     */
    @Override
    public Boolean verifyWhetherAuthenticated() {
        return StpUtil.isLogin();
    }
}
