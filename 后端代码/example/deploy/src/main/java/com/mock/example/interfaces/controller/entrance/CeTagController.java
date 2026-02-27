package com.mock.example.interfaces.controller.entrance;

import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.body.entrance.tag.BindTagsBody;
import com.mock.example.interfaces.vo.entrance.tag.TagRelVo;
import com.mock.example.modules.entrance.entity.enums.TagEnum;
import com.mock.example.modules.entrance.service.CeTagService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 标签
 *
 * @author: Mock
 * @date: 2023-04-04 14:53:42
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/college_entrance/tag")
public class CeTagController {

    private final CeTagService ceTagService;

    /**
     * 根据id和类型获取标签列表
     *
     * @param tagType 标签类型 {@link TagEnum}
     * @return 标签数据
     */
    @ApiOperation(value = "根据id和类型获取标签列表")
    @GetMapping("/{tagType}/{relId}")
    public Response<TagRelVo> getTags(@PathVariable TagEnum tagType, @PathVariable Integer relId) {
        return new Response<>(ceTagService.getTags(tagType, relId));
    }

    /**
     * 绑定标签
     *
     * @param bindTagsBody 绑定标签请求体
     * @return 结果
     */
    @ApiOperation(value = "绑定标签")
    @PostMapping
    public Response<Boolean> bindTags(@RequestBody BindTagsBody bindTagsBody) {
        return ceTagService.bindTags(bindTagsBody);
    }

}

  