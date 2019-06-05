package com.categories.domain.repo.command.categories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.categories.domain.entity.Categories;

/**
 * 카테고리 Repository 클래스(C, U, D 관련 클래스)
 * 
 * @author sinnakeWEB
  */
@Repository
public class CategoriesCommandRepository {

	@PersistenceContext(unitName = "categoriesEntityManager")
	EntityManager categoriesManagerFactory;
	
	public Categories add(Categories categories) {
		categoriesManagerFactory.persist(categories);
		
		return categories;
	}
}
