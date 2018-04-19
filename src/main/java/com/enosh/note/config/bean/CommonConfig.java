package com.enosh.note.config.bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config-${spring.profiles.active}.properties")
public class CommonConfig {

}
