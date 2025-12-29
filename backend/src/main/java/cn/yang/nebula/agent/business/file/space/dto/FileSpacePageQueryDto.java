package cn.yang.nebula.agent.business.file.space.dto;

import cn.yang.common.data.structure.vo.page.PageQuery;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 个人空间分页查询入参
 *
 * @author : QingHai
 */
@Data
public class FileSpacePageQueryDto extends PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = -4219454573549526453L;

    /**
     * 个人空间名称(模糊)
     */
    private String name;

    /**
     * 创建人id (后端自动注入，前端无需传递)
     */
    private String createUserId;
}
