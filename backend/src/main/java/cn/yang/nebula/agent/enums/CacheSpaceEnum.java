package cn.yang.nebula.agent.enums;

/**
 * 自定义的缓存空间定义
 *
 * @author : 未见清海
 */
public enum CacheSpaceEnum {

    SLIDING_VERIFICATION_CODE("sliding-verification-code", "滑动验证码"),
    ;

    /**
     *
     */
    private final String mark;

    /**
     * 描述
     */
    private final String description;

    CacheSpaceEnum(String mark, String description) {
        this.mark = mark;
        this.description = description;
    }

    public String getMark() {
        return mark;
    }

    public String getDescription() {
        return description;
    }
}
