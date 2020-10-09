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
mvn archetype:generate -DgroupId=com.edurt.sli.slidrt -DartifactId=spring-learn-integration-datajpa-redis-template -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
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

    <artifactId>spring-learn-integration-datajpa-redis-template</artifactId>

    <name>Spring DataJPA Redis教程(Template版)</name>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>${dependency.springboot2.common.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-redis</artifactId>
            <version>${dependency.spring.data.jpa.version}</version>
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

`spring-data-redis`整合Redis需要的依赖包

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
package com.edurt.sli.slidrt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p> SpringBootDataJPARedisTemplateIntegration </p>
 * <p> Description : SpringBootDataJPARedisTemplateIntegration </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-14 14:24 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@SpringBootApplication
public class SpringBootDataJPARedisTemplateIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDataJPARedisTemplateIntegration.class, args);
    }

}
```

#### 配置支持Redis

---

- 在`/src/main/java/com/edurt/sli/slidr`目录下创建*config*目录,并在该目录下新建RedisConfig文件

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
package com.edurt.sli.slidrt.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * <p> RedisConfig </p>
 * <p> Description : RedisConfig </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-14 14:26 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "custom.redis")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisConfig {

    private String server; // redis服务器地址
    private Integer port; // redis服务器地址

    @Qualifier(value = "redisTemplate")
    @Bean
    public StringRedisTemplate redisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        JedisConnectionFactory factory = jedisConnectionFactory();
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory(new RedisStandaloneConfiguration(server, port));
        factory.afterPropertiesSet();
        return factory;
    }

}
```

- 在resources资源目录下创建一个application.properties的配置文件,内容如下

```bash
custom.redis.server=localhost
custom.redis.port=6379
```


#### 操作Redis数据

---

- 在`/src/main/java/com/edurt/sli/slidrt`目录下创建*model*目录,并在该目录下新建RedisTemplateModel文件

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
package com.edurt.sli.slidrt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <p> RedisTemplateModel </p>
 * <p> Description : RedisTemplateModel </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-14 14:35 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RedisTemplateModel {

    private String id;
    private String name;

}
```

- 测试增删改查的功能

在`/src/main/java/com/edurt/sli/slidrt`目录下创建*controller*目录,并在该目录下新建RedisTemplateController文件

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
package com.edurt.sli.slidrt.controller;

import com.edurt.sli.slidrt.model.RedisTemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * <p> RedisTemplateController </p>
 * <p> Description : RedisTemplateController </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-14 14:38 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@RestController
@RequestMapping(value = "redis/template")
public class RedisTemplateController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static String KEY = "RedisTemplate";

    @GetMapping
    public Object get() {
        return this.stringRedisTemplate.opsForHash().entries(KEY);
    }

    @PostMapping
    public Object post(@RequestBody RedisTemplateModel model) {
        this.stringRedisTemplate.opsForHash().put(KEY, model.getId(), model.getName());
        return "SUCCESS";
    }

    @PutMapping
    public Object put(@RequestBody RedisTemplateModel model) {
        this.stringRedisTemplate.opsForHash().put(KEY, model.getId(), model.getName());
        return "SUCCESS";
    }

    @DeleteMapping
    public Object delete(@RequestParam String id) {
        this.stringRedisTemplate.opsForHash().delete(KEY, id);
        return "SUCCESS";
    }

}
```

添加数据

```bash
shicheng@localhost ~> curl -X POST http://localhost:8080/redis/template -H 'Content-Type:application/json' -d '{"id": "1", "name": "Hello Redis"}'
SUCCESS⏎
```

![-w1000](http://image.cdn.ttxit.com/2019-06-14-15604950390771.jpg)

修改数据

```bash
shicheng@localhost ~> curl -X PUT http://localhost:8080/redis/template -H 'Content-Type:application/json' -d '{"id": "1", "name": "Hello Redis Modfiy"}'
SUCCESS⏎
```

![-w1000](http://image.cdn.ttxit.com/2019-06-14-15604950720778.jpg)

获取数据

```bash
shicheng@shichengdeMacBook-Pro ~> curl -X GET http://localhost:8080/redis/template
[{"id":"1","name":"Hello Redis Modfiy"}]⏎
```

删除数据

```bash
shicheng@shichengdeMacBook-Pro ~> curl -X DELETE 'http://localhost:8080/redis/template?id=1'
SUCCESS⏎
```

![-w1000](http://image.cdn.ttxit.com/2019-06-14-15604951147746.jpg)

#### 打包文件部署

---

- 打包数据

```bash
mvn clean package -Dmaven.test.skip=true -X
```

运行打包后的文件即可

```bash
java -jar spring-learn-integration-datajpa/spring-learn-integration-datajpa-redis-template/target/spring-learn-integration-datajpa-redis-template-1.0.0.jar
```

#### 源码地址

---

- [GitHub](https://github.com/EdurtIO/programming-learn-integration/tree/master/datajpa/datajpa-redis-template)