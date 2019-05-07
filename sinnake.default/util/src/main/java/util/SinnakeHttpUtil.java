package util;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.errors.EncodingException;

public class SinnakeHttpUtil {
	/*
	 * Real IP 추출.
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String[] headers = {
			"X-Forwarded-For"
			, "Proxy-Client-IP"
			, "WL-Proxy-Client-IP"
			, "HTTP_CLIENT_IP"
			, "HTTP_X_FORWARDED_FOR"
			, "X-Real-IP"
		};
		
        for(int i = 0, len = headers.length; i < len; i++) {
        	String ip = request.getHeader(headers[i]);

        	if (ip != null && ip.length() > 0 && !"unknown".equalsIgnoreCase(ip)) {
        		return ip;
        	}
        }

        return request.getRemoteAddr();
	}
	
	/*******************/
	/* Encode 영역 시작*/
	/*******************/
	// encode HTML Start
	public static String encodeForHTML(String str) {
		return ESAPI.encoder().encodeForHTML(str);
	}
	
	public static void encodeForHTML(Map<String, Object> params) {
		Iterator<String> ite = params.keySet().iterator();
		
		while(ite.hasNext()) {
			String key = ite.next();
			Object value = params.get(key);
			
			if(value instanceof String) {
				params.put(key, ESAPI.encoder().encodeForHTML(value.toString()));	
			}
		}
	}
	
	public static String[] encodeForHTML(String[] params) {
		String[] rtnValue = new String[params.length];
		
		for(int i = 0, len = params.length; i < len; i++) {
			rtnValue[i] = ESAPI.encoder().encodeForHTML(params[i]);
		}
		
		return rtnValue;
	}
	// encode HTML End
	
	// encode JavaScript Start
	public static String encodeForJavaScript(String str) {
		return ESAPI.encoder().encodeForJavaScript(str);
	}
	
	public static void encodeForJavaScript(Map<String, Object> params) {
		Iterator<String> ite = params.keySet().iterator();
		
		while(ite.hasNext()) {
			String key = ite.next();
			Object value = params.get(key);
			
			if(value instanceof String) {
				params.put(key, ESAPI.encoder().encodeForJavaScript(value.toString()));	
			}
		}
	}
	// encode JavaScript End
	
	// encode URL Start
	public static String encodeForURL(String str) throws EncodingException {
		return ESAPI.encoder().encodeForURL(str);
	}
	
	public static void encodeForURL(Map<String, Object> params) throws EncodingException {
		Iterator<String> ite = params.keySet().iterator();
		
		while(ite.hasNext()) {
			String key = ite.next();
			Object value = params.get(key);
			
			if(value instanceof String) {
				params.put(key, ESAPI.encoder().encodeForURL(value.toString()));	
			}
		}
	}
	// encode URL Start
	
	// encode SQL Start
	public static String encodeForSQL(Codec codec, String params) {
		return ESAPI.encoder().encodeForSQL(codec, params);		
	}
	
	public static String[] encodeForSQL(Codec codec, String[] params) {
		String[] rtnValue = new String[params.length];
		
		for(int i = 0, len = params.length; i < len; i++) {
			rtnValue[i] = ESAPI.encoder().encodeForSQL(codec, params[i]);
		}
		
		return rtnValue;
	}
	
	public static void encodeForSQL(Codec codec, Map<String, Object> params) {
		Iterator<String> ite = params.keySet().iterator();
		
		while(ite.hasNext()) {
			String key = ite.next();
			Object value = params.get(key); 
			
			if(value instanceof String) {
				params.put(key, ESAPI.encoder().encodeForSQL(codec, value.toString()));	
			}
		}		
	}
	// encode SQL End
	/*****************/
	/* Encode 영역 끝*/
	/*****************/
	
	/*******************/
	/* decode 영역 시작*/
	/*******************/
	// decode HTML Start
	public static String decodeForHTML(String str) {
		return ESAPI.encoder().decodeForHTML(str);
	}
	// decode HTML End
	
	// decode URL Start
	public static String decodeFromURL(String str) throws EncodingException {
		return ESAPI.encoder().decodeFromURL(str);		
	}
	// decode URL End
	
	// decode Base64 Start
	public static byte[] decodeFromBase64(String str) throws IOException {
		return ESAPI.encoder().decodeFromBase64(str);
	}
	// decode Base64 End	
	/******************/
	/* decode 영역 끝*/
	/*****************/
	
	/*****************/
	/* 기타 영역 시작*/
	/*****************/	
	// canonicalize Start
	public static String canonicalize(String str) {
		return ESAPI.encoder().canonicalize(str);
	}
	// canonicalize End
	/*****************/
	/* 기타 영역 시작*/
	/*****************/	
}
