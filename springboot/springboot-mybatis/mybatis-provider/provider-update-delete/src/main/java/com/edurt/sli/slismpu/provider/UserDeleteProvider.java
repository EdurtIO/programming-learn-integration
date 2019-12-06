package com.edurt.sli.slismpu.provider;

import com.edurt.sli.slismpu.model.UserModel;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.ObjectUtils;

public class UserDeleteProvider {

    private String tableName = "user";

    public String deleteModel(UserModel user) {
        return new SQL() {{
            DELETE_FROM(tableName);
            WHERE("id=#{id}");
        }}.toString();
    }

    public String deleteModelByCustomSql(UserModel user) {
        StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM ");
        builder.append(tableName);
        builder.append(" WHERE id = ");
        builder.append(user.getId());
        return builder.toString();
    }

}
