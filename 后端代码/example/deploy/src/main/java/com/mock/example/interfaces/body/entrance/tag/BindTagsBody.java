package com.mock.example.interfaces.body.entrance.tag;

import com.mock.example.modules.entrance.entity.enums.TagEnum;
import lombok.Data;

import java.util.List;

/**
 * 绑定标签请求体
 *
 * @author: Mock
 * @date: 2023-04-04 15:30:27
 */
@Data
public class BindTagsBody {

    /**
     * 关联id
     */
    private Integer relId;

    /**
     * 标签类型
     */
    private TagEnum tagType;

    /**
     * 标签名称列表
     */
    private List<String> tagNames;

    /**
     * 参数检查
     *
     * @return 结果
     */
    public boolean checkParams() {
        return relId != null && tagType != null;
    }

}

  