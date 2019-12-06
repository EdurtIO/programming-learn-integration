package com.edurt.sli.slismpu.mapper;

import com.edurt.sli.slismpu.model.UserModel;
import com.edurt.sli.slismpu.provider.UserDeleteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.UpdateProvider;

@Mapper
public interface UserDeleteMapper {

    @UpdateProvider(type = UserDeleteProvider.class, method = "deleteModel")
    Integer deleteModel(UserModel model);

    @UpdateProvider(type = UserDeleteProvider.class, method = "deleteModelByCustomSql")
    Integer deleteModelByCustomSql(UserModel model);

}
