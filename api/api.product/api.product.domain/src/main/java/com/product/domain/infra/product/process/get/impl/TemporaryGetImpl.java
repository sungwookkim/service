package com.product.domain.infra.product.process.get.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.base.Function;
import com.product.domain.entity.Temporary;
import com.product.domain.infra.product.process.get.TemporaryGet;

import commonInterface.CommonGet;

public class TemporaryGetImpl implements TemporaryGet {
	
	public TemporaryGetImpl() {}
	
	/**
	 * 카테고리 조회 프로세스(Map 형태로 반환)
	 * 
	 * @author sinnakeWEB
	 * @param findCategories 조회 데이터를 반환할 프로세스 함수형 인터페이스
	 * @param id 카테고리 Seq 값
	 * @return 카테고리 조회 결과 값
	 */
	public Map<String, Object> findTemporary(Function<Long, Temporary> findTemporary, Long id) {

		return Optional.ofNullable(CommonGet.<Temporary, Map<String, Object>>convert(findTemporary.apply(id)
					, TemporaryGetImpl::convertProcess))
				.orElseGet(HashMap::new);
	}

	/**
	 * 조회된 결과를 기본으로 뱐환 시켜주는 메소드
	 * 
	 * @author sinnakeWEB
	 * @param c 카테고리 객체
	 * @return 카테고리 객체 데이터를 Map 형태로 변환
	 */
	public static HashMap<String, Object> convertProcess(Temporary c) {
		HashMap<String, Object> temp = new HashMap<>();
		
		temp.put("id", c.getId());
		temp.put("text", c.getText());

		return temp;
	}
}

