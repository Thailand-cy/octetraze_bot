package io.tonme.minebot.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserSharingRep {

    private Integer code;
    private String message;
    private Object callbackEnded;
    private String data;
}
