package com.edurt.sli.slismpi.mapper;

import com.edurt.sli.slismpi.model.UserModel;
import com.edurt.sli.slismpi.provider.UserInsertProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface UserInsertMapper {

    @InsertProvider(type = UserInsertProvider.class, method = "insertModelByValues")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    Integer insertModelByValues(UserModel model);

    @InsertProvider(type = UserInsertProvider.class, method = "insertModelByIntoColumnsAndValues")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    Integer insertModelByIntoColumnsAndValues(UserModel model);

    @InsertProvider(type = UserInsertProvider.class, method = "insertModelByBatch")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    Integer insertModelByBatch(@Param("list") Collection<UserModel> list);

}
