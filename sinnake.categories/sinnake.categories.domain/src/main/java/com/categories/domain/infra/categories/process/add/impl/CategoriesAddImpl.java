package com.categories.domain.infra.categories.process.add.impl;

import java.util.Optional;

import com.categories.domain.entity.Categories;
import com.categories.domain.infra.categories.process.add.CategoriesAdd;
import com.google.common.base.Function;
import com.sinnake.entity.ResultEntity;

import util.SinnakeValidate;

public class CategoriesAddImpl implements CategoriesAdd {

	private Long parentId;
	private String categoryName;
	private Function<Long, Categories> categoryFun;
	
	public CategoriesAddImpl(Long parentId, String categoryName, Function<Long, Categories> categoryFun) {
		this.parentId = parentId;
		this.categoryName = categoryName;
		this.categoryFun = categoryFun;
	}
	
	@Override
	public ResultEntity<Categories> process() {
		
		return this.add();
	}

	/**
	 * 카테고리 검증 프로세스
	 * 
	 * @author sinnakeWEB
	 * @return 결과 값
	 */
	@Override
	public ResultEntity<Categories> validate() {

		if(0L == this.parentId.longValue()) {
			return new ResultEntity<>("1");
		}
		
		if(Optional.ofNullable(this.categoryFun.apply(this.parentId))
			.map(c -> c.getId())
			.orElse(-1L) < 0) {
			return new ResultEntity<>("-1");
		}
		
		if(!new SinnakeValidate(this.categoryName)
			.required()
			.getValidResult()) {
			
			return new ResultEntity<>("-2");
		}
			
		return new ResultEntity<>("1");
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
		
		if(!"1".equals(result.getCode())  ) {
			return new ResultEntity<>(result.getCode());
		}
		
		return new ResultEntity<>(result.getCode(), new Categories(this.categoryName, this.parentId));
	}

}
