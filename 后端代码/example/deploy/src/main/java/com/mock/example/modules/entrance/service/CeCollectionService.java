package com.mock.example.modules.entrance.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mock.example.common.entity.Response;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.modules.entrance.entity.model.CeCollection;
import com.mock.example.modules.entrance.entity.model.CeCollege;
import com.mock.example.modules.entrance.mapper.CeCollectionMapper;
import com.mock.example.modules.entrance.repository.ICeCollegeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CeCollectionService {

    private final CeCollectionMapper collectionMapper;
    private final ICeCollegeRepo collegeRepo;

    /**
     * 添加收藏
     */
    public Response<Boolean> addCollection(CeCollection collection) {
        Long currentUserId = SecurityUtil.getUserId();
        collection.setUserId(currentUserId);
        
        QueryWrapper<CeCollection> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", currentUserId)
               .eq("target_id", collection.getTargetId())
               .eq("target_type", collection.getTargetType());
        
        if (collectionMapper.selectCount(wrapper) > 0) {
            return new Response<Boolean>().failMsg("您已经收藏过了");
        }

        return new Response<>(collectionMapper.insert(collection) > 0);
    }

    /**
     * 我的收藏列表 - 核心增强：联查学校信息
     */
    public List<Map<String, Object>> selectMyCollection() {
        Long userId = SecurityUtil.getUserId();
        QueryWrapper<CeCollection> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<CeCollection> collections = collectionMapper.selectList(wrapper);

        // 如果没有收藏，直接返回
        if (collections.isEmpty()) return new ArrayList<>();

        // 批量查出所有院校
        List<Integer> collegeIds = collections.stream()
                .filter(c -> c.getTargetType() == 2) // 院校类型
                .map(c -> c.getTargetId().intValue())
                .collect(Collectors.toList());

        Map<Integer, CeCollege> collegeMap = collegeRepo.listByIds(collegeIds).stream()
                .collect(Collectors.toMap(CeCollege::getId, c -> c));

        // 组装返回给前端的对象
        return collections.stream().map(c -> {
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("collectionId", c.getCollectionId());
            map.put("targetId", c.getTargetId());
            map.put("createTime", c.getCreateTime());
            
            CeCollege college = collegeMap.get(c.getTargetId().intValue());
            if (college != null) {
                map.put("collegeName", college.getCollegeName());
                map.put("collegeNo", college.getCollegeNo());
                map.put("city", college.getCity());
                map.put("ranking", college.getRanking());
            } else {
                map.put("collegeName", "未知学校 (可能已被删除)");
            }
            return map;
        }).collect(Collectors.toList());
    }

    /**
     * 删除收藏
     */
    public Response<Boolean> removeCollection(Integer id) {
        return new Response<>(collectionMapper.deleteById(id) > 0);
    }
}
