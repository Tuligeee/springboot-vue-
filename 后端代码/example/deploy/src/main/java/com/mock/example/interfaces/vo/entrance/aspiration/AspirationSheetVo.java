package com.mock.example.interfaces.vo.entrance.aspiration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
public class AspirationSheetVo {
    private Integer sheetNo;
    private boolean hasData;
    private List<VolunteerItem> details;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VolunteerItem {
        private Integer sort;
        private String collegeName;
        private String professionName;
    }
}
