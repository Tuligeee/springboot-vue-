package com.mock.example.interfaces.vo.system.post.converter;

import com.mock.example.interfaces.vo.system.post.PostVo;
import com.mock.example.modules.system.entity.model.SysPost;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 角色对象装换器
 *
 * @author: Mock
 * @date: 2025-01-04 16:24:16
 */
@Mapper
public interface PostVoConverter {

    PostVoConverter INSTANT = Mappers.getMapper(PostVoConverter.class);

    /**
     * {@link SysPost} -> {@link PostVo}
     */
    PostVo sysPostToPostVo(SysPost sysPost);
    List<PostVo> sysPostToPostVo(List<SysPost> sysPosts);

}

  