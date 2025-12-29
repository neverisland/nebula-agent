package cn.yang.nebula.agent.business.authentication.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 当前登录身份角色信息
 *
 * @author : QingHai
 */
@Data
public class CurrentUserRoleDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 9101656876639917189L;

    /**
     * 角色id
     */
    private String id;

    /**
     * 角色标识
     */
    private String mark;

    /**
     * 角色名称
     */
    private String name;

}
