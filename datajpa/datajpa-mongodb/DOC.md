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
mvn archetype:generate -DgroupId=com.edurt.sli.slidm -DartifactId=spring-learn-integration-datajpa-mongodb -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
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

    <artifactId>spring-learn-integration-datajpa-mongodb</artifactId>

    <name>Spring DataJPA MongoDB教程(基础版)</name>

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
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
            <version>${dependency.springboot2.common.version}</version>
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

`spring-boot-starter-data-mongodb`整合MongoDB需要的依赖包

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
package com.edurt.sli.slidm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p> SpringBootDataJPAMongoDBIntegration </p>
 * <p> Description : SpringBootDataJPAMongoDBIntegration </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-10-18 10:44 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@SpringBootApplication
public class SpringBootDataJPAMongoDBIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDataJPAMongoDBIntegration.class, args);
    }
    
}
```

#### 配置支持MongoDB

---

- 在resources资源目录下创建一个application.properties的配置文件,内容如下

```bash
spring.data.mongodb.host=10.100.10.4
spring.data.mongodb.port=27017
spring.data.mongodb.database=test
```

#### 操作MongoDB数据

---

- 在`/src/main/java/com/edurt/sli/slidm`目录下创建*model*目录,并在该目录下新建MongoDBModel文件

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
package com.edurt.sli.slidm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <p> MongoDBModel </p>
 * <p> Description : MongoDBModel </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-10-18 10:51 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document
public class MongoDBModel {

    @Id
    private String id;

    private String title;
    private String context;

}
```

`@Document`相当于Hibernate实体的@Entity/@Table(必写)

`@Id`相当于Hibernate实体的主键@Id注解(必写)

- 在`/src/main/java/com/edurt/sli/slidm`目录下创建*repository*目录,并在该目录下新建MongoDBSupport文件

```java
package com.edurt.sli.slidm.repository; /**
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

import com.edurt.sli.slidm.model.MongoDBModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * <p> MongoDBSupport </p>
 * <p> Description : MongoDBSupport </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-10-18 10:54 </p>
 * <p> Author Eamil: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Repository
public interface MongoDBSupport extends MongoRepository<MongoDBModel, String> {
}
```

在`MongoRepository`中提供了一些基础的增删改查以及分页的功能.

- 测试增删改查的功能

在`/src/main/java/com/edurt/sli/slidm`目录下创建*controller*目录,并在该目录下新建MongoDbController文件

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
package com.edurt.sli.slidm.controller;

import com.edurt.sli.slidm.model.MongoDBModel;
import com.edurt.sli.slidm.repository.MongoDBSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p> MongoDbController </p>
 * <p> Description : MongoDbController </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-10-18 10:57 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@RestController
@RequestMapping(value = "mongodb")
public class MongoDbController {

    @Autowired
    private MongoDBSupport support;

    @GetMapping
    public Object get() {
        return this.support.findAll();
    }

    @PostMapping
    public Object post(@RequestBody MongoDBModel mode) {
        return this.support.save(mode);
    }

    @PutMapping
    public Object put(@RequestBody MongoDBModel mode) {
        return this.support.save(mode);
    }

    @DeleteMapping
    public Object delete(@RequestParam String id) {
        this.support.deleteById(id);
        return "SUCCESS";
    }

}
```

添加数据

```bash
shicheng@shichengdeMacBook-Pro ~> curl -X POST http://localhost:8080/mongodb -H 'Content-Type:application/json' -d '{"title": "Hello MongoDB", "context": "我是SpringBoot整合MongoDB示例"}'
{"id":null,"title":"Hello MongoDB","context":"我是SpringBoot整合MongoDB示例"}⏎
```

修改数据

```bash
shicheng@shichengdeMacBook-Pro ~> curl -X PUT http://localhost:8080/mongodb -H 'Content-Type:application/json' -d '{"id": 1,"title": "Hello MongoDB", "context": "我是SpringBoot整合MongoDB示例,Modfiy"}'
{"id":1,"title":"Hello MongoDB","context":"我是SpringBoot整合MongoDB示例,Modfiy"}⏎
```

获取数据

```bash
shicheng@shichengdeMacBook-Pro ~> curl -X GET http://localhost:8080/mongodb
{"content":[{"id":null,"title":"Hello MongoDB","context":"我是SpringBoot整合MongoDB示例,Modfiy"},{"id":1,"title":"Hello MongoDB","context":"我是SpringBoot整合MongoDB示例,Modfiy"}],"pageable":{"sort":{"sorted":false,"unsorted":true},"offset":0,"pageSize":2,"pageNumber":0,"paged":true,"unpaged":false},"facets":[],"aggregations":null,"scrollId":null,"totalElements":2,"totalPages":1,"size":2,"number":0,"numberOfElements":2,"first":true,"sort":{"sorted":false,"unsorted":true},"last":true}⏎
```

删除数据

```bash
shicheng@shichengdeMacBook-Pro ~> curl -X DELETE 'http://localhost:8080/mongodb?id=1'
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
java -jar target/spring-learn-integration-datajpa-mongodb-1.0.0.jar
```

#### 源码地址

---

- [GitHub](https://github.com/EdurtIO/programming-learn-integration/tree/master/datajpa/datajpa-mongodb)
