package cn.yang.nebula.agent.business.filelibrary.service;

import cn.yang.common.data.structure.exception.BusinessException;
import cn.yang.nebula.agent.business.filelibrary.config.FileLibraryOssProperties;
import cn.yang.nebula.agent.enums.ErrorStatusCodeEnum;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 文件库 OSS 操作
 *
 * @author 未见清海
 */
@Service
public class FileLibraryOssService {

    @Resource
    private FileLibraryOssProperties ossProperties;


    /**
     * 删除对象
     *
     * @param objectKey 相对路径
     */
    public void delete(String objectKey) {
        OSS ossClient = buildClient();
        try {
            ossClient.deleteObject(ossProperties.getBucket(), buildFullKey(objectKey));
        } catch (Exception e) {
            throw new BusinessException(ErrorStatusCodeEnum.ABNORMAL_OPERATION, "文件删除失败：" + e.getMessage());
        } finally {
            shutdown(ossClient);
        }
    }


    private OSS buildClient() {
        validateProps();
        return new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
    }

    private String buildFullKey(String objectKey) {
        String basePath = ossProperties.getBasePath();
        if (!StringUtils.hasText(basePath)) {
            return objectKey;
        }
        if (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }
        if (objectKey.startsWith("/")) {
            objectKey = objectKey.substring(1);
        }
        return basePath + "/" + objectKey;
    }

    private void validateProps() {
        if (!StringUtils.hasText(ossProperties.getEndpoint())
                || !StringUtils.hasText(ossProperties.getBucket())
                || !StringUtils.hasText(ossProperties.getAccessKeyId())
                || !StringUtils.hasText(ossProperties.getAccessKeySecret())) {
            throw new BusinessException(ErrorStatusCodeEnum.ABNORMAL_OPERATION, "OSS 配置缺失，请检查 file-library.oss 配置");
        }
    }

    private void shutdown(OSS ossClient) {
        if (ossClient != null) {
            try {
                ossClient.shutdown();
            } catch (Exception ignored) {
            }
        }
    }
}

