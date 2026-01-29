package cn.yang.nebula.agent.integrate.file.service;

import cn.yang.nebula.agent.exception.CallException;
import cn.yang.nebula.agent.integrate.file.facade.FileIntegrateFacade;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author : QingHai
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileIntegrateService implements FileIntegrateFacade {

    /**
     * 桶名称
     */
    @Value("${file.oss.bucket:y-dev}")
    private String bucket;

    private final OSS ossClient;

    /**
     * 文件上传
     *
     * @param fileKey 文件唯一标识
     * @param file    文件
     */
    @Override
    public void upload(String fileKey, MultipartFile file) throws CallException {
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileKey, file.getInputStream());
            PutObjectResult result = ossClient.putObject(putObjectRequest);
//            log.info("OSS文件上传成功,fileKey:[{}],响应:[{}]", fileKey, JsonUtils.objectToString(result));
            return;
        } catch (IOException ie) {
            log.error("文件上传失败", ie);
        } catch (OSSException oe) {
            log.error("阿里云OSS处理异常,Request ID:[{}], Error Message:[{}], Error Code:[{}], Host ID:[{}],",
                    oe.getRequestId(), oe.getErrorMessage(), oe.getErrorCode(), oe.getHostId(), oe);
        }
        throw new CallException("文件上传异常.");
    }

    /**
     * 文件上传
     *
     * @param fileKey 文件唯一标识
     * @param file    文件
     */
    @Override
    public void upload(String fileKey, File file) throws CallException {
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileKey, file);
            PutObjectResult result = ossClient.putObject(putObjectRequest);
//            log.info("OSS文件上传成功,fileKey:[{}],响应:[{}]", fileKey, JsonUtils.objectToString(result));
//        } catch (IOException ie) {
//            log.error("文件上传失败", ie);
        } catch (OSSException oe) {
            log.error("阿里云OSS处理异常,Request ID:[{}], Error Message:[{}], Error Code:[{}], Host ID:[{}],",
                    oe.getRequestId(), oe.getErrorMessage(), oe.getErrorCode(), oe.getHostId(), oe);
            throw new CallException("文件上传异常.");
        }
    }

    /**
     * 获取文件
     *
     * @param fileKey 文件唯一标识
     * @return 文件流
     */
    @Override
    public InputStream obtainFile(String fileKey) throws CallException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, fileKey);
        try {
            return ossClient.getObject(getObjectRequest).getObjectContent();
        } catch (OSSException oe) {
            log.error("阿里云OSS处理异常,Request ID:[{}], Error Message:[{}], Error Code:[{}], Host ID:[{}],",
                    oe.getRequestId(), oe.getErrorMessage(), oe.getErrorCode(), oe.getHostId(), oe);
            throw new CallException("文件获取异常.");
        }
    }

    /**
     * 删除文件
     *
     * @param fileKeys 文件路径
     */
    @Override
    public void deleteFile(List<String> fileKeys) throws CallException {
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket);
        deleteObjectsRequest.setKeys(fileKeys);
        try {
            ossClient.deleteObjects(deleteObjectsRequest);
        } catch (OSSException oe) {
            log.error("阿里云OSS处理异常,Request ID:[{}], Error Message:[{}], Error Code:[{}], Host ID:[{}],",
                    oe.getRequestId(), oe.getErrorMessage(), oe.getErrorCode(), oe.getHostId(), oe);
            throw new CallException("文件删除异常.");
        }
    }
}