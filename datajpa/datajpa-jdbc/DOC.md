# Spring DataJPA JDBC教程(基础版)

本教程主要详细讲解SpringBoot整合JPA进行JDBC对数据库的各种操作.

#### 基础环境

---

|技术|版本|
|:---:|---|
|Java|1.8+|
|SpringBoot|2.x.x|
|DataJPA|2.x.x|
|MySQL|5.x.x|

#### 创建项目

---

- 初始化项目

```bash
mvn archetype:generate -DgroupId=com.edurt.sli.slidj -DartifactId=spring-learn-integration-datajpa-jdbc -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
```

- 修改pom.xml增加jdbc的支持

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <parent>
        <artifactId>spring-learn-integration-datajpa</artifactId>
        <groupId>com.edurt.sli</groupId>
        <version>1.0.0</version>
    </parent>

    <modelVersion>4.0.0</modelVersion>

    <artifactId>spring-learn-integration-datajpa-jdbc</artifactId>

    <name>Spring DataJPA JDBC教程(基础版)</name>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>${dependency.springboot2.common.version}</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${dependency.lombok.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
            <version>${dependency.springboot2.common.version}</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${dependency.mysql.version}</version>
        </dependency>
    </dependencies>

</project>
```

- `spring-boot-starter-data-jpa`整合JDBC需要的依赖包
- `mysql-connector-java`整合JDBC需要连接MySQL依赖包

- 一个简单的应用类

```java
/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.edurt.sli.slidj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p> SpringBootDataJPAJDBC </p>
 * <p> Description : SpringBootDataJPAJDBC </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-11-13 20:23 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@SpringBootApplication
public class SpringBootDataJPAJDBCIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDataJPAJDBCIntegration.class, args);
    }

}
```

#### 配置支持JDBC

---

- 在resources资源目录下创建一个application.properties的配置文件,内容如下

```bash
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/test?charset=utf8mb4&useSSL=false
spring.datasource.username=root
spring.datasource.password=123456
```

#### 操作JDBC数据

---

- 在`/src/main/java/com/edurt/sli/slidj`目录下创建*model*目录,并在该目录下新建JDBCModel文件

```java
/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.edurt.sli.slidj.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * <p> JDBCModel </p>
 * <p> Description : JDBCModel </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-11-13 20:39 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "jdbc")
public class JDBCModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    @Column(length = 64)
    private String context;

}
```

- `@Entity`必选的注解，声明这个类对应了一个数据库表
- `@Table` 可选的注解.声明了数据库实体对应的表信息.包括表名称、索引信息等,如果没有指定,则表名和实体的名称保持一致
- `@Id`注解的属性和被命名为id的属性会被当作标识属性
- `@Column`声明实体属性的表字段的定义.默认的实体每个属性都对应了表的一个字段.字段的名称默认和属性名称保持一致(并不一定相等).字段的类型根据实体属性类型自动推断.如果不这么声明,则系统会采用 255 作为该字段的长度

- 在`/src/main/java/com/edurt/sli/slidj`目录下创建*repository*目录,并在该目录下新建JDBCSupport文件

```java
/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.edurt.sli.slidj.repository;

import com.edurt.sli.slidj.model.JDBCModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p> JDBCSupport </p>
 * <p> Description : JDBCSupport </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-11-13 20:45 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Repository
public interface JDBCSupport extends JpaRepository<JDBCModel, Long> {
}
```

在`JpaRepository`中提供了一些基础的增删改查的功能以及分页等

- 测试增删改查的功能

在`/src/main/java/com/edurt/sli/slidj`目录下创建*controller*目录,并在该目录下新建JDBCController文件

```java
/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.edurt.sli.slidj.controller;

import com.edurt.sli.slidj.model.JDBCModel;
import com.edurt.sli.slidj.repository.JDBCSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p> JDBCController </p>
 * <p> Description : JDBCController </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-11-13 20:47 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@RestController
@RequestMapping(value = "jdbc")
public class JDBCController {

    @Autowired
    private JDBCSupport support;

    @GetMapping
    public Object get() {
        return this.support.findAll();
    }

    @PostMapping
    public Object post(@RequestBody JDBCModel mode) {
        return this.support.save(mode);
    }

    @PutMapping
    public Object put(@RequestBody JDBCModel mode) {
        return this.support.save(mode);
    }

    @DeleteMapping
    public Object delete(@RequestParam Long id) {
        this.support.deleteById(id);
        return "SUCCESS";
    }

}
```

- 启动服务使用curl或者其他http客户端进行测试基本的增删改查功能

#### JDBC数据分页功能

---

由于`JDBCSupport`文件将继承的`JpaRepository`接口,而`JpaRepository`底层继承`PagingAndSortingRepository`接口,所以我们的接口会自动提供分页功能,只需要在查询的时候增加分页配置即可

- 修改`JDBCController`增加以下代码

```java
    @GetMapping(value = "page")
    public Object get(@RequestParam Integer page,
                      @RequestParam Integer size) {
        return this.support.findAll(PageRequest.of(page, size));
    }
```

重启服务器测试分页功能

> 注意:由于jpa分页默认是从0开始,所以我们首页需要传递0

#### JDBC数据排序功能

---

在JPA中对查询数据排序和分页一样简单,我们只需要传递排序配置即可

- 修改`JDBCController`增加以下代码

```java
@GetMapping(value = "sort")
public Object sort() {
    return this.support.findAll(new Sort(Sort.Direction.DESC, "id"));
}
```

> 需要注意的是sort中的排序字段是我们实体类中的字段,并不是数据库中的字段

#### JDBC数据排序分页组合功能

---

我们已经实现分页和排序功能,那么我们需要讲解一下如何做组合,代码也很简单,具体代码如下

- 修改`JDBCController`增加以下代码

```java
@GetMapping(value = "page_sort")
public Object sort(@RequestParam Integer page,
                   @RequestParam Integer size) {
    Pageable pageable = PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "id"));
    return this.support.findAll(pageable);
}
```

代码中我们看到只需要在构建Pageable过程中传递Sort配置即可

#### 打包文件部署

---

- 打包数据

```bash
mvn clean package -Dmaven.test.skip=true -X
```

运行打包后的文件即可

```bash
java -jar target/spring-learn-integration-datajpa-jdbc-1.0.0.jar
```

#### 自动创建SQL

---

JPA提供自动创建SQL功能只需要在`application.properties`配置文件中加入以下配置

```java
spring.jpa.hibernate.ddl-auto=create
```

重启服务后,会自动创建数据表可用参数如下:

- `validate` 加载 Hibernate 时，验证创建数据库表结构
- `create` 每次加载 Hibernate ，重新创建数据库表结构，这就是导致数据库表数据丢失的原因。
- `create-drop` 加载 Hibernate 时创建，退出是删除表结构
- `update` 加载 Hibernate 自动更新数据库结构

如果你想保留表结构的数据,使用 update 即可

#### 打印SQL日志

---

在调试程序中我们需要查看出现问题的sql,那么我们需要在`application.properties`配置文件中加入以下配置

```java
spring.jpa.show-sql=true
```

此时重启服务器.控制台会打印如下sql

```bash
Hibernate: drop table if exists jdbc
Hibernate: create table jdbc (id bigint not null auto_increment, context varchar(64), name varchar(255), primary key (id)) engine=MyISAM
```

#### 源码地址

---

- [GitHub](https://github.com/qianmoQ/spring-learn-integration/tree/master/datajpa/datajpa-jdbc)
- [Gitee](https://gitee.com/qianmoQ/spring-learn-integration/tree/master/datajpa/datajpa-jdbc)
