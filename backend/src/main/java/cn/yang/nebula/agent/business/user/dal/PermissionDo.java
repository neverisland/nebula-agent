package cn.yang.nebula.agent.business.user.dal;

import cn.yang.nebula.agent.vo.entity.BaseTimeEntity;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 权限
 *
 * @author : QingHai
 */
@Data
public class PermissionDo extends BaseTimeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -1399888753807019456L;

    /**
     * 权限id
     */
    private String id;

    /**
     * 权限标识
     */
    private String mark;

    /**
     * 权限描述
     */
    private String description;
}
