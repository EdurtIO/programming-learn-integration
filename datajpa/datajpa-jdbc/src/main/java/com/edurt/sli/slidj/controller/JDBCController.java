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
package com.edurt.sli.slidj.controller;

import com.edurt.sli.slidj.model.JDBCModel;
import com.edurt.sli.slidj.repository.JDBCSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

/**
 * <p> JDBCController </p>
 * <p> Description : JDBCController </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-11-13 20:47 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@RestController
@RequestMapping(value = "jdbc")
public class JDBCController {

    @Autowired
    private JDBCSupport support;

    @GetMapping
    public Object get() {
        return this.support.findAll();
    }

    @PostMapping
    public Object post(@RequestBody JDBCModel mode) {
        return this.support.save(mode);
    }

    @PutMapping
    public Object put(@RequestBody JDBCModel mode) {
        return this.support.save(mode);
    }

    @DeleteMapping
    public Object delete(@RequestParam Long id) {
        this.support.deleteById(id);
        return "SUCCESS";
    }

    @GetMapping(value = "page")
    public Object get(@RequestParam Integer page,
                      @RequestParam Integer size) {
        return this.support.findAll(PageRequest.of(page, size));
    }

    @GetMapping(value = "sort")
    public Object sort() {
        return this.support.findAll(new Sort(Sort.Direction.DESC, "id"));
    }

    @GetMapping(value = "page_sort")
    public Object sort(@RequestParam Integer page,
                       @RequestParam Integer size) {
        Pageable pageable = PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "id"));
        return this.support.findAll(pageable);
    }

}
