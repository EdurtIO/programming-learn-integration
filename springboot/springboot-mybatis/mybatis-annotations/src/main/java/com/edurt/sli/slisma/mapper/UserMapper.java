package com.edurt.sli.slisma.mapper;

import com.edurt.sli.slisma.model.UserModel;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user (username) VALUES (#{user.userName})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "user.id")
    Integer insertModel(@Param(value = "user") UserModel model);

    @Results(id = "userRequiredResults", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "userName", column = "username")
    })
    @Select(value = "SELECT id, username FROM user WHERE username = #{userName}")
    UserModel findByUserName(@Param(value = "userName") String userName);

    @Results(value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "userName", column = "username")
    })
    @Select(value = "<script>" +
            "SELECT id, username FROM user WHERE 1=1" +
            "<if test='userName != null'> and username=#{userName} </if>" +
            "</script>")
    UserModel findByUserNameAndCustomScript(@Param(value = "userName") String userName);

    @ResultMap(value = "userRequiredResults")
    @Select(value = "<script>" +
            "SELECT id, username FROM user WHERE 1=1" +
            "<if test='userName != null'> and username=#{userName} </if>" +
            "</script>")
    UserModel findByUserNameAndCommonResult(@Param(value = "userName") String userName);

    @Update(value = "UPDATE user SET userName = #{user.userName} WHERE id = #{user.id}")
    void updateModel(@Param(value = "user") UserModel model);

    @Delete(value = "DELETE FROM user WHERE id = #{id}")
    Integer deleteModel(@Param(value = "id") Integer id);

}
