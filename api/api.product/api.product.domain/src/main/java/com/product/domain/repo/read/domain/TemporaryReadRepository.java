package com.product.domain.repo.read.domain;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.product.domain.entity.Temporary;


/** 
 * 템플릿 Repository 클래스(R 관련 클래스) 
 * 
 * @author sinnakeWEB
 */
@Repository
public class TemporaryReadRepository {

	@PersistenceContext(unitName="productEntityManager")
	EntityManager productEntityManagerFactory;
	
	/**
	 * 회원 ID로 회원 조회
	 *  
	 * @author sinnakeWEB
	 * @param userName 조회할 사용자 ID
	 * @return 조회 결과 값
	 */
	public Temporary findId(long id) {
		return this.productEntityManagerFactory.find(Temporary.class, id);
	}
}
