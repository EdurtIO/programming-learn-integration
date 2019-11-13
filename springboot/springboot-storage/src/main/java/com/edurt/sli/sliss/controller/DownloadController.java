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
