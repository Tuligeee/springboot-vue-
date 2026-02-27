package com.mock.example.interfaces.controller.entrance;

import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.body.entrance.college.CollegeBody;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.interfaces.vo.entrance.college.CollegeVo;
import com.mock.example.modules.entrance.service.CeCollegeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 院校查询管理
 *
 * @author: Mock
 * @date: 2025-01-31 16:40:29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/college_entrance/college")
public class CeCollegeController extends BaseController {

    private final CeCollegeService collegeService;

    /**
     * 请求院校列表
     *
     * @param collegeBody 院校请求体
     * @return 院校列表
     */
    @ApiOperation(value = "请求院校列表")
    @GetMapping("/list")
    public TableDataInfo list(CollegeBody collegeBody) {
        startPage();
        return getDataTable(collegeService.selectCollegeList(collegeBody));
    }

    /**
     * 添加院校
     *
     * @param collegeBody 院校请求体
     * @return 结果
     */
    @ApiOperation(value = "添加院校")
    @PostMapping
    public Response<Boolean> add(@RequestBody CollegeBody collegeBody) {
        return collegeService.addCollege(collegeBody);
    }

    /**
     * 编辑院校
     *
     * @param collegeBody 院校请求体
     * @return 结果
     */
    @ApiOperation(value = "编辑院校")
    @PutMapping
    public Response<Boolean> edit(@RequestBody CollegeBody collegeBody) {
        return collegeService.editCollege(collegeBody);
    }

    /**
     * 通过院校id查询院校信息
     *
     * @param collegeId 院校id
     * @return 结果
     */
    @ApiOperation(value = "通过院校id查询院校信息")
    @GetMapping("/{collegeId}")
    public Response<CollegeVo> getInfo(@PathVariable Integer collegeId) {
        return new Response<>(collegeService.getCollege(collegeId));
    }

    /**
     * 删除院校
     *
     * @param collegeIds 院校id列表
     * @return 结果
     */
    @ApiOperation(value = "删除院校")
    @DeleteMapping("/{collegeIds}")
    public Response<Boolean> remove(@PathVariable Integer[] collegeIds) {
        return collegeService.deleteCollegeByIds(collegeIds);
    }

}

  