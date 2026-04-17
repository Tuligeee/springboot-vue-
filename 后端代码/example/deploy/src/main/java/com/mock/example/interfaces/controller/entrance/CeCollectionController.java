package com.mock.example.interfaces.controller.entrance;

import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.modules.entrance.entity.model.CeCollection;
import com.mock.example.modules.entrance.service.CeCollectionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 我的收藏管理
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/college_entrance/collection")
public class CeCollectionController extends BaseController {

    private final CeCollectionService collectionService;

    @ApiOperation(value = "添加收藏")
    @PostMapping
    public Response<Boolean> add(@RequestBody CeCollection ceCollection) {
        return collectionService.addCollection(ceCollection);
    }

    @ApiOperation(value = "我的收藏列表")
    @GetMapping("/list")
    public TableDataInfo list() {
        startPage();
        List<Map<String, Object>> list = collectionService.selectMyCollection();
        return getDataTable(list);
    }

    @ApiOperation(value = "删除收藏")
    @DeleteMapping("/{ids}")
    public Response<Boolean> remove(@PathVariable Integer ids) {
        return collectionService.removeCollection(ids);
    }

    @ApiOperation(value = "检查是否已收藏")
    @GetMapping("/check")
    public Response<Boolean> check(@RequestParam Long targetId, @RequestParam Integer targetType) {
        return collectionService.checkCollected(targetId, targetType);
    }

    @ApiOperation(value = "切换收藏状态")
    @PostMapping("/toggle")
    public Response<Boolean> toggle(@RequestBody CeCollection ceCollection) {
        return collectionService.toggleCollection(ceCollection);
    }
}
