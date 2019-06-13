#### 基础环境

---

|技术|版本|
|:---:|---|
|Java|1.8+|
|SpringCloud|Finchley.RELEASE|
|SpringBoot|2.0.3.RELEASE|

#### 创建项目

---

- 生成一个主maven项目

```bash
mvn archetype:generate -DgroupId=com.edurt.slis.slisec -DartifactId=spring-learn-integration-springcloud-eureka-code -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
```

- 删除生成文件中的src源码目录下的所有源码

- 修改pom.xml配置文件为以下内容

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <parent>
        <artifactId>spring-learn-integration-springcloud</artifactId>
        <groupId>com.edurt.sli</groupId>
        <version>1.0.0</version>
    </parent>

    <modelVersion>4.0.0</modelVersion>
    
    <artifactId>spring-learn-integration-springcloud-eureka-code</artifactId>

    <name>SpringCloud Eureka教程(代码版)</name>

    <dependencyManagement>
        <dependencies>
            <!-- spring cloud -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${dependency.spring.cloud.common.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>${dependency.gson.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>${dependency.spring.boot.common.version}</version>
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

我们首先引入了**spring-cloud-starter-netflix-eureka-server**依赖,这样的话我们才能使用eureka服务.

- 创建一个java类用于做为程序的启动入口(**SpringCloudIntegrationEurekaCodeLaunch.java**)

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
package com.edurt.slis.slisec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * <p> SpringCloudIntegrationEurekaCodeLaunch </p>
 * <p> Description : SpringCloudIntegrationEurekaCodeLaunch </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-13 18:34 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@SpringBootApplication
@EnableEurekaServer
public class SpringCloudIntegrationEurekaCodeLaunch {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudIntegrationEurekaCodeLaunch.class, args);
    }

}
```

此时该应用通过使用**@SpringBootApplication**注解可以成为SpringBoot应用,使用**@EnableEurekaServer**注解应用将成为Eureka Server.

- 为了方便后期其他应用接入进来我们创建一个应用的配置文件

在resources资源目录下创建一个**application.properties**和**application-eureka.properties**的配置文件,内容如下

**application.properties**

```bash
# 服务端口
server.port=1001
```

**application-eureka.properties**

```bash
# eureka实例配置
custom.eureka.instance.hostname=localhost
custom.eureka.client.register-with-eureka=false
custom.eureka.client.fetch-registry=false
```

- 创建使用自定义配置文件的配置类


我们经过查看eureka的源码文件后发现eureka server和eureka client的配置信息核心是依靠**EurekaServerConfigBean**和**EurekaClientConfigBean**这两个bean注入进来的,那么我们需要使用自定义的一些配置信息/文件的话,我们只需要重新设置这两个bean即可实现定制化eureka使用自定义配置文件.

在`src/main/java/com/edurt/slis/slisec`目录下新建*config*文件夹,并在该文件夹下创建`EurekaServerConfig`配置文件,内容如下:

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
package com.edurt.slis.slisec.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.cloud.netflix.eureka.server.EurekaServerConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.PropertySource;

/**
 * <p> EurekaServerConfig </p>
 * <p> Description : EurekaServerConfig </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-13 18:38 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Configuration
@PropertySource(value = "application-eureka.properties")
public class EurekaServerConfig {

    @Value(value = "${custom.eureka.instance.hostname}")
    private String hostname;

    @Value(value = "${custom.eureka.client.register-with-eureka}")
    private Boolean registerWithEureka;

    @Value(value = "${custom.eureka.client.fetch-registry}")
    private Boolean fetchRegistry;

    @Bean
    @Description(value = "使用自定义配置进行配置eureka server服务")
    public EurekaServerConfigBean eurekaConfig() {
        EurekaServerConfigBean config = new EurekaServerConfigBean();
        return config;
    }

    @Bean
    @Description(value = "使用自定义配置进行配置eureka client服务")
    public EurekaClientConfigBean eurekaClientConfigBean() {
        EurekaClientConfigBean config = new EurekaClientConfigBean();
        config.setRegisterWithEureka(registerWithEureka);
        config.setFetchRegistry(fetchRegistry);
        return config;
    }

}
```

修改配置文件后重启服务打开浏览器就可以看到eureka的UI页面.

当然eureka server和eureka client的配置不是简简单单的我文中写的两个配置,具体的配置类可以参考进入**EurekaServerConfigBean**和**EurekaClientConfigBean**源码文件中查看.

#### 打包文件部署

---

- 打包数据

```bash
mvn clean package -Dmaven.test.skip=true -X
```

运行打包后的文件即可

```bash
java -jar spring-learn-integration-springcloud/spring-learn-integration-springcloud-eureka-code/target/spring-learn-integration-springcloud-eureka-code-1.0.0.jar
```

#### 源码地址

---

- [GitHub](https://github.com/qianmoQ/spring-learn-integration/tree/master/spring-learn-integration-springcloud/spring-learn-integration-springcloud-eureka-code)
- [Gitee](https://gitee.com/qianmoQ/spring-learn-integration/tree/master/spring-learn-integration-springcloud/spring-learn-integration-springcloud-eureka-code)
