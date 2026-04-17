package com.mock.example.interfaces.controller.system;

import com.mock.example.common.annotation.RepeatSubmit;
import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.body.system.config.ConfigBody;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.interfaces.vo.system.config.ConfigVo;
import com.mock.example.modules.system.service.SysConfigService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 参数配置
 *
 * @author: Mock
 * @date: 2025-01-05 08:44:11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {

    private final SysConfigService configService;

    /**
     * 获取参数配置列表
     *
     * @param configBody 配置信息
     * @return 配置列表
     */
    @ApiOperation(value = "获取参数配置列表")
    @GetMapping("/list")
    public TableDataInfo list(ConfigBody configBody) {
        startPage();
        return getDataTable(configService.selectConfigList(configBody));
    }

    /**
     * 根据键名查询参数值
     *
     * @param configKey 配置key
     * @return 参数配置
     */
    @ApiOperation(value = "根据键名查询参数值")
    @GetMapping(value = "/configKey/{configKey}")
    public Response<String> getConfigKey(@PathVariable String configKey) {
        return new Response<>(configService.selectConfigByKey(configKey));
    }

    /**
     * 根据参数编号获取详细信息
     *
     * @param configId 配置id
     * @return 配置信息
     */
    @ApiOperation(value = "根据参数编号获取详细信息")
    @GetMapping(value = "/{configId}")
    public Response<ConfigVo> getInfo(@PathVariable Integer configId) {
        return new Response<>(configService.selectConfigById(configId));
    }

    /**
     * 新增参数配置
     *
     * @param configBody 配置信息
     * @return 结果
     */
    @ApiOperation(value = "新增参数配置")
    @PostMapping
    @RepeatSubmit
    public Response<Boolean> add(@RequestBody ConfigBody configBody) {
        return configService.addConfig(configBody);
    }

    /**
     * 修改参数配置
     *
     * @param configBody 配置信息
     * @return 结果
     */
    @ApiOperation(value = "修改参数配置")
    @PutMapping
    public Response<Boolean> edit( @RequestBody ConfigBody configBody) {
        return configService.editConfig(configBody);
    }

    /**
     * 删除参数配置
     *
     * @param configIds 配置ID列表
     * @return 结果
     */
    @ApiOperation(value = "删除参数配置")
    @DeleteMapping("/{configIds}")
    public Response<Boolean> remove(@PathVariable Long[] configIds) {
        configService.deleteConfigs(configIds);
        return configService.deleteConfigs(configIds);
    }

}

  