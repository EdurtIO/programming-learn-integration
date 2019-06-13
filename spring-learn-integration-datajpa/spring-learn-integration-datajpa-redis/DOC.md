本教程主要详细讲解Spring Data Redis,它向Redis提供Spring Data平台的抽象.

Redis由基于key/value库的数据结构存数，以持久保存数据，并可用作数据库，缓存，消息代理等。

#### 基础环境

---

|技术|版本|
|:---:|---|
|Java|1.8+|
|SpringBoot|2.x.x|
|DataJPA|2.x.x|
|Jedis|2.9.x|

#### 创建项目

---

- 初始化项目

```bash
mvn archetype:generate -DgroupId=com.edurt.sli.slidr -DartifactId=spring-learn-integration-datajpa-redis -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
```

- 修改pom.xml增加redis的支持

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

    <artifactId>spring-learn-integration-datajpa-redis</artifactId>

    <name>Spring DataJPA Redis教程</name>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>${dependency.springboot2.common.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
            <version>${dependency.springboot2.common.version}</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${dependency.lombok.version}</version>
        </dependency>
        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>${dependency.jedis.version}</version>
        </dependency>
        <dependency>
            <groupId>io.lettuce</groupId>
            <artifactId>lettuce-core</artifactId>
            <version>${dependency.lettuce.version}</version>
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

`spring-boot-starter-data-redis`整合Redis需要的依赖包,或者单独使用`spring-data-redis`和`jedis`依赖包

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
package com.edurt.sli.slidr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p> SpringBootDataJPARedisIntegration </p>
 * <p> Description : SpringBootDataJPARedisIntegration </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-14 00:44 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@SpringBootApplication
public class SpringBootDataJPARedisIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDataJPARedisIntegration.class, args);
    }

}
```

#### 配置支持Redis

---

- 在resources资源目录下创建一个application.properties的配置文件,内容如下

```bash
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.database=0
spring.redis.jedis.pool.max-active=8
spring.redis.jedis.pool.max-wait=1ms
spring.redis.lettuce.pool.max-active=8
spring.redis.jedis.pool.min-idle=0
```

#### 操作Redis数据

---

- 在`/src/main/java/com/edurt/sli/slidr`目录下创建*model*目录,并在该目录下新建RedisModel文件

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
package com.edurt.sli.slidr.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * <p> RedisModel </p>
 * <p> Description : RedisModel </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-14 01:06 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@RedisHash(value = "Redis")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RedisModel {

    @Id
    private String id;
    private String name;

}
```

`@RedisHash`为每个数据库创建域类的空子类。

`@Id`注解的属性和被命名为id的属性会被当作标识属性

- 在`/src/main/java/com/edurt/sli/slidr`目录下创建*repository*目录,并在该目录下新建RedisSupport文件

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
package com.edurt.sli.slidr.repository;

import com.edurt.sli.slidr.model.RedisModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * <p> RedisSupport </p>
 * <p> Description : RedisSupport </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-14 00:59 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Repository
public interface RedisSupport extends CrudRepository<RedisModel, String> {
}
```

在`CrudRepository`中提供了一些基础的增删改查的功能.

- 模式测试增删改查的功能

在`/src/main/java/com/edurt/sli/slidr`目录下创建*controller*目录,并在该目录下新建RedisController文件

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
package com.edurt.sli.slidr.controller;

import com.edurt.sli.slidr.model.RedisModel;
import com.edurt.sli.slidr.repository.RedisSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p> RedisController </p>
 * <p> Description : RedisController </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-14 01:05 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@RestController
@RequestMapping(value = "redis")
public class RedisController {

    @Autowired
    private RedisSupport support;

    @GetMapping
    public Object get() {
        return this.support.findAll();
    }

    @PostMapping
    public Object post(@RequestBody RedisModel mode) {
        return this.support.save(mode);
    }

    @PutMapping
    public Object put(@RequestBody RedisModel mode) {
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
shicheng@shichengdeMacBook-Pro ~> curl -X POST http://localhost:8080/redis -H 'Content-Type:application/json' -d '{"id": "1", "name": "Hello Redis"}'
{"id":"1","name":"Hello Redis"}⏎
```

![-w1000](http://image.cdn.ttxit.com/2019-06-14-15604468968085.jpg)

修改数据

```bash
shicheng@shichengdeMacBook-Pro ~> curl -X PUT http://localhost:8080/redis -H 'Content-Type:application/json' -d '{"id": "1", "name": "Hello Redis Modfiy"}'
{"id":"1","name":"Hello Redis Modfiy"}⏎
```

![-w1000](http://image.cdn.ttxit.com/2019-06-14-15604469283895.jpg)

获取数据

```bash
shicheng@shichengdeMacBook-Pro ~> curl -X GET http://localhost:8080/redis
[{"id":"1","name":"Hello Redis Modfiy"}]⏎
```

删除数据

```bash
shicheng@shichengdeMacBook-Pro ~> curl -X DELETE 'http://localhost:8080/redis?id=1'
SUCCESS⏎
```

![-w1000](http://image.cdn.ttxit.com/2019-06-14-15604470076677.jpg)


#### 打包文件部署

---

- 打包数据

```bash
mvn clean package -Dmaven.test.skip=true -X
```

运行打包后的文件即可

```bash
java -jar spring-learn-integration-datajpa/spring-learn-integration-datajpa-redis/target/spring-learn-integration-datajpa-redis-1.0.0.jar
```

#### 源码地址

---

- [GitHub](https://github.com/qianmoQ/spring-learn-integration/tree/master/spring-learn-integration-datajpa/spring-learn-integration-datajpa-redis)
- [Gitee](https://gitee.com/qianmoQ/spring-learn-integration/tree/master/spring-learn-integration-datajpa/spring-learn-integration-datajpa-redis)
