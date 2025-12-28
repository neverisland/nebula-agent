package cn.yang.nebula.agent.business.file.library.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 文件库统计 VO
 *
 * @author 未见清海
 */
@Data
@Builder
public class FileLibraryStatisticsVo {

    /**
     * 文件数量
     */
    private Integer fileCount;

    /**
     * 存储大小（字节）
     */
    private Long storageUsed;
}
