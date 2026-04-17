package com.mock.example.interfaces.controller.entrance;

import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.interfaces.body.entrance.aspiration.AspirationBody;
import com.mock.example.modules.entrance.entity.model.CeAspiration;
import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.body.entrance.aspiration.AspirationFormBody;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.modules.entrance.service.CeAspirationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import com.mock.example.common.utils.ExcelUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import com.mock.example.modules.entrance.model.vo.VolunteerExportVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/college_entrance/aspiration")
public class CeAspirationController extends BaseController {

    private final CeAspirationService aspirationService;

    @ApiOperation(value = "导出志愿单")
    @GetMapping("/export/{sheetNo}")
    public Response export(@PathVariable Integer sheetNo) {
        try {
            List<VolunteerExportVo> list = aspirationService.selectVolunteerExportList(sheetNo);
            if (list == null || list.isEmpty()) {
                return new Response<>().failMsg("该方案单目前没有填报任何数据，无法导出。");
            }
            ExcelUtil<VolunteerExportVo> util = new ExcelUtil<>(VolunteerExportVo.class);
            return util.exportExcel(list, "我的志愿表-方案" + sheetNo);
        } catch (Exception e) {
            log.error("导出志愿单发生系统错误", e);
            return new Response<>().failMsg("导出失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "填报志愿")
    @PostMapping("/addFrom")
    public Response<Boolean> addFrom(@RequestBody AspirationFormBody body) {
        return new Response<>(aspirationService.addFrom(body));
    }

    @ApiOperation(value = "获取志愿单状态列表")
    @GetMapping("/listSheets")
    public Response<List<Map<String, Object>>> listSheets() {
        return new Response<>(aspirationService.listAllSheets());
    }

    @ApiOperation(value = "志愿填报筛选条件 (湖北模式)")
    @GetMapping("/selectItem")
    public Response<Map<String, Object>> selectItem(@RequestParam(required = false) Integer sheetNo) {
        return new Response<>(aspirationService.selectItemNew(sheetNo));
    }

    /**
     * 删除/清空指定志愿单
     */
    @ApiOperation(value = "删除志愿单")
    @DeleteMapping("/removeSheet/{sheetNo}")
    public Response<Boolean> removeSheet(@PathVariable Integer sheetNo) {
        return new Response<>(aspirationService.deleteSheet(sheetNo));
    }

    /**
     * 管理端：获取所有学生的志愿填报列表
     */
    @ApiOperation(value = "获取所有学生志愿列表")
    @PreAuthorize("@ss.hasAnyPermi('entrance:aspiration:index,entrance:aspiration:list,entrance:aspiration:listSheets')")
    @GetMapping("/list")
    public TableDataInfo list(AspirationBody aspirationBody) {
        startPage();
        List<CeAspiration> list = aspirationService.selectAspirationList(aspirationBody);
        return getDataTable(list);
    }

    /**
     * 管理端：获取特定学生的志愿填报摘要
     */
    @ApiOperation(value = "获取志愿填报详情")
    @PreAuthorize("@ss.hasAnyPermi('entrance:aspiration:index,entrance:aspiration:list,entrance:aspiration:listSheets')")
    @GetMapping("/detailed/{studentNo}")
    public Response getDetailed(@PathVariable String studentNo) {
        return new Response<>(aspirationService.aspirationDetail(studentNo));
    }
    /**
     * 管理端：彻底删除某项志愿记录
     */
    @ApiOperation(value = "管理员删除志愿")
    @PreAuthorize("@ss.hasAnyPermi('entrance:aspiration:index,entrance:aspiration:list,entrance:aspiration:listSheets')")
    @DeleteMapping("/remove/{id}")
    public Response remove(@PathVariable Integer id) {
        return new Response(aspirationService.deleteById(id));
    }
}
