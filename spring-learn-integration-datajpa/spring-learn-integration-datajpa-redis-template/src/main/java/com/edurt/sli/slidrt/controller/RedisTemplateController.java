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
package com.edurt.sli.slidrt.controller;

import com.edurt.sli.slidrt.model.RedisTemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * <p> RedisTemplateController </p>
 * <p> Description : RedisTemplateController </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-14 14:38 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@RestController
@RequestMapping(value = "redis/template")
public class RedisTemplateController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static String KEY = "RedisTemplate";

    @GetMapping
    public Object get() {
        return this.stringRedisTemplate.opsForHash().entries(KEY);
    }

    @PostMapping
    public Object post(@RequestBody RedisTemplateModel model) {
        this.stringRedisTemplate.opsForHash().put(KEY, model.getId(), model.getName());
        return "SUCCESS";
    }

    @PutMapping
    public Object put(@RequestBody RedisTemplateModel model) {
        this.stringRedisTemplate.opsForHash().put(KEY, model.getId(), model.getName());
        return "SUCCESS";
    }

    @DeleteMapping
    public Object delete(@RequestParam String id) {
        this.stringRedisTemplate.opsForHash().delete(KEY, id);
        return "SUCCESS";
    }

}
