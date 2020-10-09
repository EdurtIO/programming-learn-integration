# Guice依赖注入(构造函数注入)

本教程主要详细讲解Guice的构造函数注入.

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
mvn archetype:generate -DgroupId=com.edurt.sli.guice -DartifactId=guice-binder-constructor -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
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

    <artifactId>guice-binder-constructor</artifactId>
    <name>Guice依赖注入(构造函数注入)</name>

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

#### 构造函数注入

---

在Guice中我们可以通过将需要的实体信息通过构造函数直接注入到我们需要的任意地方，我们通过列举一个例子来实际说明。

- 创建`com.edurt.sli.guice.sample`文件夹，并在该文件夹下创建`Service`接口文件，用于添加我们需要测试的函数

```java
package com.edurt.sli.guice.sample;

import com.google.inject.ImplementedBy;

@ImplementedBy(ServiceImpl.class)
public interface Service {

    void print(String source);

}
```

- 创建`Service`接口的实现类`ServiceImpl`，用于实现接口中的方法，代码如下

```java
package com.edurt.sli.guice.sample;

public class ServiceImpl implements Service {
    @Override
    public void print(String source) {
        System.out.println(String.format("Hello Guice, %s", source));
    }

}
```

- 创建用于测试注入的应用类`Application`，代码如下

```java
package com.edurt.sli.guice.sample;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class Application {

    private Service service;

    @Inject
    public Application(Service service) {
        this.service = service;
    }

    public Service getService() {
        return service;
    }

    public static void main(String[] args) {
        Application application = Guice.createInjector().getInstance(Application.class);
        application.service.print("Test");
    }

}
```

我们运行程序输出

```bash
Hello Guice, Test
```

这个示例很好理解，实际就是说我们将`Service`接口通过`@Inject`注入到了`Application`应用中。当然我们通过`@ImplementedBy(ServiceImpl.class)`实现了类似`Service service = new ServiceImpl()`的操作，不过每次会生成一个新的实例，如果需要单例模式的话，需要单独操作。

> 注意：在本次程序中我们并没有通过Module关联到Guice，方便我们快速测试应用等。
> 我们无法通过非Guice容器进行注入，以下就是一个错误的示例
> static也是无法进行注入的

```java
package com.edurt.sli.guice.sample;

import com.google.inject.Inject;

public class ApplicationCustom {
    
    @Inject
    private Service service;
    
    public Service getService() {
        return this.service;
    }

    public static void main(String[] args) {
        ApplicationCustom custom = new ApplicationCustom();
        custom.getService().print("Test");
    }
    
}
```

我们运行上述代码，会提示以下错误信息

```java
Exception in thread "main" java.lang.NullPointerException
	at com.edurt.sli.guice.sample.ApplicationCustom.main(ApplicationCustom.java:16)
```

这也就说明我们无法在非Guice容器中进行实例注入

#### 多参数注入

---

上述实例我们只是注入了一个参数，那我们尝试一下多参数注入。

- 我们先书写另一个需要注入的服务和实现，`PrintService`代码如下

```java
package com.edurt.sli.guice.sample;

import com.google.inject.ImplementedBy;

@ImplementedBy(PrintServiceImpl.class)
public interface PrintService {

    void print();

}
```

- 实现类`ImplementedBy`代码如下

```java
package com.edurt.sli.guice.sample;

public class PrintServiceImpl implements PrintService {

    @Override
    public void print() {
        System.out.println("print Service");
    }

}
```

- 该服务我们只提供固定的输出字符串信息`print Service`，接下来我们进行对多参数注入，`ApplicationMultiple`代码如下

```java
package com.edurt.sli.guice.sample;

import com.google.inject.Guice;
import com.google.inject.Inject;
import lombok.Data;

@Data
public class ApplicationMultiple {

    private Service service;
    private PrintService printService;

    @Inject
    public ApplicationMultiple(Service service, PrintService printService) {
        this.service = service;
        this.printService = printService;
    }

    public static void main(String[] args) {
        ApplicationMultiple multiple = Guice.createInjector().getInstance(ApplicationMultiple.class);
        multiple.getPrintService().print();
        multiple.getService().print("Multiple");
    }

}
```

运行程序后，输出以下结果

```java
print Service
Hello Guice, Multiple
```

我们使用一个`@Inject`也能实现多个参数的实例注入，当然还支持Set方式注入，只需要在参数的set方法上增加`@Inject`注解即可实现，这里我们不多做叙述，可自行实验。

#### static静态参数注入

---

我们说过无法注入通过static属性直接进行注入使用，方法总是很多的，Guice提供了以下static注入方式

```java
package com.edurt.sli.guice.sample;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class ApplicationStatic {

    @Inject
    private static Service service;

    public static void main(String[] args) {
        Guice.createInjector(binder -> binder.requestStaticInjection(ApplicationStatic.class));
        ApplicationStatic.service.print("Static");
    }

}
```

在代码中我们没有向以上两个示例直接使用`Guice`获取实例，而是使用了`binder.requestStaticInjection`方式进行了注入，这个是和static属性息息相关的，当我们注入static属性的时候要告知Guice我们具体使用static属性的父类，这样Guice才可以帮我们注入进来。

细心的话会想到我们既然使用`binder.requestStaticInjection`方式注入static属性，那么非static属性是不是也可以通过类似的方式注入？

答案是可以的，非static的属性我们需要通过`binder.requestInjection(Type);`方式注入，实例如下：

```java
package com.edurt.sli.guice.sample;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class ApplicationBinder {

    @Inject
    private Service service;

    public static void main(String[] args) {
        ApplicationBinder applicationBinder = new ApplicationBinder();
        Guice.createInjector(binder -> binder.requestInjection(applicationBinder));
        applicationBinder.service.print("Test");
    }

}
```

当然我们还可以通过`Guice.createInjector().injectMembers(new Object());`方式注入。

> 注意我们需要创建一个主类的实例才可以注入，使用ApplicationBinder.class是无法注入的

#### 打包文件部署

---

- 打包数据

```bash
mvn clean package -Dmaven.test.skip=true -X
```

运行打包后的文件即可

```bash
java -jar target/guice-binder-constructor-1.0.0.jar
```

#### 源码地址

---
- [GitHub](https://github.com/EdurtIO/programming-learn-integration/tree/master/guice/guice-binder-constructor)