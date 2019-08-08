package com.member.domain.service.read.member;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.member.domain.entity.member.Member;
import com.member.domain.repo.read.member.MemberReadRepository;

/**
 * 멤버 Service 클래스
 * 
 * @author sinnakeWEB
 *
 */
@Service
public class MemberReadService {

	private MemberReadRepository memberReadRepository;
	
	@Autowired
	public MemberReadService(MemberReadRepository memberReadRepository) {
		this.memberReadRepository = memberReadRepository;
	}
	
	/**
	 * 
	 * @author sinnakeWEB
	 * @param name 사용자 ID
	 * @return 회원 조회 결과 값
	 */
	@Transactional(transactionManager = "memberTransactionManager")
	public Map<String, Object> findMember(String name) {
		return new Member().get().findMember(this.memberReadRepository::findName, name);
	}
}
