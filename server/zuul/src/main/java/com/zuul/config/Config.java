package com.zuul.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.zuul.bean.Bean;
import com.zuul.config.prop.PropConfig;
import com.zuul.config.prop.TokenControlConfig;

import config.http.HttpConfig;

@Configuration
@Import({
	HttpConfig.class
	, PropConfig.class
	, TokenControlConfig.class
	, Bean.class
})
public class Config {

}
