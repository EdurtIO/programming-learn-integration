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
package com.edurt.slis.slisec.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.cloud.netflix.eureka.server.EurekaServerConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.PropertySource;

/**
 * <p> EurekaServerConfig </p>
 * <p> Description : EurekaServerConfig </p>
 * <p> Author : qianmoQ </p>
 * <p> Version : 1.0 </p>
 * <p> Create Time : 2019-06-13 18:38 </p>
 * <p> Author Email: <a href="mailTo:shichengoooo@163.com">qianmoQ</a> </p>
 */
@Configuration
@PropertySource(value = "application-eureka.properties")
public class EurekaServerConfig {

    @Value(value = "${custom.eureka.instance.hostname}")
    private String hostname;

    @Value(value = "${custom.eureka.client.register-with-eureka}")
    private Boolean registerWithEureka;

    @Value(value = "${custom.eureka.client.fetch-registry}")
    private Boolean fetchRegistry;

    @Bean
    @Description(value = "使用自定义配置进行配置eureka server服务")
    public EurekaServerConfigBean eurekaConfig() {
        EurekaServerConfigBean config = new EurekaServerConfigBean();
        return config;
    }

    @Bean
    @Description(value = "使用自定义配置进行配置eureka client服务")
    public EurekaClientConfigBean eurekaClientConfigBean() {
        EurekaClientConfigBean config = new EurekaClientConfigBean();
        config.setRegisterWithEureka(registerWithEureka);
        config.setFetchRegistry(fetchRegistry);
        return config;
    }

}
