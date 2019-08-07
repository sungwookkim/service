package com.categories.domain.infra.categories.process.add.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.categories.domain.entity.categories.Categories;
import com.categories.domain.entity.searchOption.SearchOptionKind;
import com.categories.domain.entity.searchOption.SearchOptionList;
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
	protected List<Map<String, String>> searchOptionList;
	protected Function<Long, SearchOptionKind> findSearchOptionKind;
	
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

	@Override
	public void searchOptionList(List<Map<String, String>> searchOptionList
		, Function<Long, SearchOptionKind> findSearchOptionKind) {

		this.searchOptionList = searchOptionList;
		this.findSearchOptionKind = findSearchOptionKind;
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
	 * 상품옵션 리스트 검증
	 * 
	 * @author sinnakeWEB
	 * @param s 상품옵션 리스트 값
	 * @return 검증 값
	 */
	public ResultEntity<SearchOptionKind> searchOptionListValidate(Map<String, String> s) {

		Long kindId;
						
		if(!new SinnakeValidate(s.get("kind_id")).required().getValidResult()) {
			return new ResultEntity<>("-10");
		} else {
			kindId = Long.parseLong(s.get("kind_id"));
		}

		if(!new SinnakeValidate(s.get("searchOptionName")).required().getValidResult()) {
			return new ResultEntity<>("-11");
		}
		
		if(!new SinnakeValidate(s.get("type")).required().getValidResult()) {
			return new ResultEntity<>("-12");
		}
		
		SearchOptionKind kind = Optional.ofNullable(this.findSearchOptionKind.apply(kindId)).orElseGet(SearchOptionKind::new);
		
		if(!Optional.ofNullable(kind.getId()).isPresent()) { return new ResultEntity<>("-13"); }

		return new ResultEntity<>(ResultEntity.sucessCodeString(), kind);
	}
	
	/**
	 * 검색 옵션 리스트 생성
	 * 
	 * @author sinnakeWEB
	 * @param categories 검색옵션 리스트를 저장할 Categories 객체
	 * @param searchOptionName 검색옵션 리스트 이름
	 * @param type 검색옵션리스트 타입
	 * @param searchOptionKind 검색옵 종류 객체 
	 */
	public void setSearchOptionList(Categories categories, String searchOptionName, String type, SearchOptionKind searchOptionKind) {

		SearchOptionList searchOptionList = new SearchOptionList(searchOptionName
			, type
			, searchOptionKind);
			
		searchOptionList.setRegDate(new Date());
		
		categories.addSearchOptionList(searchOptionList);
	}
	
	/**
	 * 상품옵션 리스트 저장 
	 * 
	 * @author sinnakeWEB
	 * @param categories 상품옵션 리스트를 저장할 Categories 객체
	 * @return 상품옵션 리스트가 저장된 Categories 객체 반환
	 */
	@Override
	public ResultEntity<Categories> addSearchOptioinList(Categories categories) {

		if(Optional.ofNullable(this.searchOptionList)
			.filter(s -> s.size() > 0).isPresent()) {

			for(Map<String, String> s : this.searchOptionList) {
				ResultEntity<SearchOptionKind> resultEntity = this.searchOptionListValidate(s);
				
				if(!resultEntity.sucess()) { return new ResultEntity<>(resultEntity.getCode()); }

				this.setSearchOptionList(categories
					, s.get("searchOptionName")
					, s.get("type")
					, resultEntity.getResult());
			}
		}
		
		return new ResultEntity<>(ResultEntity.sucessCodeString(), categories);
	}
	
	/**
	 * 검증 성공시 프로세스
	 * 
	 * @author sinnakeWEB
	 * @param code 검증 여부 결과 값
	 * @return 검증 성공시 반환 값
	 */
	public ResultEntity<Categories> sucess(String code) {

		Categories categories = new Categories(this.categoryName, this.parentId);
		
		ResultEntity<Categories> resultEntity = this.addSearchOptioinList(categories);
		if(!resultEntity.sucess()) { return resultEntity; }
		
		return new ResultEntity<>(code, categories);
	}
	
}
