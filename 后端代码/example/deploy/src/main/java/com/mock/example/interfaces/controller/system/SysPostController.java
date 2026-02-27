package com.mock.example.interfaces.controller.system;

import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.body.system.post.PostBody;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.interfaces.vo.system.post.PostVo;
import com.mock.example.modules.system.service.SysPostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位管理
 *
 * @author: Mock
 * @date: 2025-01-11 13:35:59
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/post")
public class SysPostController extends BaseController {

    private final SysPostService postService;

    /**
     * 获取岗位列表
     *
     * @param postBody 岗位请求对象
     * @return 岗位列表
     */
    @ApiOperation(value = "获取岗位列表")
    @GetMapping("/list")
    public TableDataInfo list(PostBody postBody) {
        startPage();
        return getDataTable(postService.selectPostList(postBody));
    }

    /**
     * 根据岗位编号获取详细信息
     *
     * @param postId 岗位id
     * @return 岗位信息
     */
    @ApiOperation(value = "根据岗位编号获取详细信息")
    @GetMapping(value = "/{postId}")
    public Response<PostVo> getInfo(@PathVariable Long postId) {
        return new Response<>(postService.selectPostById(postId));
    }

    /**
     * 新增岗位
     *
     * @param postBody 岗位信息
     * @return 结果
     */
    @ApiOperation(value = "新增岗位")
    @PostMapping
    public Response add(@RequestBody PostBody postBody) {
        return postService.addPost(postBody);
    }

    /**
     * 编辑岗位
     *
     * @param postBody 岗位信息
     * @return 结果
     */
    @ApiOperation(value = "编辑岗位")
    @PutMapping
    public Response<Boolean> edit(@RequestBody PostBody postBody) {
        return postService.editPost(postBody);
    }

    /**
     * 删除岗位
     *
     * @param postIds 岗位ID列表
     * @return 结果
     */
    @ApiOperation(value = "删除岗位")
    @DeleteMapping("/{postIds}")
    public Response<Boolean> remove(@PathVariable Long[] postIds) {
        return postService.deleteByPostIds(postIds);
    }

    /**
     * 获取岗位选择框列表
     *
     * @return 岗位列表
     */
    @ApiOperation(value = "获取岗位选择框列表")
    @GetMapping("/optionselect")
    public Response<List<PostVo>> optionSelect() {
        return new Response<>(postService.selectPostAll());
    }

}

  