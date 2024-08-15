package io.tonme.minebot.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;

/**
 * BOT 菜单表 实体类。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Accessors(chain = true)
@Data
@Schema(name = "BOT 菜单表")
@Table(value = "bot_menu_commands")
public class BotMenuCommandsEntity {

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 指令
     */
    @Schema(description = "指令")
    @Column(value = "command")
    private String command;

    /**
     * 描述
     */
    @Schema(description = "描述")
    @Column(value = "description")
    private String description;

    /**
     * 顺序
     */
    @Schema(description = "顺序")
    @Column(value = "menu_order")
    private String menuOrder;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @Column(value = "createTime")
    private java.util.Date createtime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @Column(value = "updateTime")
    private java.util.Date updatetime;


}
