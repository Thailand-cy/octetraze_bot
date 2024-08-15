package io.tonme.minebot.controller;

import com.alibaba.fastjson2.JSONObject;
import com.mybatisflex.core.util.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.tonme.minebot.component.MyAmazingBot;
import io.tonme.minebot.entity.InlineKeyboardButtonEntity;
import io.tonme.minebot.enums.GameConfigEnum;
import io.tonme.minebot.enums.RedisKeysEnum;
import io.tonme.minebot.handler.BotMessageHandler;
import io.tonme.minebot.pojo.BotBroadcastReq;
import io.tonme.minebot.util.RedisUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/botUserBroadcast")
@Tag(name = "BOT 用户信息推送控制器")
public class BotUserBroadcastController {
    @Resource
    private BotMessageHandler botMessageHandler;

    @Operation(summary = "向指定用户发送广播")
    @PostMapping("/sendUser/{userId}")
    public void broadcastUserMessage(@PathVariable("userId") long userId, @RequestBody BotBroadcastReq botBroadcastReq){
        List<List<InlineKeyboardButtonEntity>> buttonList = null;
        if(botBroadcastReq.isPlay()){
            buttonList = botMessageHandler.createPlayButton();
        } else if(botBroadcastReq.isBack()){
            buttonList = botMessageHandler.createBackButton();
        } else if(botBroadcastReq.getButtonList() != null && !botBroadcastReq.getButtonList().isEmpty()){
            buttonList = botBroadcastReq.getButtonList();
        }
        InlineKeyboardMarkup buttons = null;
        if(buttonList != null)
            buttons = botMessageHandler.createButtons(buttonList);
        long chatId = userId;
        if(botBroadcastReq.getMediaUrls() == null || botBroadcastReq.getMediaUrls().isEmpty()){
            botMessageHandler.sendMessageWithButtons4Broadcast(chatId, botBroadcastReq.getMessage(), botBroadcastReq.getMode(), buttons);
        } else {
            if(botBroadcastReq.getMediaUrls().size() == 1){
                botMessageHandler.sendPhotoMessageWithButtons4Broadcast(chatId, botBroadcastReq.getMessage(), botBroadcastReq.getMediaUrls().get(0), buttons);
            } else {
                botMessageHandler.sendMediaGroup4BroadCast(chatId, botBroadcastReq.getMessage(), botBroadcastReq.getMediaUrls(), buttons);
            }
        }
    }

    @Operation(summary = "向所有用户发送广播")
    @PostMapping("/sendUserAll")
    public void broadcastAllUserMessage(@RequestBody BotBroadcastReq botBroadcastReq){
        List<List<InlineKeyboardButtonEntity>> buttonList = null;
        if(botBroadcastReq.isPlay()){
            buttonList = botMessageHandler.createPlayButton();
        } else if(botBroadcastReq.isBack()){
            buttonList = botMessageHandler.createBackButton();
        } else if(botBroadcastReq.getButtonList() != null && !botBroadcastReq.getButtonList().isEmpty()){
            buttonList = botBroadcastReq.getButtonList();
        }
        InlineKeyboardMarkup buttons = null;
        if(buttonList != null)
            buttons = botMessageHandler.createButtons(buttonList);

        Map<Object, Object> chatIds = RedisUtils.hashGetAll("{tonme}:auth:user:info");
        Set<Object> keySet = chatIds.keySet();
        for(Object key : keySet){
            if(key != null){
                Object value = chatIds.get(key);
                JSONObject jsonObject = JSONObject.parseObject(value.toString());
                if(jsonObject.isEmpty() || jsonObject.get("tgId") == null || StringUtil.isBlank(jsonObject.getString("tgId"))){
                    continue;
                }
                try{
                    value = Long.parseLong(jsonObject.getString("tgId"));
                } catch (Exception e){
                    continue;
                }
                if(value instanceof Long chatId)
                    if(botBroadcastReq.getMediaUrls() == null || botBroadcastReq.getMediaUrls().isEmpty()){
                        botMessageHandler.sendMessageWithButtons4Broadcast(chatId, botBroadcastReq.getMessage(), botBroadcastReq.getMode(), buttons);
                    } else {
                        if(botBroadcastReq.getMediaUrls().size() == 1){
                            botMessageHandler.sendPhotoMessageWithButtons4Broadcast(chatId, botBroadcastReq.getMessage(), botBroadcastReq.getMediaUrls().get(0), buttons);
                        } else {
                            botMessageHandler.sendMediaGroup4BroadCast(chatId, botBroadcastReq.getMessage(), botBroadcastReq.getMediaUrls(), buttons);
                        }
                    }
            }
        }
    }

    @Operation(summary = "向指定用户发送游戏信息")
    @PostMapping("/sendUserGameInfo/{userId}")
    public void broadcastGameInfoMessage(@PathVariable("userId") long userId){
        botMessageHandler.showGameButtonMessage(userId);
    }
}
