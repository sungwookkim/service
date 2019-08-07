package sinnake.auth.spring.security.authenticationProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.auth.domain.service.command.member.MemberService;

import sinnake.auth.spring.security.entity.User;
import sinnake.auth.spring.security.password.SinnakePasswordEncoder;
import sinnake.auth.spring.security.userDetailDao.SinnakeUserDetailService;

/**
 * 회원 비밀번호 검증.
 * Spring Security에서 사용되는 클래스.
 * 
 * @author sinnakeWEB
 */
@Configuration
public class MemberPwdAuthenticationProvider extends DaoAuthenticationProvider {
	
	private String maxAttempts;
	private MemberService memberService;
	
	/**
	 * 
	 * @author sinnakeWEB
	 * @param userDetailService 로그인 관련 사용자 정보 클래스
	 * @param passwordEncoder 비밀번호 인코딩 클래스
	 * @param memberService 회원 서비스 클래스
	 * @param maxAttempts 비밀번호 실패 횟수 기준 값
	 */
	@Autowired
	public MemberPwdAuthenticationProvider(SinnakeUserDetailService userDetailService
		, SinnakePasswordEncoder passwordEncoder
		, MemberService memberService
		, @Value("#{authServerProp['auth.maxAttempts']}") String maxAttempts) {

		// 로그인 관련 사용자 정보 클래스.
		super.setUserDetailsService(userDetailService);

		// 로그인 시도 시 패스워드 비교 클래스.
		super.setPasswordEncoder(passwordEncoder);
		
		this.memberService = memberService;
		this.maxAttempts = maxAttempts;
	}

	/**
	 * 인증 처리 관련 메서드.
	 * 
	 * @author sinnakeWEB
	 * @param authentication 인증 관련클래스
	 * @return {@link Authentication} 인증 처리 후 클래스
	 * @throws AuthenticationException 예외 처리
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			Authentication auth = super.authenticate(authentication);
			User user = (User)auth.getPrincipal();
			
			memberService.resetFailAttempts(user.getMemberId());
			
			return auth;			

		} catch(BadCredentialsException e) {
			memberService.updateFailAttempts(authentication.getPrincipal().toString(), Integer.parseInt(maxAttempts));
			throw e;
		} catch(LockedException e) {
			throw new LockedException(e.getMessage());
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}
}
