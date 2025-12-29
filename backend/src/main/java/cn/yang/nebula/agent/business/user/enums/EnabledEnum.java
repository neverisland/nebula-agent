package cn.yang.nebula.agent.business.user.enums;

import lombok.Getter;

/**
 * 是否禁用枚举
 *
 * @author : QingHai
 */
@Getter
public enum EnabledEnum {

    ENABLE(Boolean.TRUE, "正常"),

    DISABLE(Boolean.FALSE, "禁用"),
    ;

    /**
     * 锁定状态
     */
    private final Boolean enabled;

    /**
     * 描述
     */
    private final String description;

    EnabledEnum(Boolean enabled, String description) {
        this.enabled = enabled;
        this.description = description;
    }

}
