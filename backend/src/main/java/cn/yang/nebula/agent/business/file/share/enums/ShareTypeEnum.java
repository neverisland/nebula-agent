package cn.yang.nebula.agent.business.file.share.enums;

import lombok.Getter;

/**
 * 分享类型枚举
 *
 * @author 未见清海
 */
@Getter
public enum ShareTypeEnum {

    /**
     * 个人文件
     */
    FILE(1, "个人文件"),

    /**
     * 个人空间
     */
    SPACE(2, "个人空间");

    /**
     * 分享文件类型
     */
    private final Integer type;

    /**
     * 分享文件描述
     */
    private final String description;

    ShareTypeEnum(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

    /**
     * 判断是否存在
     *
     * @param shareType 分享类型
     * @return 是否存在
     */
    public static boolean isExists(Integer shareType) {
        for (ShareTypeEnum shareTypeEnum : values()) {
            if (shareTypeEnum.getType().equals(shareType)) {
                return true;
            }
        }
        return false;
    }
}
