package com.zuul.filter.pre;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.sinnake.entity.ResultEntity;
import com.util.CookieUtil;

import config.serverList.ServerListProp;
import config.serverList.ServerListProp.ServerList;
import util.RestProcess;

public class TokenRefreshFilter extends ZuulFilter {

	private RestTemplate restTemplate;
	private DefaultTokenServices defaultTokenServices;
	private JwtTokenStore jwtTokenStore;
	private JwtAccessTokenConverter jwtAccessTokenConverter;
	
	private ServerListProp serverListProp;
	private ServerList oauthServerInfo;
	private static final String GRANT_TYPE = "refresh_token";
	private static final String COOKIE_TOKEN_KEY = "accessToken";
	
	@Autowired
	public TokenRefreshFilter(RestTemplate restTemplate
		, ServerListProp serverListProp
		, DefaultTokenServices defaultTokenServices
		, JwtTokenStore jwtTokenStore
		, JwtAccessTokenConverter jwtAccessTokenConverter) {

		this.restTemplate = restTemplate;
		this.serverListProp = serverListProp;
		this.defaultTokenServices = defaultTokenServices;
		this.jwtTokenStore = jwtTokenStore;
		this.jwtAccessTokenConverter = jwtAccessTokenConverter;
		
		this.oauthServerInfo = this.serverListProp.getServerList("oauthServer");
	}
	
	@Override
	public boolean shouldFilter() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object run() throws ZuulException {
		HttpHeaders httpHeaders = new HttpHeaders();
		Map<String, ?> convertToken = null;
		
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest httpServletRequest = requestContext.getRequest();

		String accessTokenVal = Optional.ofNullable(httpServletRequest.getCookies())
			.map(cs -> {
				String tempAccessToken = "";
				
				for(Cookie cookie : cs) {
					if(COOKIE_TOKEN_KEY.equals(cookie.getName())) {
						tempAccessToken = cookie.getValue();
						break;
					}
				}
				
				return tempAccessToken;
			})
			.orElse("");
		
		String sessRefreshToken = Optional.ofNullable(httpServletRequest.getSession().getAttribute("refreshToken"))
				.map(r -> r.toString())
				.orElse("");
				
		if("".equals(accessTokenVal) || "".equals(sessRefreshToken)) {
			return null;
		}

		convertToken = this.checkToken(accessTokenVal);
		
		int statusCode = Optional.ofNullable(convertToken)
			.map(c -> {
				return Optional.ofNullable(c.get("statusCode"))
					.map(cv -> cv.toString())
					.map(Integer::parseInt)
					.orElse(-3);
			}).get();

		if(statusCode == -998) {
			String userName = Optional.ofNullable(accessTokenVal)
				.map(t -> {
					String tokenStr = new String(Base64
						.getUrlDecoder()
						.decode(t.split(Pattern.quote("."))[1]));
					
					return new Gson().fromJson(tokenStr, HashMap.class).get("user_name").toString();
				})
				.orElse("");

			new RestProcess<ResponseEntity<String>>()
				.call(() -> {
					String url = this.oauthServerInfo.getHttpFullAddress("tokenOauth");
					
					httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
					httpHeaders.add("authorization", "Basic " + Base64.getEncoder().encodeToString((userName + ":" + userName).getBytes()));

					ResponseEntity<String> response = this.restTemplate.exchange(url
							, HttpMethod.POST
							, new HttpEntity<>(new LinkedMultiValueMap<String ,String>() {
								private static final long serialVersionUID = 1L;
								{
									add("grant_type", GRANT_TYPE);
									add("refresh_token", sessRefreshToken);
								}
							} , httpHeaders)
							, String.class);

					Map<String, Object> resultToken = new Gson().fromJson(response.getBody(), HashMap.class);
					String accessToken = resultToken.get("access_token").toString();
					String refreshToken = resultToken.get("refresh_token").toString();

					CookieUtil.setCookie("accessToken", accessToken, (c) -> {
						c.setPath("/");
						c.setHttpOnly(true);
						// c.setSecure(true);
						requestContext.getResponse().addCookie(c);
					});
					
					requestContext.addZuulRequestHeader("authorization", "bearer " + accessToken);
					httpServletRequest.getSession().setAttribute("refreshToken", refreshToken);

					return new ResultEntity<>(ResultEntity.ResultCode.SUCESS.getCode(), response);
				})
				.fail(e -> {						
					e.printStackTrace();

					HashMap<String, Object> err = new HashMap<>();
					err.put("code", -899);
					err.put("erros", "");
					
					requestContext.setSendZuulResponse(false);
					requestContext.setRouteHost(null);
					requestContext.setResponseStatusCode(500);
					requestContext.setResponseBody(new Gson().toJson(err));
					
					if (e instanceof HttpStatusCodeException) {
						HttpStatusCodeException httpStatusCodeException = (HttpStatusCodeException) e;
						HttpStatus httpStatus = httpStatusCodeException.getStatusCode();
						String errBody = httpStatusCodeException.getResponseBodyAsString();
						
						requestContext.setResponseStatusCode(httpStatusCodeException.getStatusCode().value());

						if(httpStatus == HttpStatus.UNAUTHORIZED) {
							err.put("code", -898);
							err.put("erros", Arrays.asList("unauthorized_client", httpStatus.toString()));
							
							errBody = new Gson().toJson(err);
						} 
						
						requestContext.setResponseBody(errBody);
					}
					
					return new ResultEntity<>(ResultEntity.ResultCode.FAIL.getCode());
				})
				.exec();

		} else if(statusCode < 0) {
			requestContext.setSendZuulResponse(false);
			requestContext.setRouteHost(null);
			
			requestContext.setResponseStatusCode(200);
			requestContext.setResponseBody(new ResultEntity<String>(String.valueOf(statusCode), "").toString());				
		} else if(statusCode == 0) {
			requestContext.addZuulRequestHeader("authorization", "bearer " + accessTokenVal);
		}
		
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}
	
	private Map<String, ?> checkToken(String tokenStr) {
		int commonStatusCode = -999;
		
		return new RestProcess<HashMap<String, Object>>()
			.call(() -> {
				HashMap<String, Object> rtn = new HashMap<>();
				rtn.put("statusCode", commonStatusCode);
				rtn.put("data", "");
				
				this.jwtTokenStore.setTokenEnhancer(this.jwtAccessTokenConverter);
				this.defaultTokenServices.setTokenStore(this.jwtTokenStore);

				OAuth2AccessToken token = this.defaultTokenServices.readAccessToken(tokenStr);
				
				int statusCode = Optional.ofNullable(token)
					.map(t -> t.isExpired() ? -998 : 0)
					.orElse(-997);

				rtn.put("statusCode", statusCode);

				if(statusCode == 0) {
					OAuth2Authentication authentication =  this.defaultTokenServices.loadAuthentication(token.getValue());

					rtn.put("data", new DefaultAccessTokenConverter().convertAccessToken(token, authentication));				
				}
				
				return new ResultEntity<HashMap<String,Object>>(ResultEntity.ResultCode.SUCESS.getCode(), rtn);
			})
			.fail(e -> {
				e.printStackTrace();

				HashMap<String, Object> rtn = new HashMap<>();
				rtn.put("statusCode", commonStatusCode);
				rtn.put("data", "");
				
				return new ResultEntity<>(ResultEntity.ResultCode.FAIL.getCode(), rtn);
			})
			.exec()
			.getResult();
	}

}
