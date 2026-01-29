package cn.yang.nebula.agent.business.user.dto.role;

import cn.yang.nebula.agent.vo.page.PageQuery;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 角色分页查询入参
 *
 * @author : QingHai
 */
@Data
public class RolePageQuery extends PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 6525054552111338561L;

    /**
     * 搜索文本
     */
    private String searchText;

}
