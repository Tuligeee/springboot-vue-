package com.mock.example.modules.entrance.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.modules.entrance.entity.model.CeAspirationDetail;
import com.mock.example.modules.entrance.entity.model.CeCollege;
import com.mock.example.modules.entrance.entity.model.CeNews;
import com.mock.example.modules.entrance.entity.model.CeProfession;
import com.mock.example.modules.entrance.entity.model.CeScoreLine;
import com.mock.example.modules.entrance.mapper.CeAspirationDetailMapper;
import com.mock.example.modules.entrance.mapper.CeCollegeMapper;
import com.mock.example.modules.entrance.mapper.CeNewsMapper;
import com.mock.example.modules.entrance.mapper.CeProfessionMapper;
import com.mock.example.modules.entrance.mapper.CeScoreLineMapper;
import com.mock.example.modules.system.types.LoginUser;
import com.mock.example.modules.system.mapper.SysUserMapper;
import com.mock.example.modules.system.entity.model.SysUser;
import com.mock.example.modules.entrance.service.CeStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CeStatisticsServiceImpl implements CeStatisticsService {

    private final CeCollegeMapper collegeMapper;
    private final CeProfessionMapper professionMapper;
    private final CeNewsMapper newsMapper;
    private final CeScoreLineMapper scoreLineMapper;
    private final CeAspirationDetailMapper aspirationDetailMapper;
    private final SysUserMapper sysUserMapper;

    @Override
    public Map<String, Object> getOverviewStats() {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        boolean isSchoolAdmin = loginUser != null && loginUser.getUser() != null && loginUser.getUser().getRoles().stream().anyMatch(r -> "school_admin".equals(r.getRoleKey()));
        String myCollegeNo = null;

        if (isSchoolAdmin) {
            Long myCollegeId = loginUser.getUser().getCollegeId();
            if (myCollegeId != null) {
                CeCollege myCollege = collegeMapper.selectById(myCollegeId);
                if (myCollege != null) {
                    myCollegeNo = myCollege.getCollegeNo();
                }
            }
        }

        Map<String, Object> stats = new HashMap<>();

        if (isSchoolAdmin) {
            // 学校管理员的数据 (本校相关视角)
            QueryWrapper<CeAspirationDetail> stQuery = new QueryWrapper<>();
            if (myCollegeNo != null) stQuery.eq("college_no", myCollegeNo);
            stats.put("schoolStudentCount", aspirationDetailMapper.selectCount(stQuery)); // 关注我校的人次
            
            QueryWrapper<CeProfession> pQuery = new QueryWrapper<>();
            if (myCollegeNo != null) pQuery.eq("college_no", myCollegeNo);
            stats.put("schoolProfessionCount", professionMapper.selectCount(pQuery)); // 本校录入专业数

            QueryWrapper<CeScoreLine> cQuery = new QueryWrapper<>();
            if (myCollegeNo != null) cQuery.eq("college_no", myCollegeNo);
            stats.put("schoolScoreCount", scoreLineMapper.selectCount(cQuery)); // 历年分数记录数

            QueryWrapper<CeAspirationDetail> aQuery = new QueryWrapper<>();
            if (myCollegeNo != null) aQuery.eq("college_no", myCollegeNo);
            stats.put("schoolAspirationCount", aspirationDetailMapper.selectCount(aQuery)); // 志愿单填报次数
        } else {
            // 超级管理员全站数据
            stats.put("userCount", sysUserMapper.selectCount(new QueryWrapper<SysUser>()));
            stats.put("collegeCount", collegeMapper.selectCount(null));
            stats.put("professionCount", professionMapper.selectCount(null));
            stats.put("aspirationCount", aspirationDetailMapper.selectCount(null));
        }

        return stats;
    }

    @Override
    public Map<String, Object> getChartStats() {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        boolean isSchoolAdmin = loginUser != null && loginUser.getUser() != null && loginUser.getUser().getRoles().stream().anyMatch(r -> "school_admin".equals(r.getRoleKey()));
        String myCollegeNo = null;

        if (isSchoolAdmin) {
            Long myCollegeId = loginUser.getUser().getCollegeId();
            if (myCollegeId != null) {
                CeCollege myCollege = collegeMapper.selectById(myCollegeId);
                if (myCollege != null) {
                    myCollegeNo = myCollege.getCollegeNo();
                }
            }
        }

        Map<String, Object> charts = new HashMap<>();
        
        // 我们在这里构造简单的假数据或者聚合查询供图表使用
        // Pie chart data: 院校归属地分布 或者 Aspiration 的投档批次分布
        List<Map<String, Object>> pieData = new ArrayList<>();
        if (!isSchoolAdmin) {
            // 全站统计视角：查一下全站不同专业的填报热度排名
            QueryWrapper<CeAspirationDetail> q = new QueryWrapper<>();
            q.select("profession_name as name, sum(1) as value").groupBy("profession_name").orderByDesc("value").last("limit 5");
            List<Map<String, Object>> list = (List<Map<String, Object>>) (Object) aspirationDetailMapper.selectMaps(q);
            if (list == null || list.isEmpty()) {
                Map<String, Object> item1 = new HashMap<>(); item1.put("name", "机械工程"); item1.put("value", 820);
                Map<String, Object> item2 = new HashMap<>(); item2.put("name", "计算机科学与技术"); item2.put("value", 530);
                Map<String, Object> item3 = new HashMap<>(); item3.put("name", "临床医学"); item3.put("value", 310);
                Map<String, Object> item4 = new HashMap<>(); item4.put("name", "法学"); item4.put("value", 110);
                pieData.add(item1); pieData.add(item2); pieData.add(item3); pieData.add(item4);
            } else {
                pieData.addAll(list);
            }
        } else {
            // 查一下本校不同专业的填报热度排名
            QueryWrapper<CeAspirationDetail> q = new QueryWrapper<>();
            if (myCollegeNo != null) q.eq("college_no", myCollegeNo);
            q.select("profession_name as name, sum(1) as value").groupBy("profession_name").orderByDesc("value").last("limit 5");
            List<Map<String, Object>> list = (List<Map<String, Object>>) (Object) aspirationDetailMapper.selectMaps(q);
            if (list == null || list.isEmpty()) {
                Map<String, Object> d = new HashMap<>(); d.put("name", "暂无模拟填报数据"); d.put("value", 0);
                pieData.add(d);
            } else {
                pieData.addAll(list);
            }
        }
        charts.put("pieData", pieData);

        // Bar chart data
        List<String> xData = CollUtil.newArrayList("周一", "周二", "周三", "周四", "周五", "周六", "周日");
        List<Integer> yData1 = CollUtil.newArrayList(120, 200, 150, 80, 70, 110, 130);
        List<Integer> yData2 = CollUtil.newArrayList(20, 40, 35, 12, 10, 22, 33);
        
        Map<String, Object> barData = new HashMap<>();
        barData.put("xData", xData);
        barData.put("yData1", yData1);
        barData.put("yData2", yData2);
        charts.put("barData", barData);

        return charts;
    }
}
