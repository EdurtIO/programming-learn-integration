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
package com.edurt.sli.slidr.controller;

import com.edurt.sli.slidr.model.RedisModel;
import com.edurt.sli.slidr.repository.RedisSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p> RedisController </p>
 * <p> Description : RedisController </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-14 01:05 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@RestController
@RequestMapping(value = "redis")
public class RedisController {

    @Autowired
    private RedisSupport support;

    @GetMapping
    public Object get() {
        return this.support.findAll();
    }

    @PostMapping
    public Object post(@RequestBody RedisModel mode) {
        return this.support.save(mode);
    }

    @PutMapping
    public Object put(@RequestBody RedisModel mode) {
        return this.support.save(mode);
    }

    @DeleteMapping
    public Object delete(@RequestParam String id) {
        this.support.deleteById(id);
        return "SUCCESS";
    }

}
