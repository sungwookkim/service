package com.categories.domain.repo.command.categories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.categories.domain.entity.searchOption.SearchOptionKind;

/**
 * 검색 옵션 종류 Repository 클래스(C, U, D 관련 클래스)
 * 
 * @author sinnakeWEB
 */
@Repository
public class SearchOptionKindCommandRepository {

	@PersistenceContext(unitName = "categoriesEntityManager")
	EntityManager categoriesManagerFactory;
	
	/**
	 * 검색 좁션 종류 저장
	 * 
	 * @author sinnakeWEB
	 * @param searchOptionKind 저장할 SearchOptionKind 객체
	 * @return 저장한 SearchOptionKind 객체 반환
	 */
	public SearchOptionKind add(SearchOptionKind searchOptionKind) {
		categoriesManagerFactory.persist(searchOptionKind);
		
		return searchOptionKind;
	}
}
