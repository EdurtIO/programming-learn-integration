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
