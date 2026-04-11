package com.mock.example.interfaces.controller.entrance;

import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.body.entrance.student.StudentBody;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.interfaces.vo.entrance.student.StudentVo;
import com.mock.example.modules.entrance.service.CeStudentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * 学生管理
 *
 * @author: Mock
 * @date: 2025-01-31 16:40:29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/college_entrance/student")
public class CeStudentController extends BaseController {

    private final CeStudentService studentService;

    /**
     * 请求学生列表
     *
     * @param studentBody 学生请求体
     * @return 学生列表
     */
    @ApiOperation(value = "请求学生列表")
    @PreAuthorize("@ss.hasPermi('entrance:student:list')")
    @GetMapping("/list")
    public TableDataInfo list(StudentBody studentBody) {
        startPage();
        return getDataTable(studentService.selectStudentList(studentBody));
    }

    /**
     * 添加学生
     *
     * @param studentBody 学生请求体
     * @return 结果
     */
    @ApiOperation(value = "添加学生")
    @PreAuthorize("@ss.hasPermi('entrance:student:add')")
    @PostMapping
    public Response<Boolean> add(@RequestBody StudentBody studentBody) {
        return studentService.addStudent(studentBody);
    }

    /**
     * 编辑学生
     *
     * @param studentBody 学生请求体
     * @return 结果
     */
    @ApiOperation(value = "编辑学生")
    @PreAuthorize("@ss.hasPermi('entrance:student:edit')")
    @PutMapping
    public Response<Boolean> edit(@RequestBody StudentBody studentBody) {
        return studentService.editStudent(studentBody);
    }

    /**
     * 通过学生id查询学生信息
     *
     * @param studentId 学生id
     * @return 结果
     */
    @ApiOperation(value = "通过学生id查询学生信息")
    @PreAuthorize("@ss.hasPermi('entrance:student:query')")
    @GetMapping("/{studentId}")
    public Response<StudentVo> getInfo(@PathVariable Integer studentId) {
        return new Response<>(studentService.getStudent(studentId));
    }

    /**
     * 删除学生
     *
     * @param studentIds 角色id列表
     * @return 结果
     */
    @ApiOperation(value = "删除学生")
    @PreAuthorize("@ss.hasPermi('entrance:student:remove')")
    @DeleteMapping("/{studentIds}")
    public Response<Boolean> remove(@PathVariable Integer[] studentIds) {
        return studentService.deleteStudentByIds(studentIds);
    }

}

  