package com.zuul.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.zuul.bean.Bean;
import com.zuul.config.prop.PropConfig;

import config.http.HttpConfig;

@Configuration
@Import({
	HttpConfig.class
	, PropConfig.class
	, Bean.class
})
public class Config {

}
