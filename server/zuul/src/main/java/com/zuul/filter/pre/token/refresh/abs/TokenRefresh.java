package com.zuul.filter.pre.token.refresh.abs;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Pattern;

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
import com.zuul.filter.pre.token.enumPackage.TokenKey;

import util.RestProcess;

public abstract class TokenRefresh<T> {

	private String url;
	private RestTemplate restTemplate;
	private RequestContext requestContext;

	public TokenRefresh() { }
	
	public TokenRefresh(String url, RestTemplate restTemplate, RequestContext requestContext) {
		
		this.url = url;
		this.restTemplate = restTemplate;
		this.requestContext = requestContext;
	}
	
	@SuppressWarnings("unchecked")
	public void refresh(Supplier<LinkedMultiValueMap<String ,String>> tokenPostParam) {

		Map<String, ?> convertToken = null;
		
		String accessToken = this.getAccessToken();
		String refreshToekn = this.getRefreshToken();
		
		if(!"".equals(accessToken) && !"".equals(refreshToekn)) {
			convertToken = this.checkToken(accessToken);
			
			int statusCode = Optional.ofNullable(convertToken)
					.map(c -> {
						return Optional.ofNullable(c.get("statusCode"))
							.map(cv -> cv.toString())
							.map(Integer::parseInt)
							.orElse(-3);
					}).get();
			
			if(statusCode == -998) {
				String userName = Optional.ofNullable(accessToken)
					.map(t -> {
						String tokenStr = new String(Base64
							.getUrlDecoder()
							.decode(t.split(Pattern.quote("."))[1]));
						
						return new Gson().fromJson(tokenStr, HashMap.class).get("user_name").toString();
					})
					.orElse("");
				
				LinkedMultiValueMap<String ,String> postVal = tokenPostParam.get();
				
				Optional.ofNullable(postVal)
					.map(p -> {
						if(!Optional.ofNullable(p.get(TokenKey.RESULT_REFRESH_TOKEN.getKey())).isPresent()) {
							p.add(TokenKey.RESULT_REFRESH_TOKEN.getKey(), refreshToekn);
						}

						return p;
					});

				new RestProcess<ResponseEntity<T>>()
					.call(() -> {
						ResponseEntity<String> response = this.tokenPost(postVal, userName);
						ResultEntity<ResponseEntity<T>> resultEntity = this.refreshCall(response);
						
						if(resultEntity.getCode().equals(ResultEntity.ResultCode.SUCESS.getCode()) ) {
							this.tokenSave(new Gson().fromJson(response.getBody(), HashMap.class));
						}
						
						return resultEntity;
					})
					.fail(e -> this.refreshFail((Exception)e) )
					.exec();
			} else if(statusCode < 0) {
				this.requestContext.setSendZuulResponse(false);
				this.requestContext.setRouteHost(null);
				
				this.requestContext.setResponseStatusCode(200);
				this.requestContext.setResponseBody(new ResultEntity<String>(String.valueOf(statusCode), "").toString());					
			} else if(statusCode == 0) {
				this.requestContext.addZuulRequestHeader("authorization", "bearer " + accessToken);
			}
		}
	}
	
	protected ResponseEntity<String> tokenPost(LinkedMultiValueMap<String ,String> tokenPostVal, String userName) {

		HttpHeaders httpHeaders = new HttpHeaders();

		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.add("authorization", "Basic " + Base64.getEncoder().encodeToString((userName + ":" + userName).getBytes()));

		 return this.restTemplate.exchange(url
				, HttpMethod.POST
				, new HttpEntity<>(tokenPostVal, httpHeaders)
				, String.class);
	}
	
	public String getUrl() { return this.url; }
	public RestTemplate getRestTemplate() { return this.restTemplate; }
	public RequestContext getRequestContext() { return this.requestContext; }
	
	protected abstract ResultEntity<ResponseEntity<T>> refreshCall(ResponseEntity<String> response);	
	protected abstract ResultEntity<ResponseEntity<T>> refreshFail(Exception e);
	protected abstract String getAccessToken();
	protected abstract String getRefreshToken();
	protected abstract Map<String, ?> checkToken(String accessToken);
	protected abstract void tokenSave(Map<String, Object> resultToken);
}
