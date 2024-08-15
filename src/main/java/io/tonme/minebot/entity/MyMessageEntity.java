package io.tonme.minebot.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MyMessageEntity {
    private String messageUnique;
    private long autoCloseMillSeconds;
    private long lastMessage;
    private Object message;
    private long chatId;
    private int messageId;
}
