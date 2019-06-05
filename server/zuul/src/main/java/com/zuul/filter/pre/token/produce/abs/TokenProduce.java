package com.zuul.filter.pre.token.produce.abs;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.netflix.zuul.context.RequestContext;
import com.sinnake.entity.ResultEntity;

import util.RestProcess;

public abstract class TokenProduce<T> {
	
	private String url;
	private String userName;
	private RestTemplate restTemplate;
	private RequestContext requestContext;

	public TokenProduce() {}
	
	public TokenProduce(String url, String userName, RestTemplate restTemplate, RequestContext requestContext) {
		
		this.url = url;
		this.userName = userName;
		this.restTemplate = restTemplate;
		this.requestContext = requestContext;
	}
	
	@SuppressWarnings("unchecked")
	public ResultEntity<T> produce(Supplier<LinkedMultiValueMap<String ,String>> tokenPostParam) {

		return new RestProcess<T>()
			.throwUsed()
			.call(() -> {
				ResponseEntity<String> response = this.tokenPost(tokenPostParam);
				ResultEntity<T> resultEntity = this.produceCall(response);
				String statusCode = resultEntity.getCode();
				
				if("200".equals(statusCode) || ResultEntity.ResultCode.SUCESS.getCode().equals(statusCode)) {
					this.tokenSave(new Gson().fromJson(response.getBody(), HashMap.class));
				}
			
				return resultEntity;
			})
			.fail(e -> this.produceFail((Exception)e) )
			.exec();
	}

	protected ResponseEntity<String> tokenPost(Supplier<LinkedMultiValueMap<String ,String>> tokenPostParam) {
		HttpHeaders httpHeaders = new HttpHeaders();

		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.add("authorization", "Basic " + Base64.getEncoder().encodeToString((this.userName + ":" + this.userName).getBytes()));

		 return this.restTemplate.exchange(url
				, HttpMethod.POST
				, new HttpEntity<>(tokenPostParam.get(), httpHeaders)
				, String.class);
	}

	public String getUrl() { return this.url; }
	public String getUserName() { return this.userName; }
	public RestTemplate getRestTemplate() { return this.restTemplate; }
	public RequestContext getRequestContext() { return this.requestContext; }
	
	protected abstract ResultEntity<T> produceCall(ResponseEntity<String> response);	
	protected abstract ResultEntity<T> produceFail(Exception e);
	public abstract void tokenSave(Map<String, Object> resultToken);
}
