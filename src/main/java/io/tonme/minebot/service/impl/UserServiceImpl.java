package io.tonme.minebot.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.mybatisflex.core.util.StringUtil;
import io.tonme.minebot.component.MyAmazingBot;
import io.tonme.minebot.enums.RedisKeysEnum;
import io.tonme.minebot.handler.ServerHttpHandler;
import io.tonme.minebot.pojo.LoginEntityRep;
import io.tonme.minebot.pojo.LoginEntityReq;
import io.tonme.minebot.pojo.UserTelegramInfoRep;
import io.tonme.minebot.service.IUserService;
import io.tonme.minebot.util.ProfilePhotoUtil;
import io.tonme.minebot.util.RedisUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private ServerHttpHandler httpHandler;
    @Lazy
    @Resource
    private MyAmazingBot myAmazingBot;

    @Override
    public LoginEntityRep userLogin(LoginEntityReq loginEntityReq){
        LoginEntityRep loginEntityRep = httpHandler.userLoginHttp(loginEntityReq);
        if(loginEntityRep != null){
            cacheUserTelegramInfo(new UserTelegramInfoRep()
                    .setUserId(Long.parseLong(loginEntityReq.getTgId()))
                    .setUsername(loginEntityReq.getTgName()));
        }
        return loginEntityRep;
    }

    @Override
    public UserTelegramInfoRep userTelegramInfo(long userId){
        String dataJson = RedisUtils.hashGet(RedisKeysEnum.TELEGRAM_USER_INFO.getKey(), String.valueOf(userId));
        UserTelegramInfoRep userTelegramInfoRep = JSONObject.parseObject(dataJson, UserTelegramInfoRep.class);
        if(userTelegramInfoRep == null)
            return null;
        if(StringUtil.isBlank(userTelegramInfoRep.getProfilePhoto())) {
            String profilePhotoPath = ProfilePhotoUtil.getUserProfilePhoto(myAmazingBot.getTelegramClient(), userId);
            if(StringUtil.isNotBlank(profilePhotoPath)){
                userTelegramInfoRep.setProfilePhoto("https://api.telegram.org/file/bot" + myAmazingBot.getBotToken() + "/" + profilePhotoPath);
                RedisUtils.hashPut(RedisKeysEnum.TELEGRAM_USER_INFO.getKey(),String.valueOf(userId), JSONObject.toJSONString(userTelegramInfoRep));
            }
        }
        return userTelegramInfoRep;
    }

    @Override
    public UserTelegramInfoRep cacheUserTelegramInfo(UserTelegramInfoRep userTelegramInfoRep){
        if(StringUtil.isBlank(userTelegramInfoRep.getProfilePhoto())) {
            String profilePhotoPath = ProfilePhotoUtil.getUserProfilePhoto(myAmazingBot.getTelegramClient(), userTelegramInfoRep.getUserId());
            if(StringUtil.isNotBlank(profilePhotoPath)){
                userTelegramInfoRep.setProfilePhoto("https://api.telegram.org/file/bot" + myAmazingBot.getBotToken() + "/" + profilePhotoPath);
            }
        }
        RedisUtils.hashPut(RedisKeysEnum.TELEGRAM_USER_INFO.getKey(),String.valueOf(userTelegramInfoRep.getUserId()), JSONObject.toJSONString(userTelegramInfoRep));
        return userTelegramInfoRep;
    }

    @Override
    public String getUserSharingCode(long userId){
        return httpHandler.getSharingCode(userId);
    }

    @Override
    public void userTelegramHeader(long userId) {
        String dataJson = RedisUtils.hashGet(RedisKeysEnum.TELEGRAM_USER_INFO.getKey(), String.valueOf(userId));
        UserTelegramInfoRep userTelegramInfoRep = JSONObject.parseObject(dataJson, UserTelegramInfoRep.class);
        if(userTelegramInfoRep == null)
            return;
        String profilePhotoPath = ProfilePhotoUtil.getUserProfilePhoto(myAmazingBot.getTelegramClient(), userId);
        log.info("Get user info for:"+userId+", profile:"+profilePhotoPath);
        if(StringUtil.isNotBlank(profilePhotoPath)){
            userTelegramInfoRep.setProfilePhoto("https://api.telegram.org/file/bot" + myAmazingBot.getBotToken() + "/" + profilePhotoPath);
            log.info("Get user info:"+userTelegramInfoRep);
            RedisUtils.hashPut(RedisKeysEnum.TELEGRAM_USER_INFO.getKey(),String.valueOf(userId), JSONObject.toJSONString(userTelegramInfoRep));
        }
    }
}
