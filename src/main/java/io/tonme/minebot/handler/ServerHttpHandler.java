package io.tonme.minebot.handler;

import io.tonme.minebot.component.HttpClient;
import io.tonme.minebot.conts.ServiceHttpApi;
import io.tonme.minebot.pojo.LoginEntityRep;
import io.tonme.minebot.pojo.LoginEntityReq;
import io.tonme.minebot.pojo.UserSharingRep;
import io.tonme.minebot.util.EncryptUtil;
import io.tonme.minebot.util.Result;
import io.tonme.minebot.util.ResultUtil;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ServerHttpHandler {
    @Resource
    private HttpClient httpClient;

    public LoginEntityRep userLoginHttp(LoginEntityReq loginEntityReq){
        loginEntityReq.validator();
        loginEntityReq.setTimestamp(new Date().getTime());
        Map<String, String> params = new HashMap<>();
        params.put("sign", EncryptUtil.encrypt(loginEntityReq));
        ResponseEntity<LoginEntityRep> responseEntity = httpClient.post(ServiceHttpApi.SERVICE_LOGIN_AND_REGIST_URL2, params, LoginEntityRep.class);
        return ResultUtil.check(responseEntity);
    }

    public String getSharingCode(long userId){
        ResponseEntity<UserSharingRep> responseEntity = httpClient.get(ServiceHttpApi.SERVICE_GET_SHARING+"?tgId="+userId, null, UserSharingRep.class);
        UserSharingRep userSharingRep = ResultUtil.check(responseEntity);
        return userSharingRep.getData();
    }
}
