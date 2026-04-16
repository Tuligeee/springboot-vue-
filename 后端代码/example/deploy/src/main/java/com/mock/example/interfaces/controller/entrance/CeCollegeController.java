package com.mock.example.interfaces.controller.entrance;

import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.body.entrance.college.CollegeBody;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.interfaces.vo.entrance.college.CollegeListRowVo;
import com.mock.example.modules.entrance.entity.model.CeCollege;
import com.mock.example.modules.entrance.service.CeCollegeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.extern.slf4j.Slf4j;

import com.mock.example.common.utils.ExcelUtil;
import com.mock.example.modules.entrance.model.vo.CollegeImportVo;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 院校查询管理
 */
@Slf4j
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
    @PreAuthorize("@ss.hasPermi('entrance:college:index')")
    @PostMapping("/importData")
    public Response importData(MultipartFile file, boolean updateSupport) {
        log.info("【上传接收】文件名: {}, 大小: {} bytes, 内容类型: {}", 
            file.getOriginalFilename(), file.getSize(), file.getContentType());
        if (file.isEmpty()) {
            return new Response<>().failMsg("上传文件不能为空");
        }
        try {
            ExcelUtil<CollegeImportVo> util = new ExcelUtil<>(CollegeImportVo.class);
            List<CollegeImportVo> collegeList = util.importExcel(file.getInputStream());
            log.info("Excel 解析成功，共 {} 条数据", collegeList == null ? 0 : collegeList.size());
            String message = collegeService.importCollegeData(collegeList, updateSupport);
            return new Response<>().okMsg(message);
        } catch (Exception e) {
            return new Response<>().failMsg("导入失败，解析 Excel 文件时出错：" + e.getMessage());
        }
    }

    /**
     * 下载导入模板
     */
    @PreAuthorize("@ss.hasPermi('entrance:college:index')")
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
        List<CeCollege> list = collegeService.selectCollegeList(collegeBody);
        List<CollegeListRowVo> rows = list.stream().map(c -> {
            CollegeListRowVo vo = new CollegeListRowVo();
            vo.setId(c.getId());
            vo.setCollegeName(c.getCollegeName());
            vo.setCity(c.getCity());
            vo.setRanking(c.getRanking());
            vo.setPersonCount(c.getPersonCount());
            vo.setEducationLevel(c.getEducationLevel());
            return vo;
        }).collect(Collectors.toList());
        return getDataTable(rows);
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
    @PreAuthorize("@ss.hasAnyRoles('admin')") 
    @PostMapping
    public Response add(@RequestBody CollegeBody collegeBody) {
        return collegeService.addCollege(collegeBody);
    }

    /**
     * 修改院校
     */
    @ApiOperation(value = "修改院校")
    @PreAuthorize("@ss.hasAnyRoles('admin,school_admin')")
    @PutMapping
    public Response edit(@RequestBody CollegeBody collegeBody) {
        return collegeService.editCollege(collegeBody);
    }

    /**
     * 删除院校
     */
    @ApiOperation(value = "删除院校")
    @PreAuthorize("@ss.hasAnyRoles('admin')")
    @DeleteMapping("/{collegeIds}")
    public Response<Boolean> remove(@PathVariable Integer[] collegeIds) {
        return collegeService.deleteCollegeByIds(collegeIds);
    }
}
