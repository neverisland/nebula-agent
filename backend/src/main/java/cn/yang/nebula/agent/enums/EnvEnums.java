package cn.yang.nebula.agent.enums;

import lombok.Getter;

/**
 * @author : 未见清海
 */
@Getter
public enum EnvEnums {

    CHAT_API_KEY("CHAT_API_KEY", System.getenv("CHAT_API_KEY"), "模型对话Key"),

    WEATHER_API_KEY("WEATHER_API_KEY", System.getenv("WEATHER_API_KEY"), "获取天气Key"),

    ZHI_PU_API_KEY("ZHI_PU_API_KEY", System.getenv("ZHI_PU_API_KEY"), "智普Key")
    ;

    /**
     * 环境变量名称
     */
    private final String key;

    /**
     * 环境变量值
     */
    private final String value;

    /**
     * 描述
     */
    private final String description;

    EnvEnums(String key, String value, String description) {
        this.key = key;
        this.value = value;
        this.description = description;
    }

}
