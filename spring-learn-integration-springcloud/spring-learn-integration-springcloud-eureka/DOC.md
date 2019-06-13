今天我们尝试使用Spring Boot开发一个数据存储服务.

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
mvn archetype:generate -DgroupId=com.edurt.slis.slise -DartifactId=spring-learn-integration-springcloud-eureka -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
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

    <artifactId>spring-learn-integration-springcloud-eureka</artifactId>

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

- 创建一个java类用于做为程序的启动入口(**SpringCloudIntegrationEurekaLaunch.java**)

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
package com.edurt.slis.slise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p> SpringCloudIntegrationEurekaLaunch </p>
 * <p> Description : SpringCloudIntegrationEurekaLaunch </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-13 15:29 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@SpringBootApplication
public class SpringCloudIntegrationEurekaLaunch {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudIntegrationEurekaLaunch.class, args);
    }

}
```

此时该应用通过使用**@SpringBootApplication**注解可以成为SpringBoot应用,我们将其转换为Eureka服务的话我们需要在**@SpringBootApplication**注解下增加**@EnableEurekaServer**注解,这样我们的应用将成为Eureka Server,此时的最终代码为

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
package com.edurt.slis.slise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * <p> SpringCloudIntegrationEurekaLaunch </p>
 * <p> Description : SpringCloudIntegrationEurekaLaunch </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-13 15:29 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@SpringBootApplication
// 启用eureka服务
@EnableEurekaServer
public class SpringCloudIntegrationEurekaLaunch {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudIntegrationEurekaLaunch.class, args);
    }

}
```

- 启动服务浏览器打开http://localhost:8080出现以下画面

![-w1280](http://image.cdn.ttxit.com/2019-03-26-15535936121290.jpg)

此页面为eureka服务的监控页面

***_System Status_*** 显示系统的一些基本信息
***_DS Replicas_*** 显示eureka服务的一些负载/备份信息
***_Instances currently registered with Eureka_*** 显示注册到eureka的服务实例列表
***_General Info_*** 当前eureka服务的一些基本信息
***_Instance Info_*** 当前eureka服务的主机/健康信息

- 为了方便后期其他应用接入进来我们创建一个应用的配置文件

在resources资源目录下创建一个application.properties的配置文件,内容如下

```bash
# 服务端口
server.port=1000
```

我们只配置一下服务的端口,然后重启服务,浏览器打开http://localhost:1000即可看到eureka的监控页面.

- 服务启动时期会出现一个连接错误

```java
2019-03-26 17:45:59.257 ERROR 53004 --- [           main] c.n.d.s.t.d.RedirectingEurekaHttpClient  : Request execution error

com.sun.jersey.api.client.ClientHandlerException: java.net.ConnectException: Connection refused (Connection refused)
	at com.sun.jersey.client.apache4.ApacheHttpClient4Handler.handle(ApacheHttpClient4Handler.java:187) ~[jersey-apache-client4-1.19.1.jar:1.19.1]
	at com.sun.jersey.api.client.filter.GZIPContentEncodingFilter.handle(GZIPContentEncodingFilter.java:123) ~[jersey-client-1.19.1.jar:1.19.1]
	at com.netflix.discovery.EurekaIdentityHeaderFilter.handle(EurekaIdentityHeaderFilter.java:27) ~[eureka-client-1.9.2.jar:1.9.2]
	at com.sun.jersey.api.client.Client.handle(Client.java:652) ~[jersey-client-1.19.1.jar:1.19.1]
	at com.sun.jersey.api.client.WebResource.handle(WebResource.java:682) ~[jersey-client-1.19.1.jar:1.19.1]
	at com.sun.jersey.api.client.WebResource.access$200(WebResource.java:74) ~[jersey-client-1.19.1.jar:1.19.1]
	at com.sun.jersey.api.client.WebResource$Builder.get(WebResource.java:509) ~[jersey-client-1.19.1.jar:1.19.1]
	at com.netflix.discovery.shared.transport.jersey.AbstractJerseyEurekaHttpClient.getApplicationsInternal(AbstractJerseyEurekaHttpClient.java:194) ~[eureka-client-1.9.2.jar:1.9.2]
	at com.netflix.discovery.shared.transport.jersey.AbstractJerseyEurekaHttpClient.getApplications(AbstractJerseyEurekaHttpClient.java:165) ~[eureka-client-1.9.2.jar:1.9.2]
	at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator$6.execute(EurekaHttpClientDecorator.java:137) [eureka-client-1.9.2.jar:1.9.2]
	at com.netflix.discovery.shared.transport.decorator.MetricsCollectingEurekaHttpClient.execute(MetricsCollectingEurekaHttpClient.java:73) ~[eureka-client-1.9.2.jar:1.9.2]
	at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.getApplications(EurekaHttpClientDecorator.java:134) [eureka-client-1.9.2.jar:1.9.2]
	at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator$6.execute(EurekaHttpClientDecorator.java:137) [eureka-client-1.9.2.jar:1.9.2]
	at com.netflix.discovery.shared.transport.decorator.RedirectingEurekaHttpClient.executeOnNewServer(RedirectingEurekaHttpClient.java:118) ~[eureka-client-1.9.2.jar:1.9.2]
	at com.netflix.discovery.shared.transport.decorator.RedirectingEurekaHttpClient.execute(RedirectingEurekaHttpClient.java:79) ~[eureka-client-1.9.2.jar:1.9.2]
	at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.getApplications(EurekaHttpClientDecorator.java:134) [eureka-client-1.9.2.jar:1.9.2]
	at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator$6.execute(EurekaHttpClientDecorator.java:137) [eureka-client-1.9.2.jar:1.9.2]
	at com.netflix.discovery.shared.transport.decorator.RetryableEurekaHttpClient.execute(RetryableEurekaHttpClient.java:120) [eureka-client-1.9.2.jar:1.9.2]
	at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.getApplications(EurekaHttpClientDecorator.java:134) [eureka-client-1.9.2.jar:1.9.2]
	at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator$6.execute(EurekaHttpClientDecorator.java:137) [eureka-client-1.9.2.jar:1.9.2]
	at com.netflix.discovery.shared.transport.decorator.SessionedEurekaHttpClient.execute(SessionedEurekaHttpClient.java:77) [eureka-client-1.9.2.jar:1.9.2]
	at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.getApplications(EurekaHttpClientDecorator.java:134) [eureka-client-1.9.2.jar:1.9.2]
	at com.netflix.discovery.DiscoveryClient.getAndStoreFullRegistry(DiscoveryClient.java:1051) [eureka-client-1.9.2.jar:1.9.2]
	at com.netflix.discovery.DiscoveryClient.fetchRegistry(DiscoveryClient.java:965) [eureka-client-1.9.2.jar:1.9.2]
	at com.netflix.discovery.DiscoveryClient.<init>(DiscoveryClient.java:414) [eureka-client-1.9.2.jar:1.9.2]
	at com.netflix.discovery.DiscoveryClient.<init>(DiscoveryClient.java:269) [eureka-client-1.9.2.jar:1.9.2]
	at org.springframework.cloud.netflix.eureka.CloudEurekaClient.<init>(CloudEurekaClient.java:63) [spring-cloud-netflix-eureka-client-2.0.0.RELEASE.jar:2.0.0.RELEASE]
	at org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration$RefreshableEurekaClientConfiguration.eurekaClient(EurekaClientAutoConfiguration.java:269) [spring-cloud-netflix-eureka-client-2.0.0.RELEASE.jar:2.0.0.RELEASE]
	at org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration$RefreshableEurekaClientConfiguration$$EnhancerBySpringCGLIB$$d21b83eb.CGLIB$eurekaClient$1(<generated>) [spring-cloud-netflix-eureka-client-2.0.0.RELEASE.jar:2.0.0.RELEASE]
	at org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration$RefreshableEurekaClientConfiguration$$EnhancerBySpringCGLIB$$d21b83eb$$FastClassBySpringCGLIB$$cca72ab9.invoke(<generated>) [spring-cloud-netflix-eureka-client-2.0.0.RELEASE.jar:2.0.0.RELEASE]
	at org.springframework.cglib.proxy.MethodProxy.invokeSuper(MethodProxy.java:228) [spring-core-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.context.annotation.ConfigurationClassEnhancer$BeanMethodInterceptor.intercept(ConfigurationClassEnhancer.java:361) [spring-context-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration$RefreshableEurekaClientConfiguration$$EnhancerBySpringCGLIB$$d21b83eb.eurekaClient(<generated>) [spring-cloud-netflix-eureka-client-2.0.0.RELEASE.jar:2.0.0.RELEASE]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_141]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_141]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_141]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_141]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:154) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:582) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1256) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1105) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:543) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:503) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$1(AbstractBeanFactory.java:353) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.cloud.context.scope.GenericScope$BeanLifecycleWrapper.getBean(GenericScope.java:390) ~[spring-cloud-context-2.0.0.RELEASE.jar:2.0.0.RELEASE]
	at org.springframework.cloud.context.scope.GenericScope.get(GenericScope.java:184) ~[spring-cloud-context-2.0.0.RELEASE.jar:2.0.0.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:350) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.aop.target.SimpleBeanTargetSource.getTarget(SimpleBeanTargetSource.java:35) ~[spring-aop-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:193) ~[spring-aop-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at com.sun.proxy.$Proxy101.getApplications(Unknown Source) ~[na:na]
	at org.springframework.cloud.netflix.eureka.server.EurekaServerAutoConfiguration.peerAwareInstanceRegistry(EurekaServerAutoConfiguration.java:164) ~[spring-cloud-netflix-eureka-server-2.0.0.RELEASE.jar:2.0.0.RELEASE]
	at org.springframework.cloud.netflix.eureka.server.EurekaServerAutoConfiguration$$EnhancerBySpringCGLIB$$642b8ead.CGLIB$peerAwareInstanceRegistry$5(<generated>) ~[spring-cloud-netflix-eureka-server-2.0.0.RELEASE.jar:2.0.0.RELEASE]
	at org.springframework.cloud.netflix.eureka.server.EurekaServerAutoConfiguration$$EnhancerBySpringCGLIB$$642b8ead$$FastClassBySpringCGLIB$$e3f488be.invoke(<generated>) ~[spring-cloud-netflix-eureka-server-2.0.0.RELEASE.jar:2.0.0.RELEASE]
	at org.springframework.cglib.proxy.MethodProxy.invokeSuper(MethodProxy.java:228) [spring-core-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.context.annotation.ConfigurationClassEnhancer$BeanMethodInterceptor.intercept(ConfigurationClassEnhancer.java:361) [spring-context-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.cloud.netflix.eureka.server.EurekaServerAutoConfiguration$$EnhancerBySpringCGLIB$$642b8ead.peerAwareInstanceRegistry(<generated>) ~[spring-cloud-netflix-eureka-server-2.0.0.RELEASE.jar:2.0.0.RELEASE]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_141]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_141]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_141]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_141]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:154) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:582) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1256) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1105) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:543) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:503) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:317) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:222) ~[spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:315) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:251) ~[spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1138) ~[spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1065) ~[spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:818) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:724) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:474) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1256) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1105) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:543) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:503) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:317) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:222) ~[spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:315) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:251) ~[spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1138) ~[spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1065) ~[spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.inject(AutowiredAnnotationBeanPostProcessor.java:584) ~[spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.annotation.InjectionMetadata.inject(InjectionMetadata.java:91) ~[spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.postProcessPropertyValues(AutowiredAnnotationBeanPostProcessor.java:373) ~[spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1350) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:580) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:503) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:317) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:222) ~[spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:315) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199) [spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:760) ~[spring-beans-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:869) ~[spring-context-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:550) ~[spring-context-5.0.7.RELEASE.jar:5.0.7.RELEASE]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:140) ~[spring-boot-2.0.3.RELEASE.jar:2.0.3.RELEASE]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:759) ~[spring-boot-2.0.3.RELEASE.jar:2.0.3.RELEASE]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:395) ~[spring-boot-2.0.3.RELEASE.jar:2.0.3.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:327) ~[spring-boot-2.0.3.RELEASE.jar:2.0.3.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1255) ~[spring-boot-2.0.3.RELEASE.jar:2.0.3.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1243) ~[spring-boot-2.0.3.RELEASE.jar:2.0.3.RELEASE]
	at com.edurt.si.eureka.SpringCloudIntegrationEurekaLaunch.main(SpringCloudIntegrationEurekaLaunch.java:38) ~[classes/:na]
Caused by: java.net.ConnectException: Connection refused (Connection refused)
	at java.net.PlainSocketImpl.socketConnect(Native Method) ~[na:1.8.0_141]
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350) ~[na:1.8.0_141]
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206) ~[na:1.8.0_141]
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188) ~[na:1.8.0_141]
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392) ~[na:1.8.0_141]
	at java.net.Socket.connect(Socket.java:589) ~[na:1.8.0_141]
	at org.apache.http.conn.scheme.PlainSocketFactory.connectSocket(PlainSocketFactory.java:121) ~[httpclient-4.5.5.jar:4.5.5]
	at org.apache.http.impl.conn.DefaultClientConnectionOperator.openConnection(DefaultClientConnectionOperator.java:180) ~[httpclient-4.5.5.jar:4.5.5]
	at org.apache.http.impl.conn.AbstractPoolEntry.open(AbstractPoolEntry.java:144) ~[httpclient-4.5.5.jar:4.5.5]
	at org.apache.http.impl.conn.AbstractPooledConnAdapter.open(AbstractPooledConnAdapter.java:134) ~[httpclient-4.5.5.jar:4.5.5]
	at org.apache.http.impl.client.DefaultRequestDirector.tryConnect(DefaultRequestDirector.java:610) ~[httpclient-4.5.5.jar:4.5.5]
	at org.apache.http.impl.client.DefaultRequestDirector.execute(DefaultRequestDirector.java:445) ~[httpclient-4.5.5.jar:4.5.5]
	at org.apache.http.impl.client.AbstractHttpClient.doExecute(AbstractHttpClient.java:835) ~[httpclient-4.5.5.jar:4.5.5]
	at org.apache.http.impl.client.CloseableHttpClient.execute(CloseableHttpClient.java:118) ~[httpclient-4.5.5.jar:4.5.5]
	at org.apache.http.impl.client.CloseableHttpClient.execute(CloseableHttpClient.java:56) ~[httpclient-4.5.5.jar:4.5.5]
	at com.sun.jersey.client.apache4.ApacheHttpClient4Handler.handle(ApacheHttpClient4Handler.java:173) ~[jersey-apache-client4-1.19.1.jar:1.19.1]
	... 107 common frames omitted
```

这是因为我们无法连接eureka服务,你可能会问我们现在的程序就是eureka服务,为什么还会去连接其他的Eureka服务呢?这是因为在eureka源码中如果我们不设置**eureka.client.register-with-eureka**和**eureka.client.fetch-registry**这两个参数的话,那么这两个参数的默认值是true也就是说即使我们使用**@EnableEurekaServer**注解标识我们的服务是eureka服务但他同时也是一个eureka client所以会去连接eureka服务,这个错误可以忽略,或者我们修改一下**application.properties**配置文件内容如下

```bash
# 服务端口
server.port=1000
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

修改配置文件后重启服务,将不会在出现以上的错误信息

#### 打包文件部署

---

- 打包数据

```bash
mvn clean package -Dmaven.test.skip=true -X
```

运行打包后的文件即可

```bash
java -jar spring-learn-integration-springcloud/spring-learn-integration-springcloud-eureka/target/spring-learn-integration-springcloud-eureka-1.0.0.jar
```

#### 源码地址

---

- [GitHub](https://github.com/qianmoQ/spring-learn-integration/tree/master/spring-learn-integration-springcloud/spring-learn-integration-springcloud-eureka)
- [Gitee](https://gitee.com/qianmoQ/spring-learn-integration/tree/master/spring-learn-integration-springcloud/spring-learn-integration-springcloud-eureka)
