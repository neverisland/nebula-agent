package cn.yang.nebula.agent.business.authentication.utils;

import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author : QingHai
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    @Getter
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) {
        SpringContextUtil.context = applicationContext;
    }
}