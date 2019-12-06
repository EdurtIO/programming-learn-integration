package com.edurt.sli.slismpu.provider;

import com.edurt.sli.slismpu.model.UserModel;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.ObjectUtils;

public class UserUpdateProvider {

    private String tableName = "user";

    public String updateModel(UserModel user) {
        return new SQL() {{
            UPDATE(tableName);
            if (!ObjectUtils.isEmpty(user.getTitle())) {
                SET(String.join(".", tableName, "title = #{title}"));
            }
            if (!ObjectUtils.isEmpty(user.getUserName())) {
                SET(String.join(".", tableName, "userName = #{userName}"));
            }
            WHERE("id=#{id}");
        }}.toString();
    }

}
