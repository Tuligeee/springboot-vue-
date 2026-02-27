package com.mock.example.modules.entrance.entity.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 专业表
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CeProfession implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 专业代码
     */
    private String professionNo;

    /**
     * 专业名称
     */
    private String professionName;

    /**
     * 所属院校代码
     */
    private String collegeNo;

    /**
     * 修业年限
     */
    private Integer studyYear;

    /**
     * 详情
     */
    private String detailInfo;

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
     * 分数线
     */
    @TableField(exist = false)
    private List<ScoreLineWrapper> scoreLines;

    /**
     * 分数线 (组装后文案)
     */
    @TableField(exist = false)
    private String scoreLineText;

    /**
     * 院校名称
     */
    @TableField(exist = false)
    private String collegeName;

    /**
     * 填充分数线文案
     *
     * @param scoreLines 分数线数组
     */
    public void populateScoreLineText(List<CeScoreLine> scoreLines) {
        if (CollUtil.isEmpty(scoreLines)) {
            return;
        }

        //对年度降序, 拼接分数线文案
        List<String> sortScoreLineTexts = scoreLines.stream()
                .sorted(
                        Comparator.comparing(CeScoreLine::getYear).reversed()
                ).map(
                        scoreLine -> "年份：" + scoreLine.getYear() + " , " + "分数：" + scoreLine.getScore()
                ).collect(Collectors.toList());

        this.scoreLineText = StrUtil.join(";    ", sortScoreLineTexts);
    }

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


    /**
     * 分数线对象
     */
    @Data
    @Accessors(chain = true)
    public static class ScoreLineWrapper{

        /**
         * 年份
         */
        private Integer year;

        /**
         * 分数
         */
        private Integer score;
    }


}
