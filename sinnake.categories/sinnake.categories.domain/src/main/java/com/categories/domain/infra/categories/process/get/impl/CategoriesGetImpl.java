package com.categories.domain.infra.categories.process.get.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.categories.domain.entity.categories.Categories;
import com.categories.domain.entity.searchOption.SearchOptionKind;
import com.categories.domain.entity.searchOption.SearchOptionList;
import com.categories.domain.infra.categories.process.get.CategoriesGet;
import com.google.common.base.Function;

import commonInterface.CommonGet;

public class CategoriesGetImpl implements CategoriesGet {
	
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

		return Optional.ofNullable(CommonGet.<Categories, Map<String, Object>>convert(findCategories.apply(id)
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
		
		return Optional.ofNullable(CommonGet.<Categories, Map<String, Object>>convert(findChildCategories.apply(parentId)
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
				
				return CommonGet.<Long, Map<String, Object>, Categories>convert(value.stream()
						.collect(Collectors.groupingBy(Categories::getParentId))
					, CategoriesGetImpl::convertProcess);
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
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> convertProcess(Categories c) {
		HashMap<String, Object> category = new HashMap<>();
		
		category.put("id", c.getId());
		category.put("categoryName", c.getCategoryName());
		category.put("parentId", c.getParentId());
		category.put("regDate", c.getRegDate());

		HashMap<Long, Object> searchOption = new HashMap<>();
		CommonGet.<SearchOptionList, Map<String, Object>>convert(c.getSearchOptionList(), s -> {
			SearchOptionKind kind = s.getSearchOptionKind();
			HashMap<String, Object> sok = new HashMap<>();
			sok.put("id", kind.getId());
			sok.put("searchOptionName", kind.getSearchOptionName());
			sok.put("regDate", kind.getRegDate());
			
			HashMap<String, Object> sol = new HashMap<>();
			sol.put("id", s.getId());
			sol.put("regDate", s.getRegDate());
			sol.put("searchOptionName", s.getSearchOptionName());
			sol.put("type", s.getType());
			sol.put("searchOptionKind", sok);

			return sol;
		}).stream()
			.collect(Collectors.groupingBy(s -> ((Map<String, Object>)s.get("searchOptionKind")).get("id")))
			.entrySet()
			.stream().forEach(v -> {
				HashMap<String, Object> searchOptionTemp = new HashMap<>();
				List<Map<String, Object>> searchOptionList = new ArrayList<>();

				v.getValue().stream().forEach(t -> {					
					searchOptionTemp.put("searchOptionKind", t.get("searchOptionKind"));
					t.remove("searchOptionKind");
					
					searchOptionList.add(t);
				});
				
				searchOptionTemp.put("searchOptionList", searchOptionList);

				searchOption.put(Long.parseLong(v.getKey().toString()), searchOptionTemp);
			});

		category.put("searchOption", searchOption);

		return category;
	}
}

