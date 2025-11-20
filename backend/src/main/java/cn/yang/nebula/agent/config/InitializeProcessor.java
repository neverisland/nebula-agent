package cn.yang.nebula.agent.config;

import cn.yang.nebula.agent.business.user.facade.PermissionFacade;
import cn.yang.nebula.agent.business.user.facade.RoleFacade;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * 初始化处理器
 *
 * @author : 未见清海
 */
@Configuration
public class InitializeProcessor {

    private final static Logger logger = LoggerFactory.getLogger(InitializeProcessor.class);

    @Resource
    private PermissionFacade permissionFacade;

    @Resource
    private RoleFacade roleFacade;

    @PostConstruct
    public void init() {
        logger.info("系统启动初始化...");

        // 初始化角色权限
        permissionFacade.init();
        roleFacade.init();

        logger.info("系统启动初始化完成...");
    }
}
