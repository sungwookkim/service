package com.categories.domain.infra.categories.process.get.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.categories.domain.entity.Categories;
import com.google.common.base.Function;

import commonInterface.CommonGet;

public class CategoriesGetImpl implements CommonGet {
	
	public CategoriesGetImpl() {}
	
	/**
	 * 카테고리 조회 프로세스(Map 형태로 반환)
	 * 
	 * @author sinnakeWEB
	 * @param findCategories 조회 데이터를 반환할 프로세스 함수형 인터페이스
	 * @param id 카테고리 Seq 값
	 * @return 카테고리 조회 결과 값
	 */
	public Map<String, Object> findCategories(Function<Long, Categories> findCategories, Long id) {

		return Optional.ofNullable(this.<Categories, Map<String, Object>>convert(findCategories.apply(id)
					, CategoriesGetImpl::convertProcess))
				.orElseGet(HashMap::new);
	}
	
	/**
	 * 하위 카테고리 조회 프로세스(Map 형태로 반환)
	 * 
	 * @author sinnakeWEB
	 * @param findChildCategories 조회 데이터를 반환할 프로세스 함수형 인터페이스
	 * @param parentId 조회할 하위 카테고리의에 포함되어 있는 상위 카테고리 Seq 값 
	 * @return 카테고리 조회 결과 값
	 */
	public List<Map<String, Object>> findChildCategories(Function<Long, List<Categories>> findChildCategories, Long parentId) {			
		
		return Optional.ofNullable(this.<Categories, Map<String, Object>>convert(findChildCategories.apply(parentId)
					, CategoriesGetImpl::convertProcess))
				.orElseGet(ArrayList::new);
	}

	/**
	 * 전체 카테고리 조회 프로세스
	 * parent id 기준으로 그룹핑 한 데이터 형식
	 * 
	 * @author sinnakeWEB
	 * @param findAllCategories 조회 데이터를 반환할 프로세스 함수형 인터페이스
	 * @return 카테고리 조회 결과 값
	 */	
	public Map<Long, List<Map<String, Object>>> findAllCategories(Supplier<List<Categories>> findAllCategories) {
		
		return Optional.ofNullable(findAllCategories.get())
			.map(value -> {
				return value.stream()
					.collect(Collectors.groupingBy(Categories::getParentId))
					.entrySet().stream()
						.collect(Collectors.toMap(Map.Entry::getKey
							, v -> this.<Categories, Map<String, Object>>convert(v.getValue(), CategoriesGetImpl::convertProcess) ));				
			})
			.orElseGet(HashMap::new);
	
	}
	/**
	 * 조회된 결과를 기본으로 뱐환 시켜주는 메소드
	 * 
	 * @author sinnakeWEB
	 * @param c 카테고리 객체
	 * @return 카테고리 객체 데이터를 Map 형태로 변환
	 */
	protected static HashMap<String, Object> convertProcess(Categories c) {
		HashMap<String, Object> category = new HashMap<>();
		
		category.put("id", c.getId());
		category.put("categoryName", c.getCategoryName());
		category.put("parentId", c.getParentId());
		category.put("regDate", c.getRegDate());
		
		return category;
	}
}