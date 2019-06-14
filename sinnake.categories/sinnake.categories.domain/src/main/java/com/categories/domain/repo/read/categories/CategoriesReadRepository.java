package com.categories.domain.repo.read.categories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.categories.domain.entity.categories.Categories;

/**
 * 카테고리 Repository 클래스 (R 관련 클래스)
 * 
 * @author sinnakeWEB
 */
@Repository
public class CategoriesReadRepository {

	@PersistenceContext(unitName = "categoriesEntityManager")
	EntityManager categoriesManagerFactory;
	
	/**
	 * 카테고리 단일 조회
	 * 
	 * @author sinnakeWEB
	 * @param id 카테고리 Seq 값
	 * @return 카테고리 조화 결과 값
	 */
	public Categories findId(Long id) {
		return this.categoriesManagerFactory.find(Categories.class, id);
	}
	
	/**
	 * 특정 카테고리 조회
	 * 
	 * @author sinnakeWEB
	 * @param categoryId 부모 카테고리 Seq 값
	 * @return 카테고리 조회 결과 값
	 */
	public List<Categories> findParentCategories(Long categoryId) {
		String jpqlQuery = "SELECT c FROM categories c WHERE c.parentId = :categoryId";
		
		return this.categoriesManagerFactory.createQuery(jpqlQuery, Categories.class)
			.setParameter("categoryId", categoryId)
			.getResultList();
	}

	/**
	 * 전체 카테고리 조회
	 * 
	 * @author sinnakeWEBau
	 * @return 카테고리 조회 결과 값
	 */
	public List<Categories> findAllCategories() {
		String jpqlQuery = "SELECT c FROM categories c";
		
		return this.categoriesManagerFactory.createQuery(jpqlQuery, Categories.class)
			.getResultList();		
	}
}
