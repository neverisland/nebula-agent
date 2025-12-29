package cn.yang.nebula.agent.enums;

import cn.yang.common.data.structure.vo.result.ResultCodeInterface;

/**
 * 异常返回状态枚举
 *
 * @author : QingHai
 */
public enum ErrorStatusCodeEnum implements ResultCodeInterface {

    PARAMETER_VERIFICATION_EXCEPTION(1, "参数校验异常"),

    DATA_CANNOT_BE_UPDATED(2, "数据不允许修改"),

    ABNORMAL_OPERATION(3, "操作异常"),

    DATA_CANNOT_BE_INSERT(4, "数据不允许新增"),

    DATA_DOES_NOT_EXIST(5, "数据不存在"),

    AUTHENTICATION_ERROR(6, "认证失败"),

    SLIDE_CAPTCHA_CHECK_ERROR(7, "滑动验证校验失败"),

    PERMISSION_ERROR(8, "权限校验异常"),

    NOT_LOGIN_EXCEPTION(9, "用户未登录"),

    MODEL_CALL_EXCEPTION(10, "模型调用失败"),

    SYSTEM_BUSY(11, "系统繁忙");


    /**
     * 返回状态码
     */
    private final Integer statusCode;

    /**
     * 描述
     */
    private final String description;

    ErrorStatusCodeEnum(Integer statusCode, String description) {
        this.statusCode = statusCode;
        this.description = description;
    }

    @Override
    public Integer getCode() {
        return statusCode;
    }

    @Override
    public String getCodeMsg() {
        return description;
    }
}
