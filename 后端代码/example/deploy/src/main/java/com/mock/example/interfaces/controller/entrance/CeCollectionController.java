package com.mock.example.interfaces.controller.entrance;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mock.example.common.entity.Response;
import com.mock.example.common.utils.DateUtils;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.modules.entrance.entity.model.CeCollection;
import com.mock.example.modules.entrance.entity.model.CeCollege;
import com.mock.example.modules.entrance.entity.model.CeNews;
import com.mock.example.modules.entrance.mapper.CeCollectionMapper;
import com.mock.example.modules.entrance.mapper.CeCollegeMapper;
import com.mock.example.modules.entrance.mapper.CeNewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.mock.example.modules.system.entity.model.SysUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/entrance/collection")
public class CeCollectionController {

    @Autowired
    private CeCollectionMapper ceCollectionMapper;

    @Autowired
    private CeCollegeMapper ceCollegeMapper;

    @Autowired
    private CeNewsMapper ceNewsMapper;

    /**
     * 1. 查询是否已收藏
     */
    @GetMapping("/check")
    public Response checkCollect(CeCollection ceCollection) {
        Long userId = SecurityUtil.getUserId();
        LambdaQueryWrapper<CeCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CeCollection::getUserId, userId)
                .eq(CeCollection::getTargetId, ceCollection.getTargetId())
                .eq(CeCollection::getTargetType, ceCollection.getTargetType());

        Integer count = ceCollectionMapper.selectCount(queryWrapper);
        boolean isCollected = (count != null && count > 0);

        Response res = new Response();
        res.setCode(200);
        res.setData(isCollected);
        return res;
    }

    /**
     * 2. 切换收藏状态
     */
    @PostMapping("/toggle")
    public Response toggleCollect(@RequestBody Map<String, Object> params) {
        Response res = new Response();

        // 1. 在后台控制台打印一下前端到底传了什么过来，方便排查
        System.out.println("====== 收藏接口接收到的参数: " + params + " ======");

        // 2. 获取参数（兼容各种命名格式）
        Object tIdObj = params.get("targetId") != null ? params.get("targetId") : params.get("target_id");
        Object tTypeObj = params.get("targetType") != null ? params.get("targetType") : params.get("target_type");

        // 3. 严格防空判断
        if (tIdObj == null || String.valueOf(tIdObj).equals("null") || String.valueOf(tIdObj).trim().isEmpty()) {
            res.setCode(500);
            res.setMsg("收藏失败：未能获取到学校的有效ID，请刷新页面重试！");
            return res;
        }
        if (tTypeObj == null || String.valueOf(tTypeObj).equals("null")) {
            res.setCode(500);
            res.setMsg("收藏失败：未能获取到类型 targetType");
            return res;
        }

        // 4. 安全转换类型
        Long targetId = Long.parseLong(String.valueOf(tIdObj));
        Integer targetType = Integer.parseInt(String.valueOf(tTypeObj));
        Long userId = SecurityUtil.getUserId();

        // 5. 数据库操作
        LambdaQueryWrapper<CeCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CeCollection::getUserId, userId)
                .eq(CeCollection::getTargetId, targetId)
                .eq(CeCollection::getTargetType, targetType);

        CeCollection exist = ceCollectionMapper.selectOne(queryWrapper);
        res.setCode(200);

        if (exist != null) {
            ceCollectionMapper.deleteById(exist.getCollectionId());
            res.setMsg("已取消收藏");
        } else {
            CeCollection ceCollection = new CeCollection();
            ceCollection.setUserId(userId);
            ceCollection.setTargetId(targetId);
            ceCollection.setTargetType(targetType);
            ceCollection.setCreateTime(DateUtils.getNowDate());
            ceCollectionMapper.insert(ceCollection);
            res.setMsg("收藏成功");
        }
        return res;
    }

    /**
     * 3. 查询我的收藏列表 (多Tab通用)
     */
    @GetMapping("/list")
    public Response listMyCollection(CeCollection query) {
        Long userId = SecurityUtil.getUserId();
        LambdaQueryWrapper<CeCollection> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CeCollection::getUserId, userId);

        if (query.getTargetType() != null) {
            wrapper.eq(CeCollection::getTargetType, query.getTargetType());
        }
        wrapper.orderByDesc(CeCollection::getCreateTime);

        List<CeCollection> list = ceCollectionMapper.selectList(wrapper);
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (CeCollection item : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("collectionId", item.getCollectionId());
            map.put("targetId", item.getTargetId());
            map.put("targetType", item.getTargetType());
            map.put("createTime", item.getCreateTime());

            String title = "未知内容";
            if (item.getTargetType() == 1) {
                CeNews news = ceNewsMapper.selectById(item.getTargetId());
                if (news != null) title = news.getTitle();
            } else if (item.getTargetType() == 2) {
                CeCollege college = ceCollegeMapper.selectById(item.getTargetId());
                if (college != null) title = college.getCollegeName();
            }

            map.put("showTitle", title);
            resultList.add(map);
        }

        Response res = new Response();
        res.setCode(200);
        res.setData(resultList);
        return res;
    }
    /**
     * 新增：修改学校信息（带权限校验）
     */
    @PutMapping("/college")
    public Response updateCollege(@RequestBody CeCollege college) {
        // 1. 基础参数校验
        if (college.getId() == null) {
            Response res = new Response();
            res.setCode(500);
            res.setMsg("修改失败：学校ID不能为空");
            return res;
        }

        // 2. 获取当前登录用户
        // 注意：如果你的项目中是 SecurityUtil（不带s），请把下面的 SecurityUtils 改成 SecurityUtil
        SysUser currentUser = SecurityUtil.getLoginUser().getUser();

        // 3. 判断当前用户是否有“学校管理员”角色
        if (SecurityUtil.hasRole("school_admin")) {
            // 4. 如果是学校管理员，强制检查他修改的是不是自己绑定的学校
            if (currentUser.getCollegeId() == null || !currentUser.getCollegeId().equals(college.getId())) {
                Response res = new Response();
                res.setCode(500);
                res.setMsg("对不起，你只能修改自己学校的信息！");
                return res;
            }
        }

        // 5. 执行更新操作（使用你注入的 ceCollegeMapper）
        // 注意：这里使用了 MyBatis-Plus 自带的 updateById，如果你有自定义的 update 方法请替换
        int rows = ceCollegeMapper.updateById(college);

        Response res = new Response();
        if (rows > 0) {
            res.setCode(200);
            res.setMsg("修改成功");
        } else {
            res.setCode(500);
            res.setMsg("修改失败：未找到对应学校或数据未变化");
        }
        return res;
    }
}
