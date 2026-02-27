package com.mock.example.interfaces.controller.entrance;

import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.modules.entrance.entity.model.CeVolunteer;
import com.mock.example.modules.entrance.service.CeVolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/entrance/volunteer")
public class CeVolunteerController extends BaseController {

    @Autowired
    private CeVolunteerService volunteerService;

    // 获取我的志愿列表
    @GetMapping("/my")
    public TableDataInfo myVolunteer() {
        startPage();
        Long userId = SecurityUtil.getUserId();
        List<CeVolunteer> list = volunteerService.selectMyList(userId);
        return getDataTable(list);
    }

    // 添加志愿
    @PostMapping
    public Response<Boolean> add(@RequestBody CeVolunteer volunteer) {
        return new Response<>(volunteerService.addVolunteer(volunteer));
    }

    // 删除志愿
    @DeleteMapping("/{id}")
    public Response<Boolean> remove(@PathVariable Long id) {
        return new Response<>(volunteerService.removeById(id));
    }
}
