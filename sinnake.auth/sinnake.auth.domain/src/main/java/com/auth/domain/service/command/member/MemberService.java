package com.auth.domain.service.command.member;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 service 클래스.
 * 
 * @author sinnakeWEB
 */
@Service
public class MemberService {

	private MemberMapper memberMapper;
	
	@Autowired
	public MemberService(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}
	
	/**
	 * 회원 비밀번호 틀린 횟수 초기화
	 * 
	 * @author sinnakeWEB
	 * @param memberId 사용자 채번
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void resetFailAttempts(Long memberId) {
		memberMapper.setUserAttemptsRest(memberId);
	}
	
	/**
	 * 회원 비밀번호 제한 프로세스
	 * 
	 * @author sinnakeWEB
	 * @param userName 사용자 ID
	 * @param maxAttempts 비밀번호 틀린 횟수 기준 값
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateFailAttempts(String userName, int maxAttempts) {
		HashMap<String, Object> userAttempts = memberMapper.getUserAttempts(userName);
		
		Optional.ofNullable(userAttempts)
			.ifPresent(u -> {
				Long memberId = (Long) u.get("ID");
				Integer attempts = Integer.parseInt(u.get("ATTEMPTS").toString());
				
				Optional.ofNullable(memberMapper.setUserAttempts(memberId))
					.filter(c -> c <= 0)
					.ifPresent(c -> {
						HashMap<String, Object> param = new HashMap<>();
						param.put("memberId", memberId);
						param.put("attempts", 1);
						
						memberMapper.addUserAttempts(param);						
					});
				
				if(attempts + 1 >= maxAttempts) {
					HashMap<String, Object> param = new HashMap<>();
					param.put("locked", 0);
					param.put("userName", userName);
					
					memberMapper.setUserUpdateLocked(param);
				}
			});
	}
	
}
