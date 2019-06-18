package com.categories.domain.infra.categories.process.update.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import com.categories.domain.entity.categories.Categories;
import com.categories.domain.entity.searchOption.SearchOptionKind;
import com.categories.domain.entity.searchOption.SearchOptionList;
import com.categories.domain.infra.categories.process.add.impl.CategoriesAddImpl;
import com.categories.domain.infra.categories.process.update.CategoriesUpdate;
import com.sinnake.entity.ResultEntity;

import util.SinnakeValidate;

public class CategoriesUpdateImpl extends CategoriesAddImpl implements CategoriesUpdate {

	protected Supplier<Categories> findCategory;
	protected Function<Long, SearchOptionList> findSearchOptionList;
	
	public CategoriesUpdateImpl() {}
	
	@Override
	public boolean parentIdCategoryValidate() {
		return false;
	}
	
	@Override
	public boolean parentCategoryValidate() {
		return false;
	}
	
	/**
	 * 검증 성공시 프로세스
	 * 
	 * @author sinnakeWEB
	 * @param code 검증 여부 결과 값
	 * @return 검증 성공시 반환 값
	 */
	@Override
	public ResultEntity<Categories> sucess(String code) {

		ResultEntity<Categories> resultEntity;

		resultEntity = Optional.ofNullable(findCategory)
			.map(f -> {
				return Optional.ofNullable(f.get())
					.map(c -> new ResultEntity<>(ResultEntity.sucessCodeString(), c))
					.orElse(new ResultEntity<>("-2"));
			})
			.orElseThrow(RuntimeException::new);

		if(!resultEntity.sucess()) { return resultEntity; }
		
		Categories categories = resultEntity.getResult();
		categories.setCategoryName(this.categoryName);
		categories.setRegDate(new Date());

		resultEntity = this.updateParentId(categories);
		if(!resultEntity.sucess()) { return resultEntity; }

		resultEntity = this.addSearchOptioinList(categories);
		if(!resultEntity.sucess()) { return resultEntity; }

		return new ResultEntity<>(code, categories);
	}

	/**
	 * 수정할 카테고리 조회 객체 설정
	 * 
	 * @author sinnakeWEB
	 * @param findCategory 수정할 카테고리를 조회할 함수형 인터페이스 
	 */
	@Override
	public void findCategory(Supplier<Categories> findCategory) {
		this.findCategory = findCategory;
	}
	
	/**
	 * 상위 카테고리 수정
	 * 
	 * @author sinnakeWEB
	 * @param categories 상위 카테고리 값을 수정하기 위한 카테고리 객체
	 * @return 상위 카테고리가 수정된 Categories 객체
	 */
	@Override
	public ResultEntity<Categories> updateParentId(Categories categories) {
		
		return Optional.ofNullable(this.parentId)
			.filter(p -> p >= 0L)
			.map(p -> {

				if(this.parentId == 0L) {
					categories.setParentId(this.parentId);
					
					return new ResultEntity<>(ResultEntity.sucessCodeString()
						, categories);
				} else if(Optional.ofNullable(this.findParentCategories.apply(this.parentId))
					.map(c -> c.getId())
					.orElse(-1L) <= 0) {
					
					return new ResultEntity<Categories>("-3");
				}

				categories.setParentId(this.parentId);
	
				return new ResultEntity<>(ResultEntity.sucessCodeString()
					, categories);
			})
			.orElse(new ResultEntity<>(ResultEntity.sucessCodeString() ));
	}
	
	@Override
	public ResultEntity<Categories> addSearchOptioinList(Categories categories) {

		List<SearchOptionList> searchOptionLists = Optional.ofNullable(categories.getSearchOptionList())
			.orElseGet(ArrayList::new);
		
		if(searchOptionLists.size() == 0) { return super.addSearchOptioinList(categories); }

		if(Optional.ofNullable(this.searchOptionList)
			.filter(s -> s.size() > 0).isPresent()) {

			for(Map<String, String> s : this.searchOptionList) {
				ResultEntity<SearchOptionKind> resultEntity = this.searchOptionListValidate(s);
				
				if(!resultEntity.sucess()) { return new ResultEntity<>(resultEntity.getCode()); }
				
				String searchOptionName = s.get("searchOptionName");
				String type = s.get("type");
				SearchOptionKind searchOptionKind = resultEntity.getResult();
				Long id = Long.parseLong(s.get("id"));
				
				if(id == 0L) {
					this.setSearchOptionList(categories, searchOptionName, type, searchOptionKind);
				} else if(!searchOptionLists.stream()
					.filter(sol -> id == sol.getId())
					.map(sol -> {
						sol.setSearchOptionName(searchOptionName);
						sol.setType(type);
						sol.setSearchOptionKind(searchOptionKind);
						sol.setRegDate(new Date());
						
						return sol;
					})
					.findAny()
					.isPresent()) {

					return new ResultEntity<>("-15");
				};
			}
		}
		
		return new ResultEntity<>(ResultEntity.sucessCodeString(), categories);
	}
	
	@Override
	public ResultEntity<SearchOptionKind> searchOptionListValidate(Map<String, String> s) {
		if(!new SinnakeValidate(s.get("id")).required().getValidResult()) {
			return new ResultEntity<>("-14");
		}
		
		return super.searchOptionListValidate(s);
	}	
}
