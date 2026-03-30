package com.mock.example.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.mock.example.common.consts.Constants;
import com.mock.example.common.consts.UserConstants;
import com.mock.example.common.entity.Response;
import com.mock.example.common.entity.TreeSelect;
import com.mock.example.common.utils.EntityCopyUtil;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.common.utils.StringUtil;
import com.mock.example.interfaces.body.system.menu.MenuBody;
import com.mock.example.interfaces.vo.system.menu.MenuVo;
import com.mock.example.interfaces.vo.system.menu.RoleMenuTreeSelectVo;
import com.mock.example.interfaces.vo.system.menu.converter.MenuVoConverter;
import com.mock.example.interfaces.vo.system.router.MetaVo;
import com.mock.example.interfaces.vo.system.router.RouterVo;
import com.mock.example.modules.system.entity.model.SysMenu;
import com.mock.example.modules.system.repository.ISysMenuRepo;
import com.mock.example.modules.system.repository.ISysRoleMenuRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单
 *
 * @author: Mock
 * @date: 2025-01-04 09:00:39
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuService {

    private final ISysMenuRepo sysMenuRepository;

    private final ISysRoleMenuRepo sysRoleMenuRepository;

    /**
     * 查询菜单列表
     *
     * @param menuBody 菜单请求对象
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuList(MenuBody menuBody) {
        Long userId = SecurityUtil.getUserId();
        SysMenu menu = EntityCopyUtil.copyEntity(SysMenu.class, menuBody);
        // 管理员显示所有菜单信息
        return sysMenuRepository.selectMenuList(menu, userId);
    }

    /**
     * 通过menuId获取菜单
     *
     * @param menuId 菜单id
     * @return 菜单信息
     */
    public MenuVo selectMenuById(Long menuId) {
        SysMenu sysMenu = sysMenuRepository.getById(menuId);
        return MenuVoConverter.INSTANT.sysMenuToMenuVo(sysMenu);
    }

    /**
     * 新增菜单
     *
     * @param menuBody 菜单请求对象
     * @return 结果
     */
    public Response addMenu(MenuBody menuBody) {
        SysMenu sysMenu = EntityCopyUtil.copyEntity(SysMenu.class, menuBody);

        // 检查
        if (!sysMenuRepository.checkMenuNameUnique(sysMenu)) {
            return new Response().failMsg("新增菜单'" + menuBody.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menuBody.getIsFrame()) && !StringUtil.ishttp(sysMenu.getPath())) {
            return new Response().failMsg("新增菜单'" + sysMenu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }

        sysMenu.setCreateTime(DateUtil.date());
        sysMenu.setCreateBy(SecurityUtil.getUsername());
        sysMenuRepository.save(sysMenu);

        return new Response().ok();
    }

    /**
     * 修改菜单
     *
     * @param menuBody 菜单请求对象
     * @return 结果
     */
    public Response editMenu(MenuBody menuBody) {
        SysMenu sysMenu = EntityCopyUtil.copyEntity(SysMenu.class, menuBody);

        // 检查
        if (!sysMenuRepository.checkMenuNameUnique(sysMenu)) {
            return new Response().failMsg("修改菜单'" + menuBody.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menuBody.getIsFrame()) && !StringUtil.ishttp(sysMenu.getPath())) {
            return new Response().failMsg("修改菜单'" + sysMenu.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (menuBody.getMenuId().equals(menuBody.getParentId())) {
            return new Response().failMsg("修改菜单'" + sysMenu.getMenuName() + "'失败，上级菜单不能选择自己");
        }

        sysMenu.setUpdateTime(DateUtil.date());
        sysMenu.setUpdateBy(SecurityUtil.getUsername());
        sysMenuRepository.updateById(sysMenu);

        return new Response().ok();
    }

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     * @return 结果
     */
    public Response deleteMenu(Long menuId) {
        if (CollUtil.isNotEmpty(sysMenuRepository.selectMenuChildList(menuId))) {
            return new Response().failMsg("存在子菜单,不允许删除");
        }
        if (CollUtil.isNotEmpty(sysRoleMenuRepository.selectRoleMenuByMenuId(menuId))) {
            return new Response().failMsg("菜单已分配,不允许删除");
        }
        sysMenuRepository.removeById(menuId);

        return new Response().ok();
    }

    /**
     * 构建用户菜单路由
     *
     * @return 路由信息
     */
    public List<RouterVo> buildMenus() {
        Long userId = SecurityUtil.getUserId();
        List<SysMenu> menus = sysMenuRepository.selectMenuTreeByUserId(userId);

        List<com.mock.example.modules.system.entity.model.SysRole> roles = SecurityUtil.getLoginUser().getUser().getRoles();
        boolean isAdmin = roles.stream().anyMatch(r -> "admin".equals(r.getRoleKey()) || r.getRoleId() == 1L);
        boolean isSchoolAdmin = roles.stream().anyMatch(r -> "school_admin".equals(r.getRoleKey()));
        boolean isStudent = roles.stream().anyMatch(r -> r.getRoleId() == 11L || "common".equals(r.getRoleKey()) || "student".equals(r.getRoleKey()));

        // 如果用户身上多个角色叠加，优先考虑管理员，其次学校，其次学生
        if (isAdmin && isSchoolAdmin) isSchoolAdmin = false;
        if (isAdmin && isStudent) isStudent = false;
        if (isSchoolAdmin && isStudent) isStudent = false;

        customizeMenuNamesByRole(menus, isAdmin, isSchoolAdmin, isStudent);

        return buildMenus(menus);
    }

    /**
     * 动态转换不同角色的显示菜单名称以增强体验
     */
    private void customizeMenuNamesByRole(List<SysMenu> menus, boolean isAdmin, boolean isSchoolAdmin, boolean isStudent) {
        if (menus == null || menus.isEmpty()) return;
        for (SysMenu menu : menus) {
            String originalName = menu.getMenuName();
            if (isAdmin) {
                if ("院校管理".equals(originalName) || "院校查询".equals(originalName)) menu.setMenuName("全国院校库维护");
                if ("专业管理".equals(originalName) || "专业查询".equals(originalName)) menu.setMenuName("全国专业库维护");
                if ("录取分数线".equals(originalName)) menu.setMenuName("院校录取分数维护");
                if ("历年分数线".equals(originalName) || "省控线管理".equals(originalName)) menu.setMenuName("各省省控线维护");
            } else if (isSchoolAdmin) {
                if ("院校管理".equals(originalName) || "院校查询".equals(originalName)) menu.setMenuName("本校基础信息维护");
                if ("专业管理".equals(originalName) || "专业查询".equals(originalName)) menu.setMenuName("本校招生专业设置");
                if ("录取分数线".equals(originalName)) menu.setMenuName("本校历年录取分数");
                if ("历年分数线".equals(originalName) || "省控线管理".equals(originalName)) menu.setMenuName("各省省控线查询");
            } else if (isStudent) {
                if ("院校管理".equals(originalName) || "院校查询".equals(originalName)) menu.setMenuName("全国高校信息查询");
                if ("专业管理".equals(originalName) || "专业查询".equals(originalName)) menu.setMenuName("高校开设专业查询");
                if ("录取分数线".equals(originalName)) menu.setMenuName("各校录取分数查询");
                if ("历年分数线".equals(originalName) || "省控线管理".equals(originalName)) menu.setMenuName("各省省控线查询");
                if ("模拟填报".equals(originalName) || "在线志愿填报".equals(originalName)) menu.setMenuName("历年数据模拟查询");
                if ("志愿单".equals(originalName) || "志愿单管理".equals(originalName)) menu.setMenuName("我的志愿草稿表");
            }
            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                customizeMenuNamesByRole(menu.getChildren(), isAdmin, isSchoolAdmin, isStudent);
            }
        }
    }

    /**
     * 获取菜单下拉树列表
     *
     * @param menuBody 菜单请求对象
     * @return 菜单下拉树
     */
    public List<TreeSelect> buildMenuTreeSelect(MenuBody menuBody) {
        SysMenu sysMenu = EntityCopyUtil.copyEntity(SysMenu.class, menuBody);
        List<SysMenu> menus = sysMenuRepository.selectMenuList(sysMenu, SecurityUtil.getUserId());
        List<SysMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 加载对应角色菜单列表树
     *
     * @param roleId 角色id
     * @return 角色菜单下拉树
     */
    public RoleMenuTreeSelectVo buildRoleMenuTreeSelect(Long roleId) {
        List<SysMenu> menus = sysMenuRepository.selectMenuList(new SysMenu(), SecurityUtil.getUserId());
        List<SysMenu> menuTrees = buildMenuTree(menus);
        List<Integer> checkedKeys = sysMenuRepository.selectMenuListByRoleId(roleId);

        return new RoleMenuTreeSelectVo()
                .setCheckedKeys(checkedKeys)
                .setMenus(menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList()));
    }


    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    public static List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        List<Long> tempList = new ArrayList<Long>();
        for (SysMenu dept : menus) {
            tempList.add(dept.getMenuId());
        }
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext(); ) {
            SysMenu menu = (SysMenu) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private static void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private static List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private static boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    /**
     * 构建菜单路由
     *
     * @param menus 用户菜单
     * @return 路由信息
     */
    private static List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<>();

        for (SysMenu menu : menus) {

            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(
                    new MetaVo(
                            menu.getMenuName(),
                            menu.getIcon(),
                            menu.getIsCache() == 1,
                            menu.getPath())
            );

            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {

                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {

                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(
                        new MetaVo(
                                menu.getMenuName(),
                                menu.getIcon(),
                                menu.getIsCache() == 1,
                                menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {

                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/inner");
                List<RouterVo> childrenList = new ArrayList<>();
                RouterVo children = new RouterVo();
                String routerPath = StringUtils.replaceEach(
                        menu.getPath(),
                        new String[]{Constants.HTTP, Constants.HTTPS},
                        new String[]{"", ""});
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public static String getRouteName(SysMenu menu) {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public static String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            routerPath = StringUtils.replaceEach(routerPath, new String[]{Constants.HTTP, Constants.HTTPS},
                    new String[]{"", ""});
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && 1 == menu.getIsFrame()) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public static String getComponent(SysMenu menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public static boolean isMenuFrame(SysMenu menu) {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && 1 == menu.getIsFrame();
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public static boolean isInnerLink(SysMenu menu) {
        return 1 == menu.getIsFrame() && StringUtil.ishttp(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public static boolean isParentView(SysMenu menu) {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }


}

  