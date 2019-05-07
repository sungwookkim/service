package com.member.domain.repo.read.member;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.member.domain.entity.member.Member;

/** 
 * 회원 Repository 클래스(R 관련 클래스) 
 * 
 * @author sinnakeWEB
 */
@Repository
public class MemberReadRepository {

	@PersistenceContext
	EntityManager em;
	
	/**
	 * 회원 ID로 회원 조회
	 *  
	 * @author sinnakeWEB
	 * @param userName 조회할 사용자 ID
	 * @return 조회 결과 값
	 */
	public List<Member> findName(String userName) {
		return em.createQuery("SELECT m FROM member m WHERE m.userName = :userName", Member.class)
			.setParameter("userName", userName)
			.getResultList();
	}
}
