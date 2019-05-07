package sinnake.auth.spring.security.password;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import util.SinnakeAES256Util;

/**
 * 암호화된 비밀번호를 비교하기 위한 클래스.
 * 
 * @author sinnakeWEB
 */
@Configuration
public class SinnakePasswordEncoder implements PasswordEncoder{

	private String pwKey;	

	public SinnakePasswordEncoder(@Value("#{serverProp['pwAes256.key']}") String pwKey) {
		this.pwKey = pwKey;
	}
	
	/**
	 * 암호화된 비밀번호를 비교하기 위해 사용자의 원본 비밀번호를 암호화해서 반환한다.
	 * 
	 * @param rawPassword 사용자의 원본 비밀번호.
	 * @return String 암호화된 비밀번호가 반환된다. 
	 */
	@Override
	public String encode(CharSequence rawPassword) {
		SinnakeAES256Util aes256;
		try {
			aes256 = new SinnakeAES256Util(pwKey);
			
			return aes256.aesEncode(rawPassword.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rawPassword.toString();
	}

	/**
	 * 비밀번호가 일치한지 검증하는 메서드.
	 * 
	 * @param rawPassword 사용자의 원본 비밀번호.
	 * @param encodedPassword 사용자의 암호화된 비밀번호.
	 */
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if(encodedPassword.equals(rawPassword) 
			|| encodedPassword.equals(this.encode(rawPassword))) {
			return true;
		}

		return false;
	}

}
