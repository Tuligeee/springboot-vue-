package com.mock.example.interfaces.controller.entrance;

import cn.hutool.core.util.StrUtil;
import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.body.entrance.aspiration.AspirationBody;
import com.mock.example.interfaces.body.entrance.aspiration.AspirationFormBody;
import com.mock.example.interfaces.body.entrance.aspiration.EvaluateBody;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.interfaces.vo.entrance.aspiration.AspirationFormVo;
import com.mock.example.interfaces.vo.entrance.aspiration.EvaluateResultVo;
import com.mock.example.modules.entrance.service.CeAspirationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 志愿管理
 *
 * @author: Mock
 * @date: 2023-04-05 09:07:53
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/college_entrance/aspiration")
public class CeAspirationController extends BaseController {

    private final CeAspirationService aspirationService;

    /**
     * 测评分析
     *
     * @param evaluateBody 评估请求体
     * @return 评测结果
     */
    @ApiOperation(value = "测评分析")
    @GetMapping("/evaluate")
    public Response<EvaluateResultVo> evaluate(EvaluateBody evaluateBody) {
        if(StrUtil.isBlank(evaluateBody.getStudentNo())){
            return new Response<>().failMsg("需要填写下学生编号");
        }
        if(evaluateBody.getByScore() == null || evaluateBody.getByTag() == null){
            return new Response<>().failMsg("需要选择评测类型");
        }

        return new Response<>(aspirationService.evaluate(evaluateBody));
    }

    /**
     * 填报志愿
     *
     * @param aspirationFormBody 志愿填报对象
     * @return 结果
     */
    @ApiOperation(value = "填报志愿")
    @PostMapping
    public Response<Boolean> addFrom(@RequestBody AspirationFormBody aspirationFormBody) {
        return new Response<>(aspirationService.addFrom(aspirationFormBody));
    }

    /**
     * 志愿列表
     *
     * @param aspirationBody 志愿请求体
     * @return 志愿列表
     */
    @ApiOperation(value = "请求志愿列表")
    @GetMapping("/list")
    public TableDataInfo list(AspirationBody aspirationBody) {
        startPage();
        return getDataTable(aspirationService.selectAspirationList(aspirationBody));
    }

    /**
     * 志愿详情
     *
     * @param studentNo 学号
     * @return 志愿详情
     */
    @ApiOperation(value = "请求志愿列表")
    @GetMapping("/detail")
    public Response<String> list(String studentNo) {
        return new Response<>(aspirationService.aspirationDetail(studentNo));
    }

    /**
     * 志愿填报筛选条件
     *
     * @return 志愿填报筛选条件
     */
    @ApiOperation(value = "志愿填报筛选条件")
    @GetMapping("/selectItem")
    public Response<AspirationFormVo> selectItem() {
        return new Response<>(aspirationService.selectItem());
    }

}

