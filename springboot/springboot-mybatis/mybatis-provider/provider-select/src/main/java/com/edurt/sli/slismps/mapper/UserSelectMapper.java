package com.edurt.sli.slismps.mapper;

import com.edurt.sli.slismps.model.UserModel;
import com.edurt.sli.slismps.provider.UserSelectProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserSelectMapper {

    @SelectProvider(type = UserSelectProvider.class, method = "selectModel")
    @Results(id = "userInfo", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "userName", column = "userName"),
            @Result(property = "title", column = "title")
    })
    List<UserModel> selectModel();

    @ResultMap(value = "userInfo")
    @SelectProvider(type = UserSelectProvider.class, method = "selectModelByWhere")
    UserModel selectModelByWhere(@Param(value = "user") UserModel userModel);

}
