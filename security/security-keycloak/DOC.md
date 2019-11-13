今天我们尝试Spring Boot Security整合Keycloak，并决定建立一个非常简单的Spring Boot微服务，使用Keycloak作为我的身份验证源，使用Spring Security处理身份验证和授权。

#### 基础环境

---

|技术|版本|
|:---:|---|
|Java|1.8+|
|SpringBoot|1.5.x|
|KeyCloak|任意版本|

#### 设置Keycloak

---

- 首先我们需要一个Keycloak实例，让我们启动Jboss提供的Docker容器：

```bash
docker run -d \
  --name spring-learn-integration-springboot-security-keycloak \
  -e KEYCLOAK_USER=admin \
  -e KEYCLOAK_PASSWORD=admin \
  -p 9001:8080 \
  jboss/keycloak
```

- 在此之后，我们只需登录到容器并导航到`bin`文件夹。

```bash
docker exec -it spring-learn-integration-springboot-security-keycloak /bin/bash
cd keycloak/bin
```

- 首先，我们需要从CLI客户端登录keycloak服务器，之后我们不再需要身份验证：

```bash
./kcadm.sh config credentials --server http://localhost:8080/auth --realm master --user admin --password admin
```

#### 配置realm

---

- 首先，我们需要创建一个realm:

```bash
./kcadm.sh create realms -s realm=spring-learn-integration-springboot-security-keycloak -s enabled=true

Created new realm with id 'spring-learn-integration-springboot-security-keycloak'
```

- 之后，我们需要创建2个客户端，这将为我们的应用程序提供身份验证。首先我们创建一个cURL客户端，这样我们就可以通过命令行命令登录：

```bash
./kcadm.sh create clients -r spring-learn-integration-springboot-security-keycloak -s clientId=curl -s enabled=true -s publicClient=true -s baseUrl=http://localhost:8080 -s adminUrl=http://localhost:8080 -s directAccessGrantsEnabled=true

Created new client with id '8f0481cd-3bbb-4659-850f-6088466a4d89'
```

重要的是要注意2个选项：`publicClient=true`和 `directAccessGrantsEnabled=true`。第一个使这个客户端公开，这意味着我们的cURL客户端可以在不提供任何秘密的情况下启动登录。第二个使我们能够使用用户名和密码直接登录。

- 其次，我们创建了一个由REST服务使用的客户端：

```bash
./kcadm.sh create clients -r spring-learn-integration-springboot-security-keycloak -s clientId=spring-learn-integration-springboot-security-keycloak-client -s enabled=true -s baseUrl=http://localhost:8080 -s bearerOnly=true

Created new client with id 'ab9d404e-6d5b-40ac-9bc3-9e2e26b68213'
```

这里的重要配置是`bearerOnly=true`。这告诉Keycloak客户端永远不会启动登录过程，但是当它收到Bearer令牌时，它将检查所述令牌的有效性。

> 我们应该注意保留这些ID，因为我们将在接下来的步骤中使用它们。

- 我们有两个客户端，接下来是为spring-security-keycloak-example-app客户创建角色

Admin Role:

```bash
./kcadm.sh create clients/ab9d404e-6d5b-40ac-9bc3-9e2e26b68213/roles -r spring-learn-integration-springboot-security-keycloak -s name=admin -s 'description=Admin role'

Created new role with id 'admin'
```

User Role:

```bash
./kcadm.sh create clients/ab9d404e-6d5b-40ac-9bc3-9e2e26b68213/roles -r spring-learn-integration-springboot-security-keycloak -s name=user -s 'description=User role'

Created new role with id 'user'
```

> 注意client后的id是我们创建客户端输出的id

- 最后，我们应该获取客户端的配置，以便稍后提供给我们的应用程序：

```bash
./kcadm.sh  get clients/ab9d404e-6d5b-40ac-9bc3-9e2e26b68213/installation/providers/keycloak-oidc-keycloak-json -r spring-learn-integration-springboot-security-keycloak
```

> 注意client后的id是我们创建客户端输出的id

应该返回类似于此的内容：

```json
{
  "realm" : "spring-learn-integration-springboot-security-keycloak",
  "bearer-only" : true,
  "auth-server-url" : "http://localhost:8080/auth",
  "ssl-required" : "external",
  "resource" : "spring-learn-integration-springboot-security-keycloak-client",
  "verify-token-audience" : true,
  "use-resource-role-mappings" : true,
  "confidential-port" : 0
}
```

#### 配置用户

---

出于演示目的，我们创建2个具有2个不同角色的用户，以便我们验证授权是否有效。

- 首先，让我们创建一个具有admin角色的用户：

创建admin用户：

```bash
./kcadm.sh create users -r spring-learn-integration-springboot-security-keycloak -s username=admin -s enabled=true

Created new user with id '50c11a76-a8ff-42b1-80cb-d82cb3e7616d'
```

设置admin密码：

```bash
./kcadm.sh update users/50c11a76-a8ff-42b1-80cb-d82cb3e7616d/reset-password -r spring-learn-integration-springboot-security-keycloak -s type=password -s value=admin -s temporary=false -n
```

**value**: 用户密码

追加到admin角色中

```bash
./kcadm.sh add-roles -r spring-learn-integration-springboot-security-keycloak --uusername=admin --cclientid spring-learn-integration-springboot-security-keycloak-client --rolename admin
```

> 注意：从不在生产中使用此方法，它仅用于演示目的！

- 然后我们创建另一个用户，这次有角色user：

创建user用户：

```bash
./kcadm.sh create users -r spring-learn-integration-springboot-security-keycloak -s username=user -s enabled=true

Created new user with id '624434c8-bce4-4b5b-b81f-e77304785803'
```

设置user密码：

```bash
./kcadm.sh update users/624434c8-bce4-4b5b-b81f-e77304785803/reset-password -r spring-learn-integration-springboot-security-keycloak -s type=password -s value=admin -s temporary=false -n
```

追加到user角色中：

```bash
./kcadm.sh add-roles -r spring-learn-integration-springboot-security-keycloak --uusername=user --cclientid spring-learn-integration-springboot-security-keycloak-client --rolename user
```

#### Rest服务

---

我们已经配置了Keycloak并准备使用，我们只需要一个应用程序来使用它！所以我们创建一个简单的Spring Boot应用程序。我会在这里使用maven构建项目：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <parent>
        <artifactId>spring-learn-integration-springboot</artifactId>
        <groupId>com.edurt.sli</groupId>
        <version>1.0.0</version>
    </parent>

    <modelVersion>4.0.0</modelVersion>

    <artifactId>spring-learn-integration-springboot-security-keycloak</artifactId>

    <name>SpringBoot整合KeyCloak权限管理</name>

    <properties>
        <!-- dependency config -->
        <dependency.lombox.version>1.16.16</dependency.lombox.version>
        <dependency.keycloak.version>3.1.0.Final</dependency.keycloak.version>
        <!-- plugin config -->
        <plugin.maven.compiler.version>3.3</plugin.maven.compiler.version>
        <plugin.maven.javadoc.version>2.10.4</plugin.maven.javadoc.version>
        <!-- environment config -->
        <environment.compile.java.version>1.8</environment.compile.java.version>
        <!-- reporting config -->
        <reporting.maven.jxr.version>2.5</reporting.maven.jxr.version>
    </properties>

    <dependencies>
        <!-- lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${dependency.lombox.version}</version>
        </dependency>
        <!-- springboot -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <!-- keycloak -->
        <dependency>
            <groupId>org.keycloak</groupId>
            <artifactId>keycloak-spring-boot-starter</artifactId>
            <version>${dependency.keycloak.version}</version>
        </dependency>
        <dependency>
            <groupId>org.keycloak</groupId>
            <artifactId>keycloak-spring-security-adapter</artifactId>
            <version>${dependency.keycloak.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>${dependency.springboot.version}</version>
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

添加所有必需的依赖项：

- spring-security 用于保护应用程序
- keycloak-spring-boot-starter 使用Keycloak和Spring Boot
- keycloak-spring-security-adapter与Spring Security集成

一个简单的应用类：

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
package com.edurt.sli.slissk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p> SpringBootSecurityKeyCloakIntegration </p>
 * <p> Description : SpringBootSecurityKeyCloakIntegration </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-02-18 14:45 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@SpringBootApplication
public class SpringBootSecurityKeyCloakIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityKeyCloakIntegration.class, args);
    }

}
```

Rest API接口：

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
package com.edurt.sli.slissk.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> HelloController </p>
 * <p> Description : HelloController </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-02-18 14:50 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@RestController
public class HelloController {

    @GetMapping(value = "/admin")
    @Secured("ROLE_ADMIN")
    public String admin() {
        return "Admin";
    }

    @GetMapping("/user")
    @Secured("ROLE_USER")
    public String user() {
        return "User";
    }

}
```

最后是keycloak配置：

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
package com.edurt.sli.slissk.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

/**
 * <p> KeycloakSecurityConfigurer </p>
 * <p> Description : KeycloakSecurityConfigurer </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-02-18 14:51 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Configuration
@EnableWebSecurity
public class KeycloakSecurityConfigurer extends KeycloakWebSecurityConfigurerAdapter {

    @Bean
    public GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
        SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
        mapper.setConvertToUpperCase(true);
        return mapper;
    }

    @Override
    protected KeycloakAuthenticationProvider keycloakAuthenticationProvider() {
        final KeycloakAuthenticationProvider provider = super.keycloakAuthenticationProvider();
        provider.setGrantedAuthoritiesMapper(grantedAuthoritiesMapper());
        return provider;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(keycloakAuthenticationProvider());
    }

    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new NullAuthenticatedSessionStrategy();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasRole("USER")
                .anyRequest().permitAll();
    }

    @Bean
    KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
            final KeycloakAuthenticationProcessingFilter filter) {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(
            final KeycloakPreAuthActionsFilter filter) {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

}
```

KeycloakSecurityConfigurer类扩展 KeycloakWebSecurityConfigurerAdapter，这是Keycloak提供的类，它提供与Spring Security的集成。

然后我们通过添加[SimpleAuthorityMapper](http://docs.spring.io/spring-security/site/docs/current/apidocs/org/springframework/security/core/authority/mapping/SimpleAuthorityMapper.html)配置身份验证管理器，它负责转换来自Keycloak的角色名称以匹配Spring Security的约定。基本上Spring Security期望以`ROLE_`前缀开头的角色，ROLE_ADMIN可以像Keycloak一样命名我们的角色，或者我们可以将它们命名为admin，然后使用此映射器将其转换为大写并添加必要的`ROLE_`前缀：

```java
@Bean
public GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
  SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
  mapper.setConvertToUpperCase(true);
  return mapper;
}

@Override
protected KeycloakAuthenticationProvider keycloakAuthenticationProvider() {
  final KeycloakAuthenticationProvider provider = super.keycloakAuthenticationProvider();
  provider.setGrantedAuthoritiesMapper(grantedAuthoritiesMapper());
  return provider;
}

@Override
protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
  auth.authenticationProvider(keycloakAuthenticationProvider());
}
```

我们还需要为Keycloak设置会话策略，但是当我们创建无状态REST服务时，我们并不真的想要有会话，因此我们使用NullAuthenticatedSessionStrategy：

```java
@Override
protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
  return new NullAuthenticatedSessionStrategy();
}
```

通常，Keycloak Spring Security集成从keycloak.json文件中解析keycloak配置，但是我们希望有适当的Spring Boot配置，因此我们使用Spring Boot覆盖配置解析器：

```java
@Bean
KeycloakConfigResolver keycloakConfigResolver() {
  return new KeycloakSpringBootConfigResolver();
}
```

然后我们配置Spring Security来授权所有请求：

```java
@Override
protected void configure(final HttpSecurity http) throws Exception {
  super.configure(http);
  http
      .authorizeRequests()
      .anyRequest().permitAll();
}
```

最后，根据文档，我们阻止双重注册Keycloak的过滤器：

```java
@Bean
public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
    final KeycloakAuthenticationProcessingFilter filter) {
  final FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
  registrationBean.setEnabled(false);
  return registrationBean;
}

@Bean
public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(
    final KeycloakPreAuthActionsFilter filter) {
  final FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
  registrationBean.setEnabled(false);
  return registrationBean;
}
```

最后，我们需要application.properties使用之前下载的值配置我们的应用程序 ：

```java
server.port=9002
keycloak.realm=spring-learn-integration-springboot-security-keycloak
keycloak.bearer-only=true
keycloak.auth-server-url=http://localhost:9001/auth
keycloak.ssl-required=external
keycloak.resource=spring-learn-integration-springboot-security-keycloak-client
keycloak.use-resource-role-mappings=true
keycloak.principal-attribute=preferred_username
```

#### 使用应用程序

---

- 使用curl我们创建的客户端进行身份验证，以获取访问令牌：

```bash
export TOKEN=`curl -ss --data "grant_type=password&client_id=curl&username=admin&password=admin" http://localhost:9001/auth/realms/spring-learn-integration-springboot-security-keycloak/protocol/openid-connect/token | jq -r .access_token`
```

这将收到的访问令牌存储在TOKEN变量中。

现在我们可以检查我们的管理员是否可以访问自己的/admin接口

```bash
curl -H "Authorization: bearer $TOKEN" http://localhost:9002/admin

Admin
```

但它无法访问/user接口：

```bash
$ curl -H "Authorization: bearer $TOKEN" http://localhost:9002/user

{"timestamp":1498728302626,"status":403,"error":"Forbidden","message":"Access is denied","path":"/user"}
```

对于user用户也是如此，user用户无法访问admin接口。

#### 打包文件部署

---

- 打包数据

```bash
mvn clean package -Dmaven.test.skip=true -X
```

运行打包后的文件即可

```bash
java -jar target/spring-learn-integration-springboot-security-keycloak-1.0.0.jar
```

#### 源码地址

---

- [GitHub](https://github.com/qianmoQ/spring-learn-integration/tree/master/security/security-keycloak)
- [Gitee](https://gitee.com/qianmoQ/spring-learn-integration/tree/master/security/security-keycloak)
