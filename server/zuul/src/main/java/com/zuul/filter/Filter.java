package com.zuul.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.zuul.filter.post.LogoutFilter;
import com.zuul.filter.pre.TokenProduceFilter;
import com.zuul.filter.pre.TokenRefreshFilter;

@Configuration
@Import({
	TokenProduceFilter.class
	, TokenRefreshFilter.class
	, LogoutFilter.class
})
public class Filter {

}
