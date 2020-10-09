# Guice依赖注入(一)

本教程主要详细讲解Guice的一些基本注入方式,通过该简单教程让我们可以快速使用Guice进行简单系统化开发,后续我们会更深入讲解更多模块,如果还不了解Guice大家可以先去网上自行了解一下.

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
mvn archetype:generate -DgroupId=com.edurt.sli.guice -DartifactId=guice-basic -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
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

    <artifactId>guice-basic</artifactId>

    <name>Guice基础教程</name>

    <properties>
        <system.java.version>1.8</system.java.version>
        <guice.version>4.2.3</guice.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.google.inject</groupId>
            <artifactId>guice</artifactId>
            <version>${guice.version}</version>
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

#### Guice的绑定模型

- 在`src/main/java`目录下新建**com.edurt.sli.guice.sample**目录并在该目录下新建`GuiceBasicModule`类文件,在文件输入以下内容

```java
package com.edurt.sli.guice.sample;

import com.google.inject.AbstractModule;

public class GuiceBasicModule extends AbstractModule {

    @Override
    protected void configure() {
    }

}
```

Guice中的绑定模型和Spring中的一样简单,我们通过绑定可以提供给程序任意注入类.

绑定我们需要的Module只需要继承Guice中的`com.google.inject.AbstractModule`即可,在`configure`方法中实现我们需要的绑定信息.

- 在**com.edurt.sli.guice.sample**目录下构建`GuiceBasicService`接口类,内容如下

```java
package com.edurt.sli.guice.sample;

public interface GuiceBasicService {

    void print(String output);

}
```

- 在**com.edurt.sli.guice.sample**目录下构建`GuiceBasicServiceImpl`接口实现类,内容如下

```java
package com.edurt.sli.guice.sample;

public class GuiceBasicServiceImpl implements GuiceBasicService {

    public void print(String output) {
        System.out.println(String.format("print %s", output));
    }

}
```

- 接下来修改`GuiceBasicModule`将我们定义的服务进行绑定,代码如下

```java
package com.edurt.sli.guice.sample;

import com.google.inject.AbstractModule;

public class GuiceBasicModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(GuiceBasicService.class).to(GuiceBasicServiceImpl.class);
    }

}
```

这样我们就很快的绑定了一个服务,类似于Spring中的`@Bean`方式

`bind`标志我们需要绑定的类,`to`标志我们绑定的实现类

- 在**com.edurt.sli.guice.sample**目录下构建`GuiceBasicApplication`类文件用于我们测试代码,在文件输入以下内容

```java
package com.edurt.sli.guice.sample;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class GuiceBasicApplication {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new GuiceBasicModule());
        GuiceBasicService service = injector.getInstance(GuiceBasicService.class);

        service.print("Hello Guice");
    }

}
```

我们运行程序,控制台会输出如下内容:

```base
print Hello Guice
```

#### 构造函数绑定

---

- 首先我们构建用于注入的服务类,代码如下

```java
@ImplementedBy(ConstructorServiceImpl.class)
interface ConstructorService {

    void print();

}
```

`@ImplementedBy`告知我们程序我们的接口具体实现类,Guice会帮我们做自动实例化

- 服务实现类,我们打印简单的字符串,代码如下

```java
class ConstructorServiceImpl implements ConstructorService {

    @Override
    public void print() {
        System.out.println("Hello Guice By Constructor");
    }

}
```

- 使用构造函数进行注入,代码如下

```java
public class ConstructorApplication {

    private ConstructorService service;

    @Inject
    public ConstructorApplication(ConstructorService service) {
        this.service = service;
    }

    public ConstructorService getService() {
        return service;
    }

    public static void main(String[] args) {
        ConstructorApplication instance = Guice.createInjector().getInstance(ConstructorApplication.class);
        instance.getService().print();
    }

}
```

我们运行程序,控制台会输出如下内容:

```base
Hello Guice By Constructor
```


#### 打包文件部署

---

- 打包数据

```bash
mvn clean package -Dmaven.test.skip=true -X
```

运行打包后的文件即可

```bash
java -jar target/guice-binder-1.0.0.jar
```

#### 源码地址

---
- [GitHub](https://github.com/EdurtIO/programming-learn-integration/tree/master/guice/binder)