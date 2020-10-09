今天我们尝试Spring Boot整合Angular，并决定建立一个非常简单的Spring Boot微服务，使用Angular作为前端渲编程语言进行前端页面渲染.

#### 基础环境

---

|技术|版本|
|:---:|---|
|Java|1.8+|
|SpringBoot|1.5.x|
|Angular|7.x.x|

#### 创建项目

---

- 初始化项目

```bash
mvn archetype:generate -DgroupId=com.edurt.sli.slisa -DartifactId=spring-learn-integration-springboot-angular -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
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

    <artifactId>spring-learn-integration-springboot-angular</artifactId>

    <name>SpringBoot整合Angular前后端开发</name>

    <properties>
        <!-- 系统 -->
        <system.java.version>1.8</system.java.version>
        <!-- 依赖 -->
        <dependency.springboot.version>1.5.6.RELEASE</dependency.springboot.version>
        <!-- 插件 -->
        <plugin.maven.compiler.version>3.1</plugin.maven.compiler.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>${dependency.springboot.version}</version>
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
package com.edurt.sli.slisa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p> SpringBootAngularIntegration </p>
 * <p> Description : SpringBootAngularIntegration </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-03 18:26 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@SpringBootApplication
public class SpringBootAngularIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAngularIntegration.class, args);
    }

}
```

#### 添加Rest API接口功能

---

- 创建一个model文件夹并在该文件夹下创建Model类,用于存放数据模型

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
package com.edurt.sli.slisa.model;

/**
 * <p> Model </p>
 * <p> Description : Model </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-05 20:00 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
public class Model {

    private String key;
    private String value;

    public Model() {
    }

    public Model(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
```

- 创建一个controller文件夹并在该文件夹下创建HelloAngularController Rest API接口,我们只提供一个简单的添加,删除,修改,查询列表的接口

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
package com.edurt.sli.slisa.controller;

import com.edurt.sli.slisa.model.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> HelloAngularController </p>
 * <p> Description : HelloAngularController </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-05 19:36 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@RestController
@CrossOrigin
public class HelloAngularController {

    private final static Map<String, Object> DATA_STORAGE = new ConcurrentHashMap<String, Object>();

    public HelloAngularController() {
        DATA_STORAGE.put("1", "Hello Java");
    }

    /**
     * 查询所有数据信息
     *
     * @return 所有数据信息
     */
    @GetMapping(value = "get")
    public Map getAll() {
        return DATA_STORAGE;
    }

    /**
     * 新建数据
     *
     * @param model 数据模型
     * @return 创建结果
     */
    @PostMapping(value = "post")
    public String post(@RequestBody Model model) {
        DATA_STORAGE.put(model.getKey(), model.getValue());
        return "SUCCESS";
    }

    /**
     * 更新数据
     *
     * @param model 数据模型
     * @return 更新结果
     */
    @PutMapping(value = "put")
    public String put(@RequestBody Model model) {
        if (DATA_STORAGE.containsKey(model.getKey())) {
            DATA_STORAGE.put(model.getKey(), model.getValue());
        }
        return "SUCCESS";
    }

    /**
     * 删除数据
     *
     * @param key 数据标志
     * @return 删除结果
     */
    @DeleteMapping(value = "delete")
    public String delete(@RequestParam String key) {
        if (DATA_STORAGE.containsKey(key)) {
            DATA_STORAGE.remove(key);
        }
        return "SUCCESS";
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
package com.edurt.sli.slisa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p> SpringBootAngularIntegration </p>
 * <p> Description : SpringBootAngularIntegration </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-03 18:26 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@SpringBootApplication
@ComponentScan(value = {
        "com.edurt.sli.slisa.controller"
})
public class SpringBootAngularIntegration {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAngularIntegration.class, args);
    }

}
```

#### 启动服务,测试API接口可用性

---

> 在编译器中直接启动SpringBootAngularIntegration类文件即可,或者打包jar启动,打包命令mvn clean package

- 测试查询数据接口

```bash
curl -X GET http://localhost:8080/get
```

返回结果

```bash
{"1":"Hello Java"}
```

- 测试新增数据接口

```bash
curl -X POST http://localhost:8080/post -H 'Content-Type:application/json' -d '{"key": "2", "value": "Hello Angular"}'
```

返回结果

```bash
SUCCESS
```

再次查询查看数据是否添加

```bash
curl -X GET http://localhost:8080/get
```

返回结果

```bash
{"1":"Hello Java","2":"Hello Angular"}
```

- 测试修改数据接口

```bash
curl -X PUT http://localhost:8080/put -H 'Content-Type:application/json' -d '{"key": "1", "value": "Hello Angular"}'
```

返回结果

```bash
SUCCESS
```

再次查询查看数据是否修改

```bash
curl -X GET http://localhost:8080/get
```

返回结果

```bash
{"1":"Hello Angular","2":"Hello Angular"}
```

- 测试删除数据接口

```bash
curl -X DELETE 'http://localhost:8080/delete?key=1'
```

返回结果

```bash
SUCCESS
```

再次查询查看数据是否删除

```bash
curl -X GET http://localhost:8080/get
```

返回结果

```bash
{"2":"Hello Angular"}
```

#### 增加Angular支持

---

我们使用ng脚手架进行初始化一个新的angular项目.不过需要我们先安装脚手架

```bash
npm install -g @angular/cli
```

- 生成Angular项目

```bash
ng new angular
```

生成项目的时候会自动安装相关依赖可能会慢,请耐心等待,中间出现的任何输入形式我们只需要回车即可.

```
? Would you like to add Angular routing? Yes
? Which stylesheet format would you like to use? SCSS   [ http://sass-lang.com   ]
CREATE angular/README.md (1024 bytes)
CREATE angular/angular.json (3868 bytes)
CREATE angular/package.json (1306 bytes)
CREATE angular/tsconfig.json (435 bytes)
CREATE angular/tslint.json (2824 bytes)
CREATE angular/.editorconfig (246 bytes)
CREATE angular/.gitignore (576 bytes)
CREATE angular/src/favicon.ico (5430 bytes)
CREATE angular/src/index.html (294 bytes)
CREATE angular/src/main.ts (372 bytes)
CREATE angular/src/polyfills.ts (3234 bytes)
CREATE angular/src/test.ts (642 bytes)
CREATE angular/src/styles.scss (80 bytes)
CREATE angular/src/browserslist (388 bytes)
CREATE angular/src/karma.conf.js (980 bytes)
CREATE angular/src/tsconfig.app.json (166 bytes)
CREATE angular/src/tsconfig.spec.json (256 bytes)
CREATE angular/src/tslint.json (314 bytes)
CREATE angular/src/assets/.gitkeep (0 bytes)
CREATE angular/src/environments/environment.prod.ts (51 bytes)
CREATE angular/src/environments/environment.ts (662 bytes)
CREATE angular/src/app/app-routing.module.ts (245 bytes)
CREATE angular/src/app/app.module.ts (393 bytes)
CREATE angular/src/app/app.component.scss (0 bytes)
CREATE angular/src/app/app.component.html (1152 bytes)
CREATE angular/src/app/app.component.spec.ts (1098 bytes)
CREATE angular/src/app/app.component.ts (212 bytes)
CREATE angular/e2e/protractor.conf.js (752 bytes)
CREATE angular/e2e/tsconfig.e2e.json (213 bytes)
CREATE angular/e2e/src/app.e2e-spec.ts (299 bytes)
CREATE angular/e2e/src/app.po.ts (204 bytes)
npm WARN deprecated istanbul@0.4.5: This module is no longer maintained, try this instead:
npm WARN deprecated   npm i nyc
npm WARN deprecated Visit https://istanbul.js.org/integrations for other alternatives.
npm WARN deprecated circular-json@0.5.9: CircularJSON is in maintenance only, flatted is its successor.

> fsevents@1.2.9 install /Users/shicheng/angular/node_modules/fsevents
> node install

node-pre-gyp WARN Using request for node-pre-gyp https download
[fsevents] Success: "/Users/shicheng/angular/node_modules/fsevents/lib/binding/Release/node-v57-darwin-x64/fse.node" is installed via remote

> node-sass@4.10.0 install /Users/shicheng/angular/node_modules/node-sass
> node scripts/install.js

Cached binary found at /Users/shicheng/.npm/node-sass/4.10.0/darwin-x64-57_binding.node

> core-js@2.6.9 postinstall /Users/shicheng/angular/node_modules/core-js
> node scripts/postinstall || echo "ignore"


> node-sass@4.10.0 postinstall /Users/shicheng/angular/node_modules/node-sass
> node scripts/build.js

Binary found at /Users/shicheng/angular/node_modules/node-sass/vendor/darwin-x64-57/binding.node
Testing binary
Binary is fine
npm WARN ajv-keywords@3.4.0 requires a peer of ajv@^6.9.1 but none is installed. You must install peer dependencies yourself.

added 1221 packages in 64.411s
    Successfully initialized git.
```

- 创建angula源码目录

在`src/main/`下新建`angulars`目录,并将刚刚生成的代码文件全部复制到该文件夹下(注意隐藏文件也需要复制)

- 修改`app.module.ts`引入HttpClient模块增加访问后端服务支持

```typescript
import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
```

- 修改`app.component.ts`访问后端数据

```typescript
import {Component} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'angular';

  public list;
  public data = {
    key: '',
    value: ''
  }

  constructor(private http: HttpClient) {
    this.init();
  }

  init() {
    this.http.get('/get').subscribe((res) => {
        this.list = res;
        console.log(res)
      }
    );
  }

  add() {
    const headers = new HttpHeaders().set(
      "Content-type",
      "application/json; charset=UTF-8"
    );
    this.http.post('/post', this.data, {headers}).subscribe();
    this.init();
  }

  delete() {
    this.http.delete('/delete?key=' + this.data.key).subscribe();
    this.init();
  }

  modfiy() {
    const headers = new HttpHeaders().set(
      "Content-type",
      "application/json; charset=UTF-8"
    );
    this.http.post('/post', this.data, {headers}).subscribe();
    this.init();
  }

}
```

- 修改`app.component.html`渲染后端数据

```html
<!--The content below is only a placeholder and can be replaced.-->
<div style="text-align:center">
  <h1>
    Welcome to {{ title }}!
  </h1>
  <img width="300" alt="Angular Logo"
       src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAyNTAgMjUwIj4KICAgIDxwYXRoIGZpbGw9IiNERDAwMzEiIGQ9Ik0xMjUgMzBMMzEuOSA2My4ybDE0LjIgMTIzLjFMMTI1IDIzMGw3OC45LTQzLjcgMTQuMi0xMjMuMXoiIC8+CiAgICA8cGF0aCBmaWxsPSIjQzMwMDJGIiBkPSJNMTI1IDMwdjIyLjItLjFWMjMwbDc4LjktNDMuNyAxNC4yLTEyMy4xTDEyNSAzMHoiIC8+CiAgICA8cGF0aCAgZmlsbD0iI0ZGRkZGRiIgZD0iTTEyNSA1Mi4xTDY2LjggMTgyLjZoMjEuN2wxMS43LTI5LjJoNDkuNGwxMS43IDI5LjJIMTgzTDEyNSA1Mi4xem0xNyA4My4zaC0zNGwxNy00MC45IDE3IDQwLjl6IiAvPgogIDwvc3ZnPg==">
</div>
<h2>Here are some links to help you start: </h2>
<ul>
  {{list | json}}
</ul>
<form>
  <input [(ngModel)]="data.key" name="name" #name="ngModel"/>
  <input [(ngModel)]="data.value" name="value" #value="ngModel"/>
  <button (click)="add()">添加</button>
</form>

<form>
  <input [(ngModel)]="data.key" name="name" #name="ngModel"/>
  <button (click)="delete()">删除</button>
</form>

<form>
  <input [(ngModel)]="data.key" name="name" #name="ngModel"/>
  <input [(ngModel)]="data.value" name="value" #value="ngModel"/>
  <button (click)="modfiy()">修改</button>
</form>

<router-outlet></router-outlet>
```

#### 支持打包输出

---

- 修改angulars目录下的`angular.json`配置文件增加输出目录以便支持打包文件到jar中(**"outDir": "../resources/static"**)

```json
{
  "$schema": "./node_modules/@angular/cli/lib/config/schema.json",
  "version": 1,
  "newProjectRoot": "projects",
  "projects": {
    "angular": {
      "root": "",
      "sourceRoot": "src",
      "outDir": "../resources/static",
      "projectType": "application",
      "prefix": "app",
      "schematics": {
        "@schematics/angular:component": {
          "styleext": "scss"
        }
      },
      "architect": {
        "build": {
          "builder": "@angular-devkit/build-angular:browser",
          "options": {
            "outputPath": "dist/angular",
            "index": "src/index.html",
            "main": "src/main.ts",
            "polyfills": "src/polyfills.ts",
            "tsConfig": "src/tsconfig.app.json",
            "assets": [
              "src/favicon.ico",
              "src/assets"
            ],
            "styles": [
              "src/styles.scss"
            ],
            "scripts": []
          },
          "configurations": {
            "production": {
              "fileReplacements": [
                {
                  "replace": "src/environments/environment.ts",
                  "with": "src/environments/environment.prod.ts"
                }
              ],
              "optimization": true,
              "outputHashing": "all",
              "sourceMap": false,
              "extractCss": true,
              "namedChunks": false,
              "aot": true,
              "extractLicenses": true,
              "vendorChunk": false,
              "buildOptimizer": true,
              "budgets": [
                {
                  "type": "initial",
                  "maximumWarning": "2mb",
                  "maximumError": "5mb"
                }
              ]
            }
          }
        },
        "serve": {
          "builder": "@angular-devkit/build-angular:dev-server",
          "options": {
            "browserTarget": "angular:build"
          },
          "configurations": {
            "production": {
              "browserTarget": "angular:build:production"
            }
          }
        },
        "extract-i18n": {
          "builder": "@angular-devkit/build-angular:extract-i18n",
          "options": {
            "browserTarget": "angular:build"
          }
        },
        "test": {
          "builder": "@angular-devkit/build-angular:karma",
          "options": {
            "main": "src/test.ts",
            "polyfills": "src/polyfills.ts",
            "tsConfig": "src/tsconfig.spec.json",
            "karmaConfig": "src/karma.conf.js",
            "styles": [
              "src/styles.scss"
            ],
            "scripts": [],
            "assets": [
              "src/favicon.ico",
              "src/assets"
            ]
          }
        },
        "lint": {
          "builder": "@angular-devkit/build-angular:tslint",
          "options": {
            "tsConfig": [
              "src/tsconfig.app.json",
              "src/tsconfig.spec.json"
            ],
            "exclude": [
              "**/node_modules/**"
            ]
          }
        }
      }
    },
    "angular-e2e": {
      "root": "e2e/",
      "projectType": "application",
      "prefix": "",
      "architect": {
        "e2e": {
          "builder": "@angular-devkit/build-angular:protractor",
          "options": {
            "protractorConfig": "e2e/protractor.conf.js",
            "devServerTarget": "angular:serve"
          },
          "configurations": {
            "production": {
              "devServerTarget": "angular:serve:production"
            }
          }
        },
        "lint": {
          "builder": "@angular-devkit/build-angular:tslint",
          "options": {
            "tsConfig": "e2e/tsconfig.e2e.json",
            "exclude": [
              "**/node_modules/**"
            ]
          }
        }
      }
    }
  },
  "defaultProject": "angular"
}
```

- 修改maven配置增加打包依赖

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

    <artifactId>spring-learn-integration-springboot-angular</artifactId>

    <name>SpringBoot整合Angular前后端开发</name>

    <properties>
        <!-- 系统 -->
        <system.java.version>1.8</system.java.version>
        <system.node.version>v8.2.1</system.node.version>
        <system.npm.version>5.4.2</system.npm.version>
        <!-- 依赖 -->
        <dependency.springboot.version>1.5.6.RELEASE</dependency.springboot.version>
        <!-- 插件 -->
        <plugin.maven.compiler.version>3.1</plugin.maven.compiler.version>
        <plugin.frontend.version>0.0.27</plugin.frontend.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>${dependency.springboot.version}</version>
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
            <plugin>
                <groupId>com.github.eirslett</groupId>
                <artifactId>frontend-maven-plugin</artifactId>
                <version>${plugin.frontend.version}</version>
                <configuration>
                    <!-- angular 源码根目录 angular -->
                    <workingDirectory>src/main/angulars</workingDirectory>
                    <nodeDownloadRoot>https://npm.taobao.org/mirrors/node/</nodeDownloadRoot>
                    <!--<npmDownloadRoot>https://npm.taobao.org/mirrors/npm/</npmDownloadRoot>-->
                    <nodeVersion>${system.node.version}</nodeVersion>
                    <npmVersion>${system.npm.version}</npmVersion>
                    <installDirectory>target</installDirectory>
                </configuration>
                <executions>
                    <execution>
                        <id>install node and npm</id>
                        <goals>
                            <goal>install-node-and-npm</goal>
                        </goals>
                        <phase>generate-resources</phase>
                    </execution>
                    <execution>
                        <id>npm install</id>
                        <goals>
                            <goal>npm</goal>
                        </goals>
                        <configuration>
                            <arguments>install</arguments>
                            <installDirectory>target</installDirectory>
                        </configuration>
                    </execution>
                    <execution>
                        <id>angular cli build</id>
                        <goals>
                            <goal>npm</goal>
                        </goals>
                        <phase>generate-resources</phase>
                        <configuration>
                            <arguments>run build</arguments>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>
```

#### 打包文件部署

---

- 打包数据

```bash
mvn clean package -Dmaven.test.skip=true -X
```

运行打包后的文件即可

```bash
java -jar target/spring-learn-integration-springboot-angular-1.0.0.jar
```

#### 源码地址

---

- [GitHub](https://github.com/EdurtIO/programming-learn-integration/tree/master/springboot/springboot-angular)
