package com.mock.example.interfaces.controller.system;

import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.body.system.dict.DictDataBody;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.interfaces.vo.system.dict.DictDataVo;
import com.mock.example.modules.system.entity.model.SysDictData;
import com.mock.example.modules.system.service.SysDictDataService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数字字典信息
 *
 * @author: Mock
 * @date: 2025-01-05 09:13:04
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {

    private final SysDictDataService dictDataService;

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictDataBody 字典数据对象请求体
     * @return 字典类别
     */
    @ApiOperation(value = "根据字典类型查询字典数据信息")
    @GetMapping("/list")
    public TableDataInfo list(DictDataBody dictDataBody) {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictDataBody);
        return getDataTable(list);
    }

    /**
     * 根据字典类型查询字典数据信息
     *
     * @param dictType 字典类型
     * @return 字典列表
     */
    @ApiOperation(value = "根据字典类型查询字典数据信息")
    @GetMapping(value = "/type/{dictType}")
    public Response<List<DictDataVo>> dictType(@PathVariable String dictType) {
        return new Response<>(dictDataService.selectDictDataByType(dictType));
    }

    /**
     * 查询字典数据详细
     *
     * @param dictCode 字典code
     * @return 字典详情
     */
    @ApiOperation(value = "查询字典数据详细")
    @GetMapping(value = "/{dictCode}")
    public Response<DictDataVo> getInfo(@PathVariable Long dictCode) {
        return new Response<>(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 新增字典类型
     *
     * @param dictDataBody 字典请求对象
     * @return 结果
     */
    @ApiOperation(value = "新增字典类型")
    @PostMapping
    public Response<Boolean> add(@RequestBody DictDataBody dictDataBody) {
        return new Response<>(dictDataService.insertDictData(dictDataBody));
    }

    /**
     * 修改保存字典类型
     *
     * @param dictDataBody 字典请求对象
     * @return 结果
     */
    @ApiOperation(value = "修改保存字典类型")
    @PutMapping
    public Response<Boolean> edit(@RequestBody DictDataBody dictDataBody) {
        return new Response<>(dictDataService.updateDictData(dictDataBody));
    }

    /**
     * 删除字典类型
     *
     * @param dictCodes 字典codes
     * @return 结果
     */
    @ApiOperation(value = "删除字典类型")
    @DeleteMapping("/{dictCodes}")
    public Response<Boolean> remove(@PathVariable Long[] dictCodes) {
        return new Response<>(dictDataService.deleteDictDatas(dictCodes));
    }
}

  