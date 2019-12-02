package com.edurt.sli.slismpi.provider;

import com.edurt.sli.slismpi.model.UserModel;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

public class UserInsertProvider {

    private String tableName = "user";

    public String insertModelByValues(UserModel model) {
        return new SQL() {{
            INSERT_INTO(tableName);
            if (!ObjectUtils.isEmpty(model.getTitle())) {
                VALUES("title", "#{title}");
            }
            if (!ObjectUtils.isEmpty(model.getUserName())) {
                VALUES("userName", "#{userName}");
            }
        }}.toString();
    }

    public String insertModelByIntoColumnsAndValues(UserModel model) {
        return new SQL() {{
            INSERT_INTO(tableName);
            INTO_COLUMNS("title", "userName");
            INTO_VALUES("#{title}", "#{userName}");
        }}.toString();
    }

    public String insertModelByBatch(Map map) {
        List<UserModel> models = (List<UserModel>) map.get("list");
        SQL sql = new SQL()
                .INSERT_INTO(tableName).
                        INTO_COLUMNS("title", "userName");
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = models.size(); i < len; i++) {
            if (i > 0) {
                sb.append(") , (");
            }
            sb.append("#{list[");
            sb.append(i);
            sb.append("].title}, ");
            sb.append("#{list[");
            sb.append(i);
            sb.append("].userName}");
        }
        sql.INTO_VALUES(sb.toString());
        return sql.toString();
    }

}
