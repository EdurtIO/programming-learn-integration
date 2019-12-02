# SpringBoot整合MyBatis教程Provider进阶(Insert)

本教程主要详细讲解SpringBoot整合MyBatis项目高级操作模式,主要使用到`@Provider`高级模式进行MyBatis整合开发,本文主要讲解Insert输入数据操作.

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
mvn archetype:generate -DgroupId=com.edurt.sli.slismpi -DartifactId=spring-learn-integration-springboot-mybatis-provider-insert -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
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

    <artifactId>spring-learn-integration-springboot-mybatis-provider-insert</artifactId>

    <name>SpringBoot整合MyBatis教程Provider进阶(Insert)</name>

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

- 在`src/main/java`目录下新建*com.edurt.sli.slismpi*目录并在该目录下新建`SpringBootMyBatisProviderInsertIntegration`类文件,在文件输入以下内容

```java
package com.edurt.sli.slismpi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component(value = "com.edurt.sli.slismpi")
public class SpringBootMyBatisProviderInsertIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMyBatisProviderInsertIntegration.class, args);
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

- 在`/src/main/java/com/edurt/sli/slismpi`目录下创建*config*目录,并在该目录下新建`MyBatisProviderConfig`配置类,键入以下代码

```java
package com.edurt.sli.slismpi.config;

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
@MapperScan(value = {"com.edurt.sli.slismpi.mapper"})
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
            configuration.setLogPrefix("com.edurt.sli.slismpi");
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

- 在`/src/main/java/com/edurt/sli/slismpi`目录下创建*model*目录,并在该目录下新建`UserModel`实体类,键入以下代码

```java
package com.edurt.sli.slismpi.model;

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

#### 支持数据Insert功能

---

- 在`/src/main/java/com/edurt/sli/slismpi`目录下创建*provider*目录,并在该目录下新建`UserInsertProvider`类,用于转换实体与SQL脚本,键入以下代码

```java
package com.edurt.sli.slismpi.provider;

import com.edurt.sli.slismpi.model.UserModel;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.ObjectUtils;

public class UserInsertProvider {

    private String tableName = "user";

    public String insertModelByValues(UserModel model) {
        return new SQL() {{
            INSERT_INTO(tableName);
            if (!ObjectUtils.isEmpty(model.getTitle())) {
                VALUES("title", model.getTitle());
            }
            if (!ObjectUtils.isEmpty(model.getUserName())) {
                VALUES("userName", model.getUserName());
            }
        }}.toString();
    }

}
```

我们是使用MyBatis提供的动态构建SQL功能进行构建SQL语句,需要注意的是在`VALUES`中对应的column是数据表中的字段,而不是实体类的字段.

- 在`/src/main/java/com/edurt/sli/slismpi`目录下创建*mapper*目录,并在该目录下新建**UserInsertMapper**数据表操作映射类,键入以下内容

```java
package com.edurt.sli.slismpi.mapper;

import com.edurt.sli.slismpi.model.UserModel;
import com.edurt.sli.slismpi.provider.UserInsertProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface UserInsertMapper {

    @InsertProvider(type = UserInsertProvider.class, method = "insertModelByValues")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    Integer insertModel(UserModel model);

}
```

`@InsertProvider`标记我们使用高级功能**type**指定Provider类名,**method**指定的是该类中要使用的哪一个方法

- 接下来我们在`/src/test/java`目录下新建**com.edurt.sli.slismpi**目录,并在该目录下新建**UserInsertMapperTest**测试文件,键入以下内容

```java
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


}
```

> 注意: 此时运行会出现`Error updating database.  Cause: com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Unknown column 'Test' in 'field list'`字段未找到错误,这时我们需要将Provider中的**model.getTitle()**修改为**#{title}**

#### INTO_COLUMNS ... INTO_VALUES方式

---

- 修改`UserInsertProvider`类在该类中键入以下代码

```java
public String insertModelByIntoColumnsAndValues(UserModel model) {
    return new SQL() {{
        INSERT_INTO(tableName);
        INTO_COLUMNS("title", "userName");
        INTO_VALUES("#{title}", "#{userName}");
    }}.toString();
}
```

`INTO_COLUMNS`添加的是数据库中的字段名称
`INTO_VALUES`对应添加字段的数据值

> 需要注意的是`INTO_COLUMNS`和`INTO_VALUES`必须要一一对应

- 修改`UserInsertMapper`类,添加以下代码:

```java
@InsertProvider(type = UserInsertProvider.class, method = "insertModelByIntoColumnsAndValues")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    Integer insertModelByIntoColumnsAndValues(UserModel model);
```

- 修改`UserInsertMapperTest`类,添加以下测试代码:

```java
@Test
public void testInsertModelByIntoColumnsAndValues() {
    user = new UserModel();
    user.setUserName("Test");
    this.userInsertMapper.insertModelByIntoColumnsAndValues(user);
}
```

#### 批量添加数据

---

 - 修改`UserInsertProvider`类在该类中键入以下代码支持批量添加数据操作

```java
public String insertModelByBatch(List<UserModel> list) {
    SQL sql = new SQL()
            .INSERT_INTO(tableName).
                    INTO_COLUMNS("title", "userName");
    StringBuilder sb = new StringBuilder();
    for (int i = 0, len = list.size(); i < len; i++) {
        if (i > 0) {
            sb.append(") , (");
        }
        sb.append("#{list[");
        sb.append(i);
        sb.append("].title}, ");
        sb.append("#{list[");
        sb.append(i);
        sb.append("].userName}");
    }
    sql.INTO_VALUES(sb.toString());
    return sql.toString();
}
```

> 注意: 批量插入数据的values格式：values (?, ?, ?) , (?, ?, ?) , (?, ?, ?)
> MyBatis只能读取到list参数，所以使用list[i].Filed访问变量，如 #{list[0].title}

- 修改UserInsertMapper类,添加以下代码:

```java
@InsertProvider(type = UserInsertProvider.class, method = "insertModelByBatch")
@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
Integer insertModelByBatch(List<UserModel> list);
```

- 修改`UserInsertMapperTest`类,添加以下测试代码:

```java
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
```

我们运行后返回如下错误

```java
Caused by: org.apache.ibatis.builder.BuilderException: Error invoking SqlProvider method 'public java.lang.String com.edurt.sli.slismpi.provider.UserInsertProvider.insertModelByBatch(java.util.List)' with specify parameter 'class org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap'.  Cause: org.apache.ibatis.binding.BindingException: Parameter 'arg0' not found. Available parameters are [collection, list]
	at org.apache.ibatis.builder.annotation.ProviderSqlSource.createSqlSource(ProviderSqlSource.java:169)
	at org.apache.ibatis.builder.annotation.ProviderSqlSource.getBoundSql(ProviderSqlSource.java:131)
	at org.apache.ibatis.mapping.MappedStatement.getBoundSql(MappedStatement.java:297)
	at org.apache.ibatis.executor.statement.BaseStatementHandler.<init>(BaseStatementHandler.java:64)
	at org.apache.ibatis.executor.statement.PreparedStatementHandler.<init>(PreparedStatementHandler.java:41)
	at org.apache.ibatis.executor.statement.RoutingStatementHandler.<init>(RoutingStatementHandler.java:46)
	at org.apache.ibatis.session.Configuration.newStatementHandler(Configuration.java:592)
	at org.apache.ibatis.executor.SimpleExecutor.doUpdate(SimpleExecutor.java:48)
	at org.apache.ibatis.executor.BaseExecutor.update(BaseExecutor.java:117)
	at org.apache.ibatis.executor.CachingExecutor.update(CachingExecutor.java:76)
	at org.apache.ibatis.session.defaults.DefaultSqlSession.update(DefaultSqlSession.java:197)
	at org.apache.ibatis.session.defaults.DefaultSqlSession.insert(DefaultSqlSession.java:184)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:426)
	... 37 more
Caused by: org.apache.ibatis.binding.BindingException: Parameter 'arg0' not found. Available parameters are [collection, list]
	at org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap.get(DefaultSqlSession.java:342)
	at org.apache.ibatis.builder.annotation.ProviderSqlSource.extractProviderMethodArguments(ProviderSqlSource.java:198)
	at org.apache.ibatis.builder.annotation.ProviderSqlSource.createSqlSource(ProviderSqlSource.java:146)
	... 53 more
```

通过错误我们可以看到,MyBatis在解析list的时候出现了错误导致的,无法找到**list**参数,那么这个时候我们需要修改传递参数为:

```java
@InsertProvider(type = UserInsertProvider.class, method = "insertModelByBatch")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    Integer insertModelByBatch(@Param("list") Collection<UserModel> list);
```

把我们原来的**List<UserModel> list**替换为**@Param("list") Collection<UserModel> list**

修改`UserInsertProvider`的方法**insertModelByBatch**为

```java
public String insertModelByBatch(Map map) {
    List<UserModel> models = (List<UserModel>) map.get("list");
    SQL sql = new SQL()
            .INSERT_INTO(tableName).
                    INTO_COLUMNS("title", "userName");
    StringBuilder sb = new StringBuilder();
    for (int i = 0, len = models.size(); i < len; i++) {
        if (i > 0) {
            sb.append(") , (");
        }
        sb.append("#{list[");
        sb.append(i);
        sb.append("].title}, ");
        sb.append("#{list[");
        sb.append(i);
        sb.append("].userName}");
    }
    sql.INTO_VALUES(sb.toString());
    return sql.toString();
}
```

将原来的参数**List<UserModel> list**修改为**Map map**然后在获取通过**map.get("list");**获取数据集合.

#### 打包文件部署

---

- 打包数据

```bash
mvn clean package -Dmaven.test.skip=true -X
```

运行打包后的文件即可

```bash
java -jar target/spring-learn-integration-springboot-mybatis-provider-insert-1.0.0.jar
```

#### 源码地址

---

- [GitHub](https://github.com/qianmoQ/spring-learn-integration/tree/master/springboot/springboot-mybatis/mybatis-provider/provider-insert)
- [Gitee](https://gitee.com/qianmoQ/spring-learn-integration/tree/master/springboot/springboot-mybatis/mybatis-provider/provider-insert)
