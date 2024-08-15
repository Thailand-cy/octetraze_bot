package io.tonme.minebot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author bd
 * @project hot-fi-server
 * @description TODO Http状态码
 * @date 2023/8/15 16:07:43
 */
@Getter
@AllArgsConstructor
public enum ParseModeEnum {
    MARKDOWN("Markdown"),HTML("Html");
    private String mode;

}
