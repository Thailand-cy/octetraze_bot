package io.tonme.minebot.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.tonme.minebot.component.MyAmazingBot;
import io.tonme.minebot.util.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

@Tag(name = "用户群组控制器")
@RestController
@RequestMapping("/botUserGroup")
public class BotUserGroupController {
    @Resource
    private MyAmazingBot myAmazingBot;

    @Tag(name = "判断用户在不在群组中")
    @GetMapping("/check/{chatId}/{userId}")
    public Result<Boolean> checkUserGroup(@PathVariable("chatId") String chatId, @PathVariable("userId") long userId){
        try{
            GetChatMember getChatMember = GetChatMember
                    .builder()
                    .chatId(chatId)
                    .userId(userId)
                    .build();
            getChatMember.setChatId(chatId);
            getChatMember.setUserId(userId);
            ChatMember chatMember = myAmazingBot.getTelegramClient().execute(getChatMember);
            String status = chatMember.getStatus();
            boolean isMember = !status.equals("left") && !status.equals("kicked");
            return Result.success(isMember);
        } catch (Exception e){
            return Result.success(false);
        }
    }
}
