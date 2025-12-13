package cn.yang.nebula.agent.utils.image;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author : QingHai
 */
public class ImageUtils {

    /**
     * 将 MultipartFile 图片压缩后生成临时 File
     *
     * @param multipartFile 上传的文件
     * @param quality       压缩质量，0~1 之间，例如 0.8f
     * @return 压缩后的 File
     * @throws IOException 异常
     */
    public static File compressImage(MultipartFile multipartFile, float quality) throws IOException {
        // 校验是否是图片
        String contentType = multipartFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("文件不是图片类型: " + contentType);
        }

        // 读取 MultipartFile 为 BufferedImage
        BufferedImage image = ImageIO.read(multipartFile.getInputStream());
        if (image == null) {
            throw new IllegalArgumentException("无法读取图片文件");
        }

        // 创建临时文件
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = ".jpg"; // 默认后缀
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        File tempFile = File.createTempFile("compress_", suffix);
        tempFile.deleteOnExit(); // JVM 退出自动删除

        // 压缩并写入临时文件
        Thumbnails.of(image)
                .scale(1.0)         // 保持原尺寸
                .outputQuality(quality) // 压缩质量
                .toFile(tempFile);

        return tempFile;
    }
}
