package com.categories.domain.infra.searchOptionKind.process.add.impl;

import com.categories.domain.entity.searchOption.SearchOptionKind;
import com.categories.domain.infra.searchOptionKind.process.add.SearchOptionKindAdd;
import com.sinnake.entity.ResultEntity;

import util.SinnakeValidate;

/**
 * 검색 옵션 종류 저장 프로세스 클래스
 * 
 * @author sinnakeWEB
 */
public class SearchOptionKindAddImpl implements SearchOptionKindAdd {

	protected String searchOptionName;
	
	public SearchOptionKindAddImpl(String searchOptionName) {
		this.searchOptionName = searchOptionName;
	}
	
	@Override
	public ResultEntity<SearchOptionKind> process() {
		return add();
	}

	@Override
	public ResultEntity<SearchOptionKind> add() {

		ResultEntity<SearchOptionKind> result = this.validate();
		if(!result.sucess()) {
			return new ResultEntity<>(result.getCode());
		}
		
		return this.sucess(result.getCode());
	}
	
	@Override
	public ResultEntity<SearchOptionKind> validate() {

		if(!searchOptionNameValidate()) {
			return new ResultEntity<>("-1");
		}
		
		return new ResultEntity<>(ResultEntity.sucessCodeString());
	}

	/**
	 * 검색 옵션 이름 검증
	 * 
	 * @author sinnakeWEB
	 * @return 검증 값
	 */
	public boolean searchOptionNameValidate() {
		return new SinnakeValidate(this.searchOptionName)
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
	public ResultEntity<SearchOptionKind> sucess(String code) {
		return new ResultEntity<>(code, new SearchOptionKind(this.searchOptionName));
	}
}
