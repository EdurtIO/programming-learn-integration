package com.edurt.sli.slismpu.mapper;

import com.edurt.sli.slismpu.model.UserModel;
import com.edurt.sli.slismpu.provider.UserUpdateProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.UpdateProvider;

@Mapper
public interface UserUpdateMapper {

    @UpdateProvider(type = UserUpdateProvider.class, method = "updateModel")
    Integer updateModel(UserModel model);

}
