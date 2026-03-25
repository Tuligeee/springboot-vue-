package com.mock.example.interfaces.body.entrance.aspiration;

import lombok.Data;
import java.util.List;

/**
 * 志愿填报表单 (湖北模式优化版)
 */
@Data
public class AspirationFormBody {

    /**
     * 志愿单序号 (1-5)
     */
    private Integer sheetNo;

    /**
     * 院校专业组列表
     */
    private List<CollegeGroup> collegeGroups;

    @Data
    public static class CollegeGroup {
        /**
         * 院校代码
         */
        private String collegeNo;

        /**
         * 该校下选择的专业代码列表
         */
        private List<String> professionNos;
    }
}
