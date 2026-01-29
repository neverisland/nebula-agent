package cn.yang.nebula.agent.business.authentication.service;

import cn.yang.nebula.agent.aop.assignment.CurrentlyUserFacade;
import cn.yang.nebula.agent.business.authentication.facade.AuthenticationFacade;
import cn.yang.nebula.agent.exception.NotLoginException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 获取当前登录用户ID
 *
 * @author : QingHai
 */
@Service
public class CurrentlyUserService implements CurrentlyUserFacade {

    @Resource
    private AuthenticationFacade authenticationFacade;

    /**
     * 获取当前登录用户ID
     *
     * @return 用户ID
     * @throws NotLoginException 用户未登录异常
     */
    @Override
    public String getCurrentlyUserId() throws NotLoginException {
        if (RequestContextHolder.getRequestAttributes() == null) {
            throw new NotLoginException("用户未登录");
        }

        if (!authenticationFacade.verifyWhetherAuthenticated()) {
            throw new NotLoginException("用户未登录");
        }
        return authenticationFacade.getCurrentUserId();
    }

}
