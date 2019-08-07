package com.auth.domain.service.command.member;

import java.util.HashMap;

/**
 * 회원 mapper 인터페이스
 *  
 * @author sinnakeWEB
 */
public interface MemberMapper {
	/**
	 * 회원 계정 잠금
	 * 
	 * @author sinnakeWEB
	 * @param param 회원 정보
	 */
	public void setUserUpdateLocked(HashMap<String, Object> param);
	
	/**
	 * 회원 비밀번호 틀린 횟수 조회
	 * 
	 * @author sinnakeWEB
	 * @param userName 회원 ID
	 * @return 회원 비밀번호 틀린 횟수 관련 정보
	 */
	public HashMap<String, Object> getUserAttempts(String userName);
	
	/**
	 * 회원 비밀번호 제한 조건 추가
	 * 
	 * @author sinnakeWEB
	 * @param param 회원 정보
	 */
	public void addUserAttempts(HashMap<String, Object> param);
	
	/**
	 * 회원 비밀번호 틀린 횟수 증가
	 * 
	 * @author sinnakeWEB
	 * @param memberId 회원 채번 번호
	 * @return DB에서 업데이트된 카운트
	 */
	public int setUserAttempts(Long memberId);	
	
	/**
	 * 회원 비밀번호 틀린 횟수 초기화
	 * 
	 * @author sinnakeWEB
	 * @param memberId 회원 채번 번호
	 * @return DB에서 업데이트된 카운트
	 */
	public int setUserAttemptsRest(Long memberId);
}
