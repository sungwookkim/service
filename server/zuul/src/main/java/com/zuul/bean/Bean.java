package com.zuul.bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import config.serverList.ServerListProp;

@Configuration
@Import({
	ServerListProp.class
})
public class Bean {

}
