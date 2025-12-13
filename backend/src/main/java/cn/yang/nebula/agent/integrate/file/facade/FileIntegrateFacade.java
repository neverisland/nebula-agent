package cn.yang.nebula.agent.integrate.file.facade;

import cn.yang.nebula.agent.exception.CallException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author : QingHai
 */
public interface FileIntegrateFacade {

    /**
     * 文件上传
     *
     * @param fileKey 文件唯一标识
     * @param file    文件
     */
    void upload(String fileKey, MultipartFile file) throws CallException;

    /**
     * 文件上传
     *
     * @param fileKey 文件唯一标识
     * @param file    文件
     */
    void upload(String fileKey, File file) throws CallException;

    /**
     * 获取文件
     *
     * @param fileKey 文件唯一标识
     * @return 文件流
     */
    InputStream obtainFile(String fileKey) throws CallException;

    /**
     * 删除文件
     *
     * @param fileKeys 文件路径
     */
    void deleteFile(List<String> fileKeys) throws CallException;
}
