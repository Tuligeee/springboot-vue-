package com.mock.example.modules.entrance.service;

import cn.hutool.core.util.StrUtil;
import com.mock.example.common.entity.Response;
import com.mock.example.common.utils.EntityCopyUtil;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.interfaces.body.entrance.student.StudentBody;
import com.mock.example.interfaces.vo.entrance.student.StudentVo;
import com.mock.example.modules.entrance.entity.enums.TagEnum;
import com.mock.example.modules.entrance.entity.model.CeStudent;
import com.mock.example.modules.entrance.entity.model.CeTagRel;
import com.mock.example.modules.entrance.repository.ICeStudentRepo;
import com.mock.example.modules.entrance.repository.ICeTagRelRepo;
import com.mock.example.modules.system.entity.model.SysUser;
import com.mock.example.modules.system.repository.ISysUserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 学生管理业务层
 *
 * @author: Mock
 * @date: 2025-01-31 20:06:03
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CeStudentService {

    private final ICeStudentRepo studentRepo;

    private final ICeTagRelRepo tagRelRepo;

    private final ISysUserRepo userRepo;


    /**
     * 请求学生列表
     *
     * @param studentBody 学生请求体
     * @return 学生列表
     */
    public List<CeStudent> selectStudentList(StudentBody studentBody) {
        Long userId = SecurityUtil.getUserId();
        //非管理员，只展示个人信息
        if (BooleanUtils.isFalse(SecurityUtil.isAdmin(userId))) {
            studentBody.setUserId(userId);
        }

        //查询学生列表
        List<CeStudent> ceStudents = studentRepo.selectStudentList(
                EntityCopyUtil.copyEntity(CeStudent.class, studentBody)
        );

        //用户账号map
        List<Long> userIds = ceStudents.stream().map(CeStudent::getUserId).collect(Collectors.toList());
        Map<Long, String> userNameMap = userRepo.selectUserByUserIds(userIds).stream().collect(Collectors.toMap(
                SysUser::getUserId, SysUser::getUserName, (v1, v2) -> v1
        ));

        //学生标签map
        Map<Integer, List<CeTagRel>> tagRelMap = tagRelRepo.selectTagsByTypeAndRelIds(
                //通过 标签类型 和 关联的学生id列表 查询出关联标签
                TagEnum.STUDENT,
                ceStudents.stream().map(CeStudent::getId).collect(Collectors.toList())
        ).stream().collect(
                //根据关联的学生id,对标签进行分组。
                Collectors.groupingBy(CeTagRel::getRelId)
        );

        ceStudents.forEach(
                ceStudent -> {
                    //把用户名填充进去
                    ceStudent.setUserName(
                            Optional.ofNullable(userNameMap.get(ceStudent.getUserId())).orElse("")
                    );
                    //把标签填充进去
                    ceStudent.generateTagNameText(tagRelMap.get(ceStudent.getId()));
                });

        return ceStudents;

    }

    /**
     * 添加学生
     *
     * @param studentBody 学生请求体
     * @return 结果
     */
    public Response<Boolean> addStudent(StudentBody studentBody) {
        //检查学生编号是否重复
        if (BooleanUtils.isFalse(uniqueStudentNo(studentBody.getStudentNo()))) {
            return new Response<>().failMsg("保存学生信息失败 学生编号'" + studentBody.getStudentNo() + "'已存在");
        }

        CeStudent ceStudent = EntityCopyUtil.copyEntity(CeStudent.class, studentBody);
        //有输入用户账号的话，绑定下userid
        if (StrUtil.isNotBlank(studentBody.getUserName())) {
            SysUser sysUser = userRepo.selectUserByUserName(studentBody.getUserName());
            //用户不存在
            if (sysUser == null) {
                return new Response<>().failMsg("保存学生信息失败 用户名'" + studentBody.getUserName() + "'不存在");
            }
            //用户已经绑定过学生
            if (BooleanUtils.isFalse(uniqueUserId(sysUser.getUserId(), null))) {
                return new Response<>().failMsg("保存学生信息失败 用户名'" + studentBody.getUserName() + "'已经绑定过学生");
            }

            ceStudent.setUserId(sysUser.getUserId());
        }
        ceStudent.setCreatedUser(SecurityUtil.getUsername());
        studentRepo.save(ceStudent);

        return new Response<>(Boolean.TRUE);
    }

    /**
     * 编辑学生
     *
     * @param studentBody 学生请求体
     * @return 结果
     */
    public Response<Boolean> editStudent(StudentBody studentBody) {
        //检查学生编号是否重复
        CeStudent uniqueStudent = studentRepo.selectStudentByNo(studentBody.getStudentNo());
        if (uniqueStudent != null && !uniqueStudent.getStudentNo().equals(studentBody.getStudentNo())) {
            return new Response<>().failMsg("编辑学生信息失败 学生编号'" + studentBody.getStudentNo() + "'已存在");
        }

        CeStudent ceStudent = EntityCopyUtil.copyEntity(CeStudent.class, studentBody);
        //有输入用户账号的话，绑定下userid
        if (StrUtil.isNotBlank(studentBody.getUserName())) {
            SysUser sysUser = userRepo.selectUserByUserName(studentBody.getUserName());
            if (sysUser == null) {
                return new Response<>().failMsg("编辑学生信息失败 用户名'" + studentBody.getUserName() + "'不存在");
            }
            //用户已经绑定过学生
            if (BooleanUtils.isFalse(uniqueUserId(sysUser.getUserId(), studentBody.getId()))) {
                return new Response<>().failMsg("标记学生信息失败 用户名'" + studentBody.getUserName() + "'已经绑定过学生");
            }
            ceStudent.setUserId(sysUser.getUserId());
        }
        ceStudent.setUpdatedUser(SecurityUtil.getUsername());
        studentRepo.updateById(ceStudent);

        return new Response<>(Boolean.TRUE);
    }

    /**
     * 通过学生id查询学生信息
     *
     * @param studentId 学生id
     * @return 结果
     */
    public StudentVo getStudent(Integer studentId) {
        CeStudent ceStudent = studentRepo.getById(studentId);
        StudentVo studentVo = EntityCopyUtil.copyEntity(StudentVo.class, ceStudent);

        if (ceStudent.getUserId() != null) {
            SysUser sysUser = userRepo.selectByUserId(ceStudent.getUserId());
            if (sysUser != null) {
                studentVo.setUserName(sysUser.getUserName());
            }
        }
        return studentVo;
    }

    /**
     * 删除学生
     *
     * @param studentIds 角色id列表
     * @return 结果
     */
    public Response<Boolean> deleteStudentByIds(Integer[] studentIds) {
        studentRepo.removeByIds(Arrays.asList(studentIds));
        return new Response<>(Boolean.TRUE);
    }

    /**
     * 判断学生编号是否唯一
     *
     * @param studentNo 学生编号
     * @return 结果
     */
    private Boolean uniqueStudentNo(String studentNo) {
        CeStudent ceStudent = studentRepo.selectStudentByNo(studentNo);
        return ceStudent == null;
    }

    /**
     * 查询用户id是否已经绑定过学生
     *
     * @param userId    用户id
     * @param studentId 学生id
     * @return 结果
     */
    private Boolean uniqueUserId(Long userId, Integer studentId) {
        CeStudent ceStudent = studentRepo.selectStudentByUserId(userId);
        return ceStudent == null || ceStudent.getId().equals(studentId);
    }

}

  