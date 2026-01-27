package cn.yang.nebula.agent.integrate.file.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * 阿里云oss配置
 *
 * @author : QingHai
 */
@Configuration
public class OssConfig {

    private final static Logger logger = LoggerFactory.getLogger(OssConfig.class);

    /**
     * OSS 访问端点
     */
    @Value("${file.oss.endpoint:https://oss-cn-hangzhou.aliyuncs.com}")
    private String endpoint;

    @Bean
    public OSS ossClient() {
        logger.info("阿里云OSS访问端口endpoint:" + endpoint);
        String accessKeyId = System.getenv("ALI_YUN_ACCESS_KEY_ID");
        String secretAccessKey = System.getenv("ALI_YUN_ACCESS_SECRET_KEY");
        if (!StringUtils.hasLength(accessKeyId) || !StringUtils.hasLength(secretAccessKey)) {
            throw new RuntimeException("请设置环境变量 ALI_YUN_ACCESS_KEY_ID 和 ALI_YUN_ACCESS_SECRET_KEY");
        }
        return new OSSClientBuilder().build(endpoint, accessKeyId, secretAccessKey);
    }
}
