package com.mock.example.interfaces.controller.entrance;

import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.body.entrance.college.CollegeBody;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.modules.entrance.service.CeCollegeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.mock.example.common.utils.ExcelUtil;
import com.mock.example.modules.entrance.model.vo.CollegeImportVo;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * 院校查询管理
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/college_entrance/college")
public class CeCollegeController extends BaseController {

    private final CeCollegeService collegeService;

    /**
     * 导出院校及专业分数数据
     */
    @ApiOperation(value = "导出院校数据")
    @GetMapping("/export")
    public Response export(CollegeBody collegeBody) {
        List<CollegeImportVo> list = collegeService.selectCollegeExportList(collegeBody);
        ExcelUtil<CollegeImportVo> util = new ExcelUtil<>(CollegeImportVo.class);
        return util.exportExcel(list, "院校数据");
    }

    /**
     * 导入院校及专业分数数据
     */
    @ApiOperation(value = "导入院校数据")
    @PostMapping("/importData")
    public Response importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<CollegeImportVo> util = new ExcelUtil<>(CollegeImportVo.class);
        List<CollegeImportVo> collegeList = util.importExcel(file.getInputStream());
        String message = collegeService.importCollegeData(collegeList, updateSupport);
        return new Response<>().okMsg(message);
    }

    /**
     * 下载导入模板
     */
    @GetMapping("/importTemplate")
    public Response importTemplate() {
        ExcelUtil<CollegeImportVo> util = new ExcelUtil<>(CollegeImportVo.class);
        return util.importTemplateExcel("院校导入模板");
    }

    /**
     * 请求院校列表
     */
    @ApiOperation(value = "请求院校列表")
    @GetMapping("/list")
    public TableDataInfo list(CollegeBody collegeBody) {
        startPage();
        return getDataTable(collegeService.selectCollegeList(collegeBody));
    }

    /**
     * 获取当前登录用户所属院校
     */
    @ApiOperation(value = "获取所属院校")
    @GetMapping("/myCollege")
    public Response getMyCollege() {
        return collegeService.getMyCollege();
    }

    /**
     * 获取院校详情
     */
    @ApiOperation(value = "请求院校详细")
    @GetMapping("/{id}")
    public Response getInfo(@PathVariable Integer id) {
        return new Response<>(collegeService.getCollege(id));
    }

    /**
     * 新增院校
     */
    @ApiOperation(value = "新增院校")
    @PostMapping
    public Response add(@RequestBody CollegeBody collegeBody) {
        return collegeService.addCollege(collegeBody);
    }

    /**
     * 修改院校
     */
    @ApiOperation(value = "修改院校")
    @PutMapping
    public Response edit(@RequestBody CollegeBody collegeBody) {
        return collegeService.editCollege(collegeBody);
    }

    /**
     * 删除院校
     */
    @ApiOperation(value = "删除院校")
    @DeleteMapping("/{collegeIds}")
    public Response<Boolean> remove(@PathVariable Integer[] collegeIds) {
        return collegeService.deleteCollegeByIds(collegeIds);
    }
}
