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
