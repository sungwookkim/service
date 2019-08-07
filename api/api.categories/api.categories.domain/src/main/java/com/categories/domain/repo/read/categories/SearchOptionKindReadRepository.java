package com.categories.domain.repo.read.categories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.categories.domain.entity.searchOption.SearchOptionKind;

/**
 * 검색 옵션 종류 Repository 클래스(R 관련 클래스)
 * 
 * @author sinnakeWEB
 */
@Repository
public class SearchOptionKindReadRepository {

	@PersistenceContext(unitName = "categoriesEntityManager")
	EntityManager categoriesManagerFactory;
	
	/**
	 * 검색 옵션 종류 조회
	 * 
	 * @author sinnakeWEB
	 * @param id 카테고리 Seq 값
	 * @return 카테고리 조화 결과 값
	 */
	public SearchOptionKind findId(Long id) {
		return this.categoriesManagerFactory.find(SearchOptionKind.class, id);
	}
}
