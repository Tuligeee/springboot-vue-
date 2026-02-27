package com.mock.example.modules.entrance.entity.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mock.example.modules.entrance.entity.enums.SexEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 学生信息表
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CeStudent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 绑定用户id
     */
    private Long userId;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 入学年份
     */
    private Integer enrollmentYear;

    /**
     * 毕业年份
     */
    private Integer graduateYear;

    /**
     * 性别 MAN-男 WOMEN-女
     */
    private SexEnum sex;

    /**
     * 成绩分数
     */
    private Integer achievement;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 创建人
     */
    private String createdUser;

    /**
     * 修改时间
     */
    private Date updatedTime;

    /**
     * 修改人
     */
    private String updatedUser;

    /**
     * 用户名
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 标签名称展示列表
     */
    @TableField(exist = false)
    private String tagNameText;

    /**
     * 组装标签展示文本
     *
     * @param ceTagRelList 关联标签列表
     */
    public void generateTagNameText(List<CeTagRel> ceTagRelList) {
        if(CollUtil.isEmpty(ceTagRelList)){
            return;
        }
        List<String> tagNames = ceTagRelList.stream()
                .map(CeTagRel::getTagName)
                .collect(Collectors.toList());

        this.tagNameText = StrUtil.join("、", tagNames);
    }


}
