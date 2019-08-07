package prop.db.impl;

import java.util.Map;

import javax.sql.DataSource;

import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.MySQLCodec;
import org.owasp.esapi.codecs.MySQLCodec.Mode;

import prop.db.inter.DbInjectionEncode;
import util.SinnakeHttpUtil;

/**
 * MYSQL Injection 처리 클래스.
 * (owasp 라이브러리 사용)
 * 
 * @author sinnakeWEB
 */
public class MySqlInjectionEncode implements DbInjectionEncode {
	Codec codec = new MySQLCodec(Mode.ANSI);

	public String encode(String params) {	
		return SinnakeHttpUtil.encodeForSQL(codec, params);
	}
	
	public String[] encode(String[] params) {
		return SinnakeHttpUtil.encodeForSQL(codec, params);
	}
	
	public void encode(Map<String, Object> params) {
		SinnakeHttpUtil.encodeForSQL(codec, params);		
	}
	
	public Object encode(Object params) {
		return params;
	}

	public DataSource connConfig() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
