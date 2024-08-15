package io.tonme.minebot.controller;

import com.mybatisflex.core.paginate.Page;
import io.tonme.minebot.component.MyAmazingBot;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import io.tonme.minebot.service.BotMenuCommandsService;
import io.tonme.minebot.entity.BotMenuCommandsEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * BOT 菜单表 控制层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@RestController
@RequestMapping("/botMenuCommands")
@Tag(name = "BOT 菜单表控制层")
public class BotMenuCommandsController {

    @Autowired
    private BotMenuCommandsService botMenuCommandsService;
    @Resource
    private MyAmazingBot myAmazingBot;

    /**
     * 添加 BOT 菜单表
     *
     * @param botMenuCommands BOT 菜单表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @Operation(summary = "添加BOT 菜单表")
    @Parameters(value = {
            @Parameter(name = "id", description = ""),

            @Parameter(name = "command", description = "指令"),

            @Parameter(name = "description", description = "描述"),

            @Parameter(name = "createtime", description = "创建时间"),

            @Parameter(name = "updatetime", description = "更新时间")
    })
    public boolean save(@RequestBody BotMenuCommandsEntity botMenuCommands) {
        return botMenuCommandsService.save(botMenuCommands);
    }


    /**
     * 根据主键删除BOT 菜单表
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    @Operation(summary = "根据主键删除BOT 菜单表")
    @Parameters(value = {
            @Parameter(name = "id", description = "", required = true)
    })
    public boolean remove(@PathVariable Serializable id) {
        return botMenuCommandsService.removeById(id);
    }


    /**
     * 根据主键更新BOT 菜单表
     *
     * @param botMenuCommands BOT 菜单表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    @Operation(summary = "根据主键更新BOT 菜单表")
    @Parameters(value = {
            @Parameter(name = "id", description = "", required = true),

            @Parameter(name = "command", description = "指令"),

            @Parameter(name = "description", description = "描述"),

            @Parameter(name = "createtime", description = "创建时间"),

            @Parameter(name = "updatetime", description = "更新时间")
    })
    public boolean update(@RequestBody BotMenuCommandsEntity botMenuCommands) {
        return botMenuCommandsService.updateById(botMenuCommands);
    }


    /**
     * 查询所有BOT 菜单表
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有BOT 菜单表")
    public List<BotMenuCommandsEntity> list() {
        return botMenuCommandsService.list();
    }


    /**
     * 根据BOT 菜单表主键获取详细信息。
     *
     * @param id botMenuCommands主键
     * @return BOT 菜单表详情
     */
    @GetMapping("/getInfo/{id}")
    @Operation(summary = "根据BOT 菜单表主键获取详细信息")
    @Parameters(value = {
            @Parameter(name = "id", description = "", required = true)
    })
    public BotMenuCommandsEntity getInfo(@PathVariable Serializable id) {
        return botMenuCommandsService.getById(id);
    }


    /**
     * 分页查询BOT 菜单表
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询BOT 菜单表")
    @Parameters(value = {
            @Parameter(name = "pageNumber", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页大小", required = true)
    })
    public Page<BotMenuCommandsEntity> page(Page<BotMenuCommandsEntity> page) {
        return botMenuCommandsService.page(page);
    }

}