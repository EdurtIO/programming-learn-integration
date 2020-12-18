# Guice依赖注入(Provider)

本教程主要详细讲解Guice依赖注入中的Procider服务注入实现，一般都是用于外部服务的注入，比如实现Redis等。

#### 基础环境

---

| 技术  | 版本  |
| ----- | ----- |
| Java  | 1.8+  |
| Guice | 4.2.3 |

#### 初始化项目

---

- 初始化项目

```bash
mvn archetype:generate -DgroupId=com.edurt.sli.guice -DartifactId=guice-binder-provider -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
```

- 修改pom.xml增加Guice依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <parent>
        <artifactId>spring-learn-integration</artifactId>
        <groupId>com.edurt.sli</groupId>
        <version>1.0.0</version>
    </parent>

    <modelVersion>4.0.0</modelVersion>

    <artifactId>guice-binder-provider</artifactId>
    <name>Guice依赖注入(Provider)</name>

    <properties>
        <system.java.version>1.8</system.java.version>
        <guice.version>4.2.3</guice.version>
        <lombok.version>1.18.2</lombok.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.google.inject</groupId>
            <artifactId>guice</artifactId>
            <version>${guice.version}</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
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

`guice`: guice就是我们核心要使用的依赖

#### 实现Provider注入

---

如果想要注入一个服务我们可以使用`Provider`进行实现。

- 创建`com.edurt.sli.guice.provider`文件夹，并在该文件夹下创建`Service`接口文件，用于添加我们需要测试的函数

```java
package com.edurt.sli.guice.provider;

public interface Service {

    void println(String input);

}
```

- 创建`Service`接口的实现类`JavaService`，用于实现接口中的方法，代码如下

```java
package com.edurt.sli.guice.provider;

public class JavaService implements Service {

    @Override
    public void println(String input) {
        System.out.println(input);
    }

}
```

- 创建`JavaServiceProvider`类，用于实现注入

```java
package com.edurt.sli.guice.provider;

import com.google.inject.Provider;

public class JavaServiceProvider implements Provider<Service> {

    @Override
    public Service get() {
        return new JavaService();
    }

}
```

- 创建用于测试注入的应用类`JavaProviderApplication`，代码如下

```java
package com.edurt.sli.guice.provider;

import com.google.inject.*;

public class JavaProviderApplication {

    @Inject
    private Service service;

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(Service.class).toProvider(JavaServiceProvider.class);
            }
        });
        JavaProviderApplication application = injector.getInstance(JavaProviderApplication.class);
        application.service.println("Hello Java");
    }

}
```

我们运行程序输出

```bash
Hello Java
```

我们注意看`binder`的配置中，我们使用的是`toProvider`将实现类绑定到了`Service`接口中，这样就实现了对Provider的注入。

#### 自动注入服务

---

- 创建自动注入服务`GuiceService`接口类，代码如下:

```java
package com.edurt.sli.guice.provider;

import com.google.inject.ProvidedBy;

@ProvidedBy(value = GuiceServiceProvider.class)
public interface GuiceService {

    void say(String input);

}
```

注意我们使用`@ProvidedBy`标志了接口的实现类，这样的话我们就可以实现自动注入。

- 创建`GuiceServiceImpl`接口实现类，代码如下

```java
package com.edurt.sli.guice.provider;

public class GuiceServiceImpl implements GuiceService {

    @Override
    public void say(String input) {
        System.out.println(input);
    }

}
```

- 创建`GuiceServiceProvider`注入类，代码如下

```java
package com.edurt.sli.guice.provider;

import com.google.inject.Provider;

public class GuiceServiceProvider implements Provider<GuiceService> {

    @Override
    public GuiceService get() {
        return new GuiceServiceImpl();
    }

}
```

- 创建`GuiceAutoProviderApplication`应用类，代码如下

```java
package com.edurt.sli.guice.provider;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class GuiceAutoProviderApplication {

    @Inject
    private GuiceService guiceService;

    public static void main(String[] args) {
        GuiceAutoProviderApplication application = Guice.createInjector().getInstance(GuiceAutoProviderApplication.class);
        application.guiceService.say("Guice Service");
    }

}
```

这样就实现了我们不需要去绑定Module就可以实现注入。


#### 打包文件部署

---

- 打包数据

```bash
mvn clean package -Dmaven.test.skip=true -X
```

运行打包后的文件即可

```bash
java -jar target/guice-binder-provider-1.0.0.jar
```

#### 源码地址

---
- [GitHub](https://github.com/EdurtIO/spring-learn-integration/tree/master/guice/binder-provider)