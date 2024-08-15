package io.tonme.minebot.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class InlineKeyboardButtonEntity {
    private String text;
    private String callbackData;
}
