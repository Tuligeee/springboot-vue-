package com.mock.example.interfaces.body.system.menu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mock.example.modules.system.entity.model.SysMenu;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 菜单body
 *
 * @author: Mock
 * @date: 2025-01-05 18:57:29
 */
@Data
public class MenuBody {

    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 路由参数
     */
    private String query;

    /**
     * 是否为外链（0是 1否）
     */
    private Integer isFrame;

    /**
     * 是否缓存（0缓存 1不缓存）
     */
    private Integer isCache;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    private String menuType;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    private String visible;

    /**
     * 菜单状态（0正常 1停用）
     */
    private String status;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 子菜单
     */
    private List<SysMenu> children = Lists.newArrayList();

    /**
     * 请求参数
     */
    private Map<String, Object> params = Maps.newHashMap();
}

  