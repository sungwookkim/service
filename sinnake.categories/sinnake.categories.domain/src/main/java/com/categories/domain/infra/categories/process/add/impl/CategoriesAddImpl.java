package com.categories.domain.infra.categories.process.add.impl;

import java.util.Optional;
import java.util.function.Function;

import com.categories.domain.entity.categories.Categories;
import com.categories.domain.infra.categories.process.add.CategoriesAdd;
import com.sinnake.entity.ResultEntity;

import util.SinnakeValidate;

/**
 * 카테고리 저장 프로세스 클래스
 * 
 * @author sinnakeWEB
 */
public class CategoriesAddImpl implements CategoriesAdd {

	protected Long parentId;
	protected String categoryName;
	protected Function<Long, Categories> findParentCategories;
	
	public CategoriesAddImpl() {}
	
	@Override
	public ResultEntity<Categories> process() {
		
		return this.add();
	}

	/**
	 * 카테고리 생성 프로세스
	 * 
	 * @author sinnakeWEB
	 * @return 생성할 카테고리 객체 반환
	 */
	@Override
	public ResultEntity<Categories> add() {

		ResultEntity<Categories> result = this.validate();
		
		if(!result.sucess()) {
			return new ResultEntity<>(result.getCode());
		}

		return this.sucess(result.getCode());
	}
	
	/**
	 * 카테고리 검증 프로세스
	 * 
	 * @author sinnakeWEB
	 * @return 결과 값
	 */
	@Override
	public ResultEntity<Categories> validate() {

		if(parentIdCategoryValidate()) {
			return new ResultEntity<>(ResultEntity.sucessCodeString());
		}
		
		if(parentCategoryValidate()) {
			return new ResultEntity<>("-1");
		}
		
		if(!categoryNameValidate()) {
			
			return new ResultEntity<>("-2");
		}
			
		return new ResultEntity<>(ResultEntity.sucessCodeString());
	}

	@Override
	public void pareintId(Long parentId, Function<Long, Categories> findParentCategories) {
		this.parentId = parentId;
		this.findParentCategories = findParentCategories;
	}

	@Override
	public void categoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * 상위 카테고리 저장 여부 검증
	 * 
	 * @author sinnakeWEB
	 * @return 검증값
	 */
	public boolean parentIdCategoryValidate() {
		return 0L == this.parentId.longValue();
	}
	
	/**
	 * 상위 클래스 존재 여부 검증
	 * 
	 * @author sinnakeWEB
	 * @return 검증값
	 */
	public boolean parentCategoryValidate() {

		return Optional.ofNullable(this.findParentCategories)
			.map(c -> c.apply(this.parentId))
			.map(c -> c.getId())
			.orElse(-1L) < 0;	
	}
	
	/**
	 * 카테고리 이름 검증
	 * 
	 * @author sinnakeWEB
	 * @return 검증값
	 */
	public boolean categoryNameValidate() {
		return new SinnakeValidate(this.categoryName)
			.required()
			.getValidResult();
	}
	
	/**
	 * 검증 성공시 프로세스
	 * 
	 * @author sinnakeWEB
	 * @param code 검증 여부 결과 값
	 * @return 검증 성공시 반환 값
	 */
	public ResultEntity<Categories> sucess(String code) {
		return new ResultEntity<>(code
			, new Categories(this.categoryName, this.parentId));	
	}
}
