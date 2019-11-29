package com.edurt.sli.slismh.mapper;

import com.edurt.sli.slismh.model.UserModel;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Results(value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "userName", column = "username")
    })
    @Select(value = "SELECT id, username FROM user")
    Page<UserModel> getUsers();

}
