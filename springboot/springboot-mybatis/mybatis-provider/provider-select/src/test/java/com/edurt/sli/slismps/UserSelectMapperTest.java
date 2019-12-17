package com.edurt.sli.slismps;

import com.edurt.sli.slismps.mapper.UserSelectMapper;
import com.edurt.sli.slismps.model.UserModel;
import com.github.pagehelper.PageHelper;
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
        classes = SpringBootMyBatisProviderSelectIntegration.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class UserSelectMapperTest {

    @Autowired
    private UserSelectMapper userSelectMapper;

    private UserModel user;

    @Before
    public void before() {
        user = new UserModel();
    }

    @Test
    public void testSelectModel() {
        this.userSelectMapper.selectModel().forEach(System.out::println);
    }

    @Test
    public void testSelectModelByWhere() {
        UserModel user = new UserModel();
        user.setTitle("1");
        System.out.println(this.userSelectMapper.selectModelByWhere(user));
    }

    @Test
    public void testSelectModelByPageHelper() {
        Integer pageNo = 1, pageSize = 5;
        PageHelper.startPage(pageNo, pageSize);
        this.userSelectMapper.selectModel().forEach(System.out::println);
    }


}
