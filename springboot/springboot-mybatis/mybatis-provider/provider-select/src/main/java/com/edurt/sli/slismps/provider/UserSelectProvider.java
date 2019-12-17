package com.edurt.sli.slismps.provider;

import com.edurt.sli.slismps.model.UserModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.ObjectUtils;

public class UserSelectProvider {

    private String tableName = "user";

    public String selectModel() {
        return new SQL() {{
            SELECT("userName", "title");
            FROM(tableName);
        }}.toString();
    }

    public String selectModelByWhere(@Param(value = "user") UserModel userModel) {
        return new SQL() {{
            SELECT("userName", "title");
            FROM(tableName);
            WHERE("1=1");
            if (!ObjectUtils.isEmpty(userModel) && !ObjectUtils.isEmpty(userModel.getTitle())) {
                WHERE(String.join(".", tableName, "title=" + "#{user.title}"));
            }
        }}.toString();
    }

}
