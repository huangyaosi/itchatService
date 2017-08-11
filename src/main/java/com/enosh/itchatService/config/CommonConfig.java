package com.enosh.itchatService.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config-${spring.profiles.active}.properties")
public class CommonConfig {

}
