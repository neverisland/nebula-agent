package cn.yang.nebula.agent.business.file.space.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 个人空间选择
 *
 * @author : QingHai
 */
@Data
public class FileSpaceSelectVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -5402107150589072743L;

    /**
     * ID
     */
    private String id;

    /**
     * 名称
     */
    private String name;
}
