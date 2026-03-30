package com.mock.example.interfaces.controller.entrance;

import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.modules.entrance.entity.model.CeNews;
import com.mock.example.modules.entrance.service.CeNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/entrance/news")
public class CeNewsController extends BaseController {

    @Autowired
    private CeNewsService newsService;

    /** 查询列表 */
    @GetMapping("/list")
    public TableDataInfo list(CeNews news) {
        startPage();
        List<CeNews> list = newsService.selectNewsList(news);
        return getDataTable(list);
    }

    /** 获取详情 */
    @GetMapping("/{id}")
    public Response<CeNews> getInfo(@PathVariable Long id) {
        CeNews news = newsService.getNewsById(id);
        if (news != null) {
            news.setViewCount((news.getViewCount() == null ? 0 : news.getViewCount()) + 1);
            newsService.updateById(news);
        }
        return new Response<>(news);
    }

    /** 点赞 */
    @PostMapping("/like/{id}")
    public Response<Void> like(@PathVariable Long id) {
        CeNews news = newsService.getNewsById(id);
        if (news != null) {
            news.setLikeCount((news.getLikeCount() == null ? 0 : news.getLikeCount()) + 1);
            newsService.updateById(news);
        }
        return new Response<>();
    }

    /** 新增 */
    @PostMapping
    public Response<Boolean> add(@RequestBody CeNews news) {
        if (getLoginUser() != null && !getLoginUser().getUserId().equals(1L)) {
            boolean isAdmin = getLoginUser().getUser().getRoles().stream()
                    .anyMatch(r -> "admin".equals(r.getRoleKey()));
            if (!isAdmin) return new Response<Boolean>().failMsg("越权操作：仅超级管理员可发布政策资讯");
        }
        return new Response<>(newsService.addNews(news));
    }

    /** 修改 */
    @PutMapping
    public Response<Boolean> edit(@RequestBody CeNews news) {
        if (getLoginUser() != null && !getLoginUser().getUserId().equals(1L)) {
            boolean isAdmin = getLoginUser().getUser().getRoles().stream()
                    .anyMatch(r -> "admin".equals(r.getRoleKey()));
            if (!isAdmin) return new Response<Boolean>().failMsg("越权操作：仅超级管理员可修改政策资讯");
        }
        news.setUpdateTime(new java.util.Date());
        return new Response<>(newsService.updateById(news));
    }

    /** 删除 */
    @DeleteMapping("/{ids}")
    public Response<Boolean> remove(@PathVariable Long[] ids) {
        if (getLoginUser() != null && !getLoginUser().getUserId().equals(1L)) {
            boolean isAdmin = getLoginUser().getUser().getRoles().stream()
                    .anyMatch(r -> "admin".equals(r.getRoleKey()));
            if (!isAdmin) return new Response<Boolean>().failMsg("越权操作：仅超级管理员可删除政策资讯");
        }
        return new Response<>(newsService.removeByIds(Arrays.asList(ids)));
    }
}
