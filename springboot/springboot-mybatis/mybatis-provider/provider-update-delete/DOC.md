# SpringBoot整合MyBatis教程Provider进阶(Update, Delete)

本教程主要详细讲解SpringBoot整合MyBatis项目高级操作模式,主要使用到`@Provider`高级模式进行MyBatis整合开发,本文主要讲解Update更新数据操作.

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
mvn archetype:generate -DgroupId=com.edurt.sli.slismpu -DartifactId=spring-learn-integration-springboot-mybatis-provider-update-delete -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
```

- 修改pom.xml增加MyBatis

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://maven.apache.org/POM/4.0.0"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <parent>
        <artifactId>spring-learn-integration-springboot-mybatis-provider</artifactId>
        <groupId>com.edurt.sli</groupId>
        <version>1.0.0</version>
    </parent>

    <modelVersion>4.0.0</modelVersion>

    <artifactId>spring-learn-integration-springboot-mybatis-provider-update-delete</artifactId>
    <name>SpringBoot整合MyBatis教程Provider进阶(Update-Delete)</name>

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

- 在`src/main/java`目录下新建*com.edurt.sli.slismpu*目录并在该目录下新建`SpringBootMyBatisProviderUpdateDeleteIntegration`类文件,在文件输入以下内容

```java
package com.edurt.sli.slismpu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component(value = "com.edurt.sli.slismpu")
public class SpringBootMyBatisProviderUpdateDeleteIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMyBatisProviderUpdateDeleteIntegration.class, args);
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
```

我们运行该sql文件进行创建数据库/数据表操作.

#### 配置MyBatis

---

- 在`/src/main/java/com/edurt/sli/slismpu`目录下创建*config*目录,并在该目录下新建`MyBatisProviderConfig`配置类,键入以下代码

```java
package com.edurt.sli.slismpu.config;

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
@MapperScan(value = {"com.edurt.sli.slismpu.mapper"})
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
            configuration.setLogPrefix("com.edurt.sli.slismpu");
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

- 在`/src/main/java/com/edurt/sli/slismpu`目录下创建*model*目录,并在该目录下新建`UserModel`实体类,键入以下代码

```java
package com.edurt.sli.slismpu.model;

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

#### 支持数据Update功能

---

- 在`/src/main/java/com/edurt/sli/slismpu`目录下创建*provider*目录,并在该目录下新建`UserUpdateProvider`类,用于转换实体与SQL脚本,键入以下代码

```java
package com.edurt.sli.slismpu.provider;

import com.edurt.sli.slismpu.model.UserModel;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.ObjectUtils;

public class UserUpdateProvider {

    private String tableName = "user";

    public String updateModel(UserModel user) {
        return new SQL() {{
            UPDATE(tableName);
            if (!ObjectUtils.isEmpty(user.getTitle())) {
                SET(String.join(".", tableName, "name = #{title}"));
            }
            if (!ObjectUtils.isEmpty(user.getUserName())) {
                SET(String.join(".", tableName, "userName = #{userName}"));
            }
            WHERE("id=#{id}");
        }}.toString();
    }

}
```

我们在做数据插入操作的时候使用的是`VALUES`,那么在更新的时候需要使用`SET`,不能在使用`VALUES`,并且一定要在SET更新的参数加上**=**用于标记,不做肤质标记的话会出现SQL转换错误问题

- 在`/src/main/java/com/edurt/sli/slismpu`目录下创建*mapper*目录,并在该目录下新建**UserUpdateMapper**数据表操作映射类,键入以下内容

```java
package com.edurt.sli.slismpu.mapper;

import com.edurt.sli.slismpu.model.UserModel;
import com.edurt.sli.slismpu.provider.UserUpdateProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.UpdateProvider;

@Mapper
public interface UserUpdateMapper {

    @UpdateProvider(type = UserUpdateProvider.class, method = "updateModel")
    Integer updateModel(UserModel model);

}
```

`@UpdateProvider`标记我们使用高级功能**type**指定Provider类名,**method**指定的是该类中要使用的哪一个方法

- 接下来我们在`/src/test/java`目录下新建**com.edurt.sli.slismpu**目录,并在该目录下新建**UserUpdateMapperTest**测试文件,键入以下内容

```java
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
```

批量更新操作,可以自己传递List集合使用JDK Stream进行SQL组装.

> 注意: 在SET中的数据一定要使用=赋值符号连接,否则会出现SQL转换错误问题

#### 支持数据Delete功能

---

- 在`com.edurt.sli.slismpu.provider`目录下新建`UserDeleteProvider`类在该类中键入以下代码

```java
package com.edurt.sli.slismpu.provider;

import com.edurt.sli.slismpu.model.UserModel;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.ObjectUtils;

public class UserDeleteProvider {

    private String tableName = "user";

    public String deleteModel(UserModel user) {
        return new SQL() {{
            DELETE_FROM(tableName);
            WHERE("id=#{id}");
        }}.toString();
    }
    
}
```

Mapper和测试同Update一致

#### 拼接SQL

---

拼接SQL很简单就是我们将一系列的参数拼接为SQL字符串.下面是一个例子.

 - 修改`UserDeleteProvider`类在该类中键入以下代码支持批量添加数据操作

```java
public String deleteModelByCustomSql(UserModel user) {
    StringBuilder builder = new StringBuilder();
    builder.append("DELETE FROM ");
    builder.append(tableName);
    builder.append(" WHERE id = ");
    builder.append(user.getId());
    return builder.toString();
}
```

> 注意: 我们在拼接SQL的时候一定要确保SQL的准确性,主要是每个参数或者关键字之间的隔离符号(空格),这也是我们最容易出现错误的地方.new SQL()底层也是使用的Buffer进行SQL拼接.

#### 打包文件部署

---

- 打包数据

```bash
mvn clean package -Dmaven.test.skip=true -X
```

运行打包后的文件即可

```bash
java -jar target/spring-learn-integration-springboot-mybatis-provider-update-delete-1.0.0.jar
```

#### 源码地址

---

- [GitHub](https://github.com/qianmoQ/spring-learn-integration/tree/master/springboot/springboot-mybatis/mybatis-provider/provider-update-delete)
- [Gitee](https://gitee.com/qianmoQ/spring-learn-integration/tree/master/springboot/springboot-mybatis/mybatis-provider/provider-update-delete)
