package com.yeffcc.blog.strategy.context;

import com.yeffcc.blog.dto.UserInfoDTO;
import com.yeffcc.blog.enums.LoginTypeEnum;
import com.yeffcc.blog.strategy.SocialLoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 第三方登录策略上下文
 *
 * @author xoke
 * @date 2022/9/12
 */
@Service
public class SocialLoginStrategyContext {

    private Map<String, SocialLoginStrategy> socialLoginStrategyMap = new HashMap<>();

    /**
     * 执行第三方登录策略
     *
     * @param data          数据
     * @param loginTypeEnum 登入枚举类型
     * @return 用户信息
     */
    public UserInfoDTO executeLoginStrategy(String data, LoginTypeEnum loginTypeEnum) {
        return socialLoginStrategyMap.get(loginTypeEnum.getStrategy()).login(data);
    }

}
