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
package com.edurt.sli.slidmt.controller;

import com.edurt.sli.slidmt.model.MongoDBModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

/**
 * <p> MongoDbController </p>
 * <p> Description : MongoDbController </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-10-21 11:44 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@RestController
@RequestMapping(value = "mongodb/template")
public class MongoDbController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping
    public Object get() {
        return this.mongoTemplate.findOne(Query.query(Criteria.where("title").is("Hello MongoDB")), MongoDBModel.class);
    }

    @PostMapping
    public Object post(@RequestBody MongoDBModel model) {
        return this.mongoTemplate.save(model);
    }

    @PutMapping
    public Object put(@RequestBody MongoDBModel model) {
        MongoDBModel temp = this.mongoTemplate.findOne(
                Query.query(Criteria.where("title").is("Hello MongoDB")), MongoDBModel.class);
        temp.setTitle(model.getTitle());
        return this.mongoTemplate.save(temp);
    }

    @DeleteMapping
    public Object delete(@RequestParam String title) {
        MongoDBModel temp = this.mongoTemplate.findOne(
                Query.query(Criteria.where("title").is(title)), MongoDBModel.class);
        this.mongoTemplate.remove(temp);
        return "SUCCESS";
    }

}
