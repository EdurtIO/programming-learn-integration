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
