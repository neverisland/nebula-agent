package cn.yang.nebula.agent.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 权限验证配置
 *
 * @author : QingHai
 */
@Configuration
public class PermissionVerificationConfigure implements WebMvcConfigurer {

    private static final String[] DEVELOPMENT_API = {
            "/authentication/password/login", // 账号密码登录
            "/authentication/verifyWhetherAuthenticated", // 校验 token 是否有效
            "/sliding/verification/code/**", // 滑动验证码（获取/校验）
            "/file/**", // 文件下载/预览（图床核心功能）
            "/file-share/public/**", // 文件分享公开接口（无需鉴权）：/public/info、/public/incrementVisit、/public/verifyPassword、/public/downloadAll
    };

    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> {
            // 校验是否登录
            StpUtil.checkLogin();
        }))
                .addPathPatterns("/**")
                .excludePathPatterns(DEVELOPMENT_API);
    }
}
