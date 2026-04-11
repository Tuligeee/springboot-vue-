package com.mock.example.interfaces.controller.system;

import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.modules.system.entity.model.SysNotice;
import com.mock.example.modules.system.service.SysNoticeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController {

    private final SysNoticeService noticeService;

    @ApiOperation(value = "��ѯ�����б�")
    @GetMapping("/list")
    public TableDataInfo list(SysNotice notice) {
        startPage();
        return getDataTable(noticeService.selectNoticeList(notice));
    }

    @ApiOperation(value = "��ѯ��������")
    @GetMapping("/{noticeId}")
    public Response<SysNotice> getInfo(@PathVariable Long noticeId) {
        return new Response<>(noticeService.getById(noticeId));
    }

    @ApiOperation(value = "��������")
    @PostMapping
    public Response<Boolean> add(@RequestBody SysNotice notice) {
        return new Response<>(noticeService.saveNotice(notice, getUsername()));
    }

    @ApiOperation(value = "�޸Ĺ���")
    @PutMapping
    public Response<Boolean> edit(@RequestBody SysNotice notice) {
        return new Response<>(noticeService.updateNotice(notice, getUsername()));
    }

    @ApiOperation(value = "ɾ������")
    @DeleteMapping("/{noticeIds}")
    public Response<Boolean> remove(@PathVariable Long[] noticeIds) {
        return new Response<>(noticeService.removeByIds(Arrays.asList(noticeIds)));
    }

    @ApiOperation(value = "ǰ̨�����б�")
    @GetMapping("/public/list")
    public TableDataInfo publicList(@RequestParam(required = false) String noticeTitle) {
        startPage();
        return getDataTable(noticeService.selectPublicNoticeList(noticeTitle));
    }

    @ApiOperation(value = "ǰ̨��������")
    @GetMapping("/public/{noticeId}")
    public Response<SysNotice> publicInfo(@PathVariable Long noticeId) {
        SysNotice notice = noticeService.getById(noticeId);
        if (notice == null || !"0".equals(notice.getStatus())) {
            return new Response<SysNotice>().failMsg("���治���ڻ��ѹر�");
        }
        return new Response<>(notice);
    }
}
