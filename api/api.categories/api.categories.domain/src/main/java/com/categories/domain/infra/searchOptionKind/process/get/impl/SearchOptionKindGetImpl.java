package com.categories.domain.infra.searchOptionKind.process.get.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.categories.domain.entity.searchOption.SearchOptionKind;
import com.categories.domain.infra.searchOptionKind.process.get.SearchOptionKindGet;
import com.google.common.base.Function;

import commonInterface.CommonGet;

public class SearchOptionKindGetImpl implements SearchOptionKindGet {

	public SearchOptionKindGetImpl() {}
	
	/**
	 * 검색옵션 종류 조회 프로세스(Map 형태로 반환)
	 * 
	 * @author sinnakeWEB
	 * @param findSearchOptionKind 조회 데이터를 반환할 프로세스 함수형 인터페이스
	 * @param id 카테고리 Seq 값
	 * @return 검색옵션 종류 조회 결과 값
	 */
	public Map<String, Object> findSearchOptionKind(Function<Long, SearchOptionKind> findSearchOptionKind, Long id) {
		
		return Optional.ofNullable(CommonGet.<SearchOptionKind, Map<String, Object>>convert(findSearchOptionKind.apply(id)
				, SearchOptionKindGetImpl::convertProcess))
			.orElseGet(HashMap::new);		
	}
	
	/**
	 * 조회된 결과를 기본으로 뱐환 시켜주는 메소드
	 * 
	 * @author sinnakeWEB
	 * @param s 검색옵션 종류 객체
	 * @return 검색옵션 객체 데이터를 Map 형태로 변환
	 */
	protected static HashMap<String, Object> convertProcess(SearchOptionKind s) {
		HashMap<String, Object> searchOptionKind = new HashMap<>();
		
		searchOptionKind.put("id", s.getId());
		searchOptionKind.put("searchOption_name", s.getSearchOptionName());
		searchOptionKind.put("regDate", s.getRegDate());
		
		return searchOptionKind;
	}
}
