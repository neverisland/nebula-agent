package cn.yang.nebula.agent.business.file.space.dal;

import cn.yang.nebula.agent.vo.entity.BaseEntity;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 个人空间 DO
 *
 * @author : QingHai
 */
@Data
public class FileSpaceDo extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -8735603167529246877L;

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
}
