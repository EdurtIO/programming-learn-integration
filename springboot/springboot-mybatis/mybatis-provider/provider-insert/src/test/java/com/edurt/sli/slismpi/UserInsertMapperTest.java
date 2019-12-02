package com.edurt.sli.slismpi;

import com.edurt.sli.slismpi.mapper.UserInsertMapper;
import com.edurt.sli.slismpi.model.UserModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(
        classes = SpringBootMyBatisProviderInsertIntegration.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class UserInsertMapperTest {

    @Autowired
    private UserInsertMapper userInsertMapper;

    private UserModel user;

    @Before
    public void before() {
        user = new UserModel();
    }

    @Test
    public void testInsertModelByValues() {
        // userName is null
        user.setTitle("Test");
        this.userInsertMapper.insertModelByValues(user);
        // title is null
        user = new UserModel();
        user.setUserName("Test");
        this.userInsertMapper.insertModelByValues(user);
    }

    @Test
    public void testInsertModelByIntoColumnsAndValues() {
        user = new UserModel();
        user.setUserName("Test");
        this.userInsertMapper.insertModelByIntoColumnsAndValues(user);
    }

    @Test
    public void testInsertModelByBatch() {
        List<UserModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserModel user = new UserModel();
            user.setUserName(String.valueOf(i));
            user.setTitle(String.valueOf(i));
            list.add(user);
        }
        this.userInsertMapper.insertModelByBatch(list);
    }

}
