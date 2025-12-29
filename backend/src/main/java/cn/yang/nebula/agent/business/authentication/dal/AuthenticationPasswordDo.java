package cn.yang.nebula.agent.business.authentication.dal;

import cn.yang.nebula.agent.business.authentication.enums.PasswordTypeEnums;
import cn.yang.common.data.structure.entity.BaseTimeEntity;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户密码认证
 *
 * @author QingHai
 */
@Data
public class AuthenticationPasswordDo extends BaseTimeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -64796605351566625L;

    /**
     * 用户密码认证id
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码类型，0-初始密码 1-自定义密码
     * <br>
     * 参见 {@link PasswordTypeEnums}
     */
    private Integer passwordType;

}
