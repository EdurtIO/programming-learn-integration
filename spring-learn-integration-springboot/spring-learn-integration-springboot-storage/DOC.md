今天我们尝试使用Spring Boot开发一个数据存储服务.

#### 基础环境

---

|技术|版本|
|:---:|---|
|Java|1.8+|
|SpringBoot|1.5.x|

#### 创建项目

---

- 初始化项目

```bash
mvn archetype:generate -DgroupId=com.edurt.sli.sliss -DartifactId=spring-learn-integration-springboot-storage -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
```

- 修改pom.xml增加java和springboot的支持

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

    <artifactId>spring-learn-integration-springboot-storage</artifactId>

    <name>SpringBoot开发存储服务器</name>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
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
package com.edurt.sli.sliss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p> SpringBootStorageIntegration </p>
 * <p> Description : SpringBootStorageIntegration </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-10 15:53 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@SpringBootApplication
public class SpringBootStorageIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStorageIntegration.class, args);
    }

}
```

#### 添加Rest API接口功能(提供上传服务)

---

- 创建一个controller文件夹并在该文件夹下创建UploadController Rest API接口,我们提供一个简单的文件上传接口

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
package com.edurt.sli.sliss.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <p> UploadController </p>
 * <p> Description : UploadController </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-10 15:55 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@RestController
@RequestMapping(value = "upload")
public class UploadController {

    // 文件上传地址
    private final static String UPLOADED_FOLDER = "/Users/shicheng/Desktop/test/";

    @PostMapping
    public String upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传文件不能为空";
        }
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            return "上传文件成功";
        } catch (IOException ioe) {
            return "上传文件失败,失败原因: " + ioe.getMessage();
        }
    }

    @GetMapping
    public Object get() {
        File file = new File(UPLOADED_FOLDER);
        String[] filelist = file.list();
        return filelist;
    }

    @DeleteMapping
    public String delete(@RequestParam(value = "file") String file) {
        File source = new File(UPLOADED_FOLDER + file);
        source.delete();
        return "删除文件" + file + "成功";
    }

}
```

- 修改SpringBootAngularIntegration类文件增加以下设置扫描路径,以便扫描Controller

```bash
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
package com.edurt.sli.sliss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p> SpringBootStorageIntegration </p>
 * <p> Description : SpringBootStorageIntegration </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-10 15:53 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@SpringBootApplication
@ComponentScan(value = {
        "com.edurt.sli.sliss.controller"
})
public class SpringBootStorageIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStorageIntegration.class, args);
    }

}
```

#### 启动服务,测试API接口可用性

---

> 在编译器中直接启动SpringBootStorageIntegration类文件即可,或者打包jar启动,打包命令mvn clean package

- 测试上传文件接口

```bash
curl localhost:8080/upload -F "file=@/Users/shicheng/Downloads/qrcode/qrcode_for_ambari.jpg"
```

返回结果

```bash
上传文件成功
```

- 测试查询文件接口

```bash
curl localhost:8080/upload
```

返回结果

```bash
["qrcode_for_ambari.jpg"]
```

- 测试删除接口

```bash
curl -X DELETE 'localhost:8080/upload?file=qrcode_for_ambari.jpg'
```

返回结果

```bash
删除文件qrcode_for_ambari.jpg成功
```

再次查询查看文件是否被删除

```bash
curl localhost:8080/upload
```

返回结果

```bash
[]
```

#### 增加下载文件支持

---

- 在controller文件夹下创建DownloadController Rest API接口,我们提供一个文件下载接口

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
package com.edurt.sli.sliss.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * <p> DownloadController </p>
 * <p> Description : DownloadController </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-10 16:21 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@RestController
@RequestMapping(value = "download")
public class DownloadController {

    private final static String UPLOADED_FOLDER = "/Users/shicheng/Desktop/test/";

    @GetMapping
    public String download(@RequestParam(value = "file") String file,
                           HttpServletResponse response) {
        if (!file.isEmpty()) {
            File source = new File(UPLOADED_FOLDER + file);
            if (source.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + file);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fileInputStream = null;
                BufferedInputStream bufferedInputStream = null;
                try {
                    fileInputStream = new FileInputStream(source);
                    bufferedInputStream = new BufferedInputStream(fileInputStream);
                    OutputStream outputStream = response.getOutputStream();
                    int i = bufferedInputStream.read(buffer);
                    while (i != -1) {
                        outputStream.write(buffer, 0, i);
                        i = bufferedInputStream.read(buffer);
                    }
                    return "文件下载成功";
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bufferedInputStream != null) {
                        try {
                            bufferedInputStream.close();
                        } catch (IOException e) {
                            return "文件下载失败,失败原因: " + e.getMessage();
                        }
                    }
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                            return "文件下载失败,失败原因: " + e.getMessage();
                        }
                    }
                }
            }
        }
        return "文件下载失败";
    }

}
```

- 测试下载文件

```bash
curl -o a.jpg 'localhost:8080/download?file=qrcode_for_ambari.jpg'
```

出现以下进度条

```
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100  148k    0  148k    0     0  11.3M      0 --:--:-- --:--:-- --:--:-- 12.0M
```

查询是否下载到本地文件夹

```bash
ls a.jpg
```

返回结果

```bash
a.jpg
```

#### 文件大小设置

---

默认情况下，Spring Boot最大文件上传大小为1MB，您可以通过以下应用程序属性配置值：

- 配置文件

```java
#http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#common-application-properties
#search multipart
spring.http.multipart.max-file-size=10MB
spring.http.multipart.max-request-size=10MB
```

- 代码配置,创建一个config文件夹,并在该文件夹下创建MultipartConfig

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
package com.edurt.sli.sliss.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * <p> MultipartConfig </p>
 * <p> Description : MultipartConfig </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-10 16:34 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Configuration
public class MultipartConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("10240KB"); //KB,MB
        factory.setMaxRequestSize("102400KB");
        return factory.createMultipartConfig();
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
java -jar target/spring-learn-integration-springboot-storage-1.0.0.jar
```

#### 源码地址

---

- [GitHub](https://github.com/qianmoQ/spring-learn-integration/tree/master/spring-learn-integration-springboot/spring-learn-integration-springboot-storage)
- [Gitee](https://gitee.com/qianmoQ/spring-learn-integration/tree/master/spring-learn-integration-springboot/spring-learn-integration-springboot-storage)
