package cn.yang.nebula.agent.business.file.space.entity;

import cn.yang.common.data.structure.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 个人空间实体
 *
 * @author : 未见清海
 */
@Data
public class FileSpace extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -5906804370175451655L;

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
}
