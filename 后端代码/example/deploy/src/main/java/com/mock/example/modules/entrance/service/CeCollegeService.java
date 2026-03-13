package com.mock.example.modules.entrance.service;

import com.mock.example.common.entity.Response;
import com.mock.example.common.utils.EntityCopyUtil;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.interfaces.body.entrance.college.CollegeBody;
import com.mock.example.interfaces.vo.entrance.college.CollegeVo;
import com.mock.example.modules.entrance.entity.model.CeCollege;
import com.mock.example.modules.entrance.repository.ICeCollegeRepo;
import com.mock.example.modules.system.types.LoginUser; // 新增导入
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 院校查询管理
 *
 * @author: Mock
 * @date: 2023-04-01 15:07:20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CeCollegeService {

    private final ICeCollegeRepo collegeRepo;

    /**
     * 请求院校列表 (增强版：双重保底逻辑)
     */
    public List<CeCollege> selectCollegeList(CollegeBody collegeBody) {
        CeCollege queryEntity = EntityCopyUtil.copyEntity(CeCollege.class, collegeBody);

        try {
            LoginUser loginUser = SecurityUtil.getLoginUser();
            if (loginUser != null && !loginUser.getUserId().equals(1L)) {
                boolean isSchoolAdmin = loginUser.getUser().getRoles().stream()
                        .anyMatch(r -> "school_admin".equals(r.getRoleKey()));

                if (isSchoolAdmin) {
                    // 1. 优先尝试用 manager_id 过滤
                    queryEntity.setManagerId(loginUser.getUserId());
                    List<CeCollege> list = collegeRepo.selectCollegeList(queryEntity);
                    
                    // 2. 保底逻辑：如果按管理员ID查不到，说明没绑定manager_id，尝试按用户所属的 college_id 查
                    if (list.isEmpty() && loginUser.getUser().getCollegeId() != null) {
                        queryEntity.setManagerId(null); // 清除刚才的过滤
                        queryEntity.setId(loginUser.getUser().getCollegeId().intValue());
                        return collegeRepo.selectCollegeList(queryEntity);
                    }
                    return list;
                }
            }
        } catch (Exception e) {
            log.warn("获取用户信息失败: {}", e.getMessage());
        }

        return collegeRepo.selectCollegeList(queryEntity);
    }

    /**
     * 添加院校
     *
     * @param collegeBody 院校请求体
     * @return 结果
     */
    public Response<Boolean> addCollege(CollegeBody collegeBody) {
        if (BooleanUtils.isFalse(uniqueCollegeNo(collegeBody.getCollegeNo(), null))) {
            return new Response<>().failMsg("保存院校失败,代码 '" + collegeBody.getCollegeNo() + "' 已存在");
        }
        CeCollege ceCollege = EntityCopyUtil.copyEntity(CeCollege.class, collegeBody);

        // 如果是学校管理员添加，自动绑定当前用户为管理员
        try {
            LoginUser loginUser = SecurityUtil.getLoginUser();
            boolean isSchoolAdmin = loginUser.getUser().getRoles().stream()
                    .anyMatch(r -> "school_admin".equals(r.getRoleKey()));
            if (isSchoolAdmin) {
                ceCollege.setManagerId(loginUser.getUserId());
            }
        } catch (Exception ignored) {}

        ceCollege.setCreatedUser(SecurityUtil.getUsername());
        collegeRepo.save(ceCollege);

        return new Response<>(Boolean.TRUE);
    }

    /**
     * 编辑院校 (已修改：增加权限校验)
     *
     * @param collegeBody 院校请求体
     * @return 结果
     */
    public Response<Boolean> editCollege(CollegeBody collegeBody) {
        // 1. 唯一性校验
        if (BooleanUtils.isFalse(uniqueCollegeNo(collegeBody.getCollegeNo(), collegeBody.getId()))) {
            return new Response<>().failMsg("编辑院校失败,代码 '" + collegeBody.getCollegeNo() + "' 已存在");
        }

        // 2. 权限校验：如果是学校管理员，只能修改自己绑定的学校
        LoginUser loginUser = SecurityUtil.getLoginUser();
        // 排除超级管理员
        if (loginUser != null && !loginUser.getUserId().equals(1L)) {
            boolean isSchoolAdmin = loginUser.getUser().getRoles().stream()
                    .anyMatch(r -> "school_admin".equals(r.getRoleKey()));

            if (isSchoolAdmin) {
                // 查询数据库中该学校的现有信息
                CeCollege existingCollege = collegeRepo.getById(collegeBody.getId());
                // 如果学校不存在，或者该学校的管理员ID不等于当前用户ID
                if (existingCollege == null || !loginUser.getUserId().equals(existingCollege.getManagerId())) {
                    return new Response<>().failMsg("您没有权限修改该学校的信息！");
                }
            }
        }

        // 3. 执行更新
        CeCollege ceCollege = EntityCopyUtil.copyEntity(CeCollege.class, collegeBody);
        ceCollege.setUpdatedUser(SecurityUtil.getUsername());
        collegeRepo.updateById(ceCollege);
        return new Response<>(Boolean.TRUE);
    }

    /**
     * 通过院校id查询院校信息
     *
     * @param collegeId 院校id
     * @return 结果
     */
    public CollegeVo getCollege(Integer collegeId) {
        return EntityCopyUtil.copyEntity(
                CollegeVo.class, collegeRepo.getById(collegeId)
        );
    }

    /**
     * 删除院校
     *
     * @param collegeIds 院校id列表
     * @return 结果
     */
    public Response<Boolean> deleteCollegeByIds(Integer[] collegeIds) {
        // 增加权限校验：学校管理员禁止删除院校
        try {
            LoginUser loginUser = SecurityUtil.getLoginUser();
            boolean isSchoolAdmin = loginUser.getUser().getRoles().stream()
                    .anyMatch(r -> "school_admin".equals(r.getRoleKey()));
            if (isSchoolAdmin) {
                return new Response<Boolean>().failMsg("权限不足：学校管理员禁止删除院校信息！");
            }
        } catch (Exception ignored) {}

        collegeRepo.removeByIds(Arrays.asList(collegeIds));
        return new Response<>(Boolean.TRUE);
    }


    /**
     * 获取当前登录用户关联的学校信息
     */
    public Response<CeCollege> getMyCollege() {
        try {
            LoginUser loginUser = SecurityUtil.getLoginUser();
            if (loginUser == null) return new Response<CeCollege>().failMsg("用户未登录");
            
            // 如果是系统管理员，返回列表第一个（或者提示不支持此操作）
            if (loginUser.getUserId().equals(1L)) {
                List<CeCollege> list = collegeRepo.list();
                return new Response<>(list.isEmpty() ? null : list.get(0));
            }

            // 查询该用户管理的学校
            CeCollege query = new CeCollege();
            query.setManagerId(loginUser.getUserId());
            List<CeCollege> myColleges = collegeRepo.selectCollegeList(query);
            
            if (myColleges.isEmpty()) {
                return new Response<CeCollege>().failMsg("您的账号尚未绑定任何学校");
            }
            return new Response<>(myColleges.get(0));
        } catch (Exception e) {
            return new Response<CeCollege>().failMsg("获取学校信息失败");
        }
    }

    /**
     * 判断院校代码是否存在
     *
     * @param collegeNo 院校编号
     * @param collegeId 院校id
     * @return 结果
     */
    private Boolean uniqueCollegeNo(String collegeNo, Integer collegeId) {
        CeCollege ceCollege = collegeRepo.selectCollegeByNo(collegeNo);
        if (ceCollege != null && !ceCollege.getId().equals(collegeId)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
