package com.mock.example.modules.system.service;

import cn.hutool.core.date.DateUtil;
import com.mock.example.modules.entrance.entity.model.CeStudent;
import com.mock.example.modules.entrance.repository.ICeStudentRepo;
import com.mock.example.modules.system.entity.model.SysUserRole;
import com.mock.example.common.consts.UserConstants;
import com.mock.example.common.entity.Response;
import com.mock.example.common.exception.BizException;
import com.mock.example.common.utils.EntityCopyUtil;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.common.utils.StringUtil;
import com.mock.example.interfaces.body.system.user.RegisterBody;
import com.mock.example.interfaces.body.system.user.UserBody;
import com.mock.example.interfaces.vo.system.role.converter.RoleVoConverter;
import com.mock.example.interfaces.vo.system.user.UserDetailVo;
import com.mock.example.interfaces.vo.system.user.UserProfileDetailVo;
import com.mock.example.interfaces.vo.system.user.UserVo;
import com.mock.example.interfaces.vo.system.user.converter.UserVoConverter;
import com.mock.example.modules.system.entity.model.SysRole;
import com.mock.example.modules.system.entity.model.SysUser;
import com.mock.example.modules.system.entity.model.SysUserPost;
import com.mock.example.modules.system.entity.model.SysUserRole;
import com.mock.example.modules.system.repository.*;
import com.mock.example.modules.system.types.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用户管理业务层
 *
 * @author: Mock
 * @date: 2025-01-04 15:38:06
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserService {

    private final ISysUserRepo userRepository;

    private final ISysRoleRepo roleRepository;

    private final ISysUserRoleRepo userRoleRepository;

    private final ISysUserPostRepo userPostRepository;

    private final ISysPostRepo postRepository;
    private final ICeStudentRepo studentRepository;

    /**
     * 通过用户id 查询用户
     *
     * @param userId
     * @return 用户信息
     */
    public UserVo selectByUserId(Long userId) {
        return UserVoConverter.INSTANT.sysUserToUserVo(userRepository.selectByUserId(userId));
    }

    /**
     * 查询用户列表
     *
     * @param userBody 用户请求对象
     * @return 用户列表信息
     */
    public List<SysUser> listUser(UserBody userBody) {
        SysUser sysUser = EntityCopyUtil.copyEntity(SysUser.class, userBody);
        return userRepository.selectUserList(sysUser);
    }

    /**
     * 添加用户
     *
     * @param userBody 用户请求对象
     * @return 添加结果
     */
    @Transactional
    public Response<Boolean> addUser(UserBody userBody) {
        //用户参数校验
        if (BooleanUtils.isFalse(userRepository.checkUserNameUnique(userBody.getUserName()))) {
            return new Response<>().failMsg("新增用户'" + userBody.getUserName() + "'失败，登录账号已存在");
        }

        SysUser sysUser = EntityCopyUtil.copyEntity(SysUser.class, userBody);

        sysUser.setCreateBy(SecurityUtil.getLoginUser().getUsername());
        sysUser.setCreateTime(DateUtil.date());
        sysUser.setPassword(SecurityUtil.encryptPassword(userBody.getPassword()));

        // 新增用户
        userRepository.save(sysUser);
        // 新增用户岗位关联
        insertUserPost(sysUser);
        // 新增用户与角色管理
        insertUserRole(sysUser);

        return new Response<>(Boolean.TRUE);
    }

    /**
     * 编辑用户
     *
     * @param userBody 用户请求对象
     * @return 编辑结果
     */
    @Transactional
    public Response<Boolean> updateUser(UserBody userBody) {
        checkAdminUser(userBody);

        SysUser sysUser = EntityCopyUtil.copyEntity(SysUser.class, userBody);
        sysUser.setUpdateBy(SecurityUtil.getLoginUser().getUsername());
        //密码不更新
        sysUser.setPassword(null);
        sysUser.setUpdateTime(DateUtil.date());

        // 删除用户与角色关联
        userRoleRepository.deleteRoleByUserId(sysUser.getUserId());
        // 新增用户与角色管理
        insertUserRole(sysUser);
        // 删除用户与岗位关联
        userPostRepository.deletePostByUserId(sysUser.getUserId());
        // 新增用户与岗位管理
        insertUserPost(sysUser);
        // 更新用户信息
        userRepository.updateById(sysUser);

        return new Response<>(Boolean.TRUE);
    }

    /**
     * 删除用户
     *
     * @param userIds 用户id
     * @return 结果
     */
    @Transactional
    public Response<Boolean> deleteUser(Long[] userIds) {
        Arrays.stream(userIds).forEach(userId -> {
            UserBody userBody = new UserBody();
            userBody.setUserId(userId);
            checkAdminUser(userBody);
        });
        Arrays.stream(userIds).forEach(userId -> {
            // 删除用户与角色关联
            userRoleRepository.deleteRoleByUserId(userId);
            // 删除用户与岗位关联
            userPostRepository.deletePostByUserId(userId);
            // 删除用户i谢谢你
            userRepository.removeById(userId);
        });
        return new Response<>(Boolean.TRUE);
    }

    /**
     * 修改用户状态
     *
     * @param userBody 用户请求对象
     * @return 结果
     */
    public Boolean updateUserStatus(UserBody userBody) {
        checkAdminUser(userBody);
        userRepository.updateUserStatus(userBody.getUserId(), userBody.getStatus());
        return Boolean.TRUE;
    }

    /**
     * 重置用户密码
     *
     * @param userId   用户id
     * @param password 用户密码
     * @return 结果
     */
    public Boolean resetPwd(Long userId, String password) {
        userRepository.updatePassword(userId, SecurityUtil.encryptPassword(password));
        return Boolean.TRUE;
    }

    /**
     * 根据用户编号获取授权角色
     *
     * @param userId 用户id
     * @return 用户和角色信息
     */
    public UserDetailVo authRole(Long userId) {
        SysUser sysUser = userRepository.selectByUserId(userId);

        List<SysRole> sysRoles = roleRepository.selectRolePermissionByUserId(userId);
        List<SysRole> sysAllRoles = roleRepository.selectRoleList(new SysRole());

        //把用户没有的角色标识出来
        for (SysRole role : sysAllRoles) {
            for (SysRole userRole : sysRoles) {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue()) {
                    role.setFlag(true);
                    break;
                }
            }
        }

        return new UserDetailVo().setUser(UserVoConverter.INSTANT.sysUserToUserVo(sysUser)).setRoles(RoleVoConverter.INSTANT.sysRoleToRoleVo(sysRoles));
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户id
     * @param roleIds 角色id列表
     * @return 结果
     */
    @Transactional
    public Boolean insertAuthRole(Long userId, Long[] roleIds) {
        userRoleRepository.deleteRoleByUserId(userId);
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setRoleIds(roleIds);
        insertUserRole(user);

        return Boolean.TRUE;
    }

    /**
     * 获取用户个人中心信息
     *
     * @return 用户个人信息
     */
    public UserProfileDetailVo profile() {
        LoginUser loginUser = SecurityUtil.getLoginUser();

        UserProfileDetailVo userProfileDetailVo = new UserProfileDetailVo();
        userProfileDetailVo.setUser(loginUser.getUser());
        userProfileDetailVo.setRoleGroup(roleRepository.selectUserRoleGroup(loginUser.getUsername()));
        userProfileDetailVo.setPostGroup(postRepository.selectUserPostGroup(loginUser.getUsername()));

        return userProfileDetailVo;
    }

    /**
     * 修改用户中心
     *
     * @param userBody 用户信息
     * @return 结果
     */
    public Boolean updateProfile(UserBody userBody) {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        SysUser sysUser = loginUser.getUser();
        userBody.setUserId(sysUser.getUserId());
        userBody.setPassword(null);
        userRepository.updateById(EntityCopyUtil.copyEntity(SysUser.class, userBody));
        return Boolean.TRUE;
    }

    /**
     * 更新头像
     *
     * @param userId 用户id
     * @param avatar 头像
     * @return 结果
     */
    public Boolean updateAvatar(Long userId, String avatar) {
        userRepository.updateAvatar(userId, avatar);
        return Boolean.TRUE;
    }

    /**
     * 检查是超级管理员
     *
     * @param userBody 用户请求对象
     */
    private static void checkAdminUser(UserBody userBody) {
        if (userBody.getUserId() != null && userBody.isAdmin()) {
            throw new BizException("不允许操作超级管理员用户");
        }
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user) {
        Long[] posts = user.getPostIds();
        if (StringUtil.isNotNull(posts)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<>();
            for (Long postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0) {
                userPostRepository.saveBatch(list);
            }
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        if (StringUtil.isNotNull(user.getRoleIds())) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<>();
            for (Long roleId : user.getRoleIds()) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleRepository.saveBatch(list);
            }
        }
    }

    /**
     * 注册用户
     *
     * @param registerBody 注册用户信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class) // 【新增】添加事务，确保用户、角色、学生表同时成功或失败
    public String register(RegisterBody registerBody) {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();

        if (StringUtils.isEmpty(username)) {
            msg = "用户名不能为空";
        } else if (StringUtils.isEmpty(password)) {
            msg = "用户密码不能为空";
        } else if (username.length() < UserConstants.USERNAME_MIN_LENGTH || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            msg = "账户长度必须在2到20个字符之间";
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "密码长度必须在5到20个字符之间";
        } else if (UserConstants.NOT_UNIQUE.equals(userRepository.checkUserNameUnique(username))) {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        } else {
            SysUser sysUser = new SysUser();
            sysUser.setUserName(username);
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtil.encryptPassword(registerBody.getPassword()));
            sysUser.setCreateTime(DateUtil.date());

            // 1. 保存系统用户
            boolean regFlag = userRepository.save(sysUser);

            if (!regFlag) {
                msg = "注册失败,请联系系统管理人员";
            } else {
                // 2. 【核心修改】赋予“学生用户”角色 (ID: 100)
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(sysUser.getUserId());
                userRole.setRoleId(100L); // 对应数据库中学生角色的ID
                userRoleRepository.save(userRole);

                // 3. 【核心修改】初始化 ce_student 学生档案
                CeStudent student = new CeStudent();
                student.setUserId(sysUser.getUserId()); // 绑定刚刚生成的 userId
                student.setStudentName(sysUser.getNickName()); // 默认用昵称
                student.setCreatedTime(DateUtil.date());
                student.setCreatedUser(sysUser.getUserName());
                // 其他字段（如分数、生源地）留空，让学生登录后去“个人中心”完善
                studentRepository.save(student);
            }
        }
        return msg;
    }
    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @param oriPassword     初始密码
     * @return 结果
     */
    public String importUser(
            List<SysUser> userList,
            Boolean isUpdateSupport,
            String operName,
            String oriPassword
    ) {
        if (StringUtil.isNull(userList) || userList.size() == 0) {
            throw new BizException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = userRepository.selectUserByUserName(user.getUserName());
                if (StringUtil.isNull(u)) {
                    user.setPassword(SecurityUtil.encryptPassword(oriPassword));
                    user.setCreateBy(operName);
                    user.setCreateTime(DateUtil.date());
                    userRepository.save(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                } else if (isUpdateSupport) {
                    user.setUpdateBy(operName);
                    user.setUpdateTime(DateUtil.date());
                    userRepository.updateById(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new BizException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

}

