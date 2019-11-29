package com.edurt.sli.slismh.mapper;

import com.edurt.sli.slismh.SpringBootMyBatisPageHelperIntegration;
import com.github.pagehelper.PageHelper;
import org.junit.After;
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
        classes = SpringBootMyBatisPageHelperIntegration.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Before
    public void before() {
    }

    @After
    public void after() {

    }

    @Test
    public void testGetUsers() {
        Integer pageNo = 1, pageSize = 5;
        PageHelper.startPage(pageNo, pageSize);
        System.out.println(this.userMapper.getUsers());
    }

    @Test
    public void testGetUsersOrderBy() {
        Integer pageNo = 1, pageSize = 5;
        PageHelper.startPage(pageNo, pageSize);
        PageHelper.orderBy("userName");
        System.out.println(this.userMapper.getUsers());
    }

    @Test
    public void testGetUsersOrderByCustom() {
        Integer pageNo = 1, pageSize = 5;
        PageHelper.startPage(pageNo, pageSize);
        PageHelper.orderBy("userName desc");
        System.out.println(this.userMapper.getUsers());
    }

}
