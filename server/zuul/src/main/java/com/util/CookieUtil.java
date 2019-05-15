package com.util;

import java.util.function.Consumer;

import javax.servlet.http.Cookie;


public class CookieUtil {

	public static void setCookie(String key, String value, Consumer<Cookie> consumer) {
		consumer.accept(new Cookie(key, value));
	}
}
