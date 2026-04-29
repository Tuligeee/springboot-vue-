package com.mock.example.modules.entrance.service;
 
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.modules.entrance.entity.model.CeCollege;
import com.mock.example.modules.entrance.entity.model.CeScoreLine;
import com.mock.example.modules.entrance.mapper.CeCollegeMapper;
import com.mock.example.modules.entrance.repository.ICeScoreLineRepo;
import com.mock.example.modules.system.types.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 专业分数线 Service 业务层处理
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CeScoreLineService {

    private final ICeScoreLineRepo scoreLineRepo;
    private final CeCollegeMapper collegeMapper;

    /**
     * 获取当前院校管理员的 CollegeNo
     */
    private String getMyCollegeNo() {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        if (SecurityUtil.isRestrictedSchoolAdmin()) {
            Long collegeId = loginUser.getUser().getCollegeId();
            if (collegeId != null) {
                CeCollege college = collegeMapper.selectById(collegeId);
                return college != null ? college.getCollegeNo() : "NONE";
            }
            return "NONE";
        }
        return null;
    }

    /**
     * 查询专业分数线列表
     */
    public List<CeScoreLine> selectScoreLineList(CeScoreLine ceScoreLine) {
        String myCollegeNo = getMyCollegeNo();
        QueryWrapper<CeScoreLine> queryWrapper = new QueryWrapper<>();
        
        // 数据隔离：如果是院校管理员，强制锁定所属院校代码
        if (myCollegeNo != null) {
            queryWrapper.eq("college_no", myCollegeNo);
        } else if (ceScoreLine.getCollegeNo() != null) {
            queryWrapper.eq("college_no", ceScoreLine.getCollegeNo());
        }

        if (ceScoreLine.getProfessionNo() != null) {
            queryWrapper.eq("profession_no", ceScoreLine.getProfessionNo());
        }
        if (ceScoreLine.getYear() != null) {
            queryWrapper.eq("year", ceScoreLine.getYear());
        }
        queryWrapper.orderByDesc("year");
        return scoreLineRepo.list(queryWrapper);
    }

    /**
     * 查询专业分数线
     */
    public CeScoreLine selectScoreLineById(Integer id) {
        return scoreLineRepo.getById(id);
    }

    /**
     * 新增专业分数线
     */
    public boolean insertScoreLine(CeScoreLine ceScoreLine) {
        String myCollegeNo = getMyCollegeNo();
        if (myCollegeNo != null) {
            ceScoreLine.setCollegeNo(myCollegeNo);
        }
        return scoreLineRepo.save(ceScoreLine);
    }

    /**
     * 修改专业分数线
     */
    public boolean updateScoreLine(CeScoreLine ceScoreLine) {
        String myCollegeNo = getMyCollegeNo();
        if (myCollegeNo != null) {
            CeScoreLine old = scoreLineRepo.getById(ceScoreLine.getId());
            if (old == null || !myCollegeNo.equals(old.getCollegeNo())) {
                throw new RuntimeException("无权修改其他院校的分数线数据");
            }
            ceScoreLine.setCollegeNo(myCollegeNo); // 确保院校代码不被篡改
        }
        return scoreLineRepo.updateById(ceScoreLine);
    }

    /**
     * 删除专业分数线信息
     */
    public boolean deleteScoreLineByIds(Integer[] ids) {
        String myCollegeNo = getMyCollegeNo();
        if (myCollegeNo != null) {
            for (Integer id : ids) {
                CeScoreLine old = scoreLineRepo.getById(id);
                if (old != null && !myCollegeNo.equals(old.getCollegeNo())) {
                    throw new RuntimeException("无权删除其他院校的分数线数据");
                }
            }
        }
        return scoreLineRepo.removeByIds(java.util.Arrays.asList(ids));
    }
}
