package cn.yang.nebula.agent.business.authentication.repository;

import cn.yang.nebula.agent.business.authentication.mapper.AuthenticationHistoricalPasswordMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;


/**
 * 用户历史密码 仓储层
 *
 * @author QingHai
 */
@Repository
public class AuthenticationHistoricalPasswordRepository {

    @Resource
    private AuthenticationHistoricalPasswordMapper AuthenticationHistoricalPasswordMapper;

    public void insertData() {

    }

}




