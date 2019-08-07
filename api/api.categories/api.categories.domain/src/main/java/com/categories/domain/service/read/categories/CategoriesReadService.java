package com.categories.domain.service.read.categories;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.categories.domain.entity.categories.Categories;
import com.categories.domain.repo.read.categories.CategoriesReadRepository;

/**
 * 카테고리 Service 클래스
 * 
 * @author sinnakeWEB
 *
 */
@Service
public class CategoriesReadService {
	
	private CategoriesReadRepository categoriesReadRepository;
	
	@Autowired
	public CategoriesReadService(CategoriesReadRepository categoriesReadRepository) {

		this.categoriesReadRepository = categoriesReadRepository;
	}
	
	/**
	 * 카테고리 조회 서비스
	 * 
	 * @author sinnakeWEB
	 * @param id 카테고리 Seq 값
	 * @return 카테고리 조회 결과 값
	 */
	@Transactional(transactionManager = "categoriesTransactionManager")
	public Map<String, Object> findCategories(Long id) {

		return new Categories()
				.get().findCategories(this.categoriesReadRepository::findId, id);
	}
	
	/**
	 * 하위 카테고리 조회 서비스
	 * 
	 * @author sinnakeWEB
	 * @param parentId 조회할 하위 카테고리의에 포함되어 있는 상위 카테고리 Seq 값
	 * @return 카테고리 조회 결과 값
	 */
	@Transactional(transactionManager = "categoriesTransactionManager")
	public List<Map<String, Object>> findChildCategories(Long parentId) {
	
		return new Categories()
				.get().findChildCategories(this.categoriesReadRepository::findParentCategories, parentId);
	}

	/**
	 * 전체 카테고리 조회 서비스
	 * 
	 * @author sinnakeWEB
	 * @return 카테고리 조회 결과 값
	 */
	@Transactional(transactionManager = "categoriesTransactionManager")
	public Map<Long, List<Map<String, Object>>> findAllCategories() {
		return new Categories()
				.get().findAllCategories(this.categoriesReadRepository::findAllCategories);
	}
}
