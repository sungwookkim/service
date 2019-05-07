package com.zuul.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.zuul.filter.pre.TokenProduceFilter;

@Configuration
@Import({
	TokenProduceFilter.class
})
public class Filter {

}
