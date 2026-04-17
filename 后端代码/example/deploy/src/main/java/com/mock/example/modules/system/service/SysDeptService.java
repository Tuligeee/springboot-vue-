package com.mock.example.modules.system.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.mock.example.common.consts.UserConstants;
import com.mock.example.common.entity.Response;
import com.mock.example.common.entity.TreeSelect;
import com.mock.example.common.exception.BizException;
import com.mock.example.common.utils.EntityCopyUtil;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.common.utils.StringUtil;
import com.mock.example.interfaces.body.system.dept.DeptBody;
import com.mock.example.interfaces.vo.system.dept.DeptVo;
import com.mock.example.interfaces.vo.system.dept.RoleDeptTreeSelectVo;
import com.mock.example.interfaces.vo.system.dept.converter.DeptVoConverter;
import com.mock.example.modules.system.entity.model.SysDept;
import com.mock.example.modules.system.repository.ISysDeptRepo;
import com.mock.example.modules.system.repository.ISysUserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门业务层
 *
 * @author: Mock
 * @date: 2025-01-04 18:50:45
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDeptService {

    private final ISysDeptRepo deptRepository;

    private final ISysUserRepo userRepository;

    /**
     * 部门列表
     *
     * @param deptBody 部门请求对象
     * @return 部门列表
     */
    public List<SysDept> selectDeptList(DeptBody deptBody) {
        SysDept sysDept = EntityCopyUtil.copyEntity(SysDept.class, deptBody);
        return deptRepository.selectDeptList(sysDept);
    }

    /**
     * 查询部门列表（排除节点）
     *
     * @param deptId 部门id
     * @return 部门列表
     */
    public List<DeptVo> excludeChild(Long deptId){
        List<SysDept> depts = deptRepository.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext()) {
            SysDept d =  it.next();
            if (d.getDeptId().intValue() == deptId
                    || ArrayUtils.contains(StringUtil.split(d.getAncestors(), ","), deptId + "")) {
                it.remove();
            }
        }

        return DeptVoConverter.INSTANT.sysDeptToDeptVo(depts);
    }

    /**
     * 根据部门编号获取详细信息
     *
     * @param deptId 部门id
     * @return 部门信息
     */
    public DeptVo selectDeptById(Long deptId) {
        return EntityCopyUtil.copyEntity(DeptVo.class, deptRepository.getById(deptId));
    }

    /**
     * 新增部门
     *
     * @param deptBody 部门请求对象
     * @return 结果
     */
    public Response addDept(DeptBody deptBody) {
        SysDept sysDept = EntityCopyUtil.copyEntity(SysDept.class, deptBody);
        if (!deptRepository.checkDeptNameUnique(sysDept)) {
            return new Response<>().failMsg("新增部门'" + sysDept.getDeptName() + "'失败，部门名称已存在");
        }
        sysDept.setCreateBy(SecurityUtil.getUsername());
        sysDept.setCreateTime(DateUtil.date());

        SysDept parentDept = deptRepository.getById(sysDept.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(parentDept.getStatus())) {
            throw new BizException("部门停用，不允许新增");
        }

        sysDept.setAncestors(parentDept.getAncestors() + "," + parentDept.getParentId());
        deptRepository.save(sysDept);

        return new Response<>(Boolean.TRUE);
    }

    /**
     * 修改部门
     *
     * @param deptBody 部门请求对象
     * @return 结果
     */
    @Transactional
    public Response editDept(DeptBody deptBody) {
        SysDept sysDept = EntityCopyUtil.copyEntity(SysDept.class, deptBody);
        if (!deptRepository.checkDeptNameUnique(sysDept)) {

            return new Response<>().failMsg("修改部门'" + sysDept.getDeptName() + "'失败，部门名称已存在");
        } else if (deptBody.getDeptId().equals(deptBody.getParentId())) {

            return new Response<>().failMsg("修改部门'" + sysDept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtil.equals(UserConstants.DEPT_DISABLE, deptBody.getStatus())
                && deptRepository.selectNormalChildrenDeptById(deptBody.getDeptId()) > 0) {

            return new Response().failMsg("该部门包含未停用的子部门！");
        }

        updateDept(sysDept);

        return new Response<>(Boolean.TRUE);
    }

    /**
     * 删除部门
     *
     * @param deptId 部门id
     * @return 结果
     */
    public Response deleteDept(Long deptId){
        if (deptRepository.hasChildByDeptId(deptId)) {
            return new Response().failMsg("存在下级部门,不允许删除");
        }
        if (userRepository.checkDeptExistUser(deptId)) {
            return new Response().failMsg("部门存在用户,不允许删除");
        }

        deptRepository.removeById(deptId);

        return new Response<>(Boolean.TRUE);
    }

    /**
     * 入库部门
     *
     * @param sysDept 部门
     */
    private void updateDept(SysDept sysDept) {
        sysDept.setUpdateBy(SecurityUtil.getUsername());
        sysDept.setUpdateTime(DateUtil.date());

        SysDept newParentDept = deptRepository.getById(sysDept.getParentId());
        SysDept oldDept = deptRepository.getById(sysDept.getDeptId());
        if (StringUtil.isNotNull(newParentDept) && StringUtil.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            sysDept.setAncestors(newAncestors);
            updateDeptChildren(sysDept.getDeptId(), newAncestors, oldAncestors);
        }

        deptRepository.updateById(sysDept);
        if (UserConstants.DEPT_NORMAL.equals(sysDept.getStatus()) && StringUtils.isNotEmpty(sysDept.getAncestors())
                && !StringUtils.equals("0", sysDept.getAncestors())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(sysDept);
        }
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(SysDept dept) {
        String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        deptRepository.updateDeptStatusNormal(deptIds);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = deptRepository.selectChildrenDeptById(deptId);
        for (SysDept child : children) {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (children.size() > 0) {
            deptRepository.updateDeptChildren(children);
        }
    }


    /**
     * 获取部门下拉列表
     *
     * @param deptBody 部门请求对象
     * @return 部门下拉列表
     */
    public List<TreeSelect> buildDeptTreeSelect(DeptBody deptBody) {
        SysDept sysDept = EntityCopyUtil.copyEntity(SysDept.class, deptBody);
        List<SysDept> depts = deptRepository.selectDeptList(sysDept);
        List<SysDept> deptTrees = buildDeptTree(depts);

        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 加载对应角色部门列表树
     *
     * @param roleId 角色id
     * @return 角色对应部门树
     */
    public RoleDeptTreeSelectVo buildRoleDeptTreeSelect(Long roleId) {
        List<SysDept> depts = deptRepository.selectDeptList(new SysDept());
        List<SysDept> deptTrees = buildDeptTree(depts);
        List<Integer> checkedKeys = deptRepository.selectDeptListByRoleId(roleId);

        return new RoleDeptTreeSelectVo().setCheckedKeys(checkedKeys).setDepts(deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList()));
    }

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    public static List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<SysDept>();
        List<Long> tempList = new ArrayList<Long>();

        for (SysDept dept : depts) {
            tempList.add(dept.getDeptId());
        }

        for (Iterator<SysDept> iterator = depts.iterator(); iterator.hasNext(); ) {
            SysDept dept = (SysDept) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private static void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private static List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<SysDept>();
        Iterator<SysDept> it = list.iterator();
        while (it.hasNext()) {
            SysDept n = it.next();
            if (StringUtil.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getDeptId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private static boolean hasChild(List<SysDept> list, SysDept t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}

  