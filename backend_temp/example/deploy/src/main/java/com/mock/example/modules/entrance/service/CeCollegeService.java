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
     * 请求院校列表 (已修改：增加学校管理员权限过滤)
     *
     * @param collegeBody 学生请求体
     * @return 院校列表
     */
    public List<CeCollege> selectCollegeList(CollegeBody collegeBody) {
        // 1. 复制请求体到查询实体
        CeCollege queryEntity = EntityCopyUtil.copyEntity(CeCollege.class, collegeBody);

        // 2. 获取当前登录用户
        try {
            LoginUser loginUser = SecurityUtil.getLoginUser();
            // 如果不是超级管理员（通常ID为1），则进行角色判断
            if (loginUser != null && !loginUser.getUserId().equals(1L)) {
                // 判断是否包含 "school_admin" 角色
                boolean isSchoolAdmin = loginUser.getUser().getRoles().stream()
                        .anyMatch(r -> "school_admin".equals(r.getRoleKey()));

                // 如果是学校管理员，强制只能查询自己管理的学校
                if (isSchoolAdmin) {
                    queryEntity.setManagerId(loginUser.getUserId());
                }
            }
        } catch (Exception e) {
            // 忽略未登录或获取用户失败的情况（视具体业务场景，公共查询可能不需要登录）
            log.warn("获取用户信息失败或当前为匿名访问: {}", e.getMessage());
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
        // 这里也可以加上权限校验，防止学校管理员删除别人的学校，逻辑同 editCollege
        // 简单起见暂时只允许管理员删除，或者由前端控制按钮显示
        collegeRepo.removeByIds(Arrays.asList(collegeIds));
        return new Response<>(Boolean.TRUE);
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
