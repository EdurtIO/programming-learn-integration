本教程主要详细讲解Spring Data ElasticSearch,它向ElasticSearch提供Spring Data平台的抽象.

ElasticSearch是基于文档的存储,以持久保存数据,并可用作数据库,缓存,消息代理等.

#### 基础环境

---

|技术|版本|
|:---:|---|
|Java|1.8+|
|SpringBoot|2.x.x|
|DataJPA|2.x.x|
|ElasticSearch|5.x.x|

#### 创建项目

---

- 初始化项目

```bash
mvn archetype:generate -DgroupId=com.edurt.sli.slide -DartifactId=spring-learn-integration-datajpa-elasticsearch -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
```

- 修改pom.xml增加elasticsearch的支持

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

    <artifactId>spring-learn-integration-datajpa-elasticsearch</artifactId>

    <name>Spring DataJPA ElasticSearch教程(基础版)</name>

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
            <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
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

`spring-boot-starter-data-elasticsearch`整合ElasticSearch需要的依赖包

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
package com.edurt.sli.slide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p> SpringBootDataJPAElasticSearchIntegration </p>
 * <p> Description : SpringBootDataJPAElasticSearchIntegration </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-07-25 10:24 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@SpringBootApplication
public class SpringBootDataJPAElasticSearchIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDataJPAElasticSearchIntegration.class, args);
    }

}
```

#### 配置支持ElasticSearch

---

- 在resources资源目录下创建一个application.properties的配置文件,内容如下

```bash
spring.data.elasticsearch.cluster-name=es
spring.data.elasticsearch.cluster-nodes=10.10.0.17:9300
```

#### 操作ElasticSearch数据

---

- 在`/src/main/java/com/edurt/sli/slide`目录下创建*model*目录,并在该目录下新建ElasticSearchModel文件

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
package com.edurt.sli.slide.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.nio.file.attribute.FileTime;

/**
 * <p> ElasticSearchModel </p>
 * <p> Description : ElasticSearchModel </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-07-25 10:27 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "test", type = "elasticsearch", refreshInterval = "1s")
public class ElasticSearchModel {

    @Id
    private Long id;

    private String title;

    @Field(type = FieldType.Auto, index = true, store = true)
    private String context;

}
```

`@Document`相当于Hibernate实体的@Entity/@Table(必写)

|类型|属性名|默认值|描述|
|---|---|---|---|
|String|indexName|无|索引库的名称，建议以项目的名称命名
|String|type|""|类型,建议以实体的名称命名|
|short|shards|5|默认分区数|
|short|replica|1|每个分区默认的备份数|
|String|refreshInterval|1s|刷新间隔|
|String|indexStoreType|fs|索引文件存储类型|

`@Id`相当于Hibernate实体的主键@Id注解(必写)

`@Field`(相当于Hibernate实体的@Column注解),@Field默认是可以不加的,默认所有属性都会添加到ES中

|类型|属性名|默认值|说明|
|---|---|---|---|
|FileType|type|FieldType.Auto|自动检测属性的类型|
|FileType|index|FieldIndex.analyzed|默认情况下分词|
|boolean|store|false|默认情况下不存储原文|
|String|searchAnalyzer|""|指定字段搜索时使用的分词器|
|String|indexAnalyzer|""|指定字段建立索引时指定的分词器|
|String[]|ignoreFields|{}|如果某个字段需要被忽略|

- 在`/src/main/java/com/edurt/sli/slide`目录下创建*repository*目录,并在该目录下新建ElasticSearchSupport文件

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
package com.edurt.sli.slide.repository;

import com.edurt.sli.slide.model.ElasticSearchModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * <p> ElasticSearchSupport </p>
 * <p> Description : ElasticSearchSupport </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-07-25 10:36 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Repository
public interface ElasticSearchSupport extends ElasticsearchRepository<ElasticSearchModel, Long> {
}
```

在`ElasticsearchRepository`中提供了一些基础的增删改查以及分页的功能.

- 测试增删改查的功能

在`/src/main/java/com/edurt/sli/slide`目录下创建*controller*目录,并在该目录下新建ElasticSearchController文件

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
package com.edurt.sli.slide.controller;

import com.edurt.sli.slide.model.ElasticSearchModel;
import com.edurt.sli.slide.repository.ElasticSearchSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p> ElasticSearchController </p>
 * <p> Description : ElasticSearchController </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-07-25 10:39 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@RestController
@RequestMapping(value = "elasticsearch")
public class ElasticSearchController {

    @Autowired
    private ElasticSearchSupport support;

    @GetMapping
    public Object get() {
        return this.support.findAll();
    }

    @PostMapping
    public Object post(@RequestBody ElasticSearchModel mode) {
        return this.support.save(mode);
    }

    @PutMapping
    public Object put(@RequestBody ElasticSearchModel mode) {
        return this.support.save(mode);
    }

    @DeleteMapping
    public Object delete(@RequestParam String id) {
        this.support.deleteById(Long.valueOf(id));
        return "SUCCESS";
    }

}
```

添加数据

```bash
shicheng@shichengdeMacBook-Pro ~> curl -X POST http://localhost:8080/elasticsearch -H 'Content-Type:application/json' -d '{"title": "Hello ElasticSearch", "context": "我是SpringBoot整合ElasticSearch示例"}'
{"id":null,"title":"Hello ElasticSearch","context":"我是SpringBoot整合ElasticSearch示例"}⏎
```

修改数据

```bash
shicheng@shichengdeMacBook-Pro ~> curl -X PUT http://localhost:8080/elasticsearch -H 'Content-Type:application/json' -d '{"id": 1,"title": "Hello ElasticSearch", "context": "我是SpringBoot整合ElasticSearch示例,Modfiy"}'
{"id":1,"title":"Hello ElasticSearch","context":"我是SpringBoot整合ElasticSearch示例,Modfiy"}⏎
```

获取数据

```bash
shicheng@shichengdeMacBook-Pro ~> curl -X GET http://localhost:8080/elasticsearch
{"content":[{"id":null,"title":"Hello ElasticSearch","context":"我是SpringBoot整合ElasticSearch示例,Modfiy"},{"id":1,"title":"Hello ElasticSearch","context":"我是SpringBoot整合ElasticSearch示例,Modfiy"}],"pageable":{"sort":{"sorted":false,"unsorted":true},"offset":0,"pageSize":2,"pageNumber":0,"paged":true,"unpaged":false},"facets":[],"aggregations":null,"scrollId":null,"totalElements":2,"totalPages":1,"size":2,"number":0,"numberOfElements":2,"first":true,"sort":{"sorted":false,"unsorted":true},"last":true}⏎
```

删除数据

```bash
shicheng@shichengdeMacBook-Pro ~> curl -X DELETE 'http://localhost:8080/elasticsearch?id=1'
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
java -jar target/spring-learn-integration-datajpa-elasticsearch-1.0.0.jar
```

#### 源码地址

---

- [GitHub](https://github.com/qianmoQ/spring-learn-integration/tree/master/datajpa/datajpa-elasticsearch)
- [Gitee](https://gitee.com/qianmoQ/spring-learn-integration/tree/master/datajpa/datajpa-elasticsearch)
