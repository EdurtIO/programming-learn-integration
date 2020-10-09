# Guice依赖注入(接口多实现)

本教程主要详细讲解Guice依赖注入中的特性接口多实现，一般使用到guice的框架的插件机制都是基于该方式实现。

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
mvn archetype:generate -DgroupId=com.edurt.sli.guice -DartifactId=guice-binder-multiple -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
```

- 修改pom.xml增加Guice依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <parent>
        <artifactId>learn-integration-guice</artifactId>
        <groupId>com.edurt.sli.guice</groupId>
        <version>1.0.0</version>
    </parent>

    <modelVersion>4.0.0</modelVersion>

    <artifactId>guice-binder-multiple</artifactId>
    <name>Guice依赖注入(接口多实现)</name>

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

#### 接口多实现注入

---

如果一个接口有多个实现，如果单单通过`@Inject`和`Module`都难以直接实现，但多实现是经常会出现的，Guice提供了其它注入方式来解决此问题。

- 创建`com.edurt.sli.guice.multiple`文件夹，并在该文件夹下创建`Service`接口文件，用于添加我们需要测试的函数

```java
package com.edurt.sli.guice.multiple;

public interface Service {

    void print(String source);

}
```

- 创建`Service`接口的实现类`JavaService`和`GuiceService`，用于实现接口中的方法，代码如下

```java
package com.edurt.sli.guice.multiple;

public class JavaService implements Service {
    
    @Override
    public void print(String source) {
        System.out.println("Java Service " + source);
    }
    
}
```

```java
package com.edurt.sli.guice.multiple;

public class GuiceService implements Service {
    
    @Override
    public void print(String source) {
        System.out.println("Guice Service " + source);
    }
    
}
```

- 创建`Guice`和`Java`注解类，用于提供guice框架标识

```java
package com.edurt.sli.guice.multiple;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@BindingAnnotation
public @interface Guice {
}
```

```java
package com.edurt.sli.guice.multiple;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@BindingAnnotation
public @interface Java {
}
```

- 创建用于测试注入的应用类`Application`，代码如下

```java
package com.edurt.sli.guice.multiple;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class Application {

    @Inject
    @Java
    public Service java;

    @Inject
    @com.edurt.sli.guice.multiple.Guice
    public Service guice;

    public static void main(String[] args) {
        Application application = Guice.createInjector(binder -> {
            binder.bind(Service.class).annotatedWith(Java.class).to(JavaService.class);
            binder.bind(Service.class).annotatedWith(com.edurt.sli.guice.multiple.Guice.class).to(GuiceService.class);
        }).getInstance(Application.class);
        application.guice.print("sss");
        application.java.print("sss");
    }

}
```

我们运行程序输出

```bash
Guice Service sss
Java Service sss
```

我们注意看`binder`的配置中，我们将注解与实际的实现类绑定到了一起，这样就实现了绑定多接口实现的功能。

> 注意：在本次程序中我们使用的是lambda表达式进行的代码编程，需要jdk1.8及以上版本

#### 静态代码注入

---

我们如果需要进行静态代码注入服务该怎么写呢？我们参照以前讲解的`Guice依赖注入(构造函数注入)`资源中，我们创建一个`ApplicationStatic`类进行static的注入，代码如下

```java
package com.edurt.sli.guice.multiple;

import com.google.inject.Inject;

public class ApplicationStatic {

    @Inject
    @Java
    public static Service java;

    @Inject
    @com.edurt.sli.guice.multiple.Guice
    public static Service guice;

    public static void main(String[] args) {
        com.google.inject.Guice.createInjector(binder -> {
            binder.bind(Service.class).annotatedWith(Java.class).to(JavaService.class);
            binder.bind(Service.class).annotatedWith(com.edurt.sli.guice.multiple.Guice.class).to(GuiceService.class);
            binder.requestStaticInjection(ApplicationStatic.class);
        });
        ApplicationStatic.guice.print("sss");
        ApplicationStatic.java.print("sss");
    }

}
```

我们只需要在binder阶段将我们的主类注入到guice容器中，也就是我们看到的`binder.requestStaticInjection(ApplicationStatic.class);`代码，运行程序输出以下内容

```java
Guice Service sss
Java Service sss
```

#### 属性绑定多接口

---

先看一下多接口绑定的示例

```java
package com.edurt.sli.guice.multiple;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class ApplicationMultipleProperty {

    @Inject
    public Service java;

    @Inject
    public Service guice;

    public static void main(String[] args) {
        ApplicationMultipleProperty application = Guice.createInjector(binder -> {
            binder.bind(Service.class).annotatedWith(Java.class).to(JavaService.class);
            binder.bind(Service.class).annotatedWith(com.edurt.sli.guice.multiple.Guice.class).to(GuiceService.class);
        }).getInstance(ApplicationMultipleProperty.class);
        application.guice.print("sss");
        application.java.print("sss");
    }

}
```

运行以上代码，就会出现以下错误

```java
Exception in thread "main" com.google.inject.ConfigurationException: Guice configuration errors:

1) No implementation for com.edurt.sli.guice.multiple.Service was bound.
  Did you mean?
    * com.edurt.sli.guice.multiple.Service annotated with interface com.edurt.sli.guice.multiple.Java
    * com.edurt.sli.guice.multiple.Service annotated with interface com.edurt.sli.guice.multiple.Guice
  while locating com.edurt.sli.guice.multiple.Service
    for field at com.edurt.sli.guice.multiple.Application.guice(Application.java:6)
  while locating com.edurt.sli.guice.multiple.Application

2) No implementation for com.edurt.sli.guice.multiple.Service was bound.
  Did you mean?
    * com.edurt.sli.guice.multiple.Service annotated with interface com.edurt.sli.guice.multiple.Java
    * com.edurt.sli.guice.multiple.Service annotated with interface com.edurt.sli.guice.multiple.Guice
  while locating com.edurt.sli.guice.multiple.Service
    for field at com.edurt.sli.guice.multiple.Application.java(Application.java:6)
  while locating com.edurt.sli.guice.multiple.Application

2 errors
	at com.google.inject.internal.InjectorImpl.getProvider(InjectorImpl.java:1120)
	at com.google.inject.internal.InjectorImpl.getProvider(InjectorImpl.java:1078)
	at com.google.inject.internal.InjectorImpl.getInstance(InjectorImpl.java:1131)
	at com.edurt.sli.guice.multiple.Application.main(Application.java:18)
```

这是因为我们使用了属性绑定了多接口实现，导致guice无法识别具体是哪个实现类，不过guice是强大的这种问题也被考虑到了，只需要使用`@Named`模板生成注解即可解决，我们姜代码修改为以下内容

```java
package com.edurt.sli.guice.multiple;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class ApplicationMultipleProperty {

    @Inject
    @Named("Java")
    public Service java;

    @Inject
    @Named("Guice")
    public Service guice;

    public static void main(String[] args) {
        ApplicationMultipleProperty application = Guice.createInjector(binder -> {
            binder.bind(Service.class).annotatedWith(Names.named("Java")).to(JavaService.class);
            binder.bind(Service.class).annotatedWith(Names.named("Guice")).to(GuiceService.class);
        }).getInstance(ApplicationMultipleProperty.class);
        application.guice.print("sss");
        application.java.print("sss");
    }

}
```

运行程序后，输出以下结果

```java
Guice Service sss
Java Service sss
```

这个示例也很好理解，其实我们只是做了两步操作

- 在绑定实现的时候使用`annotatedWith(Names.named("Java"))`进行对该服务实现做名称标志
- 在需要使用服务实现的地方使用`@Named("Java")`进行服务的引用即可

#### 打包文件部署

---

- 打包数据

```bash
mvn clean package -Dmaven.test.skip=true -X
```

运行打包后的文件即可

```bash
java -jar target/guice-binder-multiple-1.0.0.jar
```

#### 源码地址

---
- [GitHub](https://github.com/EdurtIO/spring-learn-integration/tree/master/guice/binder-multiple)