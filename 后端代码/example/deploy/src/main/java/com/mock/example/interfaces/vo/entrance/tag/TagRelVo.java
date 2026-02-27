package com.mock.example.interfaces.vo.entrance.tag;

import com.mock.example.modules.entrance.entity.enums.TagEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 标签关联vo
 *
 * @author: Mock
 * @date: 2023-04-04 15:18:54
 */
@Data
@Accessors(chain = true)
public class TagRelVo {

    /**
     * 标签类型：STUDENT-学生 COLLEGE-院校 PROFESSION-专业
     */
    private TagEnum tagType;

    /**
     * 关联id。ce_student.id，ce_college.id，ce_profession.id 。
     */
    private Integer relId;

    /**
     * 标签名
     */
    private List<String> tagNames;

}

  