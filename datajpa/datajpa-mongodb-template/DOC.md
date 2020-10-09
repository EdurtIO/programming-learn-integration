# Spring DataJPA MongoDB Template教程

本教程主要详细讲解Spring Data MongoDB,它向MongoDB提供Spring Data平台的抽象.

MongoDB是基于文档的存储,以持久保存数据,并可用作数据库,缓存,消息代理等.

#### 基础环境

---

|技术|版本|
|:---:|---|
|Java|1.8+|
|SpringBoot|2.x.x|
|DataJPA|2.x.x|
|MongoDB|3.6.3-cmongo-|

#### 创建项目

---

- 初始化项目

```bash
mvn archetype:generate -DgroupId=com.edurt.sli.slidmt -DartifactId=spring-learn-integration-datajpa-mongodb-template -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
```

- 修改pom.xml增加mongodb的支持

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

    <artifactId>spring-learn-integration-datajpa-mongodb-template</artifactId>

    <name>Spring DataJPA MongoDB教程(Template版)</name>

    <properties>
        <spring.data.mongodb.version>2.2.0.RELEASE</spring.data.mongodb.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-mongodb</artifactId>
            <version>${spring.data.mongodb.version}</version>
        </dependency>
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
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>${dependency.springboot2.common.version}</version>
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

`spring-data-mongodb`整合MongoDB需要的依赖包

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
package com.edurt.sli.slidmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p> SpringBootDataJPAMongoDBTemplateIntegration </p>
 * <p> Description : SpringBootDataJPAMongoDBTemplateIntegration </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-10-21 11:26 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@SpringBootApplication
public class SpringBootDataJPAMongoDBTemplateIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDataJPAMongoDBTemplateIntegration.class, args);
    }

}
```

#### 配置支持MongoDB

---

- 在`/src/main/java/com/edurt/sli/slidmt`目录下创建config目录,并在该目录下新建MongoDBConfig文件

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
package com.edurt.sli.slidmt.config;

import com.mongodb.MongoClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * <p> MongoDBConfig </p>
 * <p> Description : MongoDBConfig </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-10-21 11:28 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "custom.mongodb")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MongoDBConfig {

    private String server; // mongodb服务器地址
    private Integer port; // mongodb服务器地址端口
    private String database; // mongodb访问的数据库

    @Bean
    public MongoClient mongoClient() {
        return new MongoClient(server, port);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), database);
    }

}
```

- 在resources资源目录下创建一个application.properties的配置文件,内容如下

```bash
custom.mongodb.server=localhost
custom.mongodb.port=27017
custom.mongodb.database=test
```

#### 操作MongoDB数据

---

- 在`/src/main/java/com/edurt/sli/slidmt`目录下创建*model*目录,并在该目录下新建MongoDBModel文件

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
package com.edurt.sli.slidmt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <p> MongoDBModel </p>
 * <p> Description : MongoDBModel </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-10-21 11:42 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MongoDBModel {

    private String id;
    private String title;
    private String context;

}
```

- 测试增删改查的功能

在`/src/main/java/com/edurt/sli/slidmt`目录下创建*controller*目录,并在该目录下新建MongoDbController文件

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
package com.edurt.sli.slidmt.controller;

import com.edurt.sli.slidmt.model.MongoDBModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

/**
 * <p> MongoDbController </p>
 * <p> Description : MongoDbController </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-10-21 11:44 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@RestController
@RequestMapping(value = "mongodb/template")
public class MongoDbController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping
    public Object get() {
        return this.mongoTemplate.findOne(Query.query(Criteria.where("title").is("Hello MongoDB")), MongoDBModel.class);
    }

    @PostMapping
    public Object post(@RequestBody MongoDBModel model) {
        return this.mongoTemplate.save(model);
    }

    @PutMapping
    public Object put(@RequestBody MongoDBModel model) {
        Query query = new Query(Criteria.where("title").is("Hello MongoDB"));
        Update update = new Update().set("title", model.getTitle());
        return this.mongoTemplate.findAndModify(query, update, MongoDBModel.class);
    }

    @DeleteMapping
    public Object delete(@RequestParam String id) {
        return this.mongoTemplate.remove(Query.query(Criteria.where("id").is(id)));
    }

}
```

添加数据

```bash
shicheng@shichengdeMacBook-Pro ~> curl -X POST http://localhost:8080/mongodb/template -H 'Content-Type:application/json' -d '{"title": "HelloMongoDB", "context": "我是SpringBoot整合MongoDB示例"}'
{"id":"5dad2d4ea479fc579f298545","title":"HelloMongoDB","context":"我是SpringBoot整合MongoDB示例"}⏎
```

修改数据

```bash
shicheng@shichengdeMacBook-Pro ~> curl -X PUT http://localhost:8080/mongodb/template -H 'Content-Type:application/json' -d '{"title": "HelloMongoDBModfiy", "context": "我是SpringBoot整合MongoDB示例"}'
{"id":"5dad2d4ea479fc579f298545","title":"HelloMongoDBModfiy","context":"我是SpringBoot整合MongoDB示例"}⏎
```

获取数据

```bash
shicheng@shichengdeMacBook-Pro ~> curl -X GET http://localhost:8080/mongodb/template
{"id":"5dad2d4ea479fc579f298545","title":"HelloMongoDBModfiy","context":"我是SpringBoot整合MongoDB示例"}⏎
```

删除数据

```bash
shicheng@shichengdeMacBook-Pro ~> curl -X DELETE 'http://localhost:8080/mongodb/template?title=HelloMongoDB'
SUCCESS⏎
```

#### 打包文件部署

---

- 打包数据

```bash
mvn clean package -Dmaven.test.skip=true -X
```

运行打包后的文件即可

```bash
java -jar target/spring-learn-integration-datajpa-mongodb-template-1.0.0.jar
```

#### 源码地址

---

- [GitHub](https://github.com/EdurtIO/programming-learn-integration/tree/master/datajpa/datajpa-mongodb-template)