package com.mock.example.modules.entrance.service;

import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.body.entrance.tag.BindTagsBody;
import com.mock.example.interfaces.vo.entrance.tag.TagRelVo;
import com.mock.example.modules.entrance.entity.enums.TagEnum;
import com.mock.example.modules.entrance.entity.model.CeTagRel;
import com.mock.example.modules.entrance.repository.ICeTagRelRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签业务层
 *
 * @author: Mock
 * @date: 2023-04-04 15:17:34
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CeTagService {

    private final ICeTagRelRepo ceTagRelRepo;

    /**
     * 根据id和类型获取标签列表
     *
     * @param tagType 标签类型 {@link TagEnum}
     * @return 标签列表
     */
    public TagRelVo getTags(TagEnum tagType, Integer relId) {
        List<String> tagNames = ceTagRelRepo.selectTagsByTypeAndRelId(tagType, relId)
                .stream().map(CeTagRel::getTagName).collect(Collectors.toList());

        return new TagRelVo()
                .setRelId(relId)
                .setTagType(tagType)
                .setTagNames(tagNames);
    }

    /**
     * 绑定标签
     *
     * @param bindTagsBody 绑定标签请求体
     * @return 结果
     */
    public Response<Boolean> bindTags(BindTagsBody bindTagsBody) {
        if (BooleanUtils.isFalse(bindTagsBody.checkParams())) {
            return new Response<>().failMsg("参数检查错误");
        }

        //清掉之前的标签
        ceTagRelRepo.removeTags(bindTagsBody.getTagType(), bindTagsBody.getRelId());

        //绑定所以标签
        ceTagRelRepo.bindTags(
                bindTagsBody.getTagType(), bindTagsBody.getRelId(), bindTagsBody.getTagNames()
        );
        return new Response<>(Boolean.TRUE);
    }

}

  