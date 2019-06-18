package com.categories.domain.repo.read.categories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.categories.domain.entity.searchOption.SearchOptionList;

/**
 * 검색 옵션 리스트 Repository 클래스(R 관련 클래스)
 * 
 * @author sinnakeWEB
 */
@Repository
public class SearchOptionListReadRepository {

	@PersistenceContext(unitName = "categoriesEntityManager")
	EntityManager categoriesManagerFactory;

	/**
	 * 검색 옵션 리스트 조회
	 * 
	 * @author sinnakeWEB
	 * @param id 카테고리 Seq 값
	 * @return 카테고리 조화 결과 값
	 */
	public SearchOptionList findId(Long id) {
		return this.categoriesManagerFactory.find(SearchOptionList.class, id);
	}
}
