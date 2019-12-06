package com.edurt.sli.slismpu;

import com.edurt.sli.slismpu.mapper.UserUpdateMapper;
import com.edurt.sli.slismpu.model.UserModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(
        classes = SpringBootMyBatisProviderUpdateDeleteIntegration.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class UserUpdateMapperTest {

    @Autowired
    private UserUpdateMapper userUpdateMapper;

    private UserModel user;

    @Before
    public void before() {
        user = new UserModel();
    }

    @Test
    public void testUpdateModel() {
        user.setId(1);
        user.setTitle("XXXX");
        this.userUpdateMapper.updateModel(user);
    }

}
