package sinnake.auth.spring.prop.db.inter;

import java.util.Map;

/**
 * Injection 처리를 위한 인터페이스 
 * @author sinnakeWEB
 */
public interface DbInjectionEncode {
	/**
	 * Injection 방지를 위한 변환 메서드.(문자열)
	 * 
	 * @author sinnakeWEB
	 * @param params Injection 방지를 위한 변환할 값
	 * @return String
	 */	
	public String encode(String params);
	
	/**
	 * Injection 방지를 위한 변환 메서드.(배열)
	 * 
	 * @author sinnakeWEB
	 * @param params Injection 방지를 위한 변환할 값
	 * @return String[]
	 */
	public String[] encode(String[] params);
	
	/**
	 * Injection 방지를 위한 변환 메서드.(Map)
	 * 
	 * @author sinnakeWEB
	 * @param params Injection 방지를 위한 변환할 값
	 */
	public void encode(Map<String, Object> params);
	
	/**
	 * Injection 방지를 위한 변환 메서드.(Object)
	 * 
	 * @author sinnakeWEB
	 * @param params Injection 방지를 위한 변환할 값
	 * @return Object
	 */
	public Object encode(Object params);
}
