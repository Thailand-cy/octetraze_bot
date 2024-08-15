package io.tonme.minebot.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Data
@Accessors(chain = true)
public class LoginEntityRep {

    private Integer code;
    private String message;
    private Object callbackEnded;
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private Integer uid;
        private String token;
        private String nikeName;
    }
}
