package io.tonme.minebot.pojo;

import com.mybatisflex.core.util.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import io.tonme.minebot.enums.HttpsCodeEnum;
import io.tonme.minebot.util.ValidateUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class LoginEntityReq implements BaseReq{
    @Schema(description = "tg ID")
    private String tgId;
    @Schema(description = "是否VIP")
    private Boolean vip;
    @Schema(description = "tg 名称")
    private String tgName;
    @Schema(description = "邀请码")
    private String inviteCode;
    @Schema(description = "时间")
    private long timestamp;

    @Override
    public void validator() {
        ValidateUtil.thrown(StringUtil.isBlank(tgId), "tgId: ", HttpsCodeEnum.PARAMS_ERROR);
    }
}
