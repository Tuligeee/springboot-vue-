package com.mock.example.interfaces.controller.entrance;

import com.mock.example.common.entity.Response;
import com.mock.example.modules.entrance.entity.model.CeForumComment;
import com.mock.example.modules.entrance.entity.model.CeForumPost;
import com.mock.example.modules.entrance.service.CeForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/entrance/forum")
@RequiredArgsConstructor
public class CeForumController {

    private final CeForumService forumService;

    // 帖子列表
    @GetMapping("/list")
    public Response<List<CeForumPost>> list() {
        return new Response<>(forumService.getPostList());
    }

    // 帖子详情
    @GetMapping("/{id}")
    public Response<CeForumPost> getInfo(@PathVariable("id") Long id) {
        return new Response<>(forumService.getPostDetail(id));
    }

    // 发布帖子
    @PostMapping("/add")
    public Response<Void> add(@RequestBody CeForumPost post) {
        forumService.publishPost(post);
        return new Response<>();
    }

    // 获取某帖子的评论
    @GetMapping("/comment/list/{postId}")
    public Response<List<CeForumComment>> listComments(@PathVariable("postId") Long postId) {
        return new Response<>(forumService.getComments(postId));
    }

    // 发表评论
    @PostMapping("/comment/add")
    public Response<Void> addComment(@RequestBody CeForumComment comment) {
        forumService.publishComment(comment);
        return new Response<>();
    }

    // [新增] 删除帖子
    @DeleteMapping("/{id}")
    public Response<Void> remove(@PathVariable("id") Long id) {
        forumService.deletePost(id);
        return new Response<>();
    }

    // [新增] 删除评论接口
    @DeleteMapping("/comment/{id}")
    public Response<Void> removeComment(@PathVariable("id") Long id) {
        forumService.deleteComment(id);
        return new Response<>();
    }
}
