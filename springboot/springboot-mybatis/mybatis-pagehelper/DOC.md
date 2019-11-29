# SpringBoot整合MyBatis教程(PageHelper版)

本教程主要详细讲解SpringBoot整合MyBatis使用PageHelper进行数据查询并分页功能!

#### 基础环境

---

|技术|版本|
|:---:|---|
|Java|1.8+|
|SpringBoot|2.x.x|
|MyBatis|3.5.x|
|PageHelper|5.x.x|

#### 创建项目

---

- 初始化项目

```bash
mvn archetype:generate -DgroupId=com.edurt.sli.slismh -DartifactId=spring-learn-integration-springboot-mybatis-pagehelper -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
```

- 修改pom.xml增加MyBatis

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <parent>
        <artifactId>spring-learn-integration-springboot-mybatis</artifactId>
        <groupId>com.edurt.sli</groupId>
        <version>1.0.0</version>
    </parent>

    <modelVersion>4.0.0</modelVersion>

    <artifactId>spring-learn-integration-springboot-mybatis-pagehelper</artifactId>

    <name>SpringBoot整合MyBatis教程(PageHelper版)</name>

    <properties>
        <system.java.version>1.8</system.java.version>
        <plugin.maven.compiler.version>3.3</plugin.maven.compiler.version>
        <springboot.common.version>2.1.3.RELEASE</springboot.common.version>
        <springboot.mybatis.common.version>2.1.1</springboot.mybatis.common.version>
        <mysql.version>5.1.47</mysql.version>
        <lombox.version>1.18.8</lombox.version>
        <pagehelper.version>1.2.5</pagehelper.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>${springboot.common.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <version>${springboot.common.version}</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>${springboot.mybatis.common.version}</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql.version}</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombox.version}</version>
        </dependency>
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
            <version>${pagehelper.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>${springboot.common.version}</version>
                <configuration>
                    <fork>true</fork>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${plugin.maven.compiler.version}</version>
                <configuration>
                    <source>${system.java.version}</source>
                    <target>${system.java.version}</target>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

`pagehelper-spring-boot-starter`:该starter是我们使用MyBatis集成PageHelper插件的依赖整合包

- 在`src/main/java`目录下新建*com.edurt.sli.slismh*目录并在该目录下新建`SpringBootMyBatisPageHelperIntegration`类文件,在文件输入以下内容

```java
package com.edurt.sli.slismh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component(value = "com.edurt.sli.slismh")
public class SpringBootMyBatisPageHelperIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMyBatisPageHelperIntegration.class, args);
    }

}
```

#### 构建基本使用的SQL文件

---

在`src/main`目录下新建*example.sql*文件,键入以下内容

```sql
create database spring;

use spring;

drop table if exists user;
create table user
(
    id       int(20) auto_increment,
    userName varchar(20),
    primary key (id)
)
    default charset 'utf8';

insert into user(userName) value ('1');
insert into user(userName) value ('2');
insert into user(userName) value ('3');
insert into user(userName) value ('4');
insert into user(userName) value ('5');
insert into user(userName) value ('6');
insert into user(userName) value ('7');
insert into user(userName) value ('8');
insert into user(userName) value ('9');
insert into user(userName) value ('10');
insert into user(userName) value ('11');
```

我们运行该sql文件进行创建数据库/数据表操作.

#### 配置MyBatis数据源

---

- 在`/src/main/java/com/edurt/sli/slismh`目录下创建*config*目录,并在该目录下新建`MyBatisPageHelperConfig`配置类,键入以下代码

```java
package com.edurt.sli.slismh.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan(value = {"com.edurt.sli.slismh.mapper"})
public class MyBatisPageHelperConfig {

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager(
            @Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionFactory")
    @ConditionalOnMissingBean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("reasonable", "true");
        properties.setProperty("params", "pageNum=pageNum;pageSize=pageSize");
        pageInterceptor.setProperties(properties);
        sessionFactory.setPlugins(pageInterceptor);

        return sessionFactory.getObject();
    }

    @Bean
    ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            configuration.setMapUnderscoreToCamelCase(Boolean.TRUE);
            configuration.setLogPrefix("com.edurt.sli.slismh");
        };
    }

}
```

我们需要注意的是**sqlSessionFactory**代码中的第33-39行,这些是配置MyBatis支持PageHelper插件的代码部分,具体的配置信息可以参考PageHelper官方网站的详细配置参数,在此我们只是配置一些常用参数

`@MapperScan`注解用于指定我们Mapper文件所在的位置

#### 配置Mapper支持数据查询

---

- 在`/src/main/resources`资源目录下创建一个**application.properties**的配置文件,用于数据库连接配置,键入以下内容

```bash
server.port=8989
spring.datasource.url=jdbc:mysql://localhost:3306/spring?useSSL=false
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=123456
```

- 在`/src/main/java/com/edurt/sli/slismh`目录下创建*model*目录,并在该目录下新建`UserModel`实体类,键入以下代码

```java
package com.edurt.sli.slismh.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    private Integer id;
    private String userName;

}
```

- 在`/src/main/java/com/edurt/sli/slismh`目录下创建*mapper*目录,并在该目录下新建`UserMapper`映射类,用于映射数据库和实体类,键入以下代码

```java
package com.edurt.sli.slismh.mapper;

import com.edurt.sli.slismh.model.UserModel;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Results(value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "userName", column = "username")
    })
    @Select(value = "SELECT id, username FROM user")
    Page<UserModel> getUsers();

}
```

我们只需要提供一个查询方法即可

- 接下来我们在`/src/test/java`目录下新建**com.edurt.sli.slismh**目录,并在该目录下新建**UserMapperTest**测试文件,键入以下内容

```java
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

}
```

我们在`@Before`中初始化了一个新的实体信息

然后使用`testGetUsers`方法进行测试我们**UserMapper**提供的查询分页是否有效,注意此时会出现一个错误,信息如下:

```java
Caused by: java.lang.RuntimeException: 在系统中发现了多个分页插件，请检查系统配置!
	at com.github.pagehelper.PageHelper.skip(PageHelper.java:55)
	at com.github.pagehelper.PageInterceptor.intercept(PageInterceptor.java:92)
	at org.apache.ibatis.plugin.Plugin.invoke(Plugin.java:61)
	at com.sun.proxy.$Proxy95.query(Unknown Source)
	at com.github.pagehelper.PageInterceptor.executeAutoCount(PageInterceptor.java:201)
	at com.github.pagehelper.PageInterceptor.intercept(PageInterceptor.java:113)
	at org.apache.ibatis.plugin.Plugin.invoke(Plugin.java:61)
	at com.sun.proxy.$Proxy95.query(Unknown Source)
	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:147)
	... 53 more
```

这个错误是因为我们使用的是pagehelper集成的springboot的starter插件,该插件默认会配置一个pagehelper.,而我们也在`MyBatisPageHelperConfig`代码中进行了配置,我们只需要注释以下代码即可

```java
PageInterceptor pageInterceptor = new PageInterceptor();
Properties properties = new Properties();
properties.setProperty("helperDialect", "mysql");
properties.setProperty("reasonable", "true");
properties.setProperty("params", "pageNum=pageNum;pageSize=pageSize");
pageInterceptor.setProperties(properties);
sessionFactory.setPlugins(pageInterceptor);
```

注意我们使用`Page<E>`后不需要在使用`PageInfo<E>`去修饰数据结构了.我们运行后返回的数据如下

```java
Page{count=true, pageNum=1, pageSize=5, startRow=0, endRow=5, total=11, pages=3, reasonable=false, pageSizeZero=false}[UserModel(id=2, userName=1), UserModel(id=3, userName=2), UserModel(id=4, userName=3), UserModel(id=5, userName=4), UserModel(id=6, userName=5)]
```

#### 数据排序(orderBy)

---

使用PageHelper对数据排序分为两种

- 直接在`@Select`中增加**order by**字段
- 在`PageHelper`设置排序字段(该字段对应数据表中的字段),代码如下

```java
@Test
public void testGetUsersOrderBy() {
    Integer pageNo = 1, pageSize = 5;
    PageHelper.startPage(pageNo, pageSize);
    PageHelper.orderBy("userName");
    System.out.println(this.userMapper.getUsers());
}
```

我们运行后返回的数据如下

```java
Page{count=true, pageNum=1, pageSize=5, startRow=0, endRow=5, total=11, pages=3, reasonable=false, pageSizeZero=false}[UserModel(id=2, userName=1), UserModel(id=11, userName=10), UserModel(id=12, userName=11), UserModel(id=3, userName=2), UserModel(id=4, userName=3)]
```

如果使用倒序或者顺序则将orserBy参数进行拼接,代码如下

```java
@Test
public void testGetUsersOrderByCustom() {
    Integer pageNo = 1, pageSize = 5;
    PageHelper.startPage(pageNo, pageSize);
    PageHelper.orderBy("userName desc");
    System.out.println(this.userMapper.getUsers());
}
```

我们运行后返回的数据如下

```java
Page{count=true, pageNum=1, pageSize=5, startRow=0, endRow=5, total=11, pages=3, reasonable=false, pageSizeZero=false}[UserModel(id=10, userName=9), UserModel(id=9, userName=8), UserModel(id=8, userName=7), UserModel(id=7, userName=6), UserModel(id=6, userName=5)]
```

也许大家会有以为为什么我们看到我们每次返回的是乱的,这是因为我们的userName是字符串类型的,并不是数字类型,所以我们得到的查询结果是没有问题的!

#### 数据分组(groupBy)

---

PageHelper没有帮我们进行分组配置,直接在`@Select`中增加**group by**字段

#### 打包文件部署

---

- 打包数据

```bash
mvn clean package -Dmaven.test.skip=true -X
```

运行打包后的文件即可

```bash
java -jar target/spring-learn-integration-springboot-mybatis-pagehelper-1.0.0.jar
```

#### 源码地址

---

- [GitHub](https://github.com/qianmoQ/spring-learn-integration/tree/master/springboot/springboot-mybatis/mybatis-pagehelper)
- [Gitee](https://gitee.com/qianmoQ/spring-learn-integration/tree/master/springboot/springboot-mybatis/mybatis-pagehelper)
