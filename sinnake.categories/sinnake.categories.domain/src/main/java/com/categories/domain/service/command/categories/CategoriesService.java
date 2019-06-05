package com.categories.domain.service.command.categories;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.categories.domain.entity.Categories;
import com.categories.domain.repo.command.categories.CategoriesCommandRepository;
import com.categories.domain.repo.read.categories.CategoriesReadRepository;
import com.sinnake.entity.ResultEntity;

import util.RestProcess;

/**
 * 카테고리 Service 클래스
 * 
 * @author sinnakeWEB
 *
 */
@Service
public class CategoriesService {

	private CategoriesCommandRepository categoriesCommandRepository;
	private CategoriesReadRepository categoriesReadRepository;
	
	@Autowired
	public CategoriesService(CategoriesCommandRepository categoriesCommandRepository
			, CategoriesReadRepository categoriesReadRepository) {

		this.categoriesCommandRepository = categoriesCommandRepository;
		this.categoriesReadRepository = categoriesReadRepository;
	}
	
	/**
	 * 카테고리  추가 서비스
	 * 
	 * @author sinnakeWEB
	 * @param categoryName 카테고리 명
	 * @param parentId 상위 카테고리 Seq 값
	 * @return 카테고리 결과 조회 값
	 */
	@Transactional(transactionManager = "categoriesTransactionManager",  propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResultEntity<Long> categoriesAdd(String categoryName, Long parentId) {
		
		return new RestProcess<Long>()
			.call(() -> {
				ResultEntity<Categories> categories = new Categories().add(categoryName
					, parentId
					, this.categoriesReadRepository::findId);
				
				String code = categories.getCode();
				
				if("1".equals(code)) {
					this.categoriesCommandRepository.add(categories.getResult());

					return new ResultEntity<>(ResultEntity.ResultCode.SUCESS.getCode()
						,categories.getResult().getId());
				}
								
				return new ResultEntity<>(ResultEntity.ResultCode.FAIL.getCode()
					, Long.parseLong(code) );
			})
			.fail(e -> {
				e.printStackTrace();
				
				return new ResultEntity<>(ResultEntity.ResultCode.FAIL.getCode(), -99L);
			})
			.exec();
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
