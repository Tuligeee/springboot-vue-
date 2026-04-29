package com.mock.example.interfaces.controller.entrance;

import com.github.pagehelper.PageInfo;

import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.config.ProjectConfig;
import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.body.entrance.college.CollegeBody;
import com.mock.example.common.utils.ImportProgressContext;
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
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
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
     * 导入院校数据 (异步模式)
     */
    @ApiOperation(value = "导入院校数据")
    @PreAuthorize("@ss.hasPermi('entrance:college:index')")
    @PostMapping("/importData")
    public Response importData(MultipartFile file, boolean updateSupport) throws Exception {
        if (file == null || file.isEmpty()) {
            return new Response().failMsg("上传文件不能为空");
        }
        
        // 1. 生成任务ID
        String taskId = UUID.randomUUID().toString();
        String operName = getUsername();
        
        // 2. 先将文件落地到硬盘，避免在主线程解析导致超时/内存占用
        String fileName = taskId + "_" + file.getOriginalFilename();
        String filePath = ProjectConfig.getImportPath() + File.separator + fileName;
        File dest = new File(filePath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest);
        
        // 3. 初始化进度并回执
        ImportProgressContext.setProgress(taskId, 5); // 5% 表示文件已接收
        
        // 4. 异步执行解析与导入 (传入文件路径与用户信息)
        collegeService.importCollegeDataAsync(filePath, updateSupport, taskId, operName, getUserId());
        
        return new Response(200, "文件上传成功，正在后台解析并导入", taskId);
    }

    /**
     * 获取导入进度
     */
    @ApiOperation(value = "获取导入进度")
    @GetMapping("/importProgress/{taskId}")
    public Response getImportProgress(@PathVariable String taskId) {
        Integer progress = ImportProgressContext.getProgress(taskId);
        String result = ImportProgressContext.getResult(taskId);
        List<String> errorList = ImportProgressContext.getErrorList(taskId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("progress", progress);
        data.put("result", result);
        data.put("errorList", errorList);
        data.put("finished", progress != null && progress >= 100);
        
        return new Response(data);
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
        long total = new PageInfo(list).getTotal();
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
        TableDataInfo rspData = getDataTable(rows);
        rspData.setTotal(total);
        return rspData;
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

    /**
     * 获取数据库中不重复的办学层次列表
     */
    @ApiOperation(value = "获取唯一步学层次")
    @GetMapping("/uniqueEducationLevels")
    public Response getUniqueEducationLevels() {
        return new Response(collegeService.getUniqueEducationLevels());
    }
}
