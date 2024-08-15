package io.tonme.minebot.service;

import io.tonme.minebot.pojo.LoginEntityRep;
import io.tonme.minebot.pojo.LoginEntityReq;
import io.tonme.minebot.pojo.UserTelegramInfoRep;

public interface IUserService {
    LoginEntityRep userLogin(LoginEntityReq loginEntityReq);

    UserTelegramInfoRep userTelegramInfo(long userId);

    UserTelegramInfoRep cacheUserTelegramInfo(UserTelegramInfoRep userTelegramInfoRep);

    String getUserSharingCode(long userId);

    void userTelegramHeader(long userId);
}
