package com.mock.example.common.config.security.handler;

import com.mock.example.common.exception.BizException;
import com.mock.example.modules.system.entity.enums.UserStatus;
import com.mock.example.modules.system.entity.model.SysUser;
import com.mock.example.modules.system.repository.ISysUserRepo;
import com.mock.example.modules.system.service.SysPermissionService;
import com.mock.example.modules.system.types.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户认证自定义实现
 *
 * @author: Mock
 * @date: 2023-02-26 20:18:40
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ISysUserRepo sysUserRepository;

    private final SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserRepository.selectUserByUserName(username);

        if (user == null) {

            log.info("登录用户名: {} 不存在", username);
            throw new BizException("登录用户名：" + username + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {

            log.info("登录用户名：{} 已被删除.", username);
            throw new BizException("对不起，您的账号：" + username + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {

            log.info("登录用户名：{} 已被停用.", username);
            throw new BizException("对不起，您的账号：" + username + " 已停用");
        } else {
            //do nothing
        }

        return new LoginUser(
                user.getUserId(),
                user.getDeptId(),
                user,
                permissionService.getMenuPermission(user));
    }

}

  