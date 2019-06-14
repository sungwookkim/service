package com.categories.domain.repo.command.categories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.categories.domain.entity.categories.Categories;

/**
 * 카테고리 Repository 클래스(C, U, D 관련 클래스)
 * 
 * @author sinnakeWEB
 */
@Repository
public class CategoriesCommandRepository {

	@PersistenceContext(unitName = "categoriesEntityManager")
	EntityManager categoriesManagerFactory;
	
	/**
	 * 카테고리 저장
	 * 
	 * @param categories 저장할 Categories 객체
	 * @return 저장한 Categories 객체 반환
	 */
	public Categories add(Categories categories) {
		categoriesManagerFactory.persist(categories);
		
		return categories;
	}
}
