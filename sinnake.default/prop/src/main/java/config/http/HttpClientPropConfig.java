package config.http;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "httppool")
public class HttpClientPropConfig {

	private Integer maxPerRoute;
	private Integer maxTotal;
	private Integer connectionRequestTimeout;
	private Integer connectTimeout;
	private Integer socketTimeout;

	public HttpClientPropConfig() { }
	
	public Integer getMaxPerRoute() {
		return maxPerRoute;
	}
	
	public Integer getMaxTotal() {
		return maxTotal;
	}
	
	public Integer getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}
	
	public Integer getConnectTimeout() {
		return connectTimeout;
	}
	
	public Integer getSocketTimeout() {
		return socketTimeout;
	}

	public void setMaxPerRoute(Integer maxPerRoute) {
		this.maxPerRoute = maxPerRoute;
	}

	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}

	public void setConnectionRequestTimeout(Integer connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}

	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public void setSocketTimeout(Integer socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	
}
