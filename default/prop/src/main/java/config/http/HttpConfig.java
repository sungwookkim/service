package config.http;

import java.util.List;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.IOReactorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * restTemplate 설정 클래스
 * 
 * @author sinnakeWEB
 */
@Configuration
@SuppressWarnings("deprecation")
public class HttpConfig {
	 private HttpClientPropConfig httpClientPropConfig;
	 
	 @Autowired
	 public HttpConfig(HttpClientPropConfig httpClientPropConfig) {
		 this.httpClientPropConfig = httpClientPropConfig;
	 }
	 
	 /********/
	 /* Sync */
	 /********/
	 @Bean
	 @LoadBalanced
	 public RestTemplate restTemplate() {
		 RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
		 
		 List<HttpMessageConverter<?>> converters = restTemplate
			 .getMessageConverters();

		 for (HttpMessageConverter<?> converter : converters) {
			 if (converter instanceof MappingJackson2HttpMessageConverter) {
				 MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) converter;
				 jsonConverter.setObjectMapper(new ObjectMapper());
			 }
		 }
		 
		 return restTemplate;
	 }
	 
	 @Bean
	 public ClientHttpRequestFactory httpRequestFactory() {
		 return new HttpComponentsClientHttpRequestFactory(httpClient());
	 }
	
	 @Bean
	 public CloseableHttpClient httpClient() {
		 PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		 
		 connectionManager.setMaxTotal(this.httpClientPropConfig.getMaxTotal());
		 connectionManager.setDefaultMaxPerRoute(this.httpClientPropConfig.getMaxPerRoute() );

		 RequestConfig config = RequestConfig.custom()
			 .setConnectionRequestTimeout(this.httpClientPropConfig.getConnectionRequestTimeout())
			 .setConnectTimeout(this.httpClientPropConfig.getConnectTimeout())
			 .setSocketTimeout(this.httpClientPropConfig.getSocketTimeout())
			 .build();

		 CloseableHttpClient defaultHttpClient = HttpClientBuilder.create()
			 .setConnectionManager(connectionManager)
			 .setDefaultRequestConfig(config).build();

		 return defaultHttpClient;
	 }

	 /*********/
	 /* Async */
	 @Bean
	 @LoadBalanced
	 public AsyncRestTemplate asyncRestTemplate() throws IOReactorException {
		 AsyncRestTemplate restTemplate = new AsyncRestTemplate(asyncHttpRequestFactory(), restTemplate());
		 return restTemplate;
	 }
	 
	 @Bean
	 public AsyncClientHttpRequestFactory asyncHttpRequestFactory() throws IOReactorException {
		 return new HttpComponentsAsyncClientHttpRequestFactory(asyncHttpClient());
	 }

	 @Bean
	 public CloseableHttpAsyncClient asyncHttpClient() throws IOReactorException {
		 
		 PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager(new DefaultConnectingIOReactor(IOReactorConfig.DEFAULT));
		 
		 connectionManager.setMaxTotal(this.httpClientPropConfig.getMaxTotal());
		 connectionManager.setDefaultMaxPerRoute(this.httpClientPropConfig.getMaxPerRoute());

		 RequestConfig config = RequestConfig.custom()
			 .setConnectionRequestTimeout(this.httpClientPropConfig.getConnectionRequestTimeout())
			 .setConnectTimeout(this.httpClientPropConfig.getConnectTimeout())
			 .setSocketTimeout(this.httpClientPropConfig.getSocketTimeout())
			 .build();

		 CloseableHttpAsyncClient httpclient = HttpAsyncClientBuilder.create()
			 .setConnectionManager(connectionManager)
			 .setDefaultRequestConfig(config).build();

		 return httpclient;		 
	 }
}
