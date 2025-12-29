package cn.yang.nebula.agent.enums;

import lombok.Getter;

/**
 * 自定义的缓存空间定义
 *
 * @author : QingHai
 */
@Getter
public enum CacheSpaceEnum {

    SLIDING_VERIFICATION_CODE("sliding-verification-code", "滑动验证码"),

    FILE_SHARE_VISIT_COUNT("file-share:visit-count:", "文件分享访问次数(锁)"),

    FILE_SHARE_DOWNLOAD_COUNT("file-share:download-count:", "文件分享下载次数(锁)"),

    FILE_SHARE_EXPIRE_TASK("file-share:expire-task-lock", "文件分享过期任务(锁)"),
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

}
