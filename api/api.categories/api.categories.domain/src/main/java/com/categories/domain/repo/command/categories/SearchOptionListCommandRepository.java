package com.categories.domain.repo.command.categories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.categories.domain.entity.searchOption.SearchOptionList;

/**
 * 검색 옵션 리스트 Repository 클래스(C, U, D 관련 클래스)
 * 
 * @author sinnakeWEB
 */
@Repository
public class SearchOptionListCommandRepository {

	@PersistenceContext(unitName = "categoriesEntityManager")
	EntityManager categoriesManagerFactory;
	
	/**
	 * 검색 좁션 종류 저장
	 * 
	 * @author sinnakeWEB
	 * @param SearchOptionList 저장할 SearchOptionList 객체
	 * @return 저장한 SearchOptionList 객체 반환
	 */
	public SearchOptionList add(SearchOptionList searchOptionList) {
		categoriesManagerFactory.persist(searchOptionList);
		
		return searchOptionList;
	}
}
