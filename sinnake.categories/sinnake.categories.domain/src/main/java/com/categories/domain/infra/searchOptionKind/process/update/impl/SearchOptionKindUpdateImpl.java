package com.categories.domain.infra.searchOptionKind.process.update.impl;

import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;

import com.categories.domain.entity.searchOption.SearchOptionKind;
import com.categories.domain.infra.searchOptionKind.process.add.impl.SearchOptionKindAddImpl;
import com.categories.domain.infra.searchOptionKind.process.update.SearchOptioinKindUpdate;
import com.sinnake.entity.ResultEntity;

/**
 * 검색옵션 종류 수정 프로세스 클래스
 * 
 * @author sinnakeWEB
 *
 */
public class SearchOptionKindUpdateImpl extends SearchOptionKindAddImpl implements SearchOptioinKindUpdate {

	protected SearchOptionKind searchOptionKind;
	
	public SearchOptionKindUpdateImpl(String searchOptionName) {
		super(searchOptionName);
		this.searchOptionKind = new SearchOptionKind(searchOptionName);
	}

	public SearchOptionKindUpdateImpl(String searchOptionName, Supplier<SearchOptionKind> getSearchOptionKind) {
		super(searchOptionName);
		this.searchOptionKind = Optional.ofNullable(getSearchOptionKind.get()).orElseGet(SearchOptionKind::new);
	}
	
	/**
	 * 검증 성공시 프로세스
	 * 
	 * @author sinnakeWEB
	 * @param code 검증 여부 결과 값
	 * @return 검증 성공시 반환 값
	 */
	@Override
	public ResultEntity<SearchOptionKind> sucess(String code) {

		return Optional.ofNullable(this.searchOptionKind.getId())			
			.filter(i -> 0L < i)
			.map(i -> {
				this.searchOptionKind.setSearchOptionName(this.searchOptionName);
				this.searchOptionKind.setRegDate(new Date());

				return new ResultEntity<>(code, this.searchOptionKind);				
			})
			.orElse(new ResultEntity<>("-2"));		
	}
}
