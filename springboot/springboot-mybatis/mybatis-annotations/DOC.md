# SpringBoot整合MyBatis教程(注解版)

本教程主要详细讲解SpringBoot整合MyBatis进行数据库操作,本次讲解我们使用**纯注解**方式进行。

#### 基础环境

---

|技术|版本|
|:---:|---|
|Java|1.8+|
|SpringBoot|2.x.x|
|MyBatis|3.5.x|

#### 创建项目

---

- 初始化项目

```bash
mvn archetype:generate -DgroupId=com.edurt.sli.slisma -DartifactId=spring-learn-integration-springboot-mybatis-annotations -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
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

    <artifactId>spring-learn-integration-springboot-mybatis-annotations</artifactId>

    <name>SpringBoot整合MyBatis教程(注解版)</name>

    <properties>
        <system.java.version>1.8</system.java.version>
        <plugin.maven.compiler.version>3.3</plugin.maven.compiler.version>
        <springboot.common.version>2.1.3.RELEASE</springboot.common.version>
        <springboot.mybatis.common.version>2.1.1</springboot.mybatis.common.version>
        <mysql.version>5.1.47</mysql.version>
        <lombox.version>1.18.8</lombox.version>
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

`mybatis-spring-boot-starter`:该starter是我们使用SpringBoot整合MyBatis的依赖整合包

- 在`src/main/java`目录下新建*com.edurt.sli.slisma*目录并在该目录下新建`SpringBootMyBatisIntegration`类文件,在文件输入以下内容

```java
package com.edurt.sli.slisma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component(value = "com.edurt.sli.slisma")
public class SpringBootMyBatisIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMyBatisIntegration.class, args);
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
create table user (
    id       int(20) auto_increment,
    userName varchar(20),
    primary key (id)
)
    default charset 'utf8';
```

我们运行该sql文件进行创建数据库/数据表操作.

#### 配置MyBatis数据源

---

- 在`/src/main/java/com/edurt/sli/slisma`目录下创建*config*目录,并在该目录下新建`MyBatisConfig`配置类,键入以下代码

```java
package com.edurt.sli.slisma.config;

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

@Configuration
@MapperScan(value = {"com.edurt.sli.slisma.mapper"})
public class MyBatisConfig {

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
        return sessionFactory.getObject();
    }

    @Bean
    ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            configuration.setMapUnderscoreToCamelCase(Boolean.TRUE);
            configuration.setLogPrefix("com.edurt.sli.slismat");
        };
    }

}
```

该代码中有3个Bean分别是:**transactionManager**(用于构建事务管理器),**sqlSessionFactory**(用于构建sqlSession工厂提供查询操作),**mybatisConfigurationCustomizer**(定制化MyBatis信息)

`@MapperScan`注解用于指定我们Mapper文件所在的位置

#### 配置Mapper支持数据插入(`@Insert`)

---

- 在`/src/main/java/com/edurt/sli/slisma`目录下创建*model*目录,并在该目录下新建`UserModel`实体类,键入以下代码

```java
package com.edurt.sli.slisma.model;

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

- 在`/src/main/java/com/edurt/sli/slisma`目录下创建*mapper*目录,并在该目录下新建`UserMapper`映射类,用于映射数据库和实体类,键入以下代码

```java
package com.edurt.sli.slisma.mapper;

import com.edurt.sli.slisma.model.UserModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user (username) VALUES (#{user.userName})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "user.id")
    Integer insertModel(@Param(value = "user") UserModel model);

}
```

我们在Mapper中提供了一个添加数据方法,我们使用到了`@Insert`和`@Options`注解

`@Insert`注解用于标志我们要进行数据插入操作

`@Options`该注解比较特殊,主要是用于标记我们对插入后返回数据的操作,比如我们指定了**useGeneratedKeys=true**和**keyColumn = "id"**,这样的话我们插入数据的时候会自动生成主键,**keyProperty = "user.id"**标记着我们插入数据成功后返回当前插入数据库表中的id数据.需要注意的是我们如果在传递参数的时候使用`@Param`修饰参数后,那么**keyProperty**需要指定修饰的域名,如果未进行修饰的话,我们可以直接使用实体类中的字段名称.

- 接下来我们在`/src/test/java`目录下新建**com.edurt.sli.slisma**目录,并在该目录下新建**UserMapperTest**测试文件,键入以下内容

```java
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

}
```

我们在`@Before`中初始化了一个新的实体信息

然后使用`testInsertModel`方法进行测试我们**UserMapper**提供的插入数据方法是否有效

- 在`/src/main/resources`资源目录下创建一个**application.properties**的配置文件,用于数据库连接配置,键入以下内容

```bash
server.port=8989
spring.datasource.url=jdbc:mysql://localhost:3306/spring?useSSL=false
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=123456
```

此时我们直接运行单元测试便可看到数据库表中新增的数据信息

#### 数据查询操作(`@Select`)

---

- 在`UserMapper`文件中键入以下内容支持查询操作

```java
@Results(value = {
        @Result(property = "id", column = "id", id = true),
        @Result(property = "userName", column = "username")
})
@Select(value = "SELECT id, username FROM user WHERE username = #{userName}")
UserModel findByUserName(@Param(value = "userName") String userName);
```

将顶部引入的包`import org.apache.ibatis.annotations.XXXX`修改为`import org.apache.ibatis.annotations.*`

- 在`UserMapperTest`文件中键入以下内容支持数据查询测试

```java
@Test
public void testFindByUserName() {
    Assert.assertNotNull(this.userMapper.findByUserName(value));
}
```

然后使用`testFindByUserName`方法进行测试我们UserMapper提供的查询数据方法是否有效

#### 数据查询操作(`@Select`)动态条件判断

---

我要想使用类似xml中的if-else的话我们需要用到`<script></script>`区间块标志.

- 修改`UserMapper`文件中键入以下内容支持自定义script查询操作

```java
@Results(value = {
        @Result(property = "id", column = "id", id = true),
        @Result(property = "userName", column = "username")
})
@Select(value = "<script>" +
        "SELECT id, username FROM user WHERE 1=1" +
        "<if test='userName != null'> and username=#{userName} </if>" +
        "</script>")
UserModel findByUserNameAndCustomScript(@Param(value = "userName") String userName);
```

我们在`<script></script>`区间块可以使用xml配置中的if语句

- 在`UserMapperTest`文件中键入以下内容支持自定义脚本数据查询测试

```java
@Test
public void testFindByUserNameAndCustomScript() {
    Assert.assertNotNull(this.userMapper.findByUserNameAndCustomScript(value));
}
```

> 注意: 如果字段类型是String的话test里面的字符串必需要转义,如果使用单引号的话是不支持的

#### 数据查询操作(`@Select`)共享Results结果数据

---

在实际开发中很多时候我们在不同的方法中会返回相同的结果信息,那么这个时候我们就要使用到了共享`Results`结果数据,共享结果数据很简单,我们只需要修改`@Results`在里面增加id属性(该id值是唯一的)后期在其他方法中使用即可.

- 修改`UserMapper`文件中的**findByUserName**方法在`@Results`中增加id属性,内容如下

```java
@Results(id = "userRequiredResults", value = {
        @Result(property = "id", column = "id", id = true),
        @Result(property = "userName", column = "username")
})
@Select(value = "SELECT id, username FROM user WHERE username = #{userName}")
UserModel findByUserName(@Param(value = "userName") String userName);
```

我们定义了一个id为`userRequiredResults`的返回全局结果集

- 修改`UserMapper`文件中键入以下内容支持共享结果查询操作

```java
@ResultMap(value = "userRequiredResults")
@Select(value = "<script>" +
        "SELECT id, username FROM user WHERE 1=1" +
        "<if test='userName != null'> and username=#{userName} </if>" +
        "</script>")
UserModel findByUserNameAndCommonResult(@Param(value = "userName") String userName);
```

注意此时我们使用的是`@ResultMap`注解,该注解只有一个属性,那就是value,指定的是我们需要使用的是返回哪个数据结果集

- 在`UserMapperTest`文件中键入以下内容支持共享结果集数据查询测试

```java
    @Test
public void testFindByUserNameAndCommonResult() {
    Assert.assertNotNull(this.userMapper.findByUserNameAndCommonResult(value));
}
```

关于分页的功能,我们后续会有单独文章详解

#### 数据修改操作(`@Update`)

---

- 在`UserMapper`文件中键入以下内容支持修改操作

```java
@Update(value = "UPDATE user SET userName = #{user.userName} WHERE id = #{user.id}")
void updateModel(@Param(value = "user") UserModel model);
```

- 在`UserMapperTest`文件中键入以下内容支持数据修改测试

```java
@Test
public void testUpdateModel() {
    user.setId(1);
    user.setUserName("Modify");
    this.userMapper.updateModel(user);
}
```

运行我们的测试示例,在此查询即可看到数据已经被修改

> 目前@Update值只支持返回受影响的行数,只需要修改返回值为Integer类型即可,不需要的话直接设置为void

#### 数据删除操作(@Delete)

---

- 在`UserMapper`文件中键入以下内容支持删除操作
 
```java
@Delete(value = "DELETE FROM user WHERE id = #{id}")
Integer deleteModel(@Param(value = "id") Integer id);
```

- 在`UserMapperTest`文件中键入以下内容支持数据删除测试

```java
@Test
public void testDeleteModel() {
    this.userMapper.deleteModel(1);
}
```

> 目前@Delete值只支持返回受影响的行数,只需要修改返回值为Integer类型即可,不需要的话直接设置为void

> 在MyBatis中`@Select`, `@Update`, `@Delete`, `@Insert`都是支持自定义script的

#### 打包文件部署

---

- 打包数据

```bash
mvn clean package -Dmaven.test.skip=true -X
```

运行打包后的文件即可

```bash
java -jar target/spring-learn-integration-springboot-mybatis-annotations-1.0.0.jar
```

#### 源码地址

---

- [GitHub](https://github.com/qianmoQ/spring-learn-integration/tree/master/springboot/springboot-mybatis/mybatis-annotations)
- [Gitee](https://gitee.com/qianmoQ/spring-learn-integration/tree/master/springboot/springboot-mybatis/mybatis-annotations)
