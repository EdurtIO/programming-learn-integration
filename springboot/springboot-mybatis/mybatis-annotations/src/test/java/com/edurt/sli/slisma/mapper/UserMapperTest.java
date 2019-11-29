package com.edurt.sli.slisma.mapper;

import com.edurt.sli.slisma.SpringBootMyBatisIntegration;
import com.edurt.sli.slisma.model.UserModel;
import org.junit.After;
import org.junit.Assert;
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
        classes = SpringBootMyBatisIntegration.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class UserMapperTest {

    private UserModel user;
    private String value = "UserMapperTest";

    @Autowired
    private UserMapper userMapper;

    @Before
    public void before() {
        user = new UserModel();
        user.setUserName(value);
    }

    @After
    public void after() {

    }

    @Test
    public void testInsertModel() {
        Assert.assertTrue(this.userMapper.insertModel(user) > 0);
    }

    @Test
    public void testFindByUserName() {
        Assert.assertNotNull(this.userMapper.findByUserName(value));
    }

    @Test
    public void testFindByUserNameAndCustomScript() {
        Assert.assertNotNull(this.userMapper.findByUserNameAndCustomScript(value));
    }

    @Test
    public void testFindByUserNameAndCommonResult() {
        Assert.assertNotNull(this.userMapper.findByUserNameAndCommonResult(value));
    }

    @Test
    public void testUpdateModel() {
        user.setId(1);
        user.setUserName("Modify");
        this.userMapper.updateModel(user);
    }

    @Test
    public void testDeleteModel() {
        this.userMapper.deleteModel(1);
    }

}
