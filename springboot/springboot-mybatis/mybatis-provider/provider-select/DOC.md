# SpringBoot整合MyBatis教程Provider进阶(Select)

本教程主要详细讲解SpringBoot整合MyBatis项目高级操作模式,主要使用到`@Provider`高级模式进行MyBatis整合开发,本文主要讲解Select查询数据操作.

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
mvn archetype:generate -DgroupId=com.edurt.sli.slismps -DartifactId=spring-learn-integration-springboot-mybatis-provider-select -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
```

- 修改pom.xml增加MyBatis

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>spring-learn-integration-springboot-mybatis-provider</artifactId>
        <groupId>com.edurt.sli</groupId>
        <version>1.0.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>spring-learn-integration-springboot-mybatis-provider-select</artifactId>
    <name>SpringBoot整合MyBatis教程Provider进阶(Select)</name>

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

- 在`src/main/java`目录下新建*com.edurt.sli.slismps*目录并在该目录下新建`SpringBootMyBatisProviderSelectIntegration`类文件,在文件输入以下内容

```java
package com.edurt.sli.slismps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component(value = "com.edurt.sli.slismps")
public class SpringBootMyBatisProviderSelectIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMyBatisProviderSelectIntegration.class, args);
    }

}
```

#### 构建SQL文件

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
    title    varchar(20),
    primary key (id)
)
    default charset 'utf8';

insert into user(userName, title)
VALUES ('1', '1'),
       ('2', '2'),
       ('3', '3'),
       ('4', '4'),
       ('5', '5'),
       ('6', '6'),
       ('7', '7'),
       ('8', '8'),
       ('9', '9');
```

我们运行该sql文件进行创建数据库/数据表操作.

#### 配置MyBatis

---

- 在`/src/main/java/com/edurt/sli/slismps`目录下创建*config*目录,并在该目录下新建`MyBatisProviderConfig`配置类,键入以下代码

```java
package com.edurt.sli.slismps.config;

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
@MapperScan(value = {"com.edurt.sli.slismps.mapper"})
public class MyBatisProviderConfig {

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
            configuration.setLogPrefix("com.edurt.sli.slismps");
        };
    }

}
```

`@MapperScan`注解用于指定我们Mapper文件所在的位置

- 在`/src/main/resources`资源目录下创建一个**application.properties**的配置文件,用于数据库连接配置,键入以下内容

```bash
server.port=8989
spring.datasource.url=jdbc:mysql://localhost:3306/spring?useSSL=false
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=123456
```

#### 配置数据模型

---

- 在`/src/main/java/com/edurt/sli/slismps`目录下创建*model*目录,并在该目录下新建`UserModel`实体类,键入以下代码

```java
package com.edurt.sli.slismps.model;

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
    private String title;

}
```

#### 支持数据Select查询功能

---

- 在`/src/main/java/com/edurt/sli/slismps`目录下创建*provider*目录,并在该目录下新建`UserSelectProvider`类,用于转换实体与SQL脚本,键入以下代码

```java
package com.edurt.sli.slismps.provider;

import org.apache.ibatis.jdbc.SQL;

public class UserSelectProvider {

    private String tableName = "user";

    public String selectModel() {
        return new SQL() {{
            SELECT("userName", "title");
            FROM(tableName);
        }}.toString();
    }

}
```

我们在做数据查询操作的时候使用的是`SELECT`,在输入值中可以输入任意查询字段,也可以使用`*`(不建议使用,会影响性能)

- 在`/src/main/java/com/edurt/sli/slismps`目录下创建*mapper*目录,并在该目录下新建**UserSelectMapper**数据表操作映射类,键入以下内容

```java
package com.edurt.sli.slismps.mapper;

import com.edurt.sli.slismps.model.UserModel;
import com.edurt.sli.slismps.provider.UserSelectProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface UserSelectMapper {

    @SelectProvider(type = UserSelectProvider.class, method = "selectModel")
    @Results(id = "userInfo", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "userName", column = "userName"),
            @Result(property = "title", column = "title")
    })
    List<UserModel> selectModel();

}
```

`@SelectProvider`标记我们使用高级功能**type**指定Provider类名,**method**指定的是该类中要使用的哪一个方法

- 接下来我们在`/src/test/java`目录下新建**com.edurt.sli.slismps**目录,并在该目录下新建**UserSelectMapperTest**测试文件,键入以下内容

```java
package com.edurt.sli.slismps;

import com.edurt.sli.slismps.mapper.UserSelectMapper;
import com.edurt.sli.slismps.model.UserModel;
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

}
```

> 注意: 在关联查询的时候必须要指定关联关系的字段

#### 支持WHERE条件筛选查询

---

- 修改`UserSelectProvider`类在该类中键入以下代码

```java
public String selectModelByWhere(@Param(value = "user") UserModel userModel) {
    return new SQL() {{
        SELECT("userName", "title");
        FROM(tableName);
        WHERE("1=1");
        if (!ObjectUtils.isEmpty(userModel) && !ObjectUtils.isEmpty(userModel.getTitle())) {
            WHERE(String.join(".", tableName, "title=" + "#{user.title}"));
        }
    }}.toString();
}
```

- 修改`UserSelectMapper`类在该类中键入以下代码

```java
@ResultMap(value = "userInfo")
@SelectProvider(type = UserSelectProvider.class, method = "selectModelByWhere")
UserModel selectModelByWhere(@Param(value = "user") UserModel userModel);
```

- 修改`UserSelectMapperTest`类在该类中键入以下代码

```java
@Test
public void testSelectModelByWhere() {
    UserModel user = new UserModel();
    user.setTitle("1");
    System.out.println(this.userSelectMapper.selectModelByWhere(user));
}
```

> 默认WHERE条件会使用AND操作符号,要想使用OR只需要在**WHERE**条件后添加`OR();`即可.
> 模糊查询的话只需要将`UserSelectProvider`中的**WHERE**条件的`=`修改为`like`注意条件前后要加`%`,还要注意SQL语句中的空格隔离问题.
> 当然我们也可以使用StringBuilder进行SQL拼接功能

#### 集成PageHelper

---

 - 修改`pom.xml`文件增加PageHelper支持

```java
<pagehelper.version>1.2.5</pagehelper.version>

<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>${pagehelper.version}</version>
</dependency>
```

- 修改`UserSelectMapperTest`类在该类中键入以下代码

```java
@Test
public void testSelectModelByPageHelper() {
    Integer pageNo = 1, pageSize = 5;
    PageHelper.startPage(pageNo, pageSize);
    this.userSelectMapper.selectModel().forEach(System.out::println);
}
```

#### 打包文件部署

---

- 打包数据

```bash
mvn clean package -Dmaven.test.skip=true -X
```

运行打包后的文件即可

```bash
java -jar target/spring-learn-integration-springboot-mybatis-provider-select-1.0.0.jar
```

#### 源码地址

---

- [GitHub](https://github.com/qianmoQ/spring-learn-integration/tree/master/springboot/springboot-mybatis/mybatis-provider/provider-select)
- [Gitee](https://gitee.com/qianmoQ/spring-learn-integration/tree/master/springboot/springboot-mybatis/mybatis-provider/provider-select)
