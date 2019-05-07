package com.member.domain.repo.command.member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.member.domain.entity.member.Member;

/** 
 * 회원 Repository 클래스(C, U, D 관련 클래스) 
 * 
 * @author sinnakeWEB
 */
@Repository
public class MemberCommandRepository {

	@PersistenceContext
	EntityManager em;
	
	public void save(Member member) {
		em.persist(member);
	}
}
