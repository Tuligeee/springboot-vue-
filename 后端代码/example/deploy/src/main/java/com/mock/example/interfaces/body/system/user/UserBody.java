package com.mock.example.interfaces.body.system.user;

import com.google.common.collect.Maps;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;



/**
 * 用户列表对象
 *
 * @author: Mock
 * @date: 2025-01-04 16:03:44
 */
@Data
public class UserBody implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户性别（0男 1女 2未知）
     */
    private String sex;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户类型（00系统用户）
     */
    private String userType;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 角色组
     */
    private Long[] roleIds;

    /**
     * 用户分配的某个角色
     */
    private Long roleId;

    /**
     * 岗位组
     */
    private Long[] postIds;

    /**
     * 请求参数
     */
    private Map<String, Object> params = Maps.newHashMap();

    /**
     * 是否管理员
     *
     * @return 结果
     */
    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

}
