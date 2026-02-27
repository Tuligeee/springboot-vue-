package com.mock.example.common.component.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.mock.example.common.consts.Constants;
import com.mock.example.modules.system.entity.model.SysDictData;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典缓存
 *
 * @author: Mock
 * @date: 2025-01-18 19:17:47
 */
@Component
public class DictCache {

    @Resource(name = "dictCachePool")
    private Cache<String, List<SysDictData>> dictCachePool;

    /**
     * 获取字典缓存
     *
     * @param key 缓存key
     * @return 字典
     */
    public List<SysDictData> getCache(String key) {
        return dictCachePool.getIfPresent(Constants.SYS_DICT_KEY + key);
    }

    /**
     * 放入字典缓存
     *
     * @param key             缓存key
     * @param sysDictDataList 缓存值
     */
    public void setCache(String key, List<SysDictData> sysDictDataList) {
        dictCachePool.put(Constants.SYS_DICT_KEY + key, sysDictDataList);
    }

    /**
     * 删除缓存
     *
     * @param key 缓存key
     */
    public void removeCache(String key) {
        dictCachePool.invalidate(Constants.SYS_DICT_KEY + key);
    }

    /**
     * 清除所有缓存
     */
    public void removeAllCache(){
        dictCachePool.invalidateAll();
    }

}

  