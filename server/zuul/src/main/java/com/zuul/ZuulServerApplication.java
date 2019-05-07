package com.zuul;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.PollingServerListUpdater;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import com.netflix.loadbalancer.ServerListFilter;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import com.zuul.config.Config;
import com.zuul.filter.Filter;

@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
@ComponentScan(basePackages = {"config"})
@RibbonClients(defaultConfiguration = RibbonConfig.class)
@Import({
	Config.class
	, Filter.class
})
public class ZuulServerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ZuulServerApplication.class, args);
	}
	
	@Bean
	public TomcatServletWebServerFactory containerFactory() {
		TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
		
		tomcatServletWebServerFactory.addConnectorCustomizers(connector -> {
			((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setConnectionTimeout(10000);
		});
		
		return tomcatServletWebServerFactory; 
	}
	
}

class RibbonConfig {
	@Bean 
	public ILoadBalancer ribbonLoadBalancer(IClientConfig config, ServerList<Server> serverList, ServerListFilter<Server> serverListFilter,
			IRule rule, IPing ping) {

		return new ZoneAwareLoadBalancer<>(config, rule, ping
				, serverList, serverListFilter, new PollingServerListUpdater(config));
	}
	
	@Bean
	public IPing ribbonPing(IClientConfig config) {
		return new PingUrl(false, "/actuator/health");
	}
}
