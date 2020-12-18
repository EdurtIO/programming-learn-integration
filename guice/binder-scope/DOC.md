# Guice依赖注入(Scope)

本教程主要详细讲解Guice依赖注入中的一些高级选项，他们分别是`Scope`，`Eagerly Loading Bindings`，`Stage`，`Optional Injection`。我们将一一对他们进行讲解。

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
mvn archetype:generate -DgroupId=com.edurt.sli.guice -DartifactId=guice-binder-scope -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
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

    <artifactId>guice-binder-scope</artifactId>
    <name>Guice依赖注入(Scope)</name>

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

#### Singleton

---

Guice支持我们在其他DI框架中逐渐习惯的`Scope`和`Scope`机制。Guice默认提供已定义依赖项的新实例。

- 创建`Service`接口，用于提供使用的测试使用到的方法，代码如下

```java
package com.edurt.sli.guice.scope;

public interface Service
{
    void println(String source);
}
```

- 创建`ScopeService`类，用于构建对`Service`的实现

```java
package com.edurt.sli.guice.scope;

import java.time.LocalDateTime;

public class ScopeService
        implements Service
{
    @Override
    public void println(String source)
    {
        System.out.println(String.format("%s on %s", source, LocalDateTime.now()));
    }
}
```

- 创建用于测试注入的应用类`ScopeApplication`，代码如下

```java
package com.edurt.sli.guice.scope;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;

public class ScopeApplication
{
    public static void main(String[] args)
    {
        Injector injector = Guice.createInjector(binder -> binder.bind(Service.class).to(ScopeService.class).in(Scopes.SINGLETON));
        Service service = injector.getInstance(Service.class);
        service.println("Singleton Scope");
    }
}
```

我们运行程序输出

```bash
Scope on 2020-12-18T16:01:37.792656
```

通过代码`binder.bind(Service.class).to(ScopeService.class).in(Scopes.SINGLETON)`我们指定了ScopeService的Scope，他将会被标志为一个单例实例。当然我们也可以使用`@Singleton`标志该类的作用域，我们修改`ScopeService`类文件代码如下：

```java
package com.edurt.sli.guice.scope;

import javax.inject.Singleton;

import java.time.LocalDateTime;

@Singleton
public class ScopeService
        implements Service
{
    @Override
    public void println(String source)
    {
        System.out.println(String.format("%s on %s", source, LocalDateTime.now()));
    }
}
```

将`ScopeApplication`中的`binder.bind(Service.class).to(ScopeService.class).in(Scopes.SINGLETON)`代码修改为`binder.bind(Service.class).to(ScopeService.class)`

两种方式实现的效果都是一致的。此时`ScopeService`会被构建为单例实例。

当然还有一个`asEagerSingleton()`方法也可以用来标记单例模式。

他们的对比图如下：

|使用方式|PRODUCTION|DEVELOPMENT|
|---|---|---|
|.asEagerSingleton()|eager|eager|
|.in(Singleton.class)|eager|lazy|
|.in(Scopes.SINGLETON)|eager|lazy|
|@Singleton|eager*|lazy|

#### 自定义Scope

---

Guice还支持我们用户自定义作用域，通常情况下我们不需要自己实现Scope，一般内置的作用域对于大多数的应用已经足够了。如果您正在编写一个web应用程序，那么`ServletModule`为HTTP请求和HTTP会话提供了简单的、良好作用域实现是一个很好的想法。

- 自定义Scope注解

Scope注解用于标记当前Scope在容器中使用的作用域。将使用它来注释guice构造的类型，`@Provides`方法和bind语法中的`in()`。Scope注解代码如下:

```java
package com.edurt.sli.guice.seso;

import com.google.inject.ScopeAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, METHOD})
@Retention(RUNTIME)
@ScopeAnnotation
public @interface CustomScope
{
}
```

在使用自定义Scope时，请确保导入了正确的Scope注解。否则，您可能会得到一个`SCOPE_NOT_FOUND`错误。

- 实现Scope接口

Scope接口确保每个Scope实例拥有一到多个类型实例。实现的Scope接口代码如下：

```java
package com.edurt.sli.guice.seso;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

public class CustomScopeImpl
        implements Scope
{
    ThreadLocal<Object> threadLocal = new ThreadLocal<>();

    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped)
    {
        return () -> {
            T instance = (T) threadLocal.get();
            if (instance == null) {
                instance = unscoped.get();
                threadLocal.set(instance);
            }
            return instance;
        };
    }
}
```

我们在上述代码中实现了一个简单线程抽取Scope，我们只是为了做测试使用，具体的Scope还需要根据业务自己使用。当我们传递的线程中没有构造一个对象时，先构造一个，然后放入线程上下文中，以后每次都从线程中获取对象。

- 使用创建`CustomScopeApplication`用来使用自定义的Scope代码如下：

```java
package com.edurt.sli.guice.seso;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class CustomScopeApplication
{
    public static void main(String[] args)
    {
        Injector injector = Guice.createInjector(binder -> {
            binder.bind(Service.class).to(ScopeService.class).in(new CustomScopeImpl());
        });
        for (int i = 0; i < 3; i++) {
            System.out.println(injector.getInstance(Service.class).hashCode());
        }
    }
}
```

运行程序后我们得到以下结果：

```java
1574598287
1574598287
1574598287
```

我们通过结果得到运行了3次后的实例hashCode是一致的，这就说明我们的自定义Scope已经起作用了。如果新的实例构建后那么hashCode将会被改变。

- 绑定自定义Scope注解，我们通过实现Module进行注入

```java
package com.edurt.sli.guice.seso;

import com.google.inject.AbstractModule;

public class CustomScopeModule
        extends AbstractModule
{
    @Override
    protected void configure()
    {
        bindScope(CustomScope.class, new CustomScopeImpl());
    }
}
```

需要使用到改Module只需要在Guice.createInjector构建的时候添加该Module即可，代码如下：

```java
Injector injector = Guice.createInjector(new CustomScopeModule(), binder -> {
            binder.bind(Service.class).to(ScopeService.class).in(new CustomScopeImpl());
        });
```

在`ScopeService`类上使用`@CustomScope`注解即可。

#### 打包文件部署

---

- 打包数据

```bash
mvn clean package -Dmaven.test.skip=true -X
```

运行打包后的文件即可

```bash
java -jar target/guice-binder-scope-1.0.0.jar
```

#### 源码地址

---
- [GitHub](https://github.com/EdurtIO/spring-learn-integration/tree/master/guice/binder-scope)