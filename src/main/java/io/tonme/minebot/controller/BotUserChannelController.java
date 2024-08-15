package io.tonme.minebot.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.tonme.minebot.component.MyAmazingBot;
import io.tonme.minebot.util.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Tag(name = "用户频道控制器")
@RestController
@RequestMapping("/botUserChannel")
public class BotUserChannelController {
    @Resource
    private MyAmazingBot myAmazingBot;

    @Tag(name = "判断用户在不在频道中")
    @GetMapping("/check/{channel}/{userId}")
    public Result<Boolean> checkUserChannel(@PathVariable("channel") String chatId, @PathVariable("userId") long userId){
        GetChatMember getChatMember = GetChatMember.builder()
                .chatId(chatId)
                .userId(userId)
                .build();
        try {
            ChatMember chatMember = myAmazingBot.getTelegramClient().execute(getChatMember);
            String status = chatMember.getStatus();
            // 检查用户状态
            boolean isMember = status.equals("member") || status.equals("administrator") || status.equals("creator");
            return Result.success(isMember);
        } catch (TelegramApiException ignored) {}
        return Result.success(false);
    }

    @Tag(name = "获取频道ID")
    @GetMapping("/getChannelId/{channelName}")
    public Result<String> getChannelId(@PathVariable String channelName){
        GetChat getChat = GetChat.builder().chatId(channelName).build();
        try {
            Chat chat = myAmazingBot.getTelegramClient().execute(getChat);
            Long channelId = chat.getId();
            System.out.println("Channel ID: " + channelId);
            return Result.success(String.valueOf(channelId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return Result.success(null);
    }
}
