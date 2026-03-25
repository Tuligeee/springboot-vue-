package com.mock.example.interfaces.controller.entrance;

import com.mock.example.common.entity.Response;
import com.mock.example.common.utils.DateUtils;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.modules.entrance.entity.model.CeComment;
import com.mock.example.modules.entrance.mapper.CeCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entrance/comment")
public class CeCommentController {

    @Autowired
    private CeCommentMapper ceCommentMapper;

    /**
     * 获取评论列表
     */
    @GetMapping("/list")
    public Response<List<CeComment>> list(Long targetId, String type) {
        return new Response<>(ceCommentMapper.selectCommentsWithUser(targetId, type));
    }

    /**
     * 发表评论
     */
    @PostMapping("/add")
    public Response<Void> add(@RequestBody CeComment comment) {
        // 安全校验：如果类型是2（学校），则禁止评论
        if ("2".equals(comment.getType())) {
            Response<Void> res = new Response<>();
            res.setCode(500);
            res.setMsg("学校信息暂不支持评论");
            return res;
        }

        comment.setUserId(SecurityUtil.getUserId());
        comment.setCreateTime(DateUtils.getNowDate());
        ceCommentMapper.insert(comment);
        return new Response<>();
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{id}")
    public Response<Void> remove(@PathVariable Long id) {
        CeComment comment = ceCommentMapper.selectById(id);
        if (comment != null) {
            Long userId = SecurityUtil.getUserId();
            // 只有本人或管理员可以删除
            if (userId.equals(comment.getUserId()) || userId.equals(1L)) {
                ceCommentMapper.deleteById(id);
            } else {
                Response<Void> res = new Response<>();
                res.setCode(500);
                res.setMsg("无权删除此评论");
                return res;
            }
        }
        return new Response<>();
    }
}
