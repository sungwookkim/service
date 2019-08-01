package com.categories.domain.service.read.searchOptionKind;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.categories.domain.entity.searchOption.SearchOptionKind;
import com.categories.domain.repo.read.categories.SearchOptionKindReadRepository;

/**
 * 검색 옵션 종류 Service 클래스
 * 
 * @author sinnakeWEB
 *
 */
@Service
public class SearchOptionKindReadService {
	
	private SearchOptionKindReadRepository searchOptionKindReadRepository;
	
	@Autowired
	public SearchOptionKindReadService(SearchOptionKindReadRepository searchOptionKindReadRepository) {

		this.searchOptionKindReadRepository = searchOptionKindReadRepository;
	}
	
	/**
	 * 검색옵션 종류 조회 서비스
	 * 
	 * @author sinnakeWEB
	 * @param id 검색옵션 종류 Seq 값
	 * @return 검색옵션 종류 결과 값
	 */
	@Transactional(transactionManager = "categoriesTransactionManager")
	public Map<String, Object> findSearchOptionKind(Long id) {
		
		return new SearchOptionKind()
			.get().findSearchOptionKind(searchOptionKindReadRepository::findId, id);
	}
	
}
