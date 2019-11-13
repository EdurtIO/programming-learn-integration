# Spring DataJPA Redis教程(JPA实战版)

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
mvn archetype:generate -DgroupId=com.edurt.sli.slidra -DartifactId=spring-learn-integration-datajpa-redis-action -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
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

    <artifactId>spring-learn-integration-datajpa-redis-action</artifactId>

    <name>Spring DataJPA Redis教程(DataJPA实战版)</name>

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
package com.edurt.sli.slidra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p> SpringBootDataJPARedisActionIntegration </p>
 * <p> Description : SpringBootDataJPARedisActionIntegration </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-11-05 10:17 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@SpringBootApplication
public class SpringBootDataJPARedisActionIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDataJPARedisActionIntegration.class, args);
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

- 在`/src/main/java/com/edurt/sli/slidra`目录下创建*model*目录,并在该目录下新建RedisModel文件

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
package com.edurt.sli.slidra.model;

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
 * <p> Create Time : 2019-11-05 10:19 </p>
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

- 在`/src/main/java/com/edurt/sli/slidra`目录下创建*repository*目录,并在该目录下新建RedisSupport文件

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
package com.edurt.sli.slidra.repository;

import com.edurt.sli.slidra.model.RedisModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * <p> RedisSupport </p>
 * <p> Description : RedisSupport </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-11-05 10:20 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Repository
public interface RedisSupport extends CrudRepository<RedisModel, String> {
}
```

在`CrudRepository`中提供了一些基础的增删改查的功能.

- 测试增删改查的功能

在`/src/main/java/com/edurt/sli/slidra`目录下创建*controller*目录,并在该目录下新建RedisController文件

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
package com.edurt.sli.slidra.controller;

import com.edurt.sli.slidra.model.RedisModel;
import com.edurt.sli.slidra.repository.RedisSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p> RedisController </p>
 * <p> Description : RedisController </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-11-05 10:21 </p>
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

- 测试基本的增删改查功能

#### Redis数据分页功能

---

- 修改`RedisSupport`文件将继承的`CrudRepository`修改为`PagingAndSortingRepository`

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
package com.edurt.sli.slidra.repository;

import com.edurt.sli.slidra.model.RedisModel;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * <p> RedisSupport </p>
 * <p> Description : RedisSupport </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-11-05 10:20 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Repository
public interface RedisSupport extends PagingAndSortingRepository<RedisModel, String> {

}
```

- 修改`RedisController`增加以下代码

```java
    @GetMapping(value = "page")
    public Object get(@RequestParam Integer page,
                      @RequestParam Integer size) {
        return this.support.findAll(PageRequest.of(page, size));
    }
```

重启服务器测试分页功能

```bash
> $ curl -X GET 'http://localhost:8080/redis/page?page=0&size=10'
{"content":[{"id":"1","name":"Hello Redis"}],"pageable":{"sort":{"sorted":false,"unsorted":true},"offset":0,"pageSize":10,"pageNumber":0,"unpaged":false,"paged":true},"last":true,"totalPages":1,"totalElements":1,"size":10,"number":0,"first":true,"numberOfElements":1,"sort":{"sorted":false,"unsorted":true}}%
```

由于jpa分页默认是从0开始,所以我们首页需要传递0

#### Redis数据清理

---

在DataJpa中我们设置对Redis数据清理只需要设置过期时间即可,可以分为两种方式设置(注意单位是按照秒计算)

- 全局数据模型过期时间设置

修改`RedisModel`,在`RedisHash`增加属性`timeToLive`

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
package com.edurt.sli.slidra.model;

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
 * <p> Create Time : 2019-11-05 10:19 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@RedisHash(value = "Redis", timeToLive = 60)
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

重启服务器添加数据等待1分钟进行数据查询

- 对数据增加过期时间字段设置

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
package com.edurt.sli.slidra.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

/**
 * <p> RedisModel </p>
 * <p> Description : RedisModel </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-11-05 10:19 </p>
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
    
    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long timeout;

}
```

> 注意: 使用RedisHash要注意数据过期后,jpa得分页查询会错误.redis数据过期,在redis底层是将数据删除的,因为在jpa工具中使用集成reids会做一层目录关系,这个关系是jpa维护的,但是数据清理过期是redis维护,完全两套隔离的,当redis清理数据后,并没有去清理jpa维护的关系,所以jpa中查询数据是有数据的,但是实际得详情数据已经被redis清理了,故关联不上返回的就是null,导致实际数据和jpa分页得数据不一致.

> 官方解释为: 将EXPIRE执行相应的命令。除了保留原始副本外，幻影副本还将保留在Redis中，并设置为在原始副本后五分钟到期。这样做是为了使存储库支持能够发布RedisKeyExpiredEvent，ApplicationEventPublisher只要密钥过期，即使原始值已被删除，Spring仍将过期值保存在Spring中。在使用Spring Data Redis存储库的所有已连接应用程序上都会收到到期事件。

#### 使用监听事件解决数据过期分页问题

---

- 在`/src/main/java/com/edurt/sli/slidra`目录下创建*config*目录,并在该目录下新建RedisListenerConfig文件

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
package com.edurt.sli.slidra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * <p> RedisListenerConfig </p>
 * <p> Description : RedisListenerConfig </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-11-05 14:02 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Configuration
public class RedisListenerConfig {

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
//        container.addMessageListener(new RedisExpiredListener(), new PatternTopic("__keyevent@0__:expired"));
        return container;
    }

}
```

- 在config目录下新建RedisKeyExpirationListener监听文件

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
package com.edurt.sli.slidra.config;

import com.edurt.sli.slidra.model.RedisModel;
import com.edurt.sli.slidra.repository.RedisSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * <p> RedisKeyExpirationListener </p>
 * <p> Description : RedisKeyExpirationListener </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-11-05 12:05 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Autowired
    private RedisSupport support;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 针对redis数据失效事件，进行数据处理
     *
     * @param message message.toString()获取失效的key
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        // 获得key之后可以做数据删除操作
        // expiredKey格式为value:key
    }

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
java -jar target/spring-learn-integration-datajpa-redis-action-1.0.0.jar
```

#### 源码地址

---

- [GitHub](https://github.com/qianmoQ/spring-learn-integration/tree/master/datajpa/datajpa-redis-action)
- [Gitee](https://gitee.com/qianmoQ/spring-learn-integration/tree/master/datajpa/datajpa-redis-action)
