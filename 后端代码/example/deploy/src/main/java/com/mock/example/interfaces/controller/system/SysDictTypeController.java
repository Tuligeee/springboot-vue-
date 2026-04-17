package com.mock.example.interfaces.controller.system;

import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.body.system.dict.DictTypeBody;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.interfaces.vo.system.dict.DictTypeVo;
import com.mock.example.modules.system.service.SysDictTypeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 数字字典类型
 *
 * @author: Mock
 * @date: 2025-01-05 08:19:24
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/dict/type")
public class SysDictTypeController extends BaseController {

    private final SysDictTypeService dictTypeService;

    /**
     * 获取字典列表
     *
     * @param dictTypeBody 字典类型请求对象
     * @return 字典列表
     */
    @ApiOperation(value = "获取字典列表")
    @GetMapping("/list")
    public TableDataInfo list(DictTypeBody dictTypeBody) {
        startPage();
        return getDataTable(
                dictTypeService.selectDictTypeList(dictTypeBody)
        );
    }

    /**
     * 通过字典id查询字典詳情
     *
     * @param dictId 字典id
     * @return 字典类型
     */
    @ApiOperation(value = "通过字典id查询字典詳情")
    @GetMapping(value = "/{dictId}")
    public Response<DictTypeVo> getInfo(@PathVariable Long dictId) {
        return new Response<>(dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 新增字典类型
     *
     * @param dictTypeBody 字典类型请求对象
     * @return 结果
     */
    @ApiOperation(value = "新增字典类型")
    @PostMapping
    public Response<Boolean> add(@RequestBody DictTypeBody dictTypeBody) {
        return dictTypeService.addDictType(dictTypeBody);
    }

    /**
     * 修改字典类型
     *
     * @param dictTypeBody 字典类型请求对象
     * @return 结果
     */
    @ApiOperation(value = "修改字典类型")
    @PutMapping
    public Response<Boolean> edit(@RequestBody DictTypeBody dictTypeBody) {
        return dictTypeService.editDictType(dictTypeBody);
    }

    /**
     * 删除字典类型
     *
     * @param dictIds 字典id
     * @return 结果
     */
    @ApiOperation(value = "删除字典类型")
    @DeleteMapping("/{dictIds}")
    public Response<Boolean> remove(@PathVariable Long[] dictIds) {
        return dictTypeService.deleteDictTypeByIds(dictIds);
    }

    /**
     * 刷新字典缓存
     *
     * @return 结果
     */
    @ApiOperation(value = "刷新字典缓存")
    @DeleteMapping("/refreshCache")
    public Response refreshCache()
    {
        dictTypeService.resetDictCache();
        return new Response().ok();
    }

}

  