今天我们尝试Spring Boot整合Scala，并决定建立一个非常简单的Spring Boot微服务，使用Scala作为编程语言进行编码构建。

#### 基础环境

---

|技术|版本|
|:---:|---|
|Java|1.8+|
|SpringBoot|2.1.x|
|Scala|2.12.x|

#### 创建项目

---

- 初始化项目

```bash
mvn archetype:generate -DgroupId=com.edurt.sli.sliss -DartifactId=springboot-scala-integration -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
```

- 修改pom.xml增加java和scala的支持

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

    <artifactId>spring-learn-integration-springboot-scala</artifactId>

    <name>SpringBoot整合Scala</name>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <!-- dependency config -->
        <dependency.scala.version>2.12.1</dependency.scala.version>
        <!-- plugin config -->
        <plugin.maven.scala.version>3.1.3</plugin.maven.scala.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.scala-lang</groupId>
            <artifactId>scala-library</artifactId>
            <version>${dependency.scala.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>

    <build>
        <sourceDirectory>${project.basedir}/src/main/scala</sourceDirectory>
        <testSourceDirectory>${project.basedir}/src/test/scala</testSourceDirectory>
        <plugins>
            <plugin>
                <groupId>net.alchim31.maven</groupId>
                <artifactId>scala-maven-plugin</artifactId>
                <version>${plugin.maven.scala.version}</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>compile</goal>
                            <goal>testCompile</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>2.1.3.RELEASE</version>
            </plugin>
        </plugins>
    </build>

</project>
```

- 一个简单的应用类

```scala
/**
  * Licensed to the Apache Software Foundation (ASF) under one
  * or more contributor license agreements.  See the NOTICE file
  * distributed with this work for additional information
  * regarding copyright ownership.  The ASF licenses this file
  * to you under the Apache License, Version 2.0 (the
  * "License"); you may not use this file except in compliance
  * with the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.edurt.sli.sliss

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SpringBootScalaIntegration

object SpringBootScalaIntegration extends App{

  SpringApplication.run(classOf[SpringBootScalaIntegration])

}
```

#### 添加Rest API接口功能

---

- 创建一个HelloController Rest API接口,我们只提供一个简单的get请求获取hello,scala输出信息

```scala
/**
  * Licensed to the Apache Software Foundation (ASF) under one
  * or more contributor license agreements.  See the NOTICE file
  * distributed with this work for additional information
  * regarding copyright ownership.  The ASF licenses this file
  * to you under the Apache License, Version 2.0 (the
  * "License"); you may not use this file except in compliance
  * with the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.edurt.sli.sliss.controller

import org.springframework.web.bind.annotation.{GetMapping, RestController}

@RestController
class HelloController {

  @GetMapping(value = Array("hello"))
  def hello(): String = {
    return "hello,scala"
  }

}
```

- 修改SpringBootScalaIntegration文件增加以下设置扫描路径

```scala
/**
  * Licensed to the Apache Software Foundation (ASF) under one
  * or more contributor license agreements.  See the NOTICE file
  * distributed with this work for additional information
  * regarding copyright ownership.  The ASF licenses this file
  * to you under the Apache License, Version 2.0 (the
  * "License"); you may not use this file except in compliance
  * with the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.edurt.sli.sliss

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(value = Array(
  "com.edurt.sli.sliss.controller"
))
class SpringBootScalaIntegration

object SpringBootScalaIntegration extends App {

  SpringApplication.run(classOf[SpringBootScalaIntegration])

}
```

#### 添加页面功能

---

- 修改pom.xml文件增加以下页面依赖

```xml
<!-- mustache -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mustache</artifactId>
    <version>2.1.3.RELEASE</version>
</dependency>
```

- 修改SpringBootScalaIntegration文件增加以下设置扫描路径ComponentScan的value字段中

```scala
/**
  * Licensed to the Apache Software Foundation (ASF) under one
  * or more contributor license agreements.  See the NOTICE file
  * distributed with this work for additional information
  * regarding copyright ownership.  The ASF licenses this file
  * to you under the Apache License, Version 2.0 (the
  * "License"); you may not use this file except in compliance
  * with the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.edurt.sli.sliss

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(value = Array(
  "com.edurt.sli.sliss.controller",
  "com.edurt.sli.sliss.view" // 增加扫描路径
))
class SpringBootScalaIntegration

object SpringBootScalaIntegration extends App {

  SpringApplication.run(classOf[SpringBootScalaIntegration])

}
```

- 在src/main/resources路径下创建**templates**文件夹

- 在templates文件夹下创建一个名为**hello.mustache**的页面文件

```html
<h1>Hello, Scala</h1>
```

- 创建view文件夹,并在该文件夹下创建页面转换器HelloView

```scala
/**
  * Licensed to the Apache Software Foundation (ASF) under one
  * or more contributor license agreements.  See the NOTICE file
  * distributed with this work for additional information
  * regarding copyright ownership.  The ASF licenses this file
  * to you under the Apache License, Version 2.0 (the
  * "License"); you may not use this file except in compliance
  * with the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.edurt.sli.sliss.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HelloView {

  @GetMapping(value = Array("hello_view"))
  def helloView: String = {
    return "hello";
  }

}
```

- 浏览器访问http://localhost:8080/hello_view即可看到页面内容

#### 添加数据持久化功能

---

- 修改pom.xml文件增加以下依赖(由于测试功能我们使用h2内存数据库)

```xml
<!-- data jpa and db -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
    <version>2.1.3.RELEASE</version>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.1.3.RELEASE</version>
    <scope>runtime</scope>
</dependency>
```

- 修改SpringBootScalaIntegration文件增加以下设置扫描model路径

```scala
"com.edurt.sli.sliss.model"
```

- 创建model文件夹,并在该文件夹下创建User实体

```scala
/**
  * Licensed to the Apache Software Foundation (ASF) under one
  * or more contributor license agreements.  See the NOTICE file
  * distributed with this work for additional information
  * regarding copyright ownership.  The ASF licenses this file
  * to you under the Apache License, Version 2.0 (the
  * "License"); you may not use this file except in compliance
  * with the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.edurt.sli.sliss.model

import javax.persistence.{Entity, GeneratedValue, Id}

@Entity
class UserModel {

  @Id
  @GeneratedValue
  var id: Long = 0

  var name: String = null

}
```

- 创建UserSupport dao数据库操作工具类

```scala
/**
  * Licensed to the Apache Software Foundation (ASF) under one
  * or more contributor license agreements.  See the NOTICE file
  * distributed with this work for additional information
  * regarding copyright ownership.  The ASF licenses this file
  * to you under the Apache License, Version 2.0 (the
  * "License"); you may not use this file except in compliance
  * with the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.edurt.sli.sliss.support

import com.edurt.sli.sliss.model.UserModel
import org.springframework.data.repository.PagingAndSortingRepository

trait UserSupport extends PagingAndSortingRepository[UserModel, Long] {

}
```

- 创建UserService服务类

```scala
/**
  * Licensed to the Apache Software Foundation (ASF) under one
  * or more contributor license agreements.  See the NOTICE file
  * distributed with this work for additional information
  * regarding copyright ownership.  The ASF licenses this file
  * to you under the Apache License, Version 2.0 (the
  * "License"); you may not use this file except in compliance
  * with the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.edurt.sli.sliss.service

import com.edurt.sli.sliss.model.UserModel

trait UserService {

  /**
    * save model to db
    */
  def save(model: UserModel): UserModel;

}
```

- 创建UserServiceImpl实现类

```scala
/**
  * Licensed to the Apache Software Foundation (ASF) under one
  * or more contributor license agreements.  See the NOTICE file
  * distributed with this work for additional information
  * regarding copyright ownership.  The ASF licenses this file
  * to you under the Apache License, Version 2.0 (the
  * "License"); you may not use this file except in compliance
  * with the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.edurt.sli.sliss.service

import com.edurt.sli.sliss.model.UserModel
import com.edurt.sli.sliss.support.UserSupport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service(value = "userService")
class UserServiceImpl @Autowired() (
                                     val userSupport: UserSupport
                                   ) extends UserService {

  /**
    * save model to db
    */
  override def save(model: UserModel): UserModel = {
    return this.userSupport.save(model)
  }

}
```

- 创建用户UserController进行持久化数据

```scala
/**
  * Licensed to the Apache Software Foundation (ASF) under one
  * or more contributor license agreements.  See the NOTICE file
  * distributed with this work for additional information
  * regarding copyright ownership.  The ASF licenses this file
  * to you under the Apache License, Version 2.0 (the
  * "License"); you may not use this file except in compliance
  * with the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.edurt.sli.sliss.controller

import com.edurt.sli.sliss.model.UserModel
import com.edurt.sli.sliss.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{PathVariable, PostMapping, RequestMapping, RestController}

@RestController
@RequestMapping(value = Array("user"))
class UserController @Autowired()(
                                   val userService: UserService
                                 ) {

  @PostMapping(value = Array("save/{name}"))
  def save(@PathVariable name: String): Long = {
    val userModel = {
      new UserModel()
    }
    userModel.name = name
    return this.userService.save(userModel).id
  }

}
```

- 使用控制台窗口执行以下命令保存数据

```bash
curl -X POST http://localhost:8080/user/save/qianmoQ
```

收到返回结果

```bash
1
```

表示数据保存成功

#### 增加数据读取渲染功能

---

- 修改UserService增加以下代码

```scala
/**
  * Licensed to the Apache Software Foundation (ASF) under one
  * or more contributor license agreements.  See the NOTICE file
  * distributed with this work for additional information
  * regarding copyright ownership.  The ASF licenses this file
  * to you under the Apache License, Version 2.0 (the
  * "License"); you may not use this file except in compliance
  * with the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.edurt.sli.sliss.service

import com.edurt.sli.sliss.model.UserModel
import org.springframework.data.domain.{Page, Pageable}

trait UserService {

  /**
    * save model to db
    */
  def save(model: UserModel): UserModel;

  /**
    * get all model
    */
  def getAll(page: Pageable): Page[UserModel]

}
```

- 修改UserServiceImpl增加以下代码

```scala
/**
  * Licensed to the Apache Software Foundation (ASF) under one
  * or more contributor license agreements.  See the NOTICE file
  * distributed with this work for additional information
  * regarding copyright ownership.  The ASF licenses this file
  * to you under the Apache License, Version 2.0 (the
  * "License"); you may not use this file except in compliance
  * with the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.edurt.sli.sliss.service

import com.edurt.sli.sliss.model.UserModel
import com.edurt.sli.sliss.support.UserSupport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.data.domain.{Page, Pageable}

@Service(value = "userService")
class UserServiceImpl @Autowired() (
                                     val userSupport: UserSupport
                                   ) extends UserService {

  /**
    * save model to db
    */
  override def save(model: UserModel): UserModel = {
    return this.userSupport.save(model)
  }

  /**
    * get all model
    */
  override def getAll(page: Pageable): Page[UserModel] = {
    return this.userSupport.findAll(page)
  }

}
```

- 修改UserController增加以下代码

```scala
/**
  * Licensed to the Apache Software Foundation (ASF) under one
  * or more contributor license agreements.  See the NOTICE file
  * distributed with this work for additional information
  * regarding copyright ownership.  The ASF licenses this file
  * to you under the Apache License, Version 2.0 (the
  * "License"); you may not use this file except in compliance
  * with the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.edurt.sli.sliss.controller

import com.edurt.sli.sliss.model.UserModel
import com.edurt.sli.sliss.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.{Page, PageRequest}
import org.springframework.web.bind.annotation._

@RestController
@RequestMapping(value = Array("user"))
class UserController @Autowired()(
                                   val userService: UserService
                                 ) {

  @PostMapping(value = Array("save/{name}"))
  def save(@PathVariable name: String): Long = {
    val userModel = {
      new UserModel()
    }
    userModel.name = name
    return this.userService.save(userModel).id
  }

  @GetMapping(value = Array("list"))
  def get(): Page[UserModel] = this.userService.getAll(PageRequest.of(0, 10))

}
```

- 创建UserView文件渲染User数据

```scala
/**
  * Licensed to the Apache Software Foundation (ASF) under one
  * or more contributor license agreements.  See the NOTICE file
  * distributed with this work for additional information
  * regarding copyright ownership.  The ASF licenses this file
  * to you under the Apache License, Version 2.0 (the
  * "License"); you may not use this file except in compliance
  * with the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.edurt.sli.sliss.view

import com.edurt.sli.sliss.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class UserView @Autowired()(
                             private val userService: UserService
                           ) {

  @GetMapping(value = Array("user_view"))
  def helloView(model: Model): String = {
    model.addAttribute("users", this.userService.getAll(PageRequest.of(0, 10)))
    return "user"
  }

}
```

- 创建user.mustache文件渲染数据(自行解析返回数据即可)

```scala
{{users}}
```

- 浏览器访问http://localhost:8080/user_view即可看到页面内容

#### 增加单元功能

---

- 修改pom.xml文件增加以下依赖

```xml
<!-- test -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <version>2.1.3.RELEASE</version>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <version>5.3.2</version>
    <scope>test</scope>
</dependency>
```

- 创建UserServiceTest文件进行测试UserService功能

```scala
/**
  * Licensed to the Apache Software Foundation (ASF) under one
  * or more contributor license agreements.  See the NOTICE file
  * distributed with this work for additional information
  * regarding copyright ownership.  The ASF licenses this file
  * to you under the Apache License, Version 2.0 (the
  * "License"); you may not use this file except in compliance
  * with the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.edurt.sli.sliss

import com.edurt.sli.sliss.service.UserService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest @Autowired()(
                                    private val userService: UserService) {

  @Test
  def `get all`() {
    println(">> Assert blog page title, content and status code")
    val entity = this.userService.getAll(PageRequest.of(0, 1))
    print(entity.getTotalPages)
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
java -jar target/spring-learn-integration-springboot-scala-1.0.0.jar
```

#### 源码地址

---

- [GitHub](https://github.com/EdurtIO/programming-learn-integration/tree/master/springboot/springboot-scala)
