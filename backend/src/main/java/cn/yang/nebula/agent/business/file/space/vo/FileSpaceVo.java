package cn.yang.nebula.agent.business.file.space.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 个人空间 VO
 *
 * @author : 未见清海
 */
@Data
public class FileSpaceVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -7667666649635233276L;
    
    /**
     * 个人空间id
     */
    private String id;

    /**
     * 个人空间名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 文件数量
     */
    private Integer fileCount;

    /**
     * 空间大小
     */
    private Long totalSize;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
