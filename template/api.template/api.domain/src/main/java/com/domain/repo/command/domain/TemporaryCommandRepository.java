package com.domain.repo.command.domain;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.domain.entity.Temporary;

/** 
 * 템플릿 Repository 클래스(C, U, D 관련 클래스) 
 * 
 * @author sinnakeWEB
 */
@Repository
public class TemporaryCommandRepository {

	@PersistenceContext(unitName="temporaryEntityManager")
	EntityManager memberEntityManagerFactory;
	
	public void save(Temporary temporary) {
		memberEntityManagerFactory.persist(temporary);
	}
}
