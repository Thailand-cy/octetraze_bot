package io.tonme.minebot.handler;

import io.tonme.minebot.component.MyAmazingBot;
import io.tonme.minebot.entity.MyMessageEntity;
import io.tonme.minebot.enums.RedisKeysEnum;
import io.tonme.minebot.util.RedisUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TelegramBotSendhandler {
    @Lazy
    @Resource
    private MyAmazingBot myAmazingBot;

    private static final int MESSAGE_LIMIT_PER_SECOND = 28;  // 每秒最多发送的消息数
    private static final int TELEGRAM_API_RATE_LIMIT_MS = 1000 / MESSAGE_LIMIT_PER_SECOND; // 每次发送消息的间隔时间

    private BlockingQueue<MyMessageEntity> messageQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<MyMessageEntity> messageQueue4BroadCast = new LinkedBlockingQueue<>();
    private volatile boolean running = true;

    public TelegramBotSendhandler() {
        // 启动消息处理线程
        new Thread(this::processQueue).start();
    }

    // 将消息添加到队列
    public void sendMessage(MyMessageEntity message) {
        messageQueue.offer(message);
    }

    // 将消息添加到队列
    public void sendMessage4Broadcast(MyMessageEntity message) {
        messageQueue4BroadCast.offer(message);
    }

    // 处理消息队列
    private void processQueue() {
        while (running) {
            try {
                // 从队列中获取消息，若无消息则等待
                MyMessageEntity myMessageEntity = messageQueue.poll(TELEGRAM_API_RATE_LIMIT_MS, TimeUnit.MILLISECONDS);
                if (myMessageEntity != null) {
                    sendToTelegramApi(myMessageEntity);
                    // 控制发送频率
                    Thread.sleep(TELEGRAM_API_RATE_LIMIT_MS);
                    log.info("Send message queue size:【 user:"+messageQueue.size()+" 】【 task:"+messageQueue4BroadCast.size()+" 】==> USER ID:"+myMessageEntity.getChatId());
                } else {
                    myMessageEntity = messageQueue4BroadCast.poll(TELEGRAM_API_RATE_LIMIT_MS, TimeUnit.MILLISECONDS);
                    if (myMessageEntity != null) {
                        sendToTelegramApi(myMessageEntity);
                        // 控制发送频率
                        Thread.sleep(TELEGRAM_API_RATE_LIMIT_MS);
                        log.info("Send message queue size:【 user:"+messageQueue.size()+" 】【 task:"+messageQueue4BroadCast.size()+" 】==> TASK ID:"+myMessageEntity.getChatId());
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e){
                log.error(e.getMessage());
            }
        }
    }

    // 停止消息处理线程
    public void stop() {
        running = false;
    }

    // 向 Telegram API 发送消息的逻辑
    private void sendToTelegramApi(MyMessageEntity myMessageEntity) {
        Object message = myMessageEntity.getMessage();
        try {
            if(message instanceof AnswerInlineQuery msg){
                myAmazingBot.getTelegramClient().executeAsync(msg);
            }
            if(message instanceof SendPhoto msg){
                Integer messageId = myAmazingBot.getTelegramClient().executeAsync(msg).get().getMessageId();
                myMessageEntity.setMessageId(messageId);
                sendAfter(myMessageEntity);
            }
            if(message instanceof SendMessage msg){
                Integer messageId = myAmazingBot.getTelegramClient().executeAsync(msg).get().getMessageId();
                myMessageEntity.setMessageId(messageId);
                sendAfter(myMessageEntity);
            }
            if(message instanceof SendGame msg){
                Integer messageId = myAmazingBot.getTelegramClient().executeAsync(msg).get().getMessageId();
                myMessageEntity.setMessageId(messageId);
                sendAfter(myMessageEntity);
            }
            if(message instanceof SendMediaGroup msg){
                myAmazingBot.getTelegramClient().executeAsync(msg);
            }
            if(message instanceof SendVideo msg){
                Integer messageId = myAmazingBot.getTelegramClient().executeAsync(msg).get().getMessageId();
                myMessageEntity.setMessageId(messageId);
                sendAfter(myMessageEntity);
            }
            if(message instanceof SendAudio msg){
                Integer messageId = myAmazingBot.getTelegramClient().executeAsync(msg).get().getMessageId();
                myMessageEntity.setMessageId(messageId);
                sendAfter(myMessageEntity);
            }
            if(message instanceof SendMediaGroup msg){
                myAmazingBot.getTelegramClient().executeAsync(msg).get();
            }
            if(message instanceof AnswerCallbackQuery msg){
                myAmazingBot.getTelegramClient().execute(msg);
            }
        } catch (Exception ignored) {}
    }

    private void sendAfter(MyMessageEntity myMessageEntity){
        RedisUtils.setValueTimeout(RedisKeysEnum.TELEGRAM_CACHE_MESSAGE_IDS.getKey() + myMessageEntity.getMessageUnique(), myMessageEntity.getLastMessage(),600);
        if(myMessageEntity.getAutoCloseMillSeconds() >= 0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(myMessageEntity.getAutoCloseMillSeconds());
                        DeleteMessage deleteMessage = DeleteMessage.builder()
                                .messageId(myMessageEntity.getMessageId())
                                .chatId(myMessageEntity.getChatId())
                                .build();
                        myAmazingBot.getTelegramClient().execute(deleteMessage);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }
}
