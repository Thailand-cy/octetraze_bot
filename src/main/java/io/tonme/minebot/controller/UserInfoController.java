package io.tonme.minebot.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.tonme.minebot.pojo.UserTelegramInfoRep;
import io.tonme.minebot.service.IUserService;
import io.tonme.minebot.util.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户信息控制器")
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Resource
    private IUserService userService;

    @Tag(name = "根据ID获取用户信息")
    @GetMapping("/tg/{userId}")
    public Result<UserTelegramInfoRep> getUserTelegramInfo(@PathVariable("userId") long userId){
        return Result.success(userService.userTelegramInfo(userId));
    }

    @Tag(name = "根据ID获取用户头像")
    @GetMapping("/header/{userId}")
    public Result getUserTelegramHeader(@PathVariable("userId") long userId){
        userService.userTelegramHeader(userId);
        return Result.success("success");
    }


}
