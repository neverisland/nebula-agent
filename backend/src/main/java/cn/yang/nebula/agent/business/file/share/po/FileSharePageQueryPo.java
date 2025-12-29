package cn.yang.nebula.agent.business.file.share.po;

import cn.yang.common.data.structure.vo.page.PageQuery;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分享分页查询入参
 *
 * @author QingHai
 */
@Data
public class FileSharePageQueryPo extends PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = -8755211235447772969L;

    /**
     * 搜索关键字 (名称)
     */
    private String name;

    /**
     * 分享类型: 1-个人文件, 2-个人空间
     */
    private Integer shareType;

    /**
     * 是否已过期 (null:全部, true:已过期, false:未过期)
     */
    private Boolean isExpired;

    /**
     * 当前用户ID
     */
    private String userId;
}
