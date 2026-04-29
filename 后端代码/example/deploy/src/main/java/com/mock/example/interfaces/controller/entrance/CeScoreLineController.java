package com.mock.example.interfaces.controller.entrance;

import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.modules.entrance.entity.model.CeScoreLine;
import com.mock.example.modules.entrance.service.CeScoreLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 专业分数线 Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/entrance/scoreLine")
public class CeScoreLineController extends BaseController {

    private final CeScoreLineService scoreLineService;

    /**
     * 查询专业分数线列表
     */
    @GetMapping("/list")
    public TableDataInfo list(CeScoreLine ceScoreLine) {
        startPage();
        List<CeScoreLine> list = scoreLineService.selectScoreLineList(ceScoreLine);
        return getDataTable(list);
    }

    /**
     * 获取专业分数线详细信息
     */
    @GetMapping(value = "/{id}")
    public Response getInfo(@PathVariable("id") Integer id) {
        return new Response<>(scoreLineService.selectScoreLineById(id));
    }

    /**
     * 新增专业分数线
     */
    @PreAuthorize("@ss.hasPermi('entrance:scoreLine:add')")
    @PostMapping
    public Response add(@RequestBody CeScoreLine ceScoreLine) {
        return new Response<>(scoreLineService.insertScoreLine(ceScoreLine));
    }

    /**
     * 修改专业分数线
     */
    @PreAuthorize("@ss.hasPermi('entrance:scoreLine:edit')")
    @PutMapping
    public Response edit(@RequestBody CeScoreLine ceScoreLine) {
        return new Response<>(scoreLineService.updateScoreLine(ceScoreLine));
    }

    /**
     * 删除专业分数线
     */
    @PreAuthorize("@ss.hasPermi('entrance:scoreLine:remove')")
    @DeleteMapping("/{ids}")
    public Response remove(@PathVariable Integer[] ids) {
        return new Response<>(scoreLineService.deleteScoreLineByIds(ids));
    }
}
