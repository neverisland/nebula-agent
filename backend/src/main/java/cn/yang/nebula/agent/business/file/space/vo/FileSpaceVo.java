package cn.yang.nebula.agent.business.file.space.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

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
     * 创建时间
     */
    private Date createTime;
}
